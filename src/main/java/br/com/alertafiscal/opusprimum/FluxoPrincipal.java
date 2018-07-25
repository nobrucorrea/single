package br.com.alertafiscal.opusprimum;

import static br.com.alertafiscal.opusprimum.Componente.logger;
import br.com.alertafiscal.opusprimum.ConfiguracaoContexto.VersaoTipoEntrada;
import br.com.alertafiscal.opusprimum.gui.Principal;
import br.com.alertafiscal.opusprimum.log.LoggerFachada;
import br.com.alertafiscal.opusprimum.persistencia.HibernateContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import br.com.alertafiscal.opusprimum.utils.DiretorioUtils;
import br.com.alertafiscal.opusprimum.utils.EmailUtils;
import br.com.alertafiscal.opusprimum.xml.parser.XMLParserAbstratoArquivo;
import br.com.alertafiscal.opusprimum.xml.parser.XMLParserAbstratoWebService;
import br.com.alertafiscal.opusprimum.xml.parser.XMLParserFabricaArquivo;
import br.com.alertafiscal.opusprimum.xml.parser.XMLParserFabricaWebService;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.CodigosInternosAtualizados;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author bastosbf
 */
public class FluxoPrincipal {

    public static void rodar(String tipoExecucao, LoggerFachada loggerFachada) throws Exception {
        loggerFachada.info("Opus iniciado...");
        ConfiguracaoContexto configuracaoContext = ConfiguracaoContexto.getInstance();
        Retaguarda retaguarda = HibernateContexto.instanciarRepositorio();

        VersaoTipoEntrada versaoTipoEntrada = configuracaoContext.getVersaoTipoEntrada();

        List<Produto> produtos = new ArrayList<Produto>();
        if (configuracaoContext.isXML()) {
            XMLParserAbstratoArquivo parser = XMLParserFabricaArquivo.criarParser(versaoTipoEntrada);
            String xml = configuracaoContext.getXMLCaminho();
            loggerFachada.info("Lento arquivo XML em: " + xml);
            produtos = parser.parse(new FileInputStream(new File(xml)));
            loggerFachada.info("Leitura do arquivo finalizada");
            loggerFachada.info("Inicializando atualizações...");
        } else if (configuracaoContext.isWebService()) {
            XMLParserAbstratoWebService parser = XMLParserFabricaWebService.criarParser(versaoTipoEntrada);
            String url = configuracaoContext.getWebServiceURL();
            String id = configuracaoContext.getWebServiceID();
            String token = configuracaoContext.getWebServiceToken();
            loggerFachada.info("Acessando WebService para ID: " + id + " e Token: " + token);
            File ultimaAtualizacao = new File(new DiretorioUtils().folder(".opus").asFile(), "ultima-atualizacao.dat");
            if (Principal.TIPO_EXECUCAO_PARCIAL.equalsIgnoreCase(tipoExecucao) && ultimaAtualizacao.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ultimaAtualizacao));
                Date data = (Date) ois.readObject();
                ois.close();
                CodigosInternosAtualizados codigosInternosAtualizados = parser.parseiaDataeCodigosInternos(url, id, token, data);
                List<String> codigosInternos = codigosInternosAtualizados.getCodigosInternos();
                for (String codigoInterno : codigosInternos) {
                    Produto produto = parser.parseiaProduto(url, id, token, codigoInterno, null);
                    if (produto != null) {
                        produtos.add(produto);
                    }
                    Date ultimaData = codigosInternosAtualizados.getData();
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ultimaAtualizacao));
                    oos.writeObject(ultimaData);
                    oos.flush();
                    oos.close();
                }
            } else if (Principal.TIPO_EXECUCAO_TOTAL.equalsIgnoreCase(tipoExecucao) || !ultimaAtualizacao.exists()) {
                Map<String, String> produtosNaoEncontrados = new HashMap<>();
                Map<String, String> mapa = retaguarda.mapeiaCodigoInternoEAN();
                Iterator<String> i = mapa.keySet().iterator();
                while (i.hasNext()) {
                    String chave = i.next();
                    String[] dados = chave.split("::");
                    String codigoInterno = dados[0];
                    String ean = dados[1];
                    String valor = mapa.get(chave);
                    if (ean != null) {
                        if (!validarEan(ean)) {
                            ean = null;
                        }
                    }
                    Produto produto = parser.parseiaProduto(url, id, token, codigoInterno, ean);
                    if (produto != null) {
                        produtos.add(produto);
                    } else {
                        if (ean != null) {
                            produtosNaoEncontrados.put(codigoInterno + "::" + ean, valor);
                        }
                    }
                }
                //vai salvar a ultima data baseado na data do computador
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ultimaAtualizacao));
                oos.writeObject(new Date());
                oos.flush();
                oos.close();

                Iterator<String> it = produtosNaoEncontrados.keySet().iterator();
                if (it.hasNext()) {
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Produtos não encontrados");
                    int contador = 0;
                    Row linha = sheet.createRow(contador);
                    {
                        Cell cell = linha.createCell(0);
                        cell.setCellValue("Código interno");
                    }
                    {
                        Cell cell = linha.createCell(1);
                        cell.setCellValue("Ean");
                    }
                    {
                        Cell cell = linha.createCell(2);
                        cell.setCellValue("Nome do produto");
                    }
                    while (it.hasNext()) {
                        String chave = it.next();
                        String[] dados = chave.split("::");
                        String codigoInterno = dados[0];
                        String ean = dados[1];
                        String valor = produtosNaoEncontrados.get(chave);
                        linha = sheet.createRow(++contador);
                        {
                            Cell cell = linha.createCell(0);
                            cell.setCellValue(codigoInterno);
                        }
                        {
                            Cell cell = linha.createCell(1);
                            cell.setCellValue(ean);
                        }
                        {
                            Cell cell = linha.createCell(2);
                            cell.setCellValue(valor);
                        }
                    }
                    File arquivoRelatorio = new File(new DiretorioUtils().folder(".opus").asFile(), "relatorio-produtos-nao-encontrados.xls");
                    logger.info("Criando Relatório de produtos não encontrados.");
                    FileOutputStream out = new FileOutputStream(arquivoRelatorio);
                    workbook.write(out);
                    out.flush();
                    out.close();
                    EmailUtils.enviaEmail(ConfiguracaoContexto.getInstance().getUsuarioSMTP(),
                            ConfiguracaoContexto.getInstance().getEmailRelatorio(),
                            "Produtos não encontrados no Opus para Id: " + id + "\n Token: " + token,
                            null,
                            arquivoRelatorio.getAbsolutePath(),
                            arquivoRelatorio.getName());
                    logger.info("E-mail de produtos não encontrados enviado com sucesso.");
                }
            }
            loggerFachada.info("Acesso ao WebService finalizado");
            loggerFachada.info("Inicializando atualizações...");
        }
        retaguarda.atualizar(produtos);
    }

    private static boolean validarEan(String ean) {
        boolean valido = false;
        if (ean != null && ean.matches("\\d+")) {
            char[] chars = ean.toCharArray();
            int somaPares = 0;
            int somaImpares = 0;
            for (int i = 0; i < chars.length - 1; i++) {
                if ((i + 1) % 2 == 0) {
                    somaPares += Integer.parseInt(String.valueOf(chars[i]));
                } else {
                    somaImpares += Integer.parseInt(String.valueOf(chars[i]));
                }
            }
            int resultado = (somaImpares * 3) + somaPares;
            int digito = (10 - (resultado % 10)) % 10;
            valido = Integer.parseInt(String.valueOf(chars[chars.length - 1])) == digito;
        }
        return valido;
    }
}
