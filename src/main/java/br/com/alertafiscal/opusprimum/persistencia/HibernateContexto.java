package br.com.alertafiscal.opusprimum.persistencia;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateContexto extends Componente {

    private static Configuration configuration;
    private static SessionFactory factory;
    private static String classeRepositorio;

    public static Session getSession() {
        if (factory == null) {
            logger.info("Instância de factory nula; instanciando factory do Hibernate.");
            init();
        }

        logger.debug("Retornando sessão da factory '%s'.", factory);
        return factory.openSession();
    }

    /**
     * Inicia a SessionFactory
     */
    private static void init() {
        if (configuration == null) {
            try {
                configuration = new Configuration().setProperties(ConfiguracaoContexto.getInstance().getConfiguracao());
            } catch (Exception e) {
                logger.exception(e);
            }
        }

        try {
            logger.info("Chamando buildSessionFactory().");
            factory = configuration.buildSessionFactory();
        } catch (Exception e) {
            logger.exception(e);
        }
    }

    public static Retaguarda instanciarRepositorio() {
        if (classeRepositorio == null) {
            logger.info("Carregando nome da classe padrão de retaguarda.");
            classeRepositorio = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("retaguarda.impl");
        }

        logger.info("Classe de retaguarda encontrada=%s.", classeRepositorio);

        try {
            return (Retaguarda) Class.forName(classeRepositorio).newInstance();
        } catch (Exception ex) {
            logger.exception(ex);
            return null;
        }
    }
}
