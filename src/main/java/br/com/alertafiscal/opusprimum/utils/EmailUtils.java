package br.com.alertafiscal.opusprimum.utils;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author bastosbf
 */
public class EmailUtils {

    public static void enviaEmail(String de, String para, String assunto, String corpo, String caminhoArquivo, String nomeArquivo) throws MessagingException {
        String servidorSMTP = ConfiguracaoContexto.getInstance().getServidorSMTP();
        String portaSMTP = ConfiguracaoContexto.getInstance().getPortaSMTP();
        String usuarioSMTP = ConfiguracaoContexto.getInstance().getUsuarioSMTP();
        String senhaSMTP = ConfiguracaoContexto.getInstance().getSenhaSMTP();

        Properties props = System.getProperties();
        props.put("mail.smtp.host", servidorSMTP);
        props.put("mail.smtp.port", portaSMTP);
        props.put("mail.smtp.user", usuarioSMTP);
        Session session = Session.getDefaultInstance(props, null);
        Message mensagem = new MimeMessage(session);
        mensagem.setFrom(new InternetAddress(de));
        mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para, false));
        mensagem.setSubject(assunto);
        mensagem.setHeader("ALERTA-FISCAL", "EMAIL-NOTIFICATION");
        mensagem.setSentDate(new Date());

        Multipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(caminhoArquivo);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(nomeArquivo);
        multipart.addBodyPart(messageBodyPart);

        mensagem.setContent(multipart);
        
        Transport tr = session.getTransport("smtp");
        tr.connect(servidorSMTP, usuarioSMTP, senhaSMTP);
        mensagem.saveChanges();
        tr.sendMessage(mensagem, mensagem.getAllRecipients());
        tr.close();
    }
}
