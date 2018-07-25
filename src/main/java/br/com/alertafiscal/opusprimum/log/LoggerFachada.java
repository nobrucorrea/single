package br.com.alertafiscal.opusprimum.log;

import java.io.Serializable;
import org.slf4j.Logger;

public class LoggerFachada implements Serializable {

    private final Logger logger;

    public LoggerFachada(Logger logger) {
        this.logger = logger;
    }
    public void debug(String message, Object... params) {
        logger.debug(message, params);
    }

    public void exception(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }

    public void exception(Exception ex, String message, Object... params) {
        logger.error(String.format(message.replaceAll("\\{\\}", "%s"), params), ex);
    }

    public void info(String message, Object... params) {
        logger.info(message, params);
    }

    public void warn(String message, Object... params) {
        logger.warn(message, params);
    }
    
    public void trace(String message, Object... params) {
        logger.trace(message, params);
    }
}
