package br.com.alertafiscal.opusprimum.retaguarda.alterdata.postgresql;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class RetaguardaAlterdataPostgreSQL extends Retaguarda {

    private List<Integer> cfops;
    private List<String> estadosSaida;
    private List<String> estadosEntrada;

    public RetaguardaAlterdataPostgreSQL() {
        logger.info("Inicializando lista de CFOP's.");
        inicializarListaCFOPs();
        logger.info("Inicializando lista de estados.");
        inicializarListaEstados();
    }

    @Override
    public Map<String, String> mapeiaCodigoInternoEAN() {
        String sql = "SELECT P.IdProduto, CE.DsCodigo, P.nmproduto FROM wshop.Produto P LEFT JOIN wshop.codigos C ON (C.Idproduto =  P.idproduto AND C.TpCodigo ='Chamada') LEFT JOIN wshop.codigos CE ON (CE.Idproduto =  P.idproduto AND CE.TpCodigo ='EAN13') LIMIT 1";
        List<Object[]> lista = (List<Object[]>) executeQuery(sql);
        Map<String, String> mapa = new HashMap<>();
        for (Object[] dados : lista) {
            mapa.put(String.valueOf(dados[0]) + "::" + String.valueOf(dados[1]), String.valueOf(dados[2]));
        }
        return mapa;
    }

    @Override
    public void preProcessamento(Produto produto) {
        inicializarICMS(produto);
    }

    private void inicializarListaCFOPs() {
        logger.info("Buscando lista de CFOPs disponíveis no arquivo de configurações.");
        String cfopsConfigurados = ConfiguracaoContexto.getInstance().getConfiguracao().containsKey("alterdata.cfops") ? ConfiguracaoContexto.getInstance().getConfiguracao().get("alterdata.cfops").toString() : "";

        List<String> cfopsObtidos = Arrays.asList(cfopsConfigurados.split(","));
        cfops = new ArrayList<>();

        logger.info("Iterando por %s CFOP's para inclusão na lista de inteiros com escopo de class.", cfopsObtidos.size());
        cfopsObtidos.stream().forEach(cfop -> cfops.add(Integer.parseInt(cfop.trim())));

        if (cfops.isEmpty()) {
            logger.warn("A lista de CFOP's disponíveis está vazia.");
        }
    }

    private void inicializarListaEstados() {
        logger.info("Buscando lista de estados disponíveis no arquivo de configurações.");
        String estadosEntradaConfigurados = ConfiguracaoContexto.getInstance().getConfiguracao().containsKey("alterdata.estados.entrada") ? ConfiguracaoContexto.getInstance().getConfiguracao().get("alterdata.estados.entrada").toString() : "";
        estadosEntrada = Arrays.asList(estadosEntradaConfigurados.split(","));
        logger.info("Obteve lista de estados de entrada: %s", estadosEntrada);
        if (estadosEntrada.isEmpty()) {
            logger.warn("A lista de estados de entrada disponíveis está vazia.");
        }
        String estadosSaidaConfigurados = ConfiguracaoContexto.getInstance().getConfiguracao().containsKey("alterdata.estados.saida") ? ConfiguracaoContexto.getInstance().getConfiguracao().get("alterdata.estados.saida").toString() : "";
        estadosSaida = Arrays.asList(estadosSaidaConfigurados.split(","));
        logger.info("Obteve lista de estados de saida: %s", estadosSaida);
        if (estadosSaida.isEmpty()) {
            logger.warn("A lista de estados de saida disponíveis está vazia.");
        }
    }

    private Map<Integer, String> calculosICMS000 = new HashMap<>();
    private Map<String, String> calculosICMS060e010 = new HashMap<>();
    private int contadorIdICMS = 0;
    private int contadorCdChamada = 0;

    private void inicializarICMS(Produto produto) {
        String codigoInterno = produto.getCodigoInterno();
        logger.debug("Inicializando cálculos de ICMS para: " + codigoInterno);
        String icmsCstSaida = produto.getIcmsCstSaidaConsumidorFinal();
        Integer icmsCstSaidaInteger = Integer.parseInt(icmsCstSaida);
        BigDecimal icmsSaidaBigDecimal = produto.getIcmsSaidaConsumidorFinalBigDecimal();
        String mvaInterno = produto.getMvaInterno();

        if (icmsSaidaBigDecimal == null || (icmsCstSaidaInteger != 0 && icmsCstSaidaInteger != 10 && icmsCstSaidaInteger != 60)) {
            return;
        }
        if (icmsCstSaidaInteger == 0 && calculosICMS000.containsKey(icmsSaidaBigDecimal.intValue())) {
            return;
        }
        if ((icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10) && calculosICMS060e010.containsKey(mvaInterno)) {
            return;
        }

        String idCalculoICMS = null;
        String cdChamada = null;
        {
            List<?> resultado = executeQuery("SELECT IdCalculoICMS, CdChamada FROM WSHOP.ICMS"
                    + " WHERE DsCalculoICMS = '" + criaDsCalculoICMS(icmsCstSaidaInteger, icmsSaidaBigDecimal, mvaInterno)
                    + "' AND IdCalculoICMS LIKE '999AAA%'");
            if (!resultado.isEmpty()) {
                Object[] dados = (Object[]) resultado.get(0);
                idCalculoICMS = String.valueOf(dados[0]);
                cdChamada = String.valueOf(dados[1]);
                executeUpdate("UPDATE WSHOP.PRODUTO SET IdCalculoICMS = '' WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
                executeUpdate("DELETE FROM WSHOP.ICMS_UF WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
                executeUpdate("DELETE FROM WSHOP.ICMS WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
            }
        }
        if (idCalculoICMS == null) {
            if (contadorIdICMS == 0) {
                List<?> resultado = executeQuery("SELECT MAX(IdCalculoICMS) FROM WSHOP.ICMS WHERE IdCalculoICMS LIKE '999AAA%'");
                if (!resultado.isEmpty() && resultado.get(0) != null) {
                    String r = String.valueOf(resultado.get(0)).trim();
                    contadorIdICMS = Integer.parseInt(r.substring(6, 10));
                }
            }
            contadorIdICMS++;
            idCalculoICMS = String.format("999AAA%s", StringUtils.leftPad(String.valueOf(contadorIdICMS), 4, '0'));
        }
        if (cdChamada == null) {
            if (contadorCdChamada == 0) {
                List<?> resultado = executeQuery("SELECT MAX(CdChamada) FROM WSHOP.ICMS WHERE IdCalculoICMS LIKE '999AAA%'");
                if (!resultado.isEmpty() && resultado.get(0) != null) {
                    String r = String.valueOf(resultado.get(0)).trim();
                    contadorCdChamada = Integer.parseInt(r.substring(2, 6));
                }
            }
            contadorCdChamada++;
            cdChamada = String.format("05%s", StringUtils.leftPad(String.valueOf(contadorCdChamada), 4, '0'));
        }
        if (icmsCstSaidaInteger == 0) {
            calculosICMS000.put(icmsSaidaBigDecimal.intValue(), idCalculoICMS);
            executeUpdate(String.format("INSERT INTO WSHOP.ICMS VALUES ('%s', '%s', 'Cálculo para %s%%', 'A', '')", idCalculoICMS, cdChamada, icmsSaidaBigDecimal.intValue()));
            loopcfops:
            for (Integer cfop : cfops) {
                boolean comeca1ou5 = String.valueOf(cfop).charAt(0) == '1' || String.valueOf(cfop).charAt(0) == '5';
                boolean comeca2ou6 = String.valueOf(cfop).charAt(0) == '2' || String.valueOf(cfop).charAt(0) == '6';
                boolean st = String.valueOf(cfop).charAt(1) == '4';
                //so insete se o segundo digito for diferente de 4 para cst for 000
                //if (!st) {
                    for (String ufEntrada : estadosEntrada) {
                        for (String ufSaida : estadosSaida) {
                            if (comeca2ou6 && ufEntrada.equalsIgnoreCase(ufSaida)) {
                                continue;
                            }
                            if (comeca1ou5 || ufEntrada.equalsIgnoreCase(ufSaida)) {
                                executeUpdate(String.format("INSERT INTO WSHOP.ICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, CdSituacaoTributariaJur%s) VALUES ('%s', '%s', '%s', '%d', '%s', %d, '%s'%s)", (st ? ", StCalculaSubstTributaria" : ""), idCalculoICMS, ufSaida, ufSaida, cfop, "000", icmsSaidaBigDecimal.intValue(), "000", (st ? ", 'T'" : "")));
                                continue loopcfops;
                            }
                            executeUpdate(String.format("INSERT INTO WSHOP.ICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, CdSituacaoTributariaJur%s) VALUES ('%s', '%s', '%s', '%d', '%s', 12, '%s'%s)", (st ? ", StCalculaSubstTributaria" : ""), idCalculoICMS, ufEntrada, ufSaida, cfop, "000", "000", (st ? ", 'T'" : "")));
                        }
                    }
                //}
            }
        } else if (icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10) {
            calculosICMS060e010.put(mvaInterno, idCalculoICMS);
            executeUpdate(String.format("INSERT INTO WSHOP.ICMS VALUES ('%s', '%s', 'Cálculo para ST MVA %s%%', 'A', '')", idCalculoICMS, cdChamada, mvaInterno));
            List<Integer> cfops010 = new ArrayList<Integer>();
            cfops010.add(5403);
            cfops010.add(6403);
            cfops010.add(5401);
            cfops010.add(6401);
            loopcfops:
            for (Integer cfop : cfops) {
                boolean comeca1ou5 = String.valueOf(cfop).charAt(0) == '1' || String.valueOf(cfop).charAt(0) == '5';
                boolean comeca2ou6 = String.valueOf(cfop).charAt(0) == '2' || String.valueOf(cfop).charAt(0) == '6';
                boolean st = String.valueOf(cfop).charAt(1) == '4';
                //so insere se segundo numero for 4 para csts 010 e 060
                //if (st) {
                    String cst = "060";
                    if (cfops010.contains(cfop)) {
                        cst = "010";
                    }
                    for (String ufEntrada : estadosEntrada) {
                        for (String ufSaida : estadosSaida) {
                            Integer icmsAtacado = produto.getIcmsAtacadoDecimal(ufEntrada).intValue();
                            String mvaExterno = produto.getMvaExterno(ufEntrada);
                            if (comeca2ou6 && ufEntrada.equalsIgnoreCase(ufSaida)) {
                                continue;
                            }
                            if (comeca1ou5) {
                                executeUpdate(String.format("INSERT INTO WSHOP.ICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, AlLucro, CdSituacaoTributariaJur%s) VALUES ('%s', '%s', '%s', '%d', '%s', %d, %s, '%s'%s)", (st ? ", StCalculaSubstTributaria" : ""), idCalculoICMS, ufSaida, ufSaida, cfop, cst, icmsAtacado, mvaInterno, cst, (st ? ", 'T'" : "")));
                                continue loopcfops;
                            }
                            executeUpdate(String.format("INSERT INTO WSHOP.ICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, AlLucro, CdSituacaoTributariaJur%s) VALUES ('%s', '%s', '%s', '%d', '%s', %d, %s, '%s'%s)", (st ? ", StCalculaSubstTributaria" : ""), idCalculoICMS, ufEntrada, ufSaida, cfop, cst, icmsAtacado, ufEntrada.equalsIgnoreCase(ufSaida) ? mvaInterno : mvaExterno, cst, (st ? ", 'T'" : "")));
                        }
                    }
                //}
            }
        }
    }

    private String criaDsCalculoICMS(Integer icmsCstSaidaInteger, BigDecimal icmsSaidaBigDecimal, String mvaInterno) {
        if (icmsCstSaidaInteger == 0) {
            return "Cálculo para " + icmsSaidaBigDecimal.intValue() + "%";
        }
        return "Cálculo para ST MVA " + mvaInterno + "%";
    }

    @Override
    public boolean atualizarNCM(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String ncmLimpo = produto.getNcmLimpo();
        String ncmDoisPrimeirosDigitos = produto.getNcmDoisPrimeirosDigitos();
        logger.debug("Realizando update no NCM " + ncmLimpo + " para: " + codigoInterno);
        String sql = "UPDATE WSHOP.produto SET cdtratamentoespecial = 'A', cdipi = '" + ncmLimpo + "', cdgenero = '" + ncmDoisPrimeirosDigitos + "' WHERE idproduto = '" + codigoInterno + "'";
        int retorno = executeUpdate(sql);        
        logger.debug("retorno update %d", retorno);
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

    @Override
    public boolean atualizarPISCOFINS(Produto produto) throws Exception {
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

    private String transformaPara2CasasDecimais(int valor) {
        if (valor < 10) {
            return "0" + valor;
        }
        return String.valueOf(valor);
    }

    @Override
    public boolean atualizarICMS(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String icmsCstSaida = produto.getIcmsCstSaidaConsumidorFinal();
        Integer icmsCstSaidaInteger = Integer.parseInt(icmsCstSaida);
        BigDecimal icmsSaidaBigDecimal = produto.getIcmsSaidaConsumidorFinalBigDecimal();
        String mvaInterno = produto.getMvaInterno();
        logger.debug("Realizando update no ICMS para: " + codigoInterno);
        StringBuilder builder = new StringBuilder();
        if (icmsCstSaidaInteger == 40 || icmsCstSaidaInteger == 41) {
            // CST Isento ou Não Tributado
            builder.append("UPDATE WSHOP.produto SET TRIBUTACAO = 'I', idcalculoicms = '' ");
            builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
        } else if (icmsCstSaidaInteger == 0) {
            String calculoICMS = calculosICMS000.get(icmsSaidaBigDecimal.intValue());
            builder.append("UPDATE WSHOP.produto SET ");
            builder.append("TRIBUTACAO = 'T', ");
            builder.append("idcalculoicms = '");
            builder.append(calculoICMS);
            builder.append("' ");
            builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
        } else if (icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10) {
            String calculoICMS = calculosICMS060e010.get(mvaInterno);
            builder.append("UPDATE WSHOP.produto SET ");
            builder.append("TRIBUTACAO = 'S', ");
            builder.append("idcalculoicms = '");
            builder.append(calculoICMS);
            builder.append("' ");
            builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
        }
        executeUpdate(builder.toString());
        return true;
    }

    @Override
    public void posProcessamento(Produto produto) throws Exception {

    }

    @Override
    public boolean atualizarNatureza(Produto produto) throws Exception {
        return true;
    }
}
