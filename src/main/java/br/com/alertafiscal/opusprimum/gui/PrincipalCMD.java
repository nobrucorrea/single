package br.com.alertafiscal.opusprimum.gui;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.FluxoPrincipal;
import br.com.alertafiscal.opusprimum.log.LoggerFachada;
import br.com.alertafiscal.opusprimum.log.impl.LoggerCMD;

/**
 *
 * @author bastosbf
 */
public class PrincipalCMD {

    public static void rodar(String tipoExecucao) {
        LoggerCMD logger = new LoggerCMD(System.out);
        LoggerFachada loggerFachada = new LoggerFachada(logger);
        Componente.setLogger(loggerFachada);
        try {
            FluxoPrincipal.rodar(tipoExecucao, loggerFachada);
        } catch (Exception ex) {
            loggerFachada.exception(ex);
            System.out.println("Ocorreu um erro ao tentar executar o Opus, entre em contato com o suporte!");
        }
    }

}
