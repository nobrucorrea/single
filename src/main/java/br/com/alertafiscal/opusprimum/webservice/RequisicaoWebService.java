package br.com.alertafiscal.opusprimum.webservice;

import br.com.alertafiscal.opusprimum.Componente;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author bastosbf
 */
public class RequisicaoWebService extends Componente {
    
    public static InputStream consultaProduto(String url, String id, String token, String codigoInterno, String ean, String data) throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        try {
            SOAPMessage soapResponse = soapConnection.call(criaRequisicaoSOAPProduto(id, token, codigoInterno, ean, data), url);
            return leRespostaSOAP(soapResponse);
        } finally {
            soapConnection.close();
        }
    }

    public static InputStream consultaCodigosInternos(String url, String id, String token, Date data) throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        try {
            return null;
            //SOAPMessage soapResponse = soapConnection.call(criaRequisicaoSOAPCodigosInternos(id, token, data), url);
            //return leRespostaSOAP(soapResponse);
        } finally {
            soapConnection.close();
        }
    }

    private static SOAPMessage criaRequisicaoSOAPCodigosInternos(String id, String token, String data) throws Exception {
        return null;
    }

    private static SOAPMessage criaRequisicaoSOAPProduto(String id, String token, String codigoInterno, String ean, String data) throws Exception {
        String serverURI = "http://soap.alertafiscalintranet.com.br";
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.setPrefix("soap");
        envelope.removeAttribute("xmlns:SOAP-ENV");
        envelope.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        envelope.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
        SOAPHeader header = envelope.getHeader();
        header.setPrefix("soap");
        SOAPElement soapHeaderElemValidationSoapHeader = header.addChildElement(new QName(serverURI, "ValidationSoapHeader"));
        SOAPElement soapHeaderElemId = soapHeaderElemValidationSoapHeader.addChildElement("Id");
        soapHeaderElemId.addTextNode(id);
        SOAPElement soapHeaderElemToken = soapHeaderElemValidationSoapHeader.addChildElement("Token");
        soapHeaderElemToken.addTextNode(token);
        SOAPBody soapBody = envelope.getBody();
        soapBody.setPrefix("soap");
        SOAPElement soapBodyElemConsultarTributacoesRJFinal = soapBody.addChildElement(new QName(serverURI, "ConsultarTributacoesRJFinal"));
        SOAPElement soapBodyElemObjConsulta = soapBodyElemConsultarTributacoesRJFinal.addChildElement("objConsulta");
        SOAPElement soapBodyElemId = soapBodyElemObjConsulta.addChildElement("Id");
        soapBodyElemId.addTextNode("0");
        SOAPElement soapBodyElemToken = soapBodyElemObjConsulta.addChildElement("Token");
        soapBodyElemToken.addTextNode("");
        SOAPElement soapBodyElemEan = soapBodyElemObjConsulta.addChildElement("Ean");
        if (ean != null) {
            soapBodyElemEan.addTextNode(ean);
        } else {
            soapBodyElemEan.addTextNode("");
        }
        SOAPElement soapBodyElemCodigoInterno = soapBodyElemObjConsulta.addChildElement("CodigoInterno");
        if (codigoInterno != null && ean == null) {
            soapBodyElemCodigoInterno.addTextNode(codigoInterno);
        } else {
            soapBodyElemCodigoInterno.addTextNode("");
        }
        SOAPElement soapBodyElemNcm = soapBodyElemObjConsulta.addChildElement("Ncm");
        soapBodyElemNcm.addTextNode("");
        SOAPElement soapBodyElemDataFiltro = soapBodyElemObjConsulta.addChildElement("DataFiltro");
        if (data != null) {
            soapBodyElemDataFiltro.addTextNode(data);
        } else {
            soapBodyElemDataFiltro.addTextNode("0001-01-01T00:00:00.000-00:00");
        }

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://soap.alertafiscalintranet.com.br/ConsultarTributacoesRJFinal");

        soapMessage.saveChanges();
        return soapMessage;
    }

    private static InputStream leRespostaSOAP(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        final Source sourceContent = soapResponse.getSOAPPart().getContent();
        PipedOutputStream out = new PipedOutputStream();
        new Thread(new Runnable() {
            public void run() {
                StreamResult result = new StreamResult(out);
                try {
                    transformer.transform(sourceContent, result);
                    out.close();
                } catch (TransformerException ex) {
                    logger.exception(ex);
                } catch (IOException ex) {
                    logger.exception(ex);
                }
            }
        }).start();
        return new PipedInputStream(out);
    }

}
