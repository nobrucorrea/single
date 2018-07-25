package br.com.alertafiscal.opusprimum.xml.parser.elemento;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bastosbf
 */
public class Produto {

    private String codigoInterno;

    private String ncm;
    private String descricaoNcm;
    private String descricaoCest;

    private String cest;

    private String ipi;

    private String pisEntrada;
    private String pisSaida;
    private String cofinsEntrada;
    private String cofinsSaida;
    private String piscofinsCstEntrada;
    private String piscofinsCstSaida;
    private String pisconfinsSituacao;
    private String pisCofinsNaturezaReceita;

    private String icmsCstSaidaConsumidorFinal;
    private String icmsSaidaConsumidorFinal;
    private String icmsCstSaidaRevenda;
    private String icmsSaidaRevenda;
    private String mvaInterno;
    private String codigoCsosn;
    private Map<String,String> mvaExterno;
    private Map<String, String> icmsAtacado;

    public Produto() {
        icmsAtacado = new HashMap<>();
        mvaExterno = new HashMap<>();
    }

    /**
     * @return the codigoInterno
     */
    public String getCodigoInterno() {
        return codigoInterno;
    }

    /**
     * @param codigoInterno the codigoInterno to set
     */
    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    /**
     * @return the ncm
     */
    public String getNcm() {
        if (ncm != null) {
            ncm = ncm.trim();
        }
        return ncm;
    }

    public String getNcmLimpo() {
        return getNcm() == null ? "" : getNcm().replaceAll("\\.", "");
    }

    public String getNcmDoisPrimeirosDigitos() {
        return ncm == null || ncm.length() < 2 ? "" : ncm.substring(0, 2);
    }

    /**
     * @param ncm the ncm to set
     */
    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    /**
     * @return the descricaoNcm
     */
    public String getDescricaoNcm() {
        return descricaoNcm;
    }

    /**
     * @param descricaoNcm the descricaoNcm to set
     */
    public void setDescricaoNcm(String descricaoNcm) {
        this.descricaoNcm = descricaoNcm;
    }

    /**
     * @return the cest
     */
    public String getCest() {
        if (cest != null) {
            cest = cest.trim();
        }
        return cest;
    }
    
    public String getCestLimpo() {
        return getCest() == null ? "" : getCest().replaceAll("\\.", "");
    }

    /**
     * @param cest the cest to set
     */
    public void setCest(String cest) {
        this.cest = cest;
    }

    /**
     * @return the ipi
     */
    public String getIpi() {
        return ipi;
    }

    /**
     * @param ipi the ipi to set
     */
    public void setIpi(String ipi) {
        this.ipi = ipi;
    }

    /**
     * @return the pisEntrada
     */
    public String getPisEntrada() {
        return pisEntrada;
    }

    public BigDecimal getPisEntradaDecimal() {
        if (pisEntrada != null && !pisEntrada.trim().isEmpty()) {
            return new BigDecimal(pisEntrada);
        }

        return null;
    }

    /**
     * @param pisEntrada the pisSaida to set
     */
    public void setPisEntrada(String pisEntrada) {
        this.pisEntrada = pisEntrada;
    }

    /**
     * @return the pisSaida
     */
    public String getPisSaida() {
        return pisSaida;
    }

    public BigDecimal getPisSaidaDecimal() {
        if (pisSaida != null && !pisSaida.trim().isEmpty()) {
            return new BigDecimal(pisSaida);
        }

        return null;
    }

    /**
     * @param pisSaida the pisSaida to set
     */
    public void setPisSaida(String pisSaida) {
        this.pisSaida = pisSaida;
    }

    /**
     * @return the cofinsEntrada
     */
    public String getCofinsEntrada() {
        return cofinsEntrada;
    }

    public BigDecimal getCofinsEntradaDecimal() {
        if (cofinsEntrada != null && !cofinsEntrada.trim().isEmpty()) {
            return new BigDecimal(cofinsEntrada);
        }

        return null;
    }

    /**
     * @param cofinsEntrada the cofinsSaida to set
     */
    public void setCofinsEntrada(String cofinsEntrada) {
        this.cofinsEntrada = cofinsEntrada;
    }

    /**
     * @return the cofinsSaida
     */
    public String getCofinsSaida() {
        return cofinsSaida;
    }

    public BigDecimal getCofinsSaidaDecimal() {
        if (cofinsSaida != null && !cofinsSaida.trim().isEmpty()) {
            return new BigDecimal(cofinsSaida);
        }

        return null;
    }

    /**
     * @param cofinsSaida the cofinsSaida to set
     */
    public void setCofinsSaida(String cofinsSaida) {
        this.cofinsSaida = cofinsSaida;
    }

    /**
     * @return the piscofinsCstEntrada
     */
    public String getPiscofinsCstEntrada() {
        return piscofinsCstEntrada;
    }

    /**
     * @param piscofinsCstEntrada the piscofinsCstEntrada to set
     */
    public void setPiscofinsCstEntrada(String piscofinsCstEntrada) {
        this.piscofinsCstEntrada = piscofinsCstEntrada;
    }

    /**
     * @return the piscofinsCstSaida
     */
    public String getPiscofinsCstSaida() {
        return piscofinsCstSaida;
    }

    /**
     * @param piscofinsCstSaida the piscofinsCstSaida to set
     */
    public void setPiscofinsCstSaida(String piscofinsCstSaida) {
        this.piscofinsCstSaida = piscofinsCstSaida;
    }

    /**
     * @return the icmsCstSaidaConsumidorFinal
     */
    public String getIcmsCstSaidaConsumidorFinal() {
        if (icmsCstSaidaConsumidorFinal != null) {
            icmsCstSaidaConsumidorFinal = icmsCstSaidaConsumidorFinal.trim();
        }
        return icmsCstSaidaConsumidorFinal;
    }

    /**
     * @param icmsCstSaidaConsumidorFinal the icmsCstSaidaConsumidorFinal to set
     */
    public void setIcmsCstSaidaConsumidorFinal(String icmsCstSaidaConsumidorFinal) {
        this.icmsCstSaidaConsumidorFinal = icmsCstSaidaConsumidorFinal;
    }

    /**
     * @return the icmsSaidaConsumidorFinal
     */
    public String getIcmsSaidaConsumidorFinal() {
        return icmsSaidaConsumidorFinal;
    }

    public BigDecimal getIcmsSaidaConsumidorFinalBigDecimal() {
        if (icmsCstSaidaConsumidorFinal != null && !icmsCstSaidaConsumidorFinal.trim().isEmpty()) {
            return new BigDecimal(icmsSaidaConsumidorFinal);
        }
        return null;
    }

    /**
     * @param icmsSaidaConsumidorFinal the icmsSaidaConsumidorFinal to set
     */
    public void setIcmsSaidaConsumidorFinal(String icmsSaidaConsumidorFinal) {
        this.icmsSaidaConsumidorFinal = icmsSaidaConsumidorFinal;
    }
    
      public String getIcmsCstSaidaRevenda() {
        return icmsCstSaidaRevenda;
    }

    public void setIcmsCstSaidaRevenda(String icmsCstSaidaRevenda) {
        this.icmsCstSaidaRevenda = icmsCstSaidaRevenda;
    }

    public String getIcmsSaidaRevenda() {
        return icmsSaidaRevenda;
    }
    
    public BigDecimal getIcmsSaidaRevendaBigDecimal() {
        if (icmsCstSaidaRevenda != null && !icmsCstSaidaRevenda.trim().isEmpty()) {
            return new BigDecimal(icmsSaidaRevenda);
        }
        return null;
    }

    public void setIcmsSaidaRevenda(String icmsSaidaRevenda) {
        this.icmsSaidaRevenda = icmsSaidaRevenda;
    }

    /**
     * @return the mvaInterno
     */
    public String getMvaInterno() {
        return mvaInterno;
    }

    /**
     * @param mvaInterno the mvaInterno to set
     */
    public void setMvaInterno(String mvaInterno) {
        this.mvaInterno = mvaInterno;
    }

    /**
     * @return the mvaExterno
     */
    public String getMvaExterno(String estado) {
        return mvaExterno.get(estado.toUpperCase());
    }

    /**
     * @param mvaExterno the mvaExterno to set
     */
    public void addMvaExterno(String estado, String mvaExterno) {
        this.mvaExterno.put(estado.toUpperCase(), mvaExterno);
    }

    /**
     * @return the icmsAtacado
     */
    public String getIcmsAtacado(String estado) {
        return icmsAtacado.get(estado.toUpperCase());
    }

    public BigDecimal getIcmsAtacadoDecimal(String estado) {
        String icms = icmsAtacado.get(estado.toUpperCase());
        if (icms != null && !icms.trim().isEmpty()) {
            return new BigDecimal(icms);
        }
        return null;
    }

    /**
     * @param icmsAtacado the icmsAtacado to set
     */
    public void addIcmsAtacado(String estado, String icmsAtacado) {
        this.icmsAtacado.put(estado.toUpperCase(), icmsAtacado);
    }
    
    public String getCodigoCsosn() {
        return codigoCsosn;
    }

    /**
     * @param codigoCSosn the codigoCSosn to set
     */
    public void setCodigoCSosn(String codigoCSosn) {
        this.codigoCsosn = codigoCSosn;
    }

    /**
     * @return the pisconfinsSituacao
     */
    public String getPisconfinsSituacao() {
        return pisconfinsSituacao;
    }

    /**
     * @param pisconfinsSituacao the pisconfinsSituacao to set
     */
    public void setPisconfinsSituacao(String pisconfinsSituacao) {
        this.pisconfinsSituacao = pisconfinsSituacao;
    }

    /**
     * @return the pisCofinsNaturezaReceita
     */
    public String getPisCofinsNaturezaReceita() {
        return pisCofinsNaturezaReceita;
    }

    /**
     * @param pisCofinsNaturezaReceita the pisCofinsNaturezaReceita to set
     */
    public void setPisCofinsNaturezaReceita(String pisCofinsNaturezaReceita) {
        this.pisCofinsNaturezaReceita = pisCofinsNaturezaReceita;
    }

    /**
     * @return the descricaoCest
     */
    public String getDescricaoCest() {
        return descricaoCest;
    }

    /**
     * @param descricaoCest the descricaoCest to set
     */
    public void setDescricaoCest(String descricaoCest) {
        this.descricaoCest = descricaoCest;
    }

}
