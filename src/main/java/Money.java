package main.java;

public class Money {

    private final double valorMonetario;
    private final String moneda;

    public Money(String moneda, String valorMonetario) {

        if(Double.isNaN(Double.parseDouble(valorMonetario)))
            throw new IllegalArgumentException("Valor monetario no valido");

        if(moneda == null || moneda.isBlank())
            throw new IllegalArgumentException("Tipo de moneda no valido");

        this.moneda = moneda;
        this.valorMonetario = Double.parseDouble(valorMonetario);
    }

    public double getValorMonetario() {
        return this.valorMonetario;
    }

    public String getMoneda() {
        return this.moneda;
    }
}
