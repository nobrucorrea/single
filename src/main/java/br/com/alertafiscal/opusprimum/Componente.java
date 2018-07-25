package br.com.alertafiscal.opusprimum;

import br.com.alertafiscal.opusprimum.log.LoggerFachada;

public class Componente {

    protected static LoggerFachada logger;

    public static void setLogger(LoggerFachada logger) {
        Componente.logger = logger;
    }
    
    public static LoggerFachada getLogger() {
        return Componente.logger;
    }
    
}
