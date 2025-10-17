package com.example.base.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

@Route(value = "currency-converter")
public class ViewConversao extends VerticalLayout {

    public ViewConversao() {
        setPadding(true);
        setSpacing(true);

        Span title = new Span("Conversor de Moedas"); // 🔁 Label → Span
        title.getStyle().set("font-size", "24px").set("font-weight", "bold");
        add(title);

        // Input de valor
        NumberField valorField = new NumberField("Valor");
        valorField.setValue(0.0);

        // Moeda de origem
        ComboBox<String> origemBox = new ComboBox<>("Moeda de origem");
        origemBox.setItems("USD", "EUR", "GBP", "BRL", "JPY");
        origemBox.setValue("USD");

        // Moeda de destino
        ComboBox<String> destinoBox = new ComboBox<>("Moeda de destino");
        destinoBox.setItems("USD", "EUR", "GBP", "BRL", "JPY");
        destinoBox.setValue("EUR");

        // Botão de converter
        Button converterBtn = new Button("Converter");
        Span resultadoLabel = new Span("");

        converterBtn.addClickListener(e -> {
            try {
                double valor = valorField.getValue();
                String moedaOrigem = origemBox.getValue();
                String moedaDestino = destinoBox.getValue();

                // Cria valor monetário
                MonetaryAmount montanteOrigem = Money.of(valor, moedaOrigem);

                // Provider de taxas de câmbio
                ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();
                CurrencyConversion conversion = provider.getCurrencyConversion(moedaDestino);

                // Converte
                MonetaryAmount montanteDestino = montanteOrigem.with(conversion);

                resultadoLabel.setText("Resultado: " + montanteDestino);
            } catch (Exception ex) {
                Notification.show("Erro na conversão: " + ex.getMessage());
            }
        });

        FormLayout form = new FormLayout();
        form.add(valorField, origemBox, destinoBox, converterBtn, resultadoLabel);
        add(form);
    }
}