/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.alertafiscal.opusprimum.retaguarda.alterdata.postgresql;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Bruno
 */
public class RetaguardaAlterdataPostgresSQLSingle extends Retaguarda{

   
    @Override
    public Map<String, String> mapeiaCodigoInternoEAN() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public void preProcessamento(Produto produto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public boolean atualizarNCM(Produto produto) throws Exception {
        
        String codigoInterno = produto.getCodigoInterno();
        String ncmLimpo = produto.getNcmLimpo();
        String ncmDoisPrimeirosDigitos = produto.getNcmDoisPrimeirosDigitos();
        //logger.debug("Realizando update no NCM " + ncmLimpo + " para: " + codigoInterno);
        String sql = "UPDATE WSHOP.produto SET cdtratamentoespecial = 'A', cdipi = '" + ncmLimpo + "', cdgenero = '" + ncmDoisPrimeirosDigitos + "' WHERE idproduto = '" + codigoInterno + "'";
        executeUpdate(sql);
        return true;
    }

    @Override
    public boolean atualizarCEST(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String cest = produto.getCestLimpo();
        if (cest != null && !cest.isEmpty()) {
            logger.debug("Realizando update no CEST " + cest + " para: " + codigoInterno);
            String sql = "UPDATE WSHOP.produto SET cest = '" + cest + "' WHERE idproduto = '" + codigoInterno + "'";
            executeUpdate(sql);
            return true;
        }
        return false;
    }

    @Override
    public boolean atualizarIPI(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String ipi = produto.getIpi();
        logger.debug("Realizando update no IPI " + ipi + " para: " + codigoInterno);
        String sql = "UPDATE WSHOP.produto SET CDSTTRIBIPIENTRADA = '03', CDSTTRIBIPISAIDA = '53', alipivenda = " + ipi + " WHERE idproduto = '" + codigoInterno + "'";
        executeUpdate(sql);
        return true;
    }

    private String transformaPara2CasasDecimais(int valor) {
        if (valor < 10) {
            return "0" + valor;
        }
        return String.valueOf(valor);
    }
    
    @Override
    public boolean atualizarPISCOFINS(Produto produto) throws Exception {
         logger.debug("Realizando update na Natureza PIS/COFINS para: XXXXXX");
        String codigoInterno = produto.getCodigoInterno();
        boolean simples = ConfiguracaoContexto.getInstance().isOptantePeloSimplesNacional();
        BigDecimal tempAliquotaPis = simples ? null : produto.getPisSaidaDecimal();
        BigDecimal tempAliquotaCofins = simples ? null : produto.getCofinsSaidaDecimal();
        BigDecimal tempAliquotaPisEntrada = simples ? null : produto.getPisEntradaDecimal();
        BigDecimal tempAliquotaCofinsentrada = simples ? null : produto.getCofinsEntradaDecimal();

        String piscofinsCstEntrada = produto.getPiscofinsCstEntrada();
        Integer piscofinsCstEntradaInteger = Integer.parseInt(piscofinsCstEntrada);
        String piscofinsCstSaida = produto.getPiscofinsCstSaida();
        Integer piscofinsCstSaidaInteger = Integer.parseInt(piscofinsCstSaida);

        logger.debug("Realizando update na Natureza PIS/COFINS para: " + codigoInterno);

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE WSHOP.detalhe ");
        builder.append("SET cdsittribpisentrada = '").append(transformaPara2CasasDecimais(piscofinsCstEntradaInteger)).append("', ");
        builder.append("cdsittribcofinsentrada = '").append(transformaPara2CasasDecimais(piscofinsCstEntradaInteger)).append("', ");
        builder.append("cdsittribpis = '").append(transformaPara2CasasDecimais(piscofinsCstSaidaInteger)).append("', ");
        builder.append("cdsittribcofins = '").append(transformaPara2CasasDecimais(piscofinsCstSaidaInteger)).append("', ");
        builder.append("alpiscompra = ").append(tempAliquotaPisEntrada).append(", ");
        builder.append("alcofinscompra = ").append(tempAliquotaCofinsentrada).append(", ");
        builder.append("VlPerPIS = ").append(tempAliquotaPis).append(", ");
        builder.append("vlpercofins = ").append(tempAliquotaCofins).append(" ");
        builder.append("WHERE idproduto = '").append(codigoInterno).append("'");

        executeUpdate(builder.toString());
        return true;
    }

    @Override
    public boolean atualizarICMS(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String icmsCstSaida = produto.getIcmsCstSaidaConsumidorFinal();
        //String codigoCstIcmsSaidaRevenda = produto.getCodigoCstIcmsSaidaRevenda();
        //String icmsCstSaidaRevenda =produto.getIcmsCstSaidaRevenda();
        String codigoCsosn = produto.getCodigoCsosn();
        Integer icmsCstSaidaInteger = Integer.parseInt(icmsCstSaida);
        Integer codigoCsosnInteger = Integer.parseInt(codigoCsosn);
        //Integer codigoCstIcmsSaidaRevendaInteger = Integer.parseInt(codigoCstIcmsSaidaRevenda);
        //Integer icmsCstSaidaRevendaInteger = Integer.parseInt(icmsCstSaidaRevenda);
        
        String modalidade = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("opus.modalidade.juridica".trim());

        //modalidade  = simples usar csosn
        //modalidade = luroreal ou lucro_presumido usar cst
        BigDecimal icmsSaidaBigDecimal = produto.getIcmsSaidaConsumidorFinalBigDecimal();
        String mvaInterno = produto.getMvaInterno();
        logger.debug("Realizando update no ICMS para: " + codigoInterno);
        StringBuilder builder = new StringBuilder();

        if (modalidade.equals("lucro_presumido") || modalidade.equals("lucro_real")) {
            if (icmsCstSaidaInteger == 40 || icmsCstSaidaInteger == 41) {
                // CST Isento ou Não Tributado
                builder.append("UPDATE WSHOP.produto SET TRIBUTACAO = 'I', idcalculoicms = '' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            } else if (icmsCstSaidaInteger == 0) {
               //String calculoICMS = calculosICMS000.get(icmsSaidaBigDecimal.intValue());                
                builder.append("UPDATE WSHOP.produto SET ");
                builder.append("TRIBUTACAO = 'T', ");
                builder.append("idcalculoicms = '");
                //builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            } else if (icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10 || codigoCsosnInteger == 500) {
               //String calculoICMS = calculosICMS060e010.get(mvaInterno);                
                builder.append("UPDATE WSHOP.produto SET ");
                builder.append("TRIBUTACAO = 'S', ");
                builder.append("idcalculoicms = '");
                //builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            }

            //MINHAS IMPLEMNTAÇÃOES DA PRIMEIRA VEZ Q ENTENDI O VAGNER
            // QUANFO FOR 102 DEVO CRUZAR COM CST PQ 102 PODE SER VARIOS CSTS
            //500 NAO, TRATAR COMO 60 E ACABOU
            //isento nao tributado
            /*if (codigoCsosnInteger == 102 && codigoCstIcmsSaidaRevendaInteger == 40) {
                builder.append("UPDATE WSHOP.produto SET TRIBUTACAO = 'I', idcalculoicms = '' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            }

            if (codigoCsosnInteger == 102 && codigoCstIcmsSaidaRevendaInteger == 0) {

                //mudar para codigocsticmssaidarevenda
                String calculoICMS = calculosICMS000.get(icmsSaidaBigDecimal.intValue());
                builder.append("UPDATE WSHOP.produto SET ");
                builder.append("TRIBUTACAO = 'T', ");
                builder.append("idcalculoicms = '");
                builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            }*/

            //executeUpdate(builder.toString());
            //return true;

        }

        if (modalidade.equals("simples")) {

            if (codigoCsosnInteger == 500) {
                //String calculoICMS = calculosICMS060e010.get(mvaInterno);
                
               // logger.debug("&&&&&&& calculo csosn 500 %s produto %s", calculoICMS, produto.getCodigoInterno());
                builder.append("UPDATE WSHOP.produto SET ");
                builder.append("TRIBUTACAO = 'S', ");
                builder.append("idcalculoicms = '");
                //builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
                
                

            }

            if (codigoCsosnInteger == 102) {
                //String calculoICMS = calculosICMS000.get(icmsSaidaBigDecimal.intValue());
                String teste = String.valueOf(icmsSaidaBigDecimal.intValue());
                //logger.debug("###########" + calculoICMS + " mva "+ teste);
                //logger.debug("&&&&&&& calculo csosn 102 %s produto %s", calculoICMS, produto.getCodigoInterno());
                builder.append("UPDATE WSHOP.produto SET ");
                builder.append("TRIBUTACAO = 'T', ");                
                builder.append("idcalculoicms = '");
               // builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
                
            }

            logger.debug("query %s", builder.toString());
            int retorno = executeUpdate(builder.toString());        
            logger.debug("retorno %d", retorno);
                return true;
            

        }
        return false;
    }

    @Override
    public boolean atualizarNatureza(Produto produto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void posProcessamento(Produto produto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
