package br.com.alertafiscal.opusprimum.utils;

/**
 *
 * @author bastosbf
 */
public class OpusUtils {

    public static String removePontos(String string) {
        return string == null ? "" : string.replaceAll("\\.", "");
    }
    
    /*
    Insere zeros a esquerda do id
    na retaguarda syspdv o id do produto tem 14 caracteres sendo varios zeros a esquerda
    o xml da revisao o id do produto vem sem os zeros a esquerda
    */
    public static String completeToLeft(String value, char c, int size){
        String result = value;
		while (result.length() < size) {
			result = c + result;
		}
		return result;
    
    }
    
}
