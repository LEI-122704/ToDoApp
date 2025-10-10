package envio_email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Scanner;

public class SendEmail {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Sistema de Envio de Emails ===");

        // Solicita dados do usuário
        System.out.print("Digite o email de destino: ");
        String destinatario = scanner.nextLine();

        System.out.print("Digite o assunto: ");
        String assunto = scanner.nextLine();

        System.out.print("Digite a mensagem: ");
        String corpo = scanner.nextLine();

        // Configurações básicas de SMTP
        String host = "smtp.example.com"; // Ex: smtp.gmail.com
        String porta = "587";             // 587 (TLS) ou 465 (SSL)
        String remetente = "seu_email@example.com";
        String senha = "sua_senha";

        // Propriedades do servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", porta);

        // Cria sessão autenticada
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        try {
            // Monta mensagem
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(assunto);
            message.setText(corpo);

            // Envia
            Transport.send(message);

            System.out.println("\n✅ Email enviado com sucesso para " + destinatario);
        } catch (MessagingException e) {
            System.out.println("\n❌ Erro ao enviar email: " + e.getMessage());
        }

        scanner.close();
    }
}
