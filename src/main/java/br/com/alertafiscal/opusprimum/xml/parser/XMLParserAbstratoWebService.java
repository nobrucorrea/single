package br.com.alertafiscal.opusprimum.xml.parser;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.CodigosInternosAtualizados;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.util.Date;

/**
 *
 * @author bastosbf
 */
public abstract class XMLParserAbstratoWebService extends Componente {
    
    public abstract Produto parseiaProduto(String url, String id, String token, String codigoInterno, String ean);
    public abstract CodigosInternosAtualizados parseiaDataeCodigosInternos(String url, String id, String token, Date data);
    
}
