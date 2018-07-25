package br.com.alertafiscal.opusprimum.retaguarda.syspdv.firebird;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import br.com.alertafiscal.opusprimum.utils.OpusUtils;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RetaguardaSysPDVFirebird extends Retaguarda {

    //CODIGO DOS TRIBUTOS ICMS
    String T07 = "T07";
    String T13 = "T13";
    String T14 = "T14";
    String T20 = "T20";
    String T27 = "T27";
    String N00 = "N00";
    String I00 = "I00";
    String F00 = "F00"; //  substiuicao tributaria

    //CODIGO DOS TRIBUTOS PIS COFINS
    String codPisTributado;
    String codCofinsTributado;
    String codPisMonofasico;
    String codCofinsMonofasico;
    String codPisSubstituicao;
    String codCofinsSubstituicao;
    String codPisAliquotaZero;
    String codCofinsAliquotaZero;
    String codPisOutros;
    String codCofinsOutros;
    @Override
    public Map<String, String> mapeiaCodigoInternoEAN() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preProcessamento(Produto produto) throws Exception {

                         
        codPisTributado = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("pis.tributado");
        codCofinsTributado = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("cofins.tributado");
        codPisMonofasico = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("pis.monofasico");
        codCofinsMonofasico = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("cofins.monofasico");
        codPisSubstituicao = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("pis.substituicao");
        codCofinsSubstituicao = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("cofins.substituicao");
        codPisAliquotaZero = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("pis.aliquotazero");
        codCofinsAliquotaZero = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("confins.aliquotazero");
        codPisOutros = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("pis.outros");
        codCofinsOutros = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("confins.outros");
        
        
        

    }

    @Override
    public boolean atualizarNCM(Produto produto) throws Exception {
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        String codigoNCM = produto.getNcmLimpo();

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE produto SET proncm = '");
        builder.append(codigoNCM);
        builder.append("' WHERE procod = '");
        builder.append(codigoInterno);
        builder.append("';");

        executeUpdate(builder.toString());
        logger.debug("Atualizado NCM PRODUTO %s", codigoInterno);
        return true;
    }

    @Override
    public boolean atualizarCEST(Produto produto) throws Exception {
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        String codigoCEST = produto.getCestLimpo();

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE produto SET procest= '");
        builder.append(codigoCEST);
        builder.append("' WHERE procod = '");
        builder.append(codigoInterno);
        builder.append("';");

        executeUpdate(builder.toString());

        logger.debug("Atualizado CEST PRODUTO %s", codigoInterno);
        return true;
    }

    @Override
    public boolean atualizarIPI(Produto produto) throws Exception {
        //logger.debug(null, "estou atualizando IPI");
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        Double ipiDouble = Double.valueOf(produto.getIpi());

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE produto SET proipi = ");
        builder.append(ipiDouble);
        builder.append(" WHERE procod = '");
        builder.append(codigoInterno);
        builder.append("';");

        executeUpdate(builder.toString());
        return true;
    }

    @Override
    public boolean atualizarPISCOFINS(Produto produto) throws Exception {

        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        BigDecimal aliquotaPis = produto.getCofinsSaidaDecimal();
        BigDecimal aliquotaConfins = produto.getCofinsSaidaDecimal();

        String pisConfisnCStSaida = produto.getPiscofinsCstSaida();
        Integer cstSaida = Integer.parseInt(produto.getPiscofinsCstSaida());
        String pisCofinsCStEntrada = produto.getPiscofinsCstEntrada();

        logger.debug("Realizando update na Natureza PIS/COFINS para: " + codigoInterno);

        //cst saida
        //01 - tributado - entrada 50 - nno banco 01 pis e 02 cofins
        //04 monofasico - entrada 70 - no banco 03 pis e 04 cofins
        //05 substituuição tributada -  entrada75 - no banco 07 e 08 pis e cofins
        //06 aliquota zero  -entrada 73 - no banco 09 10
        //outros  entrada 99
        //apago as aliquotas ja existentes na tabela impostos_federais_produto;
        StringBuilder queryDeleteImpostosFederais = new StringBuilder();
        queryDeleteImpostosFederais.append("DELETE FROM impostos_federais_produto WHERE procod = '");
        queryDeleteImpostosFederais.append(codigoInterno);
        queryDeleteImpostosFederais.append("';");
        executeUpdate(queryDeleteImpostosFederais.toString());

        StringBuilder builderPis = new StringBuilder();
        StringBuilder builderCofins = new StringBuilder();

        String idPis = "99";
        String idCofins = "99";

        if (cstSaida == 1) {
            idPis = codPisTributado;
            idCofins = codCofinsTributado;
        }
        if (cstSaida == 4) {
            idPis = codPisMonofasico;
            idCofins = codCofinsMonofasico;
        }
        if (cstSaida == 5) {
            idPis = codPisSubstituicao;
            idCofins = codCofinsTributado;
        }
        if (cstSaida == 6) {
            idPis = codPisAliquotaZero;
            idCofins = codCofinsAliquotaZero;
        }
        if(cstSaida == 49){
            idPis = codPisOutros;
            idCofins = codCofinsOutros;
        }
        
        

        if (!idPis.equals("99") && !idCofins.equals("99")) {

            //insere imposto federal
            builderPis.append("INSERT INTO impostos_federais_produto (IMPFEDSIM, PROCOD) VALUES ('");
            builderPis.append(idPis);
            builderPis.append("','");
            builderPis.append(codigoInterno);
            builderPis.append("');");

            builderCofins.append("INSERT INTO impostos_federais_produto ( IMPFEDSIM, PROCOD ) VALUES ('");
            builderCofins.append(idCofins);
            builderCofins.append("','");
            builderCofins.append(codigoInterno);
            builderCofins.append("');");

            executeUpdate(builderPis.toString());
            executeUpdate(builderCofins.toString());

            return true;

        }

        logger.debug(null, "Pis Cofins Atualizado");
        return true;
    }

    @Override
    public boolean atualizarICMS(Produto produto) throws Exception {
        logger.debug("Atualizando icms produto %s", produto.getCodigoInterno());

        //inserir  0000  o xml de revisao nao possui zeros a esquerda, mas no banco possui 14 caracteres
        // muitos zeros a esquerda  
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        String codigoCsosn = produto.getCodigoCsosn();
        String icmsSaidaConsumidorFinal = produto.getIcmsSaidaConsumidorFinal();
        Double icmsSaidaConsumidorFinalDouble = Double.parseDouble(produto.getIcmsSaidaConsumidorFinal());
        String icmsCstSadiaConsumidorFinal = produto.getIcmsCstSaidaConsumidorFinal();
        String modalidade = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("opus.modalidade.juridica");

        //t07
        StringBuilder builder = new StringBuilder();

        //Lucro Real
        if (modalidade.equals("lucro_real") || modalidade.equals("lucro_presumido") ) {                                                                  

            //cesta basica
            //isento I00
            if ( icmsCstSadiaConsumidorFinal.equals("040") ) {

                builder.append("UPDATE produto SET trbid = '");
                builder.append(I00);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }
            //nao tributado
            //N00            
            if( icmsCstSadiaConsumidorFinal.equals("041")){
                builder.append("UPDATE produto SET trbid = '");
                builder.append(N00);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            
            }
            //subtituicao tributaria
            //F10
            if(icmsCstSadiaConsumidorFinal.equals("060")){
                builder.append("UPDATE produto SET trbid = '");
                builder.append(F00);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 7) {//0 ou 00               
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T07);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 13) {//0 ou 00               
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T13);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 14) {//0 ou 00               
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T14);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 20) {//0 ou 00               
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T20);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 27) {//0 ou 00               
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T27);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

        }

        //SIMPLES
        if (modalidade.equals("simples")) {

            if (codigoCsosn.equals("300") || codigoCsosn.equals("500")) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(N00);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 0) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(N00);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 7) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T07);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 13) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T13);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 14) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T14);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 20) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T20);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 27) {
                builder.append("UPDATE produto SET trbid = '");
                builder.append(T27);
                builder.append("' WHERE procod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

        }

        
        executeUpdate(builder.toString());

        return true;
    }

    @Override
    public void posProcessamento(Produto produto) throws Exception {
        // logger.debug(null, "PREPROCESSMENTO ");
    }

    @Override
    public boolean atualizarNatureza(Produto produto) throws Exception {
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 14);
        String pisCofinsNaturezaReceita = produto.getPisCofinsNaturezaReceita();
        
        if(pisCofinsNaturezaReceita == null || pisCofinsNaturezaReceita.isEmpty() || pisCofinsNaturezaReceita.trim().equals("")){
            
            logger.debug("PRODUTO %s nao tem natureza de receita ", codigoInterno);
            
        }else{
        
            StringBuilder builder = new StringBuilder();
        
            builder.append("UPDATE produto SET natcodigo = '");
            builder.append(pisCofinsNaturezaReceita);
            builder.append("' WHERE procod = '");
            builder.append(codigoInterno);
            builder.append("';");
            
            //logger.info("entrei  %s", pisCofinsNaturezaReceita);
            executeUpdate(builder.toString());
            
            
            
        
        }
               
        
        return true;
    }

}
