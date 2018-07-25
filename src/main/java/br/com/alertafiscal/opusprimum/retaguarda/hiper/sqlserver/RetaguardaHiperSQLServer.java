//package opusprimum.repository;
//
//import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
//import br.com.alertafiscal.opusprimum.xml.Segmento;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javafx.stage.FileChooser;
//import javax.swing.JFileChooser;
//
//public class RetaguardaHiperSQLServer extends Retaguarda {
//
//    private List<HiperProduto> banco;
//    private final List<HiperProduto> exportacao;
//
//    public RetaguardaHiperSQLServer() {
//        logger.info("Inicializando lista de produtos para exportação.");
//        exportacao = new ArrayList<>();
//    }
//
//    @Override
//    boolean salvar(Salvavel salvavel) {
//        if (banco == null || banco.isEmpty()) {
//            loggerWarns.warn("Banco nulo! Iniciando tentativa de carregá-lo!");
//            carregarBanco();
//
//            if (banco == null || banco.isEmpty()) {
//                throw new RepositoryException("Não foi possível carregar um arquivo de banco de dados");
//            }
//        }
//
//        Segmento segmento = (Segmento) salvavel;
//
//        HiperProduto hiperProduto = carregarProduto(segmento.getRevisao().getCodigoInterno());
//
//        if (hiperProduto == null) {
//            logger.info("Produto não encontrado={}.", salvavel._getCodigoInterno());
//            return false;
//        }
//
//        if (hiperProduto.getIdProdutoCadastradoBaseDados() == null || hiperProduto.getIdProdutoCadastradoBaseDados().isEmpty()) {
//            logger.debug("Ignorando produto={}.", salvavel._getCodigoInterno());
//            return false;
//        }
//
//        if (ConfiguracaoContext.getInstance().isAtualizarNCM()) {
//            logger.debug("Realizando update no NCM.");
//            hiperProduto.setNcmCadastradoBaseDados(segmento.getDetalhes().getNcm());
//        }
//
//        if (ConfiguracaoContext.getInstance().isAtualizarICMS()) {
//            logger.debug("Realizando update no ICMS.");
//            hiperProduto.setAliquotaIcms(segmento.getSaida().getIcmsSaida());
//            hiperProduto.setIdRegraTributacaoCadastradaBaseDados(segmento.getSaida().getIcmsCstSaida(), true);
//        }
//
//        if (ConfiguracaoContext.getInstance().isAtualizarIPI()) {
//            logger.debug("Realizando update no IPI.");
//            hiperProduto.setAliquotaIpi(segmento.getSaida().getIpi());
//        }
//
//        if (ConfiguracaoContext.getInstance().isAtualizarPisCofins()) {
//            logger.debug("Realizando update na Natureza PIS/COFINS.");
//            hiperProduto.setCodigoSituacaoTributariaCofins(segmento.getFederal().getPiscofinsCstSaida());
//            hiperProduto.setAliquotaCofins(segmento.getFederal().getCofinsSaida());
//
//            hiperProduto.setCodigoSituacaoTributariaPis(segmento.getFederal().getPiscofinsCstSaida());
//            hiperProduto.setAliquotaPis(segmento.getFederal().getPisSaida());
//        }
//
//        /*
//         * Segundo o e-mail do Rafael: vê se dá pra achar ai qual é o Mva maior
//         * e coloca externo e o menor interno.
//         */
//        logger.debug("Atualizando MVAs de entrada e saída.");
//        hiperProduto.setAliquotaMvaEntrada(segmento.getEntrada().getMvaImportado());
//        hiperProduto.setAliquotaMvaSaida(segmento.getEntrada().getMvaInterno());
//
//        exportacao.add(hiperProduto);
//
//        return true;
//    }
//
//    @Override
//    public void salvarEmArquivo() throws FileNotFoundException, IOException {
//        exportarArquivo();
//    }
//
//    @Override
//    public List listarPisCofinsCodigosNaturezaReceita() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<ChaveValor> listarCSTICMSs() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<ChaveValor> listarNCMs() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<ChaveValor> listarPISCOFINS() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public List<Extracao> obterExtracoes(String... codigosInternos) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Revisao obterRevisaoProdutoPorEan(String eans) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public HiperProduto carregarProduto(final String codigoInterno) {
//        return banco.stream().filter(prod -> codigoInterno.equals(prod._getCodigoInterno())).findFirst().orElse(null);
//    }
//
//    /**
//     * Ao ser chamado, este método realiza a exportação de todos os objetos
//     * pré-armazenados neste repositório sejam gravados em um arquivo.
//     *
//     * @throws FileNotFoundException
//     * @throws IOException
//     */
//    public void exportarArquivo() throws FileNotFoundException, IOException {
//        logger.debug("Instanciando {} para armazenamento do conteúdo do arquivo.", StringBuilder.class);
//        StringBuilder builder = new StringBuilder();
//
//        /*
//         * Registro de inicialização é o Registro 0001, a sua estrutura está
//         * detalhada na pagina 6 do Guia de Desenvolvimento HiperConnection.
//         * 
//         * Por: Marilea Andrade (contato@mldisplay.com.br)
//         */
//        logger.debug("Gerando o primeiro registro (inicialização).");
//        builder.append("|0001|1|");
//        builder.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
//        builder.append("|0|");
//        builder.append(System.getProperty("line.separator"));
//
//        HiperProduto hiperProduto;
//        logger.debug("Iterando por {} produtos.", exportacao.size());
//        for (int i = 0; i < exportacao.size(); i++) {
//            hiperProduto = exportacao.get(i);
//
//            logger.debug("Setando a sequência do registro no arquivo.");
//            hiperProduto.setSequenciaRegistroArquivo(String.valueOf(i + 1));
//
//            builder.append(hiperProduto.converterRegistroHiper());
//            builder.append(System.getProperty("line.separator"));
//        }
//
//        /*
//         * Registro de Finalização é o Registro 9999, a sua estrutura está
//         * detalhada na pagina 24 do mesmo guia.
//         *
//         * Por: Casanova (contato@mldisplay.com.br)
//         */
//        logger.debug("Gerando o último registro (encerramento do arquivo).");
//        builder.append("|9999|1|").append(exportacao.size() + 2).append("|");
//
//        logger.debug("Criando arquivo a ser gravado.");
//        File arquivo = new File(new LocalFolderHelper().folder(".opus").asFile(), "hiperproduto-" + new Date().getTime() + ".txt");
//
//        logger.debug("Instanciando {}.", FileOutputStream.class);
//        FileOutputStream out = new FileOutputStream(arquivo);
//
//        logger.debug("Escrevendo {} caracteres em {}.", builder.length(), out);
//        out.write(builder.toString().getBytes());
//
//        logger.debug("Chamando flush de {}.", out);
//        out.flush();
//
//        logger.debug("Encerrando {}.", out);
//        out.close();
//
//        listeners.stream().forEach((listener) -> listener.onInformacao("Arquivo " + arquivo.getAbsolutePath() + " gravado com sucesso."));
//    }
//
//    private void carregarBanco() {
//        logger.info("Abrindo janela para seleção de arquivo.");
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setMultiSelectionEnabled(false);
//        fileChooser.showOpenDialog(null);
//
//        logger.info("{}.getSelectedFile()={}.", FileChooser.class, fileChooser.getSelectedFile());
//        File file = fileChooser.getSelectedFile();
//
//        if (file == null) {
//            loggerWarns.warn("Nenhum arquivo selecionado para carregar o banco do Hiper.");
//            return;
//        }
//
//        logger.info("Inicializando 'banco'.");
//        banco = new ArrayList<>();
//
//        try {
//            new ArquivoLinhasHelper(this, file.getAbsolutePath()).iterar();
//        } catch (Exception ex) {
//            logger.exception(ex);
//        }
//    }
//
//    @Override
//    public void handle(String linha) {
//        logger.info("Interpretando linha={}.", linha);
//        HiperProduto hiperProduto = new HiperProduto();
//
//        String[] valores = linha.split("\\|");
//        
//        if(valores.length != 36) {
//            loggerWarns.warn("Linha inválida: {}.", linha);
//            return;
//        }
//
//        hiperProduto.setIdProdutoCadastradoBaseDados(valores[0]);
//        hiperProduto.setNomeProduto(valores[1]);
//        hiperProduto.setSituacao(valores[2]);
//        hiperProduto.setCnpjCpfEntidadeFornecedor(valores[3]);
//        hiperProduto.setNomeMarcaProduto(valores[4]);
//        hiperProduto.setCodigoCategoriaCadastradaBaseDados(valores[5]);
//        hiperProduto.setSiglaUnidadeMedidaProduto(valores[6]);
//        hiperProduto.setTipoGradeProduto(valores[7]);
//        hiperProduto.setIdTabelaGradeA(valores[8]);
//        hiperProduto.setIdTabelaGradeB(valores[9]);
//        hiperProduto.setReferenciaInternaProduto(valores[10]);
//        hiperProduto.setPrecoCusto(valores[11]);
//        hiperProduto.setPrecoFornecedor(valores[12]);
//        hiperProduto.setPrecoVenda(valores[13]);
//        hiperProduto.setNcmCadastradoBaseDados(valores[14]);
//        hiperProduto.setCodigoSituacaoTributariaPis(valores[16]);
//        hiperProduto.setAliquotaPis(valores[17]);
//        hiperProduto.setCodigoSituacaoTributariaCofins(valores[19]);
//        hiperProduto.setAliquotaCofins(valores[20]);
//        hiperProduto.setCodigoSituacaoTributariaIpi(valores[21]);
//        hiperProduto.setAliquotaIpi(valores[22]);
//        hiperProduto.setOrigemProduto(valores[23]);
//        hiperProduto.setTipoItem(valores[24]);
//        hiperProduto.setEnviarProdutoBalanca(valores[25]);
//        hiperProduto.setIdRegraTributacaoCadastradaBaseDados(valores[26], false);
//        hiperProduto.setAliquotaIcms(valores[27]);
//        hiperProduto.setControlarValidadeProduto(valores[28]);
//        hiperProduto.setAliquotaMvaSaida(valores[29]);
//        hiperProduto.setAliquotaMvaEntrada(valores[30]);
//        hiperProduto.setSolicitarIdentificacaoVendedorVenda(valores[31]);
//        hiperProduto.setPeso(valores[32]);
//        hiperProduto.setPrecoMinimoVenda(valores[33]);
//        hiperProduto.setNumeroFci(valores[34]);
//        hiperProduto.setControlarNumeroDeSerieDoproduto(valores[35]);
//
//        /*
//         * De acordo com e-mail do Casanova cujo título é "HiperConnection" em
//         * 20 de maio de 2015, os campos abaixo são apenass flags. Devemos
//         * passar "1" quando houver necessidade de atualizar o PIS ou COFINS e
//         * "0" quando eles forem os mesmos.
//         *
//         * Por questões técnicas, as alíquotas de COFINS e PIS são nulas quando
//         * um objeto de HiperProduto é criado (já que a String da alíquota é
//         * nula). O preenchimento nas linhas acima acabaria setando a flag de
//         * diferenciação como "1" sempre quando a alíquota fosse diferente de
//         * zero, mas não queremos isso, pois só queremos setá-la como "1" quando
//         * houver realmente diferença nas alíquotas. Sendo assim, resetamos
//         * abaixo suas flags para "0", e deixaremos que ela seja setada como "1"
//         * apenas durante a comparação do algoritmo que varre o XML, setando as
//         * alíquotas reais.
//         *
//         * UPDATE: Após e-mail do Vagner em 01/04/2016, cujo título é
//         * ENC: HiperConnection Não apaga os dados de PISCOFINS: "Bom dia
//         * Adriano, conforme email do suporte da Hiper, devemos colocar o '1'
//         * para todos os PIS COFINS."
//         */
//        hiperProduto.setProdutoPossuiTributacaoPisDiferenciada("1");
//        hiperProduto.setProdutoPossuiTributacaoCofinsDiferenciada("1");
//
//        comunicarListenersInformacao("Carregando produto=" + hiperProduto._getDescricao());
//        logger.debug("Incluindo {}={}.", HiperProduto.class, hiperProduto._getCodigoInterno());
//        banco.add(hiperProduto);
//    }
//
//    @Override
//    public String getConsultaTesteConexao() {
//        return "";
//    }
//
//    @Override
//    public List<Class> mapearClasses() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public boolean atualizarNCM(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean atualizarCEST(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean atualizarIPI(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean atualizarPISCOFINS(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public boolean atualizarICMS(Segmento segmento) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
