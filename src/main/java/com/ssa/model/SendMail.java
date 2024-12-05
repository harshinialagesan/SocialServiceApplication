//package com.ssa.model;
//
//import java.net.PasswordAuthentication;
//import java.util.Properties;
//
//public class SendMail {
//
//    public SendMail(String subject, String inMsg) {
//        Logger log = Logger.getLogger(SendMail.class);
//        log.debug("Error SendMail: " + subject + " | Msg: " + inMsg);
//        final String smtpAuthUserName = "eSolutions@ntca.org";
//        final String smtpAuthPassword = "ITDev.2015";
//        String emailTo = "pswain@ntca.org";   // all emails will go to this address
//        Authenticator authenticator = new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(smtpAuthUserName, smtpAuthPassword);
//            }
//        };
//        Properties properties = new Properties();
//        properties.setProperty("mail.smtp.host", "smtp.office365.com");
//        properties.setProperty("mail.smtp.port", "587");
//        properties.setProperty("mail.smtp.auth", "true");
//        properties.setProperty("mail.smtp.starttls.enable", "true");
//        Session session = Session.getInstance(properties, authenticator);
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(smtpAuthUserName));
//            InternetAddress[] to = { new InternetAddress(emailTo) };
//            message.setRecipients(Message.RecipientType.TO, to);
//            message.setSubject(subject);
//            message.setText(inMsg);
//            Transport.send(message);
//        } catch (MessagingException exception) {
//            exception.printStackTrace();
//        }
//    }
//}
