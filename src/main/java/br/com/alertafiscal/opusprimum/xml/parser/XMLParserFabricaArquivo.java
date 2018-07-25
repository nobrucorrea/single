package br.com.alertafiscal.opusprimum.xml.parser;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto.VersaoTipoEntrada;
import static br.com.alertafiscal.opusprimum.ConfiguracaoContexto.VersaoTipoEntrada.MODELO_ANTIGO;
import static br.com.alertafiscal.opusprimum.ConfiguracaoContexto.VersaoTipoEntrada.SOAP_FINAL;
import br.com.alertafiscal.opusprimum.xml.parser.impl.XMLParserModeloAntigo;
import br.com.alertafiscal.opusprimum.xml.parser.impl.XMLParserSOAPFinal;
import br.com.alertafiscal.opusprimum.xml.parser.impl.XMLParserSOAPFinalRJ;

/**
 *
 * @author bastos
 */
public abstract class XMLParserFabricaArquivo {

    public static XMLParserAbstratoArquivo criarParser(VersaoTipoEntrada versaoTipoEntrada) {
        switch (versaoTipoEntrada) {
            case MODELO_ANTIGO:
                return new XMLParserModeloAntigo();
            case SOAP_FINAL_RJ:
                return new XMLParserSOAPFinalRJ();
            case SOAP_FINAL:
            default : 
                return new XMLParserSOAPFinal();
        }
    }

}
