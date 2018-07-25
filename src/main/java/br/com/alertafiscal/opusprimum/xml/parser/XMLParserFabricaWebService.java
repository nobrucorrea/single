package br.com.alertafiscal.opusprimum.xml.parser;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto.VersaoTipoEntrada;
import br.com.alertafiscal.opusprimum.xml.parser.impl.XMLParserWebServiceRJ;

/**
 *
 * @author bastos
 */
public abstract class XMLParserFabricaWebService {

    public static XMLParserAbstratoWebService criarParser(VersaoTipoEntrada versaoTipoEntrada) {
        switch (versaoTipoEntrada) {
            case WEBSERVICE_RJ:
            default:
                return new XMLParserWebServiceRJ();
        }
    }

}
