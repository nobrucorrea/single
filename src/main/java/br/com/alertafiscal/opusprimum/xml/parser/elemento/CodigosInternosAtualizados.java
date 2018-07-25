package br.com.alertafiscal.opusprimum.xml.parser.elemento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bastosbf
 */
public class CodigosInternosAtualizados {

    private Date data;
    private List<String> codigosInternos;

    public CodigosInternosAtualizados() {
        codigosInternos = new ArrayList<>();
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void addCodigoInterno(String codigoInterno) {
        this.codigosInternos.add(codigoInterno);
    }

    public List<String> getCodigosInternos() {
        return codigosInternos;
    }
}
