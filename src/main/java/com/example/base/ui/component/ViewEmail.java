package com.example.base.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@Route(value = "email-sender")
public class ViewEmail extends VerticalLayout {

    public ViewEmail() {
        setPadding(true);
        setSpacing(true);

        // Título atualizado (Label -> H1)
        H1 title = new H1("Envio de Emails");
        title.getStyle().set("font-weight", "bold");
        add(title);

        // Campos do formulário
        EmailField destinatarioField = new EmailField("Destinatário");
        destinatarioField.setPlaceholder("exemplo@dominio.com");

        TextField assuntoField = new TextField("Assunto");
        assuntoField.setPlaceholder("Assunto do email");

        TextArea mensagemField = new TextArea("Mensagem");
        mensagemField.setPlaceholder("Digite sua mensagem aqui...");
        mensagemField.setHeight("150px");

        // Botão de envio
        Button enviarBtn = new Button("Enviar Email");
        enviarBtn.getStyle()
                .set("background-color", "#006ee6")
                .set("color", "white");

        enviarBtn.addClickListener(e -> {
            try {
                String destinatario = destinatarioField.getValue();
                String assunto = assuntoField.getValue();
                String corpo = mensagemField.getValue();

                enviarEmail(destinatario, assunto, corpo);
                Notification.show("✅ Email enviado com sucesso para " + destinatario);
            } catch (Exception ex) {
                Notification.show("❌ Erro ao enviar email: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        // Layout do formulário
        FormLayout form = new FormLayout();
        form.add(destinatarioField, assuntoField, mensagemField, enviarBtn);
        add(form);
    }

    private void enviarEmail(String destinatario, String assunto, String corpo) throws MessagingException {
        // Configurações SMTP — substitua pelos seus dados reais
        String host = "smtp.example.com"; // Exemplo: smtp.gmail.com
        String porta = "587"; // TLS
        String remetente = "seu_email@example.com";
        String senha = "sua_senha";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", porta);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senha);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(remetente));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        message.setSubject(assunto);
        message.setText(corpo);

        Transport.send(message);
    }
}
