package br.com.alertafiscal.opusprimum.relatorio;

/**
 *
 * @author bastosbf
 */
public class Registro {

    private String codigoInterno;
    private String codigoEan;
    private String descricao;   
    private Boolean atualizouNCM;
    private Boolean atualizouCEST;
    private Boolean atualizouIPI;
    private Boolean atualizouPISCOFINS;
    private Boolean atualizouICMS;

    public Registro(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getCodigoEan() {
        return codigoEan;
    }
    
    public void setCodigoEan(String codigoEan) {
        this.codigoEan = codigoEan;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public Boolean isAtualizouNCM() {
        return atualizouNCM;
    }

    public void setAtualizouNCM(Boolean atualizouNCM) {
        this.atualizouNCM = atualizouNCM;
    }

    public Boolean isAtualizouCEST() {
        return atualizouCEST;
    }

    public void setAtualizouCEST(Boolean atualizouCEST) {
        this.atualizouCEST = atualizouCEST;
    }

    public Boolean isAtualizouIPI() {
        return atualizouIPI;
    }

    public void setAtualizouIPI(Boolean atualizouIPI) {
        this.atualizouIPI = atualizouIPI;
    }

    public Boolean isAtualizouPISCOFINS() {
        return atualizouPISCOFINS;
    }

    public void setAtualizouPISCOFINS(Boolean atualizouPISCOFINS) {
        this.atualizouPISCOFINS = atualizouPISCOFINS;
    }

    public Boolean isAtualizouICMS() {
        return atualizouICMS;
    }

    public void setAtualizouICMS(Boolean atualizouICMS) {
        this.atualizouICMS = atualizouICMS;
    }
}
