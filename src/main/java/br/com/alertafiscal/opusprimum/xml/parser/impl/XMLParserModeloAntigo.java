package br.com.alertafiscal.opusprimum.xml.parser.impl;

import br.com.alertafiscal.opusprimum.xml.parser.XMLParserAbstratoArquivo;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author bastosbf
 */
public class XMLParserModeloAntigo extends XMLParserAbstratoArquivo {

    @Override
    public List<Produto> parse(InputStream xml) {
        List<Produto> produtos = new ArrayList<>();
        try {
            logger.info("Criando '%s'.", DocumentBuilderFactory.class);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            logger.info("Criando '%s'.", DocumentBuilder.class);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            logger.info("Realizando o parse do conteúdo do XML.");
            Document document = dBuilder.parse(xml);

            logger.info("Normalizando o documento '%s'.", document);
            document.getDocumentElement().normalize();
            logger.debug("Obtendo o nó-raiz '%s'.", document.getDocumentElement().getNodeName());
            NodeList nodeList = document.getElementsByTagName("tributacoes");

            logger.debug("Convertendo nó para elemento.");
            Element elementoPrincipal = (Element) nodeList.item(0);

            NodeList nodeListSegmentos = elementoPrincipal.getElementsByTagName("segmento");
            for (int i = 0; i < nodeListSegmentos.getLength(); i++) {
                Element elementoSeguimento  = (Element) nodeListSegmentos.item(i);
                Produto produto = new Produto();
                Node nodeRevisao = elementoSeguimento.getElementsByTagName("revisao").item(0);
                {
                    Element elemento = (Element) nodeRevisao;
                    produto.setCodigoInterno(elemento.getElementsByTagName("codigoInterno").item(0).getTextContent());
                }
                Node nodeDetalhes = elementoSeguimento.getElementsByTagName("detalhes").item(0);
                {
                    Element elemento = (Element) nodeDetalhes;
                    produto.setNcm(elemento.getElementsByTagName("ncm").item(0).getTextContent());
                    produto.setDescricaoNcm(elemento.getElementsByTagName("descricaoNcm").item(0).getTextContent());
                }
                Node nodeSaida = elementoSeguimento.getElementsByTagName("saida").item(0);
                {
                   Element elemento = (Element) nodeSaida; 
                   produto.setCest(elemento.getElementsByTagName("codCest").item(0).getTextContent());
                   produto.setIpi(elemento.getElementsByTagName("ipi").item(0).getTextContent());
                   produto.setIcmsCstSaidaConsumidorFinal(elemento.getElementsByTagName("icmsCstSaida").item(0).getTextContent());
                   produto.setIcmsSaidaConsumidorFinal(elemento.getElementsByTagName("icmsSaida").item(0).getTextContent());
                }
                Node nodeEntrada = elementoSeguimento.getElementsByTagName("entrada").item(0);
                {
                   Element elemento = (Element) nodeEntrada;                   
                   produto.setMvaInterno(elemento.getElementsByTagName("mvaInterno").item(0).getTextContent());
                   
                   produto.addMvaExterno("AC", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("AL", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("AM", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("AP", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("BA", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("CE", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("DF", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("ES", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("GO", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("MA", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("MG", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("MS", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("MT", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("PA", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("PB", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("PE", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("PI", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("PR", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("RJ", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("RS", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("RN", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("RO", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("RR", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("SC", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("SE", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("SP", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   produto.addMvaExterno("TO", elemento.getElementsByTagName("mvaExterno").item(0).getTextContent());
                   
                   produto.addIcmsAtacado("AC", elemento.getElementsByTagName("icmsAtacadoAC").item(0).getTextContent());
                   produto.addIcmsAtacado("AL", elemento.getElementsByTagName("icmsAtacadoAL").item(0).getTextContent());
                   produto.addIcmsAtacado("AM", elemento.getElementsByTagName("icmsAtacadoAM").item(0).getTextContent());
                   produto.addIcmsAtacado("AP", elemento.getElementsByTagName("icmsAtacadoAP").item(0).getTextContent());
                   produto.addIcmsAtacado("BA", elemento.getElementsByTagName("icmsAtacadoBA").item(0).getTextContent());
                   produto.addIcmsAtacado("CE", elemento.getElementsByTagName("icmsAtacadoCE").item(0).getTextContent());
                   produto.addIcmsAtacado("DF", elemento.getElementsByTagName("icmsAtacadoDF").item(0).getTextContent());
                   produto.addIcmsAtacado("ES", elemento.getElementsByTagName("icmsAtacadoES").item(0).getTextContent());
                   produto.addIcmsAtacado("GO", elemento.getElementsByTagName("icmsAtacadoGO").item(0).getTextContent());
                   produto.addIcmsAtacado("MA", elemento.getElementsByTagName("icmsAtacadoMA").item(0).getTextContent());
                   produto.addIcmsAtacado("MG", elemento.getElementsByTagName("icmsAtacadoMG").item(0).getTextContent());
                   produto.addIcmsAtacado("MS", elemento.getElementsByTagName("icmsAtacadoMS").item(0).getTextContent());
                   produto.addIcmsAtacado("MT", elemento.getElementsByTagName("icmsAtacadoMT").item(0).getTextContent());
                   produto.addIcmsAtacado("PA", elemento.getElementsByTagName("icmsAtacadoPA").item(0).getTextContent());
                   produto.addIcmsAtacado("PB", elemento.getElementsByTagName("icmsAtacadoPB").item(0).getTextContent());
                   produto.addIcmsAtacado("PE", elemento.getElementsByTagName("icmsAtacadoPE").item(0).getTextContent());
                   produto.addIcmsAtacado("PI", elemento.getElementsByTagName("icmsAtacadoPI").item(0).getTextContent());
                   produto.addIcmsAtacado("PR", elemento.getElementsByTagName("icmsAtacadoPR").item(0).getTextContent());
                   produto.addIcmsAtacado("RJ", elemento.getElementsByTagName("icmsAtacadoRJ").item(0).getTextContent());
                   produto.addIcmsAtacado("RS", elemento.getElementsByTagName("icmsAtacadoRS").item(0).getTextContent());
                   produto.addIcmsAtacado("RN", elemento.getElementsByTagName("icmsAtacadoRN").item(0).getTextContent());
                   produto.addIcmsAtacado("RO", elemento.getElementsByTagName("icmsAtacadoRO").item(0).getTextContent());
                   produto.addIcmsAtacado("RR", elemento.getElementsByTagName("icmsAtacadoRR").item(0).getTextContent());
                   produto.addIcmsAtacado("SC", elemento.getElementsByTagName("icmsAtacadoSC").item(0).getTextContent());
                   produto.addIcmsAtacado("SE", elemento.getElementsByTagName("icmsAtacadoSE").item(0).getTextContent());
                   produto.addIcmsAtacado("SP", elemento.getElementsByTagName("icmsAtacadoSP").item(0).getTextContent());
                   produto.addIcmsAtacado("TO", elemento.getElementsByTagName("icmsAtacadoTO").item(0).getTextContent());
                }
                Node nodeFederal = elementoSeguimento.getElementsByTagName("federal").item(0);
                {
                    Element elemento = (Element) nodeFederal;
                    produto.setPisEntrada(elemento.getElementsByTagName("pisEntrada").item(0).getTextContent());
                    produto.setPisSaida(elemento.getElementsByTagName("pisSaida").item(0).getTextContent());
                    produto.setCofinsEntrada(elemento.getElementsByTagName("cofinsEntrada").item(0).getTextContent());
                    produto.setCofinsSaida(elemento.getElementsByTagName("cofinsSaida").item(0).getTextContent());
                    produto.setPiscofinsCstEntrada(elemento.getElementsByTagName("piscofinsCstEntrada").item(0).getTextContent());
                    produto.setPiscofinsCstSaida(elemento.getElementsByTagName("piscofinsCstSaida").item(0).getTextContent());
                }
                produtos.add(produto);
            }
        } catch (ParserConfigurationException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (SAXException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (IOException ex) {
            logger.exception(ex, "Erro no parser do XML");
        }
        return produtos;
    }

}
