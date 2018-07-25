package br.com.alertafiscal.opusprimum.xml.parser;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author bastosbf
 */
public abstract class XMLParserAbstratoArquivo extends Componente {
    
    public abstract List<Produto> parse(InputStream xml);
    
}
