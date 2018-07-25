package br.com.alertafiscal.opusprimum.utils;

import br.com.alertafiscal.opusprimum.Componente;

public final class InteiroUtils extends Componente {

    /**
     * <p>Tenta converter o objeto passado por parâmetro em um inteiro. Caso não
     * seja possível, retorna o retorno padrão.</p>
     *
     * @param object        a ser convertido em inteiro.
     * @param retornoPadrao
     * @return o objeto passado por parâmetro ou o retorno padrão quando não for
     *         possível.
     */
    public int converterParaInteiro(Object object, int retornoPadrao) {
        try {
            return Integer.parseInt(String.valueOf(object));
        } catch (Exception ex) {
            return retornoPadrao;
        }
    }
}
