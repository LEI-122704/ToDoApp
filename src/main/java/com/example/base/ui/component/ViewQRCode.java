package com.example.base.ui.component;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Route(value = "qrcode-tool")
public class ViewQRCode extends VerticalLayout {

    private final Image qrImage;

    public ViewQRCode() {
        setPadding(true);
        setSpacing(true);

        // Título
        H1 title = new H1("Gerador e Leitor de QR Codes");
        title.getStyle().set("font-weight", "bold");
        add(title);

        // Campo de texto
        TextArea textoField = new TextArea("Texto / URL");
        textoField.setPlaceholder("Digite o conteúdo do QR Code...");
        textoField.setWidth("400px");
        textoField.setHeight("120px");

        // Botão de gerar
        Button gerarBtn = new Button("Gerar QR Code");
        gerarBtn.getStyle()
                .set("background-color", "#006ee6")
                .set("color", "white");

        // Botão de ler QR Code
        Button lerBtn = new Button("Ler QR Code (de ficheiro)");
        lerBtn.getStyle()
                .set("background-color", "#00994d")
                .set("color", "white");

        qrImage = new Image();
        qrImage.setWidth("250px");
        qrImage.setHeight("250px");
        qrImage.setVisible(false);

        gerarBtn.addClickListener(e -> {
            try {
                String texto = textoField.getValue();
                if (texto == null || texto.isEmpty()) {
                    Notification.show("⚠️ Digite algum texto primeiro!");
                    return;
                }

                String nomeArquivo = "qrcode.png";
                gerarQRCode(texto, nomeArquivo);

                qrImage.setSrc("qrcode.png"); // não mostra diretamente — apenas referência
                qrImage.getElement().setAttribute("src", "data:image/png;base64," + gerarQRCodeBase64(texto));
                qrImage.setVisible(true);

                Notification.show("✅ QR Code gerado com sucesso!");
            } catch (Exception ex) {
                Notification.show("❌ Erro ao gerar QR Code: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        lerBtn.addClickListener(e -> {
            try {
                String caminho = "qrcode.png"; // exemplo fixo
                String conteudo = lerQRCode(caminho);
                Notification.show("📄 Conteúdo lido: " + conteudo);
            } catch (Exception ex) {
                Notification.show("❌ Erro ao ler QR Code: " + ex.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        FormLayout form = new FormLayout();
        form.add(textoField, gerarBtn, lerBtn);

        add(form, qrImage);
    }

    private void gerarQRCode(String texto, String nomeArquivo) throws Exception {
        int largura = 300;
        int altura = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

        Path path = FileSystems.getDefault().getPath(nomeArquivo);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    private String lerQRCode(String caminho) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new File(caminho));
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }

    private String gerarQRCodeBase64(String texto) throws Exception {
        int largura = 300;
        int altura = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        return java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
