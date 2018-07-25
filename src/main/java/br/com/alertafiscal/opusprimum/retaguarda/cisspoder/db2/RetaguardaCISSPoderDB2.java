//package br.com.alertafiscal.opusprimum.retaguarda.cisspoder.db2;
//
//import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
//import br.com.alertafiscal.opusprimum.xml.Revisao;
//import br.com.alertafiscal.opusprimum.xml.Segmento;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import org.hibernate.SQLQuery;
//import org.hibernate.Session;
//
//public class RetaguardaCISSPoderDB2 extends Retaguarda {
//
//    @Override
//    public boolean salvar(Salvavel salvavel) {
//        logger.debug("Criando objeto de resumo de alterações.");
//        resumoAlteracoes = new ResumoAlteracoes();
//        resumoAlteracoes.setCodigoProduto(salvavel._getCodigoInterno());
//        resumoAlteracoes.setDescricaoProduto(salvavel._getDescricao());
//
//        Session session = null;
//
//        try {
//            logger.debug("Obtendo a session do Hibernate.");
//            session = HibernateContext.getSession();
//
//            logger.debug("Abrindo transação.");
//            session.beginTransaction();
//
//            if (ConfiguracaoContext.getInstance().isAtualizarNCM()) {
//                logger.debug("Realizando update no NCM.");
//                atualizarNCM((Segmento) salvavel, session);
//            }
//
//            if (ConfiguracaoContext.getInstance().isAtualizarICMS()) {
//                logger.debug("Realizando update na situação tributária do ICMS de entrada.");
//                atualizarSituacaoTributariaICMS((Segmento) salvavel, "ent", session);
//
//                logger.debug("Realizando update na situação tributária do ICMS de saída.");
//                atualizarSituacaoTributariaICMS((Segmento) salvavel, "sai", session);
//
//                logger.debug("Realizando update no ICMS de entrada.");
//                atualizarICMS((Segmento) salvavel, "ent", session);
//
//                logger.debug("Realizando update no ICMS de saída.");
//                atualizarICMS((Segmento) salvavel, "sai", session);
//            }
//
//            if (ConfiguracaoContext.getInstance().isAtualizarIPI()) {
//                logger.debug("Realizando update no IPI.");
//                atualizarIPI((Segmento) salvavel, session);
//            }
//
//            if (ConfiguracaoContext.getInstance().isAtualizarPisCofins()) {
//                logger.debug("Realizando update na Natureza PIS/COFINS.");
//                atualizarNaturezaPisCofins((Segmento) salvavel, session);
//
//                logger.debug("Realizando update no CST do PIS/COFINS de entrada.");
//                atualizarCstPisCofins((Segmento) salvavel, "entrada", session);
//
//                logger.debug("Realizando update no CST do PIS/COFINS de saída.");
//                atualizarCstPisCofins((Segmento) salvavel, "saida", session);
//
//                logger.debug("Realizando update no PIS de entrada.");
//                atualizarPisCofins((Segmento) salvavel, "pis", "", session);
//
//                logger.debug("Realizando update no PIS de saída.");
//                atualizarPisCofins((Segmento) salvavel, "pis", "saida", session);
//
//                logger.debug("Realizando update no COFINS de entrada.");
//                atualizarPisCofins((Segmento) salvavel, "cofins", "", session);
//
//                logger.debug("Realizando update no COFINS de saída.");
//                atualizarPisCofins((Segmento) salvavel, "cofins", "saida", session);
//            }
//
//            logger.debug("Comitando transação.");
//            session.getTransaction().commit();
//
//            logger.debug("Encerrando a sessão.");
//            session.close();
//
//            if (resumoAlteracoes.isContemAlteracoes()) {
//                logger.debug("O resumo contém alterações... enviando!");
//                enviarResumoAlteracoes(resumoAlteracoes);
//            }
//        } catch (Exception ex) {
//            new EncerradorSessaoHelper().encerrarSessaoEmExcecoes(session);
//
//            logger.exception(ex, "Erro ao salvar tributacao ({}).", salvavel._getCodigoInterno());
//            comunicarListenersErroSalvar(salvavel);
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public List listarPisCofinsCodigosNaturezaReceita() {
//        final List pisCofinsCodigosNaturezaReceita = new ArrayList();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                logger.debug("Obtendo a session do Hibernate.");
//                session = HibernateContext.getSession();
//
//                logger.debug("Abrindo transação.");
//                session.beginTransaction();
//
//                StringBuilder builder = new StringBuilder();
//                builder.append("SELECT * from DBA.PISCOFINS_CODIGO_NATUREZA_RECEITA");
//
//                logger.debug("Criando query: {}.", builder.toString());
//                SQLQuery query = session.createSQLQuery(builder.toString());
//
//                logger.debug("Listando registros.");
//                pisCofinsCodigosNaturezaReceita.addAll(query.list());
//            }
//        }.executar();
//
//        return pisCofinsCodigosNaturezaReceita;
//    }
//
//    @Override
//    public List<ChaveValor> listarCSTICMSs() {
//        final List<ChaveValor> cstsicms = new ArrayList<>();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                logger.debug("Criando query");
//                SQLQuery query = session.createSQLQuery("SELECT idsubproduto, idsittribent FROM dba.produto_tributacao_estado WHERE UFORIGEM='RJ' AND UF='RJ' ORDER BY idsubproduto");
//
//                logger.debug("Listando CSTs de ICMSs.");
//                List<Object[]> registros = query.list();
//
//                String idsubproduto, csticms;
//                logger.debug("Iterando por {} registros.", registros.size());
//                for (Object[] registro : registros) {
//                    idsubproduto = String.valueOf(registro[0]);
//                    csticms = String.valueOf(registro[1]);
//
//                    cstsicms.add(new ChaveValor(String.valueOf(idsubproduto), csticms));
//                }
//            }
//        }.executar();
//
//        return cstsicms;
//    }
//
//    @Override
//    public List<ChaveValor> listarNCMs() {
//        final List<ChaveValor> ncms = new ArrayList<>();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                logger.debug("Criando query");
//                SQLQuery query = session.createSQLQuery("SELECT idsubproduto, ncm FROM dba.produto_grade ORDER BY idsubproduto");
//
//                logger.debug("Listando NCMs.");
//                List<Object[]> registros = query.list();
//
//                Integer idsubproduto;
//                String ncm;
//                logger.debug("Iterando por {} registros.", registros.size());
//                for (Object[] registro : registros) {
//                    idsubproduto = (Integer) registro[0];
//                    ncm = (String) registro[1];
//
//                    ncm = ncm != null && !ncm.isEmpty() ? ncm.replaceAll("\\.", "") : "N/D";
//                    ncms.add(new ChaveValor(String.valueOf(idsubproduto), ncm));
//                }
//            }
//        }.executar();
//
//        return ncms;
//    }
//
//    @Override
//    public List<ChaveValor> listarPISCOFINS() {
//        final List<ChaveValor> cstspiscofinsentrada = new ArrayList<ChaveValor>();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                logger.debug("Criando query");
//                SQLQuery query = session.createSQLQuery("SELECT pg.idsubproduto, p.idcstpiscofinsentrada FROM dba.produto p INNER JOIN dba.produto_grade pg ON p.idproduto = pg.idproduto");
//
//                logger.debug("Listando PIS/COFINS.");
//                List<Object[]> registros = query.list();
//
//                Integer idsubproduto;
//                Integer cstpiscofinsentrada;
//                logger.debug("Iterando por {} registros.", registros.size());
//                for (Object[] registro : registros) {
//                    idsubproduto = (Integer) registro[0];
//                    cstpiscofinsentrada = (Integer) registro[1];
//
//                    cstspiscofinsentrada.add(new ChaveValor(String.valueOf(idsubproduto), String.valueOf(cstpiscofinsentrada)));
//                }
//            }
//        }.executar();
//
//        return cstspiscofinsentrada;
//    }
//
//    /**
//     * <p>
//     * Retorna objetos de {@link Extracao}, utilizados para realizar as
//     * exportações dos produtos para revisão do Alerta Fiscal.</p>
//     *
//     * @param codigosInternos
//     * @return objeto de {@link Extracao}.
//     */
//    @Override
//    public List<Extracao> obterExtracoes(final String... codigosInternos) {
//        final List<Extracao> extracoes = new ArrayList<>();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                Extracao extracao;
//                for (String codigoInterno : codigosInternos) {
//                    extracao = new Extracao();
//                    extracao.setCodigoInterno(codigoInterno);
//
//                    logger.debug("Criando query");
//                    SQLQuery query = session.createSQLQuery("SELECT idsubproduto, idcodbarprod, descrresproduto, ncm FROM dba.produto_grade WHERE idsubproduto = :codigoInterno FETCH FIRST 1 ROWS ONLY");
//
//                    logger.debug("Setando codigoInterno = {}.", codigoInterno);
//                    query.setParameter("codigoInterno", codigoInterno);
//
//                    logger.debug("Executando query = {}.", query);
//                    Object[] retorno = (Object[]) query.uniqueResult();
//
//                    if (retorno != null) {
//                        logger.debug("Setando os valores de dba.produto_grade no objeto da extração.");
//                        extracao.setCodigoEan(String.valueOf(retorno[1]));
//                        extracao.setDescricaoProduto(String.valueOf(retorno[2]));
//                        extracao.setNcm(String.valueOf(retorno[3]));
//                    }
//
//                    extracoes.add(extracao);
//                }
//            }
//        }.executar();
//
//        return extracoes;
//    }
//
//    /**
//     * <p>
//     * Sabe-se que na tabela {@code produto_grade}, cada EAN equivale a um
//     * subproduto. Este método retorna o ID do subproduto para um dado código
//     * EAN (código de barras).</p>
//     *
//     * <p>
//     * Caso o código EAN não retorne resultados, o método retorna
//     * {@code null}.</p>
//     *
//     * @param ean
//     * @return o ID do subproduto ou {@code null}.
//     */
//    @Override
//    public Revisao obterRevisaoProdutoPorEan(final String ean) {
//        final Revisao revisao = new Revisao();
//
//        new TransacaoWrapper() {
//            @Override
//            void transacao(Session session) {
//                logger.debug("Criando query");
//                SQLQuery query = session.createSQLQuery("SELECT idsubproduto, idcodbarprod, descrresproduto FROM dba.produto_grade WHERE idcodbarprod = :ean FETCH FIRST 1 ROWS ONLY");
//
//                logger.debug("Setando EAN = {}.", ean);
//                query.setParameter("ean", ean);
//
//                logger.debug("Executando query = {}.", query);
//                Object[] retorno = (Object[]) query.uniqueResult();
//
//                if (retorno != null) {
//                    revisao.setCodigoInterno(String.valueOf(retorno[0]));
//                    revisao.setCodigoEan(String.valueOf(retorno[1]));
//                    revisao.setDescricaoProduto(String.valueOf(retorno[2]));
//                }
//            }
//        }.executar();
//
//        return revisao;
//    }
//
//    /**
//     * <p>
//     * No CISS, o ID que representa o produto em dba.produto se encontra na
//     * tabela dba.produto_grade fazendo parte da chave composta com o ID do
//     * subproduto. Já no XML do Figura Fiscal, o código interno do produto é o
//     * id do subproduto de dba.produto_grade. Sendo assim, precisamos consultar
//     * na tabela produto_grade para obter o ID do produto pra daí sim atualizar
//     * seus campos.</p>
//     *
//     * @param segmento
//     * @param session
//     * @return ID do produto na tabela dba.produto_grade
//     */
//    private String obterIdProduto(Segmento segmento, Session session) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT idproduto FROM dba.produto WHERE idproduto = (SELECT idproduto FROM dba.produto_grade WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//        builder.append(") FETCH FIRST 1 ROWS ONLY");
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Obtendo ID.");
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foi encontrado um correspondente na tabela de produtos para '{}'.", segmento.getRevisao().getCodigoInterno());
//            return "-1";
//        }
//
//        return String.valueOf(registros.get(0));
//    }
//
//    private int atualizarNCM(Segmento segmento, Session session) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT ncm FROM dba.produto_grade WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando NCMs.");
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados NCMs para o produto '{}'.", segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        logger.debug("Inicializando lista de NCMs encontrados.");
//        List<String> ncmsEncontrados = new ArrayList<>();
//
//        if (registros.size() > 1) {
//            loggerWarns.warn("Recuperando NCM dentro de uma lista de NCMs para o produto: {} (possivel duplicidade de registros).", segmento.getRevisao().getCodigoInterno());
//            for (Object registro : registros) {
//                String ncm = (String) registro;
//
//                logger.debug("Removendo os pontos do NCM '{}'.", ncm);
//                ncm = ncm.replace(".", "");
//
//                ncmsEncontrados.add(ncm);
//            }
//        } else {
//            ncmsEncontrados.add((String) registros.get(0));
//        }
//        logger.debug("NCMs encontrados para o produto '{}': {}.", segmento.getRevisao().getCodigoInterno(), ncmsEncontrados);
//
//        String ncmAtualizado = segmento.getDetalhes().getNcm();
//        logger.debug("NCM encontrado no segmento: {}.", ncmAtualizado);
//
//        logger.debug("Removendo os pontos do NCM atualizado '{}'.", ncmAtualizado);
//        ncmAtualizado = ncmAtualizado.replace(".", "");
//
//        logger.debug("Comparando a lista {} com o NCM atualizado '{}'.", ncmsEncontrados, ncmAtualizado);
//        boolean existeDiferenca = false;
//        for (String ncmEncontrado : ncmsEncontrados) {
//            if (!ncmEncontrado.equals(ncmAtualizado)) {
//                existeDiferenca = true;
//                break;
//            }
//        }
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && !existeDiferenca) {
//            logger.debug("Não existem divergências nos CNMs do produto '{}'.", segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto_grade SET ncm = '");
//        builder.append(ncmAtualizado);
//        builder.append("' WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//
//        logger.info("Criando query de atualização do NCM: {}.", builder.toString());
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        resumoAlteracoes.setNcmAntigo(ncmsEncontrados.toString());
//        resumoAlteracoes.setNcmAtualizado(ncmAtualizado);
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    private int atualizarICMS(Segmento segmento, String complemento, Session session) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT pericm");
//        builder.append(complemento);
//        builder.append(" FROM dba.produto_tributacao_estado WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//        builder.append(" AND uf = 'RJ' AND uforigem = 'RJ'");
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando pericm{}.", complemento);
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados registros de ICMS para o produto '{}'.", segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        /*
//         * Este trecho é necessário pois na tabela de tributação por estado
//         * (dba.produto_tributacao_estado) o mesmo produto pode aparecer muitas
//         * vezes. Sendo assim precisaremos atualizar todos os valores.
//         */
//        List<BigDecimal> icmsEncontrados = new ArrayList<BigDecimal>();
//
//        if (registros.size() == 1) {
//            logger.debug("ICMS({}) encontrado: {}.", complemento, registros.get(0));
//            icmsEncontrados.add((BigDecimal) registros.get(0));
//        } else {
//            loggerWarns.warn("Recuperando ICMSs dentro de uma lista de ICMSs para o produto: {} (possivel duplicidade de registros).", segmento.getRevisao().getCodigoInterno());
//            for (Object registro : registros) {
//                BigDecimal cst = (BigDecimal) registro;
//
//                icmsEncontrados.add(cst);
//            }
//        }
//
//        BigDecimal icmsAtualizado;
//        logger.debug("Obtendo ICMS a partir do arquivo XML.");
//        if (Integer.valueOf("60").equals(Integer.valueOf(segmento.getSaida().getIcmsCstSaida()))) {
//            icmsAtualizado = new BigDecimal(segmento.getEntrada().getIcmsInterno());
//        } else {
//            icmsAtualizado = new BigDecimal(segmento.getSaida().getIcmsSaida());
//        }
//
//        logger.debug("Comparando a lista {} com o ICMS atualizado '{}'.", icmsEncontrados, icmsAtualizado);
//        boolean existeDiferenca = false;
//        for (BigDecimal icmsEncontrado : icmsEncontrados) {
//            if (!icmsEncontrado.equals(icmsAtualizado)) {
//                existeDiferenca = true;
//                break;
//            }
//        }
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && !existeDiferenca) {
//            logger.debug("Não existem divergências nos ICMS({}) do produto '{}'.", complemento, segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        logger.debug("Obtendo tipo de situação tributária através do mapeamento de '{}'.", segmento.getSaida().getIcmsSituacao());
//        Character tipoSituacaoTributaria = TipoSituacaoTributariaICMSConversor.getInstance().getSituacaoTributariaReal(segmento.getSaida().getIcmsSituacao());
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto_tributacao_estado SET pericm");
//        builder.append(complemento);
//        builder.append(" = ");
//        builder.append(icmsAtualizado);
//        builder.append(", tiposittrib");
//        builder.append(complemento);
//        builder.append(" = '");
//        builder.append(tipoSituacaoTributaria);
//        builder.append("' WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//        builder.append(" AND uf = 'RJ' AND uforigem = 'RJ'");
//
//        logger.info("Criando query de atualização do ICMS: {} para o produto '{}'.", builder.toString(), segmento.getRevisao().getCodigoInterno());
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        if ("ent".equals(complemento)) {
//            resumoAlteracoes.setIcmsEntradaAntigo(icmsEncontrados.toString());
//            resumoAlteracoes.setIcmsEntradaAtualizado(String.valueOf(icmsAtualizado));
//        } else {
//            resumoAlteracoes.setIcmsSaidaAntigo(icmsEncontrados.toString());
//            resumoAlteracoes.setIcmsSaidaAtualizado(String.valueOf(icmsAtualizado));
//        }
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    private int atualizarSituacaoTributariaICMS(Segmento segmento, String complemento, Session session) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT idsittrib");
//        builder.append(complemento);
//        builder.append(" FROM dba.produto_tributacao_estado WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando idsittrib{}.", complemento);
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados registros de ICMS para o produto '{}'.", segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        /*
//         * Este trecho é necessário pois na tabela de tributação por estado
//         * (dba.produto_tributacao_estado) o mesmo produto pode aparecer muitas
//         * vezes. Sendo assim precisaremos atualizar todos os códigos.
//         */
//        List<Integer> cstsEncontrados = new ArrayList<>();
//
//        if (registros.size() == 1) {
//            logger.debug("CST({}) encontrado: {}.", complemento, registros.get(0));
//            cstsEncontrados.add((Integer) registros.get(0));
//        } else {
//            loggerWarns.warn("Recuperando CSTs dentro de uma lista de CSTs para o produto: {} (possivel duplicidade de registros).", segmento.getRevisao().getCodigoInterno());
//            for (Object registro : registros) {
//                Integer cst = (Integer) registro;
//
//                cstsEncontrados.add(cst);
//            }
//        }
//
//        logger.debug("Obtendo CST do ICMS a partir do arquivo XML.");
//        Integer cstAtualizado = Integer.valueOf(segmento.getSaida().getIcmsCstSaida());
//
//        logger.debug("Comparando a lista {} com o CST atualizado '{}'.", cstsEncontrados, cstAtualizado);
//        boolean existeDiferenca = false;
//        for (Integer cstEncontrado : cstsEncontrados) {
//            if (!cstEncontrado.equals(cstAtualizado)) {
//                existeDiferenca = true;
//                break;
//            }
//        }
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && !existeDiferenca) {
//            logger.debug("Não existem divergências nos CST({}) do produto '{}'.", complemento, segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto_tributacao_estado SET idsittrib");
//        builder.append(complemento);
//        builder.append(" = ");
//        builder.append(cstAtualizado);
//        builder.append(" WHERE idsubproduto = ");
//        builder.append(segmento.getRevisao().getCodigoInterno());
//
//        logger.info("Criando query de atualização do CST: {} para o produto '{}'.", builder.toString(), segmento.getRevisao().getCodigoInterno());
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        if ("ent".equals(complemento)) {
//            resumoAlteracoes.setCstIcmsEntradaAntigo(cstsEncontrados.toString());
//            resumoAlteracoes.setCstIcmsEntradaAtualizado(String.valueOf(cstAtualizado));
//        } else {
//            resumoAlteracoes.setCstIcmsSaidaAntigo(cstsEncontrados.toString());
//            resumoAlteracoes.setCstIcmsSaidaAtualizado(String.valueOf(cstAtualizado));
//        }
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    private int atualizarIPI(Segmento segmento, Session session) {
//        String idProduto = obterIdProduto(segmento, session);
//
//        if ("-1".equals(idProduto)) {
//            logger.info("O produto '{}' ({}) não se encontra em produto_grade.", segmento.getRevisao().getDescricaoProduto(), segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT peripi FROM dba.produto WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando peripi.");
//        List registros = query.list();
//
//        logger.debug("Convertendo IPI {} para BigDecimal.", registros.get(0));
//        BigDecimal ipiEncontrado = (BigDecimal) registros.get(0);
//
//        logger.debug("Convertendo IPI atualizado {} para BigDecimal.", segmento.getSaida().getIpi());
//        BigDecimal ipiAtualizado = new BigDecimal(segmento.getSaida().getIpi());
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && ipiEncontrado.doubleValue() == ipiAtualizado.doubleValue()) {
//            logger.debug("Não existem divergências no IPI({}) do produto '{}'.", ipiEncontrado, segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto SET peripi = ");
//        builder.append(ipiAtualizado);
//        builder.append(" WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.info("Criando query de atualização do CST: {} para o produto '{}'.", builder.toString(), segmento.getRevisao().getCodigoInterno());
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        resumoAlteracoes.setIpiAntigo(String.valueOf(ipiEncontrado));
//        resumoAlteracoes.setIpiAtualizado(String.valueOf(ipiAtualizado));
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    private int atualizarNaturezaPisCofins(Segmento segmento, Session session) {
//        String idProduto = obterIdProduto(segmento, session);
//
//        if ("-1".equals(idProduto)) {
//            logger.info("O produto '{}' ({}) não se encontra em produto_grade.", segmento.getRevisao().getDescricaoProduto(), segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT idnaturezapiscofins FROM dba.produto WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando idnaturezapiscofins.");
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados registros de natureza PIS/COFINS para o produto '{}'.", idProduto);
//            return 0;
//        }
//
//        logger.debug("Convertendo naturezaPisCofins encontrada {} para Integer.", registros.get(0));
//        Integer naturezaPisCofinsEncontrada = registros.get(0) == null ? -1 : Integer.valueOf(String.valueOf(registros.get(0)));
//
//        logger.debug("Convertendo naturezaPisCofins atualizada {} para Integer.", segmento.getFederal().getPiscofinsNatRec());
//        Integer naturezaPisCofinsAtualizada = segmento.getFederal().getPiscofinsNatRec().trim().isEmpty() ? -1 : Integer.valueOf(segmento.getFederal().getPiscofinsNatRec());
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && naturezaPisCofinsEncontrada.equals(naturezaPisCofinsAtualizada)) {
//            logger.debug("Não existem divergências na natureza PIS/COFINS do produto '{}'.", idProduto);
//            return 0;
//        }
//
//        logger.debug("Recuperando piscofinsCstSaida atualizado {}.", segmento.getFederal().getPiscofinsCstSaida());
//        Integer piscofinsCstSaida = segmento.getFederal().getPiscofinsCstSaida().trim().isEmpty() ? -1 : Integer.valueOf(segmento.getFederal().getPiscofinsCstSaida());
//
//        /*
//         * Descobriu-se que nas tabelas do CISS que o código da natureza não é a
//         * chave primária correta. Isto é, o código de natureza da receita 119,
//         * por exemplo, não tem o valor 119 como chave do campo
//         * idnaturezapiscofins. Dessa forma, é necessário buscar em um
//         * mapeamento adicional para descobrir qual é o verdadeiro ID da
//         * natureza da receita correspondente e é isso que o método abaixo faz.
//         */
//        logger.debug("Recuperando o ID do código de natureza da tabela do CISS com naturezaPisCofinsAtualizada={} e piscofinsCstSaida={}.", naturezaPisCofinsAtualizada, piscofinsCstSaida);
//        Integer idRealNaturezaReceita = CodigoNaturezaReceitaConversor.getInstance().getIdReal(naturezaPisCofinsAtualizada, piscofinsCstSaida);
//
//        logger.debug("Convertendo os valores iguais a -1 para null (por conta das queries de update).");
//        naturezaPisCofinsEncontrada = naturezaPisCofinsEncontrada.equals(-1) ? null : naturezaPisCofinsEncontrada;
//        idRealNaturezaReceita = idRealNaturezaReceita.equals(-1) ? null : idRealNaturezaReceita;
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto SET idnaturezapiscofins = ");
//        builder.append(idRealNaturezaReceita);
//        builder.append(" WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.info("Criando query de atualização da natureza PIS/COFINS: {} para o produto '{}'.", builder.toString(), idProduto);
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        resumoAlteracoes.setNaturezaPisCofinsAntigo(String.valueOf(naturezaPisCofinsEncontrada));
//        resumoAlteracoes.setNaturezaPisCofinsAtualizado(String.valueOf(naturezaPisCofinsAtualizada));
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    private int atualizarCstPisCofins(Segmento segmento, String complemento, Session session) {
//        String idProduto = obterIdProduto(segmento, session);
//
//        if ("-1".equals(idProduto)) {
//            logger.info("O produto '{}' ({}) não se encontra em produto_grade.", segmento.getRevisao().getDescricaoProduto(), segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT idcstpiscofins");
//        builder.append(complemento);
//        builder.append(" FROM dba.produto WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando idcstpiscofins{}.", complemento);
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados registros de CST PIS/COFINS para o produto '{}'.", idProduto);
//            return 0;
//        }
//
//        logger.debug("Convertendo idcstpiscofins{} encontrado {} para Integer.", complemento, registros.get(0));
//        Integer cstPisCofinsEncontrado = registros.get(0) == null ? -1 : Integer.valueOf(String.valueOf(registros.get(0)));
//
//        //O CST varia de acordo com o complemento (entrada ou saída);
//        String cstPisCofinsAtualizadoString = "entrada".equals(complemento) ? segmento.getFederal().getPiscofinsCstEntrada() : segmento.getFederal().getPiscofinsCstSaida();
//
//        logger.debug("Convertendo idcstpiscofins{} atualizado {} para Integer.", complemento, cstPisCofinsAtualizadoString);
//        Integer cstPisCofinsAtualizado = cstPisCofinsAtualizadoString.trim().isEmpty() ? -1 : Integer.valueOf(cstPisCofinsAtualizadoString);
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && cstPisCofinsEncontrado.equals(cstPisCofinsAtualizado)) {
//            logger.debug("Não existem divergências em idcstpiscofins{} do produto '{}'.", complemento, idProduto);
//            return 0;
//        }
//
//        logger.debug("Convertendo os valores iguais a -1 para null (por conta das queries de update).");
//        cstPisCofinsEncontrado = cstPisCofinsEncontrado.equals(-1) ? null : cstPisCofinsEncontrado;
//        cstPisCofinsAtualizado = cstPisCofinsAtualizado.equals(-1) ? null : cstPisCofinsAtualizado;
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto SET idcstpiscofins");
//        builder.append(complemento);
//        builder.append(" = ");
//        builder.append(cstPisCofinsAtualizado);
//        builder.append(" WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.info("Criando query de atualização de idcstpiscofins: {} para o produto '{}'.", builder.toString(), idProduto);
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        if ("entrada".equals(complemento)) {
//            resumoAlteracoes.setPiscofinsCstEntradaAntigo(String.valueOf(cstPisCofinsEncontrado));
//            resumoAlteracoes.setPiscofinsCstEntradaAtualizado(String.valueOf(cstPisCofinsAtualizado));
//        } else {
//            resumoAlteracoes.setPiscofinsCstSaidaAntigo(String.valueOf(cstPisCofinsEncontrado));
//            resumoAlteracoes.setPiscofinsCstSaidaAtualizado(String.valueOf(cstPisCofinsAtualizado));
//        }
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    /**
//     * @param segmento
//     * @param identificacao envie "pis" ou "cofins".
//     * @param complemento envie "" para entrada ou "saida" (estes valores serão
//     * concatenados para se adequarem às colunas do banco).
//     * @param session
//     * @return
//     */
//    private int atualizarPisCofins(Segmento segmento, String identificacao, String complemento, Session session) {
//        String idProduto = obterIdProduto(segmento, session);
//
//        if ("-1".equals(idProduto)) {
//            logger.info("O produto '{}' ({}) não se encontra em produto_grade.", segmento.getRevisao().getDescricaoProduto(), segmento.getRevisao().getCodigoInterno());
//            return 0;
//        }
//
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT per");
//        builder.append(identificacao);
//        builder.append(complemento);
//        builder.append(" FROM dba.produto WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.debug("Criando query: {}.", builder.toString());
//        SQLQuery query = session.createSQLQuery(builder.toString());
//
//        logger.debug("Listando per{}{}.", identificacao, complemento);
//        List registros = query.list();
//
//        if (registros.isEmpty()) {
//            logger.debug("Não foram encontrados registros de PIS/COFINS para o produto '{}'.", idProduto);
//            return 0;
//        }
//
//        logger.debug("Convertendo Listando per{}{} encontrado para BigDecimal.", identificacao, complemento);
//        BigDecimal valorEncontrado = (BigDecimal) registros.get(0);
//
//        //O CST varia de acordo com o identificador ("pis" ou "cofins") e o complemento ("" ou "saída");
//        String valorAtualizadoString;
//        if ("pis".equals(identificacao)) {
//            if (complemento.isEmpty()) {
//                valorAtualizadoString = segmento.getFederal().getPisEntrada();
//            } else {
//                valorAtualizadoString = segmento.getFederal().getPisSaida();
//            }
//        } else {
//            if (complemento.isEmpty()) {
//                valorAtualizadoString = segmento.getFederal().getCofinsEntrada();
//            } else {
//                valorAtualizadoString = segmento.getFederal().getCofinsSaida();
//            }
//        }
//
//        logger.debug("Convertendo per{}{} atualizado para Integer.", identificacao, complemento);
//        BigDecimal valorAtualizado = valorAtualizadoString.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(valorAtualizadoString);
//
//        if (!ConfiguracaoContext.getInstance().isForcarAtualizacoes() && valorEncontrado.doubleValue() == valorAtualizado.doubleValue()) {
//            logger.debug("Não existem divergências em per{}{} do produto '{}'.", identificacao, complemento);
//            return 0;
//        }
//
//        builder = new StringBuilder();
//        builder.append("UPDATE dba.produto SET per");
//        builder.append(identificacao);
//        builder.append(complemento);
//        builder.append(" = ");
//        builder.append(valorAtualizado);
//        builder.append(" WHERE idproduto = ");
//        builder.append(idProduto);
//
//        logger.info("Criando query de atualização de per{}{}: para o produto '{}'.", new Object[]{identificacao, complemento, idProduto});
//        query = session.createSQLQuery(builder.toString());
//
//        logger.info("Executando query '{}'.", query);
//        int linhas = query.executeUpdate();
//
//        logger.debug("Atualizando resumo.");
//        if ("pis".equals(identificacao)) {
//            if (complemento.isEmpty()) {
//                resumoAlteracoes.setPisEntradaAntigo(String.valueOf(valorEncontrado));
//                resumoAlteracoes.setPisEntradaAtualizado(String.valueOf(valorAtualizado));
//            } else {
//                resumoAlteracoes.setPisSaidaAntigo(String.valueOf(valorEncontrado));
//                resumoAlteracoes.setPisSaidaAtualizado(String.valueOf(valorAtualizado));
//            }
//        } else {
//            if (complemento.isEmpty()) {
//                resumoAlteracoes.setCofinsEntradaAntigo(String.valueOf(valorEncontrado));
//                resumoAlteracoes.setCofinsEntradaAtualizado(String.valueOf(valorAtualizado));
//            } else {
//                resumoAlteracoes.setCofinsSaidaAntigo(String.valueOf(valorEncontrado));
//                resumoAlteracoes.setCofinsSaidaAtualizado(String.valueOf(valorAtualizado));
//            }
//        }
//
//        logger.info(linhas + " linhas afetadas.");
//        return linhas;
//    }
//
//    @Override
//    public String getConsultaTesteConexao() {
//        return "SELECT * FROM SYSIBM.SYSDUMMY1";
//    }
//
//    @Override
//    public List<Class> mapearClasses() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public boolean atualizarNCM(Segmento segmento) throws Exception {
//        String codigoInterno = segmento.getRevisao().getCodigoInterno();
//        String ncmLimpo = segmento.getDetalhes().getNcmLimpo();
//
//        logger.info("Realizando update no NCM " + ncmLimpo + " para: " + codigoInterno);
//        StringBuilder builder = new StringBuilder();
//        builder.append("UPDATE dba.produto_grade SET ncm = '");
//        builder.append(ncmLimpo);
//        builder.append("' WHERE idsubproduto = ");
//        builder.append(codigoInterno);
//        executeUpdate(builder.toString());
//        return true;
//    }
//
//    @Override
//    public boolean atualizarCEST(Segmento segmento) throws Exception {
//        return false;
//    }
//
//    @Override
//    public boolean atualizarIPI(Segmento segmento) throws Exception {
//        String codigoInterno = segmento.getRevisao().getCodigoInterno();
//        String ipi = segmento.getSaida().getIpi();
//
//        logger.info("Realizando update no IPI " + ipi + " para: " + codigoInterno);
//        StringBuilder builder = new StringBuilder();
//        builder.append("UPDATE dba.produto SET peripi = ");
//        builder.append(ipi);
//        builder.append(" WHERE idproduto = ");
//        builder.append(codigoInterno);
//        executeUpdate(builder.toString());
//        return true;
//    }
//
//    @Override
//    public boolean atualizarPISCOFINS(Segmento segmento) throws Exception {
//        {
//            StringBuilder builder = new StringBuilder();
//            builder.append("UPDATE dba.produto SET idnaturezapiscofins = ");
//            builder.append(idRealNaturezaReceita);
//            builder.append(" WHERE idproduto = ");
//            builder.append(idProduto);
//            executeUpdate(builder.toString());
//        }
//        return true;
//    }
//
//    @Override
//    public boolean atualizarICMS(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
