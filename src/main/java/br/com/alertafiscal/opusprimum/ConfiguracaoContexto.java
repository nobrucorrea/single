package br.com.alertafiscal.opusprimum;

import br.com.alertafiscal.opusprimum.utils.DiretorioUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ConfiguracaoContexto extends Componente {

    private Ambiente ambiente;
    private TipoEntrada tipoEntrada;
    private VersaoTipoEntrada versaoTipoEntrada;
    private ModalidadeJuridica modalidadeJuridica;
    private File arquivoConfiguracoes;
    private Properties configuracao;
    private static ConfiguracaoContexto instance;

    private ConfiguracaoContexto() {
        try {
            File arquivoConfiguracoesLocal = new File("config.properties");
            if (arquivoConfiguracoesLocal.exists()) {
                logger.info("Arquivo de configurações localizado no diretório atual.");
                logger.info("Copiando arquivo: " + arquivoConfiguracoesLocal.getAbsolutePath());
                logger.info("Para o diretório: " + new DiretorioUtils().folder(".opus").asFile().getAbsolutePath());
                Files.copy(arquivoConfiguracoesLocal.toPath(), new File(new DiretorioUtils().folder(".opus").asFile(), "config.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            logger.info("Instanciando arquivo de configurações.");
            arquivoConfiguracoes = new File(new DiretorioUtils().folder(".opus").asFile(), "config.properties");

            logger.info("Iniciando procedimentos de instalação das configurações.");
            arquivoConfiguracoes();

            logger.info("Criando o properties com as configurações do Opus Primum.");
            configuracao = new Properties();

            InputStream input;
            logger.info("Criando o inputStream para o arquivo: %s.", arquivoConfiguracoes);
            input = new FileInputStream(arquivoConfiguracoes);

            logger.info("Carregando as propriedades do arquivo no objeto '%s'.", configuracao);
            configuracao.load(input);

            logger.info("Obtendo ambiente de execução do Opus Primum: %s.", configuracao.getProperty("opus.ambiente"));
            ambiente = "desenvolvimento".equalsIgnoreCase(configuracao.getProperty("opus.ambiente")) ? Ambiente.DESENVOLVIMENTO : Ambiente.PRODUCAO;
            logger.info("Obtendo tipo de entrada do Opus Primum: %s.", configuracao.getProperty("opus.tipo.entrada"));
            tipoEntrada = "xml".equalsIgnoreCase(configuracao.getProperty("opus.tipo.entrada")) ? TipoEntrada.XML : TipoEntrada.WEBSERVICE;
            String propriedadeVersaoTipoEntrada = String.valueOf(configuracao.getProperty("opus.tipo.entrada.versao")).toLowerCase();
            logger.info("Obtendo versão do XML: %s.", propriedadeVersaoTipoEntrada);
            switch (propriedadeVersaoTipoEntrada) {
                case "modelo_antigo":
                case "modelo antigo":
                    versaoTipoEntrada = VersaoTipoEntrada.MODELO_ANTIGO;
                    break;
                case "webservice_rj":
                case "webservice rj":
                    versaoTipoEntrada = VersaoTipoEntrada.WEBSERVICE_RJ;
                    break;
                case "soap_final_rj":
                case "soap final rj":
                    versaoTipoEntrada = VersaoTipoEntrada.SOAP_FINAL_RJ;
                    break;
                case "soap_final":
                case "soap final":
                default:
                    versaoTipoEntrada = VersaoTipoEntrada.SOAP_FINAL;
                    break;
            }
            String propriedadeModalidadeJuridica = String.valueOf(configuracao.getProperty("opus.modalidade.juridica")).toLowerCase();
            logger.info("Obtendo modalidade jurídica para o Opus Primum: %s.", propriedadeModalidadeJuridica);
            switch (propriedadeModalidadeJuridica) {
                case "simples_nacional":
                case "simples nacional":
                    modalidadeJuridica = ModalidadeJuridica.SIMPLES_NACIONAL;
                    break;
                case "lucro_presumido":
                case "lucro presumido":
                    modalidadeJuridica = ModalidadeJuridica.LUCRO_PRESUMIDO;
                    break;
                case "lucro_real":
                case "lucro real":
                default:
                    modalidadeJuridica = ModalidadeJuridica.LUCRO_REAL;

            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Arquivo de configurações não encontrado ou configuração errada. Por favor, entre em contato com o responsável técnico pela aplicação.");
            logger.exception(ex, "Erro ao carregar as configurações do Opus Primum.");
        }
    }

    public static ConfiguracaoContexto getInstance() {
        if (instance == null) {
            instance = new ConfiguracaoContexto();
        }
        return instance;
    }

    public boolean isProducao() {
        return ambiente == Ambiente.PRODUCAO;
    }

    public boolean isDesenvolvimento() {
        return ambiente == Ambiente.DESENVOLVIMENTO;
    }

    public boolean isXML() {
        return tipoEntrada == TipoEntrada.XML;
    }

    public boolean isWebService() {
        return tipoEntrada == TipoEntrada.WEBSERVICE;
    }

    public VersaoTipoEntrada getVersaoTipoEntrada() {
        return versaoTipoEntrada;
    }

    public Properties getConfiguracao() {
        return configuracao;
    }

    public void setConfiguracao(Properties configuracao) {
        this.configuracao = configuracao;
    }

    public String getServidorSMTP() {
        return configuracao.getProperty("email.smtp.servidor");
    }

    public String getPortaSMTP() {
        return configuracao.getProperty("email.smtp.porta");
    }

    public String getUsuarioSMTP() {
        return configuracao.getProperty("email.smtp.usuario");
    }

    public String getSenhaSMTP() {
        return configuracao.getProperty("email.smtp.senha");
    }

    public String getEmailRelatorio() {
        return configuracao.getProperty("email.relatorio");
    }

    public String getXMLCaminho() {
        return configuracao.getProperty("xml.caminho");
    }

    public String getWebServiceURL() {
        return configuracao.getProperty("webservice.url");
    }

    public String getWebServiceID() {
        return configuracao.getProperty("webservice.id");
    }

    public String getWebServiceToken() {
        return configuracao.getProperty("webservice.token");
    }

    public boolean isOptantePeloSimplesNacional() {
        return modalidadeJuridica == ModalidadeJuridica.SIMPLES_NACIONAL;
    }

    private void arquivoConfiguracoes() throws IOException, URISyntaxException {
        logger.info("Iniciando procedimento de verificação e criação do arquivo de configurações.");
        File arquivo = new File(ConfiguracaoContexto.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        logger.info("Arquivo da aplicação=%s.", arquivo);

        logger.info("Extraindo pasta.");
        File pasta = arquivo.getParentFile();
        logger.info("Pasta da aplicação=%s.", pasta);

        logger.info("Instanciando arquivo de configurações para cópia.");
        File arquivoConfiguracoesInstalacao = new File(pasta, "config.properties");

        if (!arquivoConfiguracoesInstalacao.exists()) {
            logger.info("Não foi possível localizar um arquivo de configuração para instalação.");
            return;
        }
    }

    public static enum Ambiente {

        DESENVOLVIMENTO, PRODUCAO
    }

    public static enum TipoEntrada {

        XML, WEBSERVICE
    }

    public static enum VersaoTipoEntrada {

        MODELO_ANTIGO, SOAP_FINAL, SOAP_FINAL_RJ, WEBSERVICE_RJ
    }

    public static enum ModalidadeJuridica {

        SIMPLES_NACIONAL, LUCRO_PRESUMIDO, LUCRO_REAL
    }
}
