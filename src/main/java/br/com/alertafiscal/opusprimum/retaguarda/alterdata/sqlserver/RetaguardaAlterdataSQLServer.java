package br.com.alertafiscal.opusprimum.retaguarda.alterdata.sqlserver;

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

public class RetaguardaAlterdataSQLServer extends Retaguarda {

    private List<Integer> cfops;
    private List<String> estadosSaida;
    private List<String> estadosEntrada;

    public RetaguardaAlterdataSQLServer() {
        logger.info("Inicializando lista de CFOP's.");
        inicializarListaCFOPs();
        logger.info("Inicializando lista de estados.");
        inicializarListaEstados();
    }

    @Override
    public Map<String, String> mapeiaCodigoInternoEAN() {
        String sql = "SELECT TOP 1 p.IdProduto, cp.CdChamada, p.NmProduto FROM produto p INNER JOIN CodigoProduto cp ON (p.idproduto = cp.idproduto and IdTipoCodigoProduto = '00A0000004')";
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
        inicializarNCM(produto);
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
        Integer icmsCstSaidaInteger = null;
        if (icmsCstSaida != null && !icmsCstSaida.isEmpty()) {
            icmsCstSaidaInteger = Integer.parseInt(icmsCstSaida);
        }
        BigDecimal icmsSaidaBigDecimal = produto.getIcmsSaidaConsumidorFinalBigDecimal();
        String mvaInterno = produto.getMvaInterno();

        if (icmsSaidaBigDecimal == null || icmsCstSaidaInteger == null) {
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
            List<?> resultado = executeQuery("SELECT IdCalculoICMS, CdChamada FROM CalculoICMS"
                    + " WHERE DsCalculoICMS = '" + criaDsCalculoICMS(icmsCstSaidaInteger, icmsSaidaBigDecimal, mvaInterno)
                    + "' AND IdCalculoICMS like '0000F%'");
            if (!resultado.isEmpty()) {
                Object[] dados = (Object[]) resultado.get(0);
                idCalculoICMS = String.valueOf(dados[0]);
                cdChamada = String.valueOf(dados[1]);
                executeUpdate("UPDATE Produto SET IdCalculoICMS = '' WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
                executeUpdate("DELETE FROM CalculoICMS_UF WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
                executeUpdate("DELETE FROM CalculoICMS WHERE IdCalculoICMS = '" + idCalculoICMS + "'");
            }
        }
        if (idCalculoICMS == null) {
            if (contadorIdICMS == 0) {
                List<?> resultado = executeQuery("SELECT MAX(IdCalculoICMS) FROM CalculoICMS WHERE IdCalculoICMS LIKE '0000F%'");
                if (!resultado.isEmpty() && resultado.get(0) != null) {
                    String r = String.valueOf(resultado.get(0)).trim();
                    contadorIdICMS = Integer.parseInt(r.substring(5, 9));
                }
            }
            contadorIdICMS++;
            idCalculoICMS = String.format("0000F%s", StringUtils.leftPad(String.valueOf(contadorIdICMS), 4, '0'));
        }
        if (cdChamada == null) {
            if (contadorCdChamada == 0) {
                List<?> resultado = executeQuery("SELECT MAX(CdChamada) FROM CalculoICMS WHERE IdCalculoICMS LIKE '0000F%'");
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
            executeUpdate(String.format("INSERT INTO CalculoICMS VALUES ('%s', '%s', 'Cálculo para %s%%')", idCalculoICMS, cdChamada, icmsSaidaBigDecimal.intValue()));
            loopcfops:
            for (Integer cfop : cfops) {
                boolean comeca1ou5 = String.valueOf(cfop).charAt(0) == '1' || String.valueOf(cfop).charAt(0) == '5';
                boolean comeca2ou6 = String.valueOf(cfop).charAt(0) == '2' || String.valueOf(cfop).charAt(0) == '6';
                boolean st = String.valueOf(cfop).charAt(1) == '4';
                String strCFOP = Integer.toString(cfop).substring(0, 1) + '.' + Integer.toString(cfop).substring(1, 4);

                //so insete se o segundo digito for diferente de 4 para cst for 000
                if (!st) {
                    for (String ufEntrada : estadosEntrada) {
                        for (String ufSaida : estadosSaida) {
                            if (comeca2ou6 && ufEntrada.equalsIgnoreCase(ufSaida)) {
                                continue;
                            }
                            if (comeca1ou5 || ufEntrada.equalsIgnoreCase(ufSaida)) {
                                executeUpdate(String.format("INSERT INTO CalculoICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, AlICMS, CdSituacaoTributaria) VALUES ('%s', '%s', '%s', '%s', %d, '%s')", idCalculoICMS, ufSaida, ufSaida, strCFOP, icmsSaidaBigDecimal.intValue(), icmsCstSaida.substring(1, 3)));
                                continue loopcfops;
                            }
                            executeUpdate(String.format("INSERT INTO CalculoICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, AlICMS, CdSituacaoTributaria) VALUES ('%s', '%s', '%s', '%s', %d, '%s')", idCalculoICMS, ufEntrada, ufSaida, strCFOP, icmsSaidaBigDecimal.intValue(), icmsCstSaida.substring(1, 3)));
                        }
                    }
                }
            }
        } else if (icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10) {
            calculosICMS060e010.put(mvaInterno, idCalculoICMS);
            executeUpdate(String.format("INSERT INTO CalculoICMS VALUES ('%s', '%s', 'Cálculo para ST MVA %s%%')", idCalculoICMS, cdChamada, mvaInterno));
            loopcfops:
            for (Integer cfop : cfops) {
                boolean comeca1ou5 = String.valueOf(cfop).charAt(0) == '1' || String.valueOf(cfop).charAt(0) == '5';
                boolean comeca2ou6 = String.valueOf(cfop).charAt(0) == '2' || String.valueOf(cfop).charAt(0) == '6';
                boolean st = String.valueOf(cfop).charAt(1) == '4';
                String strCFOP = Integer.toString(cfop).substring(0, 1) + '.' + Integer.toString(cfop).substring(1, 4);
                //so insere se segundo numero for 4 para csts 010 e 060
                if (st) {
                    for (String ufEntrada : estadosEntrada) {
                        for (String ufSaida : estadosSaida) {
                            Integer icmsAtacado = produto.getIcmsAtacadoDecimal(ufEntrada).intValue();
                            String mvaExterno = produto.getMvaExterno(ufEntrada);
                            if (comeca2ou6 && ufEntrada.equalsIgnoreCase(ufSaida)) {
                                continue;
                            }
                            if (comeca1ou5) {
                                executeUpdate(String.format("INSERT INTO CalculoICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, AlLucro%s) VALUES "
                                        + "('%s', '%s', '%s', '%s', '%s', %d, %s%s)",
                                        (st ? ", StCalculaSubstTributariaICMS, TpCalcSubstTribAntecipacaoICMS" : ""), idCalculoICMS, ufSaida, ufSaida, strCFOP, icmsCstSaida.substring(1, 3), icmsAtacado, mvaInterno, (st ? ", 'S', 'L'" : "")));
                                continue loopcfops;
                            }
                            executeUpdate(String.format("INSERT INTO CalculoICMS_UF (IdCalculoICMS, IdUF, IdUFDestino, IdCFOP, CdSituacaoTributaria, AlICMS, AlLucro%s) VALUES "
                                    + "('%s', '%s', '%s', '%s', '%s', %d, %s%s)",
                                    (st ? ", StCalculaSubstTributariaICMS, TpCalcSubstTribAntecipacaoICMS" : ""), idCalculoICMS, ufEntrada, ufSaida, strCFOP, icmsCstSaida.substring(1, 3), icmsAtacado, ufEntrada.equalsIgnoreCase(ufSaida) ? mvaInterno : mvaExterno, (st ? ", 'S', 'L'" : "")));
                        }
                    }
                }
            }
        }
    }

    private String criaDsCalculoICMS(Integer icmsCstSaidaInteger, BigDecimal icmsSaidaBigDecimal, String mvaInterno) {
        if (icmsCstSaidaInteger == 0) {
            return "Cálculo para " + icmsSaidaBigDecimal.intValue() + "%";
        }
        return "Cálculo para ST MVA " + mvaInterno + "%";
    }

    private Map<String, String> calculosNCM = new HashMap<>();
    private int contadorIdClassificacaoFiscal = 0;
    private int contadorCdReduzido = 0;

    private void inicializarNCM(Produto produto) {
        String codigoInterno = produto.getCodigoInterno();
        String ncmLimpo = produto.getNcmLimpo();
        String descricaoNcm = produto.getDescricaoNcm();
        logger.debug("Inicializando cálculos de NCM para: " + codigoInterno);
        if (calculosNCM.containsKey(ncmLimpo)) {
            return;
        }        
        String idClassificacaoFiscal = null;
        String cdReduzido = null;
        {
            List<?> resultado = executeQuery("SELECT IdClassificacaoFiscal, CdReduzido FROM ClassificacaoFiscal WHERE CdClassificacao = '" + ncmLimpo + "'  AND IdClassificacaoFiscal LIKE '0000F%'");
            if (!resultado.isEmpty()) {
                Object[] dados = (Object[]) resultado.get(0);
                idClassificacaoFiscal = String.valueOf(dados[0]);
                cdReduzido = String.valueOf(dados[1]);
                executeUpdate("UPDATE Produto SET IdClassificacaoFiscal = '' WHERE IdClassificacaoFiscal = '" + idClassificacaoFiscal + "'");
                executeUpdate("DELETE FROM ClassificacaoFiscalItem WHERE IdClassificacaoFiscalItem = '" + idClassificacaoFiscal + "'");
                executeUpdate("DELETE FROM ClassificacaoFiscal WHERE IdClassificacaoFiscal = '" + idClassificacaoFiscal + "'");
            }
        }
        if (idClassificacaoFiscal == null) {
            if (contadorIdICMS == 0) {
                List<?> resultado = executeQuery("SELECT MAX(IdClassificacaoFiscal) FROM ClassificacaoFiscal WHERE IdClassificacaoFiscal LIKE '0000F%'");
                if (!resultado.isEmpty() && resultado.get(0) != null) {
                    String r = String.valueOf(resultado.get(0)).trim();
                    contadorIdClassificacaoFiscal = Integer.parseInt(r.substring(6, 10));
                }
            }
            contadorIdClassificacaoFiscal++;
            idClassificacaoFiscal = String.format("0000F%s", StringUtils.leftPad(String.valueOf(contadorIdClassificacaoFiscal), 4, '0'));
        }
        if (cdReduzido == null) {
            if (contadorCdReduzido == 0) {
                List<?> resultado = executeQuery("SELECT MAX(CdReduzido) FROM ClassificacaoFiscal WHERE IdClassificacaoFiscal LIKE '0000F%'");
                if (!resultado.isEmpty() && resultado.get(0) != null) {
                    String r = String.valueOf(resultado.get(0)).trim();
                    contadorCdReduzido = Integer.parseInt(r);
                }
            }
            contadorCdReduzido++;
            cdReduzido = String.format("%s", StringUtils.leftPad(String.valueOf(contadorCdReduzido), 4, '0'));
        }
        executeUpdate(String.format("INSERT INTO ClassificacaoFiscal (IdClassificacaoFiscal, CdReduzido, CdClassificacao, DsClassificacaoFiscal, AlTotalTributosPrecedentes) VALUES ('%s','%s','%s', '%s', NULL)", idClassificacaoFiscal, cdReduzido, ncmLimpo, (descricaoNcm.length() > 50 ? descricaoNcm.substring(0, 49) : descricaoNcm)));
        executeUpdate(String.format("INSERT INTO ClassificacaoFiscalItem VALUES ('%s','%s',NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'S', '49', '99', 'N', NULL, NULL, NULL, '0', '0', NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, '0')", idClassificacaoFiscal, cdReduzido));
        calculosNCM.put(ncmLimpo, idClassificacaoFiscal);
    }

    @Override
    public boolean atualizarNCM(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String ncmLimpo = produto.getNcmLimpo();
        logger.debug("Realizando update no NCM " + ncmLimpo + " para: " + codigoInterno);
        String sql = "UPDATE Produto SET IdClassificacaoFiscal = '"
                + calculosNCM.get(ncmLimpo)
                + "' WHERE IdProduto = '" + codigoInterno + "'";
        executeUpdate(sql);
        return true;
    }

    @Override
    public boolean atualizarCEST(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String cest = produto.getCest();
        String ncmLimpo = produto.getNcmLimpo();
        if (cest != null && !cest.isEmpty()) {
            logger.debug("Realizando update no CEST " + cest + " para: " + codigoInterno);
            List<?> list = executeQuery("SELECT IdCEST FROM CEST WHERE CdCEST = '" + cest + "'");
            if (list.isEmpty()) {
                executeUpdate("INSERT INTO CEST (CdCEST) VALUES ('" + cest + "')");
                list = executeQuery("SELECT IdCEST FROM CEST WHERE CdCEST = '" + cest + "'");
            }
            if (list.size() > 1) {
                logger.warn("Atenção! Existem mais do que um CEST [" + cest + "] cadastrados no banco!");
            }
            String idCest = String.valueOf(list.get(0));
            String sql = "UPDATE Produto SET IdCEST = " + idCest + " WHERE IdProduto = '" + codigoInterno + "'";
            executeUpdate(sql);
            String ncmId = calculosNCM.get(ncmLimpo);
            executeUpdate("UPDATE ClassificacaoFiscal SET IdCEST = " + idCest + " WHERE IdClassificacaoFiscal = '" + ncmId + "'");
            return true;
        }
        return false;
    }

    @Override
    public boolean atualizarIPI(Produto produto) throws Exception {
        String codigoInterno = produto.getCodigoInterno();
        String ipi = produto.getIpi();
        String ncmLimpo = produto.getNcmLimpo();
        logger.debug("Realizando update no IPI " + ipi + " para: " + codigoInterno);
        executeUpdate("UPDATE ClassificacaoFiscalItem SET AlIPI = " + ipi + " WHERE IdClassificacaoFiscal = '" + calculosNCM.get(ncmLimpo) + "'");
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
        String tempCalculaPISCOFINS = simples ? "N" : "S";

        String piscofinsCstEntrada = produto.getPiscofinsCstEntrada();
        Integer piscofinsCstEntradaInteger = Integer.parseInt(piscofinsCstEntrada);
        String piscofinsCstSaida = produto.getPiscofinsCstSaida();
        Integer piscofinsCstSaidaInteger = Integer.parseInt(piscofinsCstSaida);

        logger.debug("Realizando update na Natureza PIS/COFINS para: " + codigoInterno);

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE Produto ");
        builder.append("SET cdsittributpisentrada = '").append(transformaPara2CasasDecimais(piscofinsCstEntradaInteger)).append("', ");
        builder.append("cdsittributcofinsentrada = '").append(transformaPara2CasasDecimais(piscofinsCstEntradaInteger)).append("', ");
        builder.append("cdsittributpissaida = '").append(transformaPara2CasasDecimais(piscofinsCstSaidaInteger)).append("', ");
        builder.append("cdsittributcofinssaida = '").append(transformaPara2CasasDecimais(piscofinsCstSaidaInteger)).append("', ");
        builder.append("alpis = ").append(tempAliquotaPis).append(", ");
        builder.append("alcofins = ").append(tempAliquotaCofins).append(", ");
        builder.append("StCalculaPIS = '").append(tempCalculaPISCOFINS).append("', ");
        builder.append("StCalculaCOFINS = '").append(tempCalculaPISCOFINS).append("' ");
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
        Integer icmsCstSaidaInteger = null;
        if (icmsCstSaida != null && !icmsCstSaida.isEmpty()) {
            icmsCstSaidaInteger = Integer.parseInt(icmsCstSaida);
        }
        if (icmsCstSaidaInteger != null) {
            BigDecimal icmsSaidaBigDecimal = produto.getIcmsSaidaConsumidorFinalBigDecimal();
            String mvaInterno = produto.getMvaInterno();
            logger.debug("Realizando update no ICMS para: " + codigoInterno);
            StringBuilder builder = new StringBuilder();
            if (icmsCstSaidaInteger == 40 || icmsCstSaidaInteger == 41) {
                builder.append("UPDATE produto SET idcalculoicms = '' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            } else if (icmsCstSaidaInteger == 0) {
                String calculoICMS = calculosICMS000.get(icmsSaidaBigDecimal.intValue());
                builder.append("UPDATE produto SET ");
                builder.append("idcalculoicms = '");
                builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            } else if (icmsCstSaidaInteger == 60 || icmsCstSaidaInteger == 10) {
                String calculoICMS = calculosICMS060e010.get(mvaInterno);
                builder.append("UPDATE produto SET ");
                builder.append("idcalculoicms = '");
                builder.append(calculoICMS);
                builder.append("' ");
                builder.append("WHERE idproduto = '").append(codigoInterno).append("'");
            }
            executeUpdate(builder.toString());
        }
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
