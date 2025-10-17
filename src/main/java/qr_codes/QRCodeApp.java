package qr_codes;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

public class QRCodeApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sistema de Geração e Leitura de QR Codes ===");
        System.out.println("1 - Gerar QR Code");
        System.out.println("2 - Ler QR Code");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        switch (opcao) {
            case 1 -> gerarQRCode(scanner);
            case 2 -> lerQRCode(scanner);
            default -> System.out.println("❌ Opção inválida.");
        }

        scanner.close();
    }

    private static void gerarQRCode(Scanner scanner) {
        try {
            System.out.print("Digite o texto ou URL para o QR Code: ");
            String texto = scanner.nextLine();

            System.out.print("Digite o nome do ficheiro (sem extensão): ");
            String nomeArquivo = scanner.nextLine();

            String caminho = nomeArquivo + ".png";
            int largura = 300;
            int altura = 300;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

            Path path = FileSystems.getDefault().getPath(caminho);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("\n✅ QR Code gerado com sucesso: " + caminho);
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao gerar QR Code: " + e.getMessage());
        }
    }

    private static void lerQRCode(Scanner scanner) {
        try {
            System.out.print("Digite o caminho do ficheiro (ex: qrcode.png): ");
            String caminho = scanner.nextLine();

            BufferedImage bufferedImage = ImageIO.read(new File(caminho));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            System.out.println("\n✅ Conteúdo do QR Code: " + result.getText());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao ler QR Code: " + e.getMessage());
        }
    }
}
