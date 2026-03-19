package src.main.java;

public class Money {
    private String moneda;
    private int monto;

    public Money(int monto, String moneda){
        this.monto = monto;
        this.moneda = moneda;
    }

    protected int getMoneda(){
        return monto;
    }

    protected void setMoneda(int monto){
        this.monto = monto;
    }
    
}
