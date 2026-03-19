package src.main.java;

public class Transaction {
    private String ID;
    private boolean tipo;
    private int monto;
    private String descripcion;
    private Money cuentaMoney; 

    public Transaction(String ID, boolean tipo, int monto, String descripcion, Money cuentaMoney){
        this.cuentaMoney = cuentaMoney;
        int dinero = cuentaMoney.getMoneda();
        this.ID = ID;
        this.tipo = tipo;
        this.monto = monto;
        if (tipo){
            cuentaMoney.setMoneda(dinero + monto); 
        } 
        else {
            cuentaMoney.setMoneda(dinero - monto);
        }
        this.descripcion = descripcion;
    }

    private void verificar(){
        
    }
}
