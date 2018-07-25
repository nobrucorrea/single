package br.com.alertafiscal.opusprimum.xml.parser.impl;

import br.com.alertafiscal.opusprimum.webservice.RequisicaoWebService;
import br.com.alertafiscal.opusprimum.xml.parser.XMLParserAbstratoWebService;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.CodigosInternosAtualizados;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author bastosbf
 */
public class XMLParserWebServiceRJ extends XMLParserAbstratoWebService {

    @Override
    public Produto parseiaProduto(String url, String id, String token, String codigoInterno, String ean) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            WebServiceDefaultHandler handler = new WebServiceDefaultHandler() {

                private Produto produto = null;
                private boolean novoProduto = false;
                private boolean Ncm = false;
                private boolean DescricaoNcm = false;
                private boolean CodigoCest = false;
                private boolean Ipi = false;
                private boolean CodigoCstIcmsSaidaConsumidorFinal = false;
                private boolean IcmsSaidaConsumidorFinal = false;
                private boolean MvaInternoAtacadoRJ = false;
                private boolean MvaExternoAtacadoAC = false;
                private boolean MvaExternoAtacadoAL = false;
                private boolean MvaExternoAtacadoAM = false;
                private boolean MvaExternoAtacadoAP = false;
                private boolean MvaExternoAtacadoBA = false;
                private boolean MvaExternoAtacadoCE = false;
                private boolean MvaExternoAtacadoDF = false;
                private boolean MvaExternoAtacadoES = false;
                private boolean MvaExternoAtacadoGO = false;
                private boolean MvaExternoAtacadoMA = false;
                private boolean MvaExternoAtacadoMG = false;
                private boolean MvaExternoAtacadoMS = false;
                private boolean MvaExternoAtacadoMT = false;
                private boolean MvaExternoAtacadoPA = false;
                private boolean MvaExternoAtacadoPB = false;
                private boolean MvaExternoAtacadoPE = false;
                private boolean MvaExternoAtacadoPI = false;
                private boolean MvaExternoAtacadoPR = false;
                private boolean MvaExternoAtacadoRJ = false;
                private boolean MvaExternoAtacadoRS = false;
                private boolean MvaExternoAtacadoRN = false;
                private boolean MvaExternoAtacadoRO = false;
                private boolean MvaExternoAtacadoRR = false;
                private boolean MvaExternoAtacadoSC = false;
                private boolean MvaExternoAtacadoSE = false;
                private boolean MvaExternoAtacadoSP = false;
                private boolean MvaExternoAtacadoTO = false;
                private boolean CreditoIcmsAtacadoAC = false;
                private boolean CreditoIcmsAtacadoAL = false;
                private boolean CreditoIcmsAtacadoAM = false;
                private boolean CreditoIcmsAtacadoAP = false;
                private boolean CreditoIcmsAtacadoBA = false;
                private boolean CreditoIcmsAtacadoCE = false;
                private boolean CreditoIcmsAtacadoDF = false;
                private boolean CreditoIcmsAtacadoES = false;
                private boolean CreditoIcmsAtacadoGO = false;
                private boolean CreditoIcmsAtacadoMA = false;
                private boolean CreditoIcmsAtacadoMG = false;
                private boolean CreditoIcmsAtacadoMS = false;
                private boolean CreditoIcmsAtacadoMT = false;
                private boolean CreditoIcmsAtacadoPA = false;
                private boolean CreditoIcmsAtacadoPB = false;
                private boolean CreditoIcmsAtacadoPE = false;
                private boolean CreditoIcmsAtacadoPI = false;
                private boolean CreditoIcmsAtacadoPR = false;
                private boolean CreditoIcmsAtacadoRJ = false;
                private boolean CreditoIcmsAtacadoRS = false;
                private boolean CreditoIcmsAtacadoRN = false;
                private boolean CreditoIcmsAtacadoRO = false;
                private boolean CreditoIcmsAtacadoRR = false;
                private boolean CreditoIcmsAtacadoSC = false;
                private boolean CreditoIcmsAtacadoSE = false;
                private boolean CreditoIcmsAtacadoSP = false;
                private boolean CreditoIcmsAtacadoTO = false;
                private boolean PisEntrada = false;
                private boolean PisSaida = false;
                private boolean CofinsEntrada = false;
                private boolean CofinsSaida = false;
                private boolean CodigoCstPisCofinsEntrada = false;
                private boolean CodigoCstPisCofinsSaida = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("CategoriaProduto")) {
                        novoProduto = true;
                    }
                    if (qName.equalsIgnoreCase("Ncm")) {
                        Ncm = true;
                    }
                    if (qName.equalsIgnoreCase("DescricaoNcm")) {
                        DescricaoNcm = true;
                    }
                    if (qName.equalsIgnoreCase("CodigoCest")) {
                        CodigoCest = true;
                    }
                    if (qName.equalsIgnoreCase("Ipi")) {
                        Ipi = true;
                    }
                    if (qName.equalsIgnoreCase("CodigoCstIcmsSaidaConsumidorFinal")) {
                        CodigoCstIcmsSaidaConsumidorFinal = true;
                    }
                    if (qName.equalsIgnoreCase("IcmsSaidaConsumidorFinal")) {
                        IcmsSaidaConsumidorFinal = true;
                    }
                    if (qName.equalsIgnoreCase("MvaInternoAtacadoRJ")) {
                        MvaInternoAtacadoRJ = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoAC")) {
                        MvaExternoAtacadoAC = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoAL")) {
                        MvaExternoAtacadoAL = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoAM")) {
                        MvaExternoAtacadoAM = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoAP")) {
                        MvaExternoAtacadoAP = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoBA")) {
                        MvaExternoAtacadoBA = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoCE")) {
                        MvaExternoAtacadoCE = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoDF")) {
                        MvaExternoAtacadoDF = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoES")) {
                        MvaExternoAtacadoES = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoGO")) {
                        MvaExternoAtacadoGO = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoMA")) {
                        MvaExternoAtacadoMA = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoMG")) {
                        MvaExternoAtacadoMG = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoMS")) {
                        MvaExternoAtacadoMS = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoMT")) {
                        MvaExternoAtacadoMT = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoPA")) {
                        MvaExternoAtacadoPA = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoPB")) {
                        MvaExternoAtacadoPB = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoPE")) {
                        MvaExternoAtacadoPE = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoPI")) {
                        MvaExternoAtacadoPI = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoPR")) {
                        MvaExternoAtacadoPR = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoRJ")) {
                        MvaExternoAtacadoRJ = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoRS")) {
                        MvaExternoAtacadoRS = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoRN")) {
                        MvaExternoAtacadoRN = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoRO")) {
                        MvaExternoAtacadoRO = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoRR")) {
                        MvaExternoAtacadoRR = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoSC")) {
                        MvaExternoAtacadoSC = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoSE")) {
                        MvaExternoAtacadoSE = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoSP")) {
                        MvaExternoAtacadoSP = true;
                    }
                    if (qName.equalsIgnoreCase("MvaExternoAtacadoTO")) {
                        MvaExternoAtacadoTO = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoAC")) {
                        CreditoIcmsAtacadoAC = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoAL")) {
                        CreditoIcmsAtacadoAL = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoAM")) {
                        CreditoIcmsAtacadoAM = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoAP")) {
                        CreditoIcmsAtacadoAP = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoBA")) {
                        CreditoIcmsAtacadoBA = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoCE")) {
                        CreditoIcmsAtacadoCE = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoDF")) {
                        CreditoIcmsAtacadoDF = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoES")) {
                        CreditoIcmsAtacadoES = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoGO")) {
                        CreditoIcmsAtacadoGO = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoMA")) {
                        CreditoIcmsAtacadoMA = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoMG")) {
                        CreditoIcmsAtacadoMG = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoMS")) {
                        CreditoIcmsAtacadoMS = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoMT")) {
                        CreditoIcmsAtacadoMT = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoPA")) {
                        CreditoIcmsAtacadoPA = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoPB")) {
                        CreditoIcmsAtacadoPB = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoPE")) {
                        CreditoIcmsAtacadoPE = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoPI")) {
                        CreditoIcmsAtacadoPI = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoPR")) {
                        CreditoIcmsAtacadoPR = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoRJ")) {
                        CreditoIcmsAtacadoRJ = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoRS")) {
                        CreditoIcmsAtacadoRS = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoRN")) {
                        CreditoIcmsAtacadoRN = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoRO")) {
                        CreditoIcmsAtacadoRO = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoRR")) {
                        CreditoIcmsAtacadoRR = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoSC")) {
                        CreditoIcmsAtacadoSC = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoSE")) {
                        CreditoIcmsAtacadoSE = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoSP")) {
                        CreditoIcmsAtacadoSP = true;
                    }
                    if (qName.equalsIgnoreCase("CreditoIcmsAtacadoTO")) {
                        CreditoIcmsAtacadoTO = true;
                    }
                    if (qName.equalsIgnoreCase("PisEntrada")) {
                        PisEntrada = true;
                    }

                    if (qName.equalsIgnoreCase("PisSaida")) {
                        PisSaida = true;
                    }

                    if (qName.equalsIgnoreCase("CofinsEntrada")) {
                        CofinsEntrada = true;
                    }
                    if (qName.equalsIgnoreCase("CofinsSaida")) {
                        CofinsSaida = true;
                    }
                    if (qName.equalsIgnoreCase("CodigoCstPisCofinsEntrada")) {
                        CodigoCstPisCofinsEntrada = true;
                    }
                    if (qName.equalsIgnoreCase("CodigoCstPisCofinsSaida")) {
                        CodigoCstPisCofinsSaida = true;
                    }

                }

                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (novoProduto) {
                        produto = new Produto();
                        produto.setCodigoInterno(codigoInterno);
                        novoProduto = false;
                    }
                    if (Ncm) {
                        produto.setNcm(new String(ch, start, length));
                        Ncm = false;
                    }
                    if (DescricaoNcm) {
                        produto.setDescricaoNcm(new String(ch, start, length));
                        DescricaoNcm = false;
                    }
                    if (CodigoCest) {
                        produto.setCest(new String(ch, start, length));
                        CodigoCest = false;
                    }
                    if (Ipi) {
                        produto.setIpi(new String(ch, start, length));
                        Ipi = false;
                    }
                    if (CodigoCstIcmsSaidaConsumidorFinal) {
                        produto.setIcmsCstSaidaConsumidorFinal(new String(ch, start, length));
                        CodigoCstIcmsSaidaConsumidorFinal = false;
                    }
                    if (IcmsSaidaConsumidorFinal) {
                        produto.setIcmsSaidaConsumidorFinal(new String(ch, start, length));
                        IcmsSaidaConsumidorFinal = false;
                    }
                    if (MvaInternoAtacadoRJ) {
                        produto.setMvaInterno(new String(ch, start, length));
                        MvaInternoAtacadoRJ = false;
                    }
                    if (MvaExternoAtacadoAC) {
                        produto.addMvaExterno("AC", (new String(ch, start, length)));
                        MvaExternoAtacadoAC = false;
                    }
                    if (MvaExternoAtacadoAL) {
                        produto.addMvaExterno("AL", (new String(ch, start, length)));
                        MvaExternoAtacadoAL = false;
                    }
                    if (MvaExternoAtacadoAM) {
                        produto.addMvaExterno("AM", (new String(ch, start, length)));
                        MvaExternoAtacadoAM = false;
                    }
                    if (MvaExternoAtacadoAP) {
                        produto.addMvaExterno("AP", (new String(ch, start, length)));
                        MvaExternoAtacadoAP = false;
                    }
                    if (MvaExternoAtacadoBA) {
                        produto.addMvaExterno("BA", (new String(ch, start, length)));
                        MvaExternoAtacadoBA = false;
                    }
                    if (MvaExternoAtacadoCE) {
                        produto.addMvaExterno("CE", (new String(ch, start, length)));
                        MvaExternoAtacadoCE = false;
                    }
                    if (MvaExternoAtacadoDF) {
                        produto.addMvaExterno("DF", (new String(ch, start, length)));
                        MvaExternoAtacadoDF = false;
                    }
                    if (MvaExternoAtacadoES) {
                        produto.addMvaExterno("ES", (new String(ch, start, length)));
                        MvaExternoAtacadoES = false;
                    }
                    if (MvaExternoAtacadoGO) {
                        produto.addMvaExterno("GO", (new String(ch, start, length)));
                        MvaExternoAtacadoGO = false;
                    }
                    if (MvaExternoAtacadoMA) {
                        produto.addMvaExterno("MA", (new String(ch, start, length)));
                        MvaExternoAtacadoMA = false;
                    }
                    if (MvaExternoAtacadoMG) {
                        produto.addMvaExterno("MG", (new String(ch, start, length)));
                        MvaExternoAtacadoMG = false;
                    }
                    if (MvaExternoAtacadoMS) {
                        produto.addMvaExterno("MS", (new String(ch, start, length)));
                        MvaExternoAtacadoMS = false;
                    }
                    if (MvaExternoAtacadoMT) {
                        produto.addMvaExterno("MT", (new String(ch, start, length)));
                        MvaExternoAtacadoMT = false;
                    }
                    if (MvaExternoAtacadoPA) {
                        produto.addMvaExterno("PA", (new String(ch, start, length)));
                        MvaExternoAtacadoPA = false;
                    }
                    if (MvaExternoAtacadoPB) {
                        produto.addMvaExterno("PB", (new String(ch, start, length)));
                        MvaExternoAtacadoPB = false;
                    }
                    if (MvaExternoAtacadoPE) {
                        produto.addMvaExterno("PE", (new String(ch, start, length)));
                        MvaExternoAtacadoPE = false;
                    }
                    if (MvaExternoAtacadoPI) {
                        produto.addMvaExterno("PI", (new String(ch, start, length)));
                        MvaExternoAtacadoPI = false;
                    }
                    if (MvaExternoAtacadoPR) {
                        produto.addMvaExterno("PR", (new String(ch, start, length)));
                        MvaExternoAtacadoPR = false;
                    }
                    if (MvaExternoAtacadoRJ) {
                        produto.addMvaExterno("RJ", (new String(ch, start, length)));
                        MvaExternoAtacadoRJ = false;
                    }
                    if (MvaExternoAtacadoRS) {
                        produto.addMvaExterno("RS", (new String(ch, start, length)));
                        MvaExternoAtacadoRS = false;
                    }
                    if (MvaExternoAtacadoRN) {
                        produto.addMvaExterno("RN", (new String(ch, start, length)));
                        MvaExternoAtacadoRN = false;
                    }
                    if (MvaExternoAtacadoRO) {
                        produto.addMvaExterno("RO", (new String(ch, start, length)));
                        MvaExternoAtacadoRO = false;
                    }
                    if (MvaExternoAtacadoRR) {
                        produto.addMvaExterno("RR", (new String(ch, start, length)));
                        MvaExternoAtacadoRR = false;
                    }
                    if (MvaExternoAtacadoSC) {
                        produto.addMvaExterno("SC", (new String(ch, start, length)));
                        MvaExternoAtacadoSC = false;
                    }
                    if (MvaExternoAtacadoSE) {
                        produto.addMvaExterno("SE", (new String(ch, start, length)));
                        MvaExternoAtacadoSE = false;
                    }
                    if (MvaExternoAtacadoSP) {
                        produto.addMvaExterno("SP", (new String(ch, start, length)));
                        MvaExternoAtacadoSP = false;
                    }
                    if (MvaExternoAtacadoTO) {
                        produto.addMvaExterno("TO", (new String(ch, start, length)));
                        MvaExternoAtacadoTO = false;
                    }
                    if (CreditoIcmsAtacadoAC) {
                        produto.addIcmsAtacado("AC", (new String(ch, start, length)));
                        CreditoIcmsAtacadoAC = false;
                    }
                    if (CreditoIcmsAtacadoAL) {
                        produto.addIcmsAtacado("AL", (new String(ch, start, length)));
                        CreditoIcmsAtacadoAL = false;
                    }
                    if (CreditoIcmsAtacadoAM) {
                        produto.addIcmsAtacado("AM", (new String(ch, start, length)));
                        CreditoIcmsAtacadoAM = false;
                    }
                    if (CreditoIcmsAtacadoAP) {
                        produto.addIcmsAtacado("AP", (new String(ch, start, length)));
                        CreditoIcmsAtacadoAP = false;
                    }
                    if (CreditoIcmsAtacadoBA) {
                        produto.addIcmsAtacado("BA", (new String(ch, start, length)));
                        CreditoIcmsAtacadoBA = false;
                    }
                    if (CreditoIcmsAtacadoCE) {
                        produto.addIcmsAtacado("CE", (new String(ch, start, length)));
                        CreditoIcmsAtacadoCE = false;
                    }
                    if (CreditoIcmsAtacadoDF) {
                        produto.addIcmsAtacado("DF", (new String(ch, start, length)));
                        CreditoIcmsAtacadoDF = false;
                    }
                    if (CreditoIcmsAtacadoES) {
                        produto.addIcmsAtacado("ES", (new String(ch, start, length)));
                        CreditoIcmsAtacadoES = false;
                    }
                    if (CreditoIcmsAtacadoGO) {
                        produto.addIcmsAtacado("GO", (new String(ch, start, length)));
                        CreditoIcmsAtacadoGO = false;
                    }
                    if (CreditoIcmsAtacadoMA) {
                        produto.addIcmsAtacado("MA", (new String(ch, start, length)));
                        CreditoIcmsAtacadoMA = false;
                    }
                    if (CreditoIcmsAtacadoMG) {
                        produto.addIcmsAtacado("MG", (new String(ch, start, length)));
                        CreditoIcmsAtacadoMG = false;
                    }
                    if (CreditoIcmsAtacadoMS) {
                        produto.addIcmsAtacado("MS", (new String(ch, start, length)));
                        CreditoIcmsAtacadoMS = false;
                    }
                    if (CreditoIcmsAtacadoMT) {
                        produto.addIcmsAtacado("MT", (new String(ch, start, length)));
                        CreditoIcmsAtacadoMT = false;
                    }
                    if (CreditoIcmsAtacadoPA) {
                        produto.addIcmsAtacado("PA", (new String(ch, start, length)));
                        CreditoIcmsAtacadoPA = false;
                    }
                    if (CreditoIcmsAtacadoPB) {
                        produto.addIcmsAtacado("PB", (new String(ch, start, length)));
                        CreditoIcmsAtacadoPB = false;
                    }
                    if (CreditoIcmsAtacadoPE) {
                        produto.addIcmsAtacado("PE", (new String(ch, start, length)));
                        CreditoIcmsAtacadoPE = false;
                    }
                    if (CreditoIcmsAtacadoPI) {
                        produto.addIcmsAtacado("PI", (new String(ch, start, length)));
                        CreditoIcmsAtacadoPI = false;
                    }
                    if (CreditoIcmsAtacadoPR) {
                        produto.addIcmsAtacado("PR", (new String(ch, start, length)));
                        CreditoIcmsAtacadoPR = false;
                    }
                    if (CreditoIcmsAtacadoRJ) {
                        produto.addIcmsAtacado("RJ", (new String(ch, start, length)));
                        CreditoIcmsAtacadoRJ = false;
                    }
                    if (CreditoIcmsAtacadoRS) {
                        produto.addIcmsAtacado("RS", (new String(ch, start, length)));
                        CreditoIcmsAtacadoRS = false;
                    }
                    if (CreditoIcmsAtacadoRN) {
                        produto.addIcmsAtacado("RN", (new String(ch, start, length)));
                        CreditoIcmsAtacadoRN = false;
                    }
                    if (CreditoIcmsAtacadoRO) {
                        produto.addIcmsAtacado("RO", (new String(ch, start, length)));
                        CreditoIcmsAtacadoRO = false;
                    }
                    if (CreditoIcmsAtacadoRR) {
                        produto.addIcmsAtacado("RR", (new String(ch, start, length)));
                        CreditoIcmsAtacadoRR = false;
                    }
                    if (CreditoIcmsAtacadoSC) {
                        produto.addIcmsAtacado("SC", (new String(ch, start, length)));
                        CreditoIcmsAtacadoSC = false;
                    }
                    if (CreditoIcmsAtacadoSE) {
                        produto.addIcmsAtacado("SE", (new String(ch, start, length)));
                        CreditoIcmsAtacadoSE = false;
                    }
                    if (CreditoIcmsAtacadoSP) {
                        produto.addIcmsAtacado("SP", (new String(ch, start, length)));
                        CreditoIcmsAtacadoSP = false;
                    }
                    if (CreditoIcmsAtacadoTO) {
                        produto.addIcmsAtacado("TO", (new String(ch, start, length)));
                        CreditoIcmsAtacadoTO = false;
                    }
                    if (PisEntrada) {
                        produto.setPisEntrada(new String(ch, start, length));
                        PisEntrada = false;
                    }

                    if (PisSaida) {
                        produto.setPisSaida(new String(ch, start, length));
                        PisSaida = false;
                    }

                    if (CofinsEntrada) {
                        produto.setCofinsEntrada(new String(ch, start, length));
                        CofinsEntrada = false;
                    }
                    if (CofinsSaida) {
                        produto.setCofinsSaida(new String(ch, start, length));
                        CofinsSaida = false;
                    }
                    if (CodigoCstPisCofinsEntrada) {
                        produto.setPiscofinsCstEntrada(new String(ch, start, length));
                        CodigoCstPisCofinsEntrada = false;
                    }
                    if (CodigoCstPisCofinsSaida) {
                        produto.setPiscofinsCstSaida(new String(ch, start, length));
                        CodigoCstPisCofinsSaida = false;
                    }
                }

                @Override
                public Produto getProduto() {
                    return produto;
                }
            };
            InputStream xml = RequisicaoWebService.consultaProduto(url, id, token, codigoInterno, ean, null);
            saxParser.parse(xml, handler);
            return handler.getProduto();
        } catch (ParserConfigurationException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (SAXException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (IOException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (Exception ex) {
            logger.exception(ex, "Erro no parser do XML");
        }
        return null;
    }

    private static abstract class WebServiceDefaultHandler extends DefaultHandler {

        public abstract Produto getProduto();
    }

    @Override
    public CodigosInternosAtualizados parseiaDataeCodigosInternos(String url, String id, String token, Date data) {
        CodigosInternosAtualizados produto = new CodigosInternosAtualizados();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                private boolean Ncm = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("Ncm")) {
                        Ncm = true;
                    }
                }

                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (Ncm) {
                        //produto.setNcm(new String(ch, start, length));
                        Ncm = false;
                    }
                }
            };
            InputStream xml = RequisicaoWebService.consultaCodigosInternos(url, id, token, data);
            saxParser.parse(xml, handler);
        } catch (ParserConfigurationException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (SAXException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (IOException ex) {
            logger.exception(ex, "Erro no parser do XML");
        } catch (Exception ex) {
            logger.exception(ex, "Erro no parser do XML");
        }
        return produto;
    }
}
