package uy.com.ucu.web.utilities;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtilities {
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    static String login = "farmaciayaing3@gmail.com";
    static String password = "putoelquelee";
    static String to = "dbrignar@gmail.com";
    static String cc = null;
    static String bcc = null;
    static String subject = "prueba";
    static String body = "contendo";
    
    public static void config(String... namesAndValues) {
        if (namesAndValues.length % 2 == 1) {
            throw new IllegalArgumentException("The number of arguments must be pair.");
        }
        String nameConfig = null, valueConfig = null;
        for (int i = 0; i < namesAndValues.length - 1; i += 2) {
            nameConfig = namesAndValues[i].trim().toLowerCase();
            valueConfig = namesAndValues[i +1];
            switch    (nameConfig) {
                case "username":
                case "login":
                	MailUtilities.login = valueConfig;
                    break;
                case "password":
                case "pass":
                	MailUtilities.password = valueConfig;
                    break;
                case "to":
                	MailUtilities.to = valueConfig;
                    break;
                case "title":
                case "subject":
                	MailUtilities.subject = valueConfig;
                    break;
                case "msg":
                case "body":
                	MailUtilities.body = valueConfig;
            }
        }
    }
    public static Boolean send(String... namesAndValues) {
    	MailUtilities.config(namesAndValues);
        try {
            return MailUtilities.send();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    public static Boolean send() throws AddressException, MessagingException {
        Boolean success = false;
        // step 1 set connection properties
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
        mailServerProperties.put("mail.smtp.socketFactory.port", "465");
        mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.port", "465");
        // step 2 Authentication
        if (MailUtilities.login == null || MailUtilities.password == null) {
                return success;
        }
        Session getMailSession = Session.getDefaultInstance(mailServerProperties,new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(MailUtilities.login, MailUtilities.password);
            }
        });

        // step 3 sending Email
        generateMailMessage = new MimeMessage(getMailSession);
        if (MailUtilities.to != null) {
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(MailUtilities.to));
        }
        if (MailUtilities.cc != null) {
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(MailUtilities.cc));
        }
        if (MailUtilities.bcc != null) {
            generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(MailUtilities.bcc));
        }
        if (MailUtilities.subject == null) {
        	MailUtilities.subject = "subject gmail object";
        }
        generateMailMessage.setSubject(MailUtilities.subject);
        if (MailUtilities.body == null) {
        	MailUtilities.body = "<h1>body gmail object</h1><p>it's a simple test</p>";
        }
        generateMailMessage.setContent(MailUtilities.body, "text/html");
        javax.mail.Transport.send(generateMailMessage);
        return success;
    }

}