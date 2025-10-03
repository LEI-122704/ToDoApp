package cambio_moedas;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import org.javamoney.moneta.Money;

import java.util.Scanner;

public class Conversao {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicita valor ao utilizador
        System.out.print("Digite o valor a converter: ");
        double valorInput = scanner.nextDouble();

        // Solicita moeda de origem
        System.out.print("Digite a moeda de origem (ex: USD): ");
        String moedaOrigem = scanner.next().toUpperCase();

        // Solicita moeda de destino
        System.out.print("Digite a moeda de destino (ex: EUR): ");
        String moedaDestino = scanner.next().toUpperCase();

        // Criar valor monetário de origem
        MonetaryAmount valor = Money.of(valorInput, moedaOrigem);

        try {
            // Obter provider de taxas de câmbio
            ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();

            // Converter valor
            MonetaryAmount valorConvertido = valor.with(provider.getCurrencyConversion(moedaDestino));

            System.out.println("Valor original: " + valor);
            System.out.println("Valor convertido para " + moedaDestino + ": " + valorConvertido);
        } catch (Exception e) {
            System.out.println("Erro na conversão: " + e.getMessage());
        }

        scanner.close();
    }

}
