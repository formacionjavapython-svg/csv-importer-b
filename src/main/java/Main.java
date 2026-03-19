package src.main.java;

public class Main {
    public static void main(String[] args) {
        System.out.println("___Cuenta___");
        Money cuenta1 = new Money(1000, "pesos");
        Importer proceso1 = new Importer();
        proceso1.importar("Pruebas/dinero.csv", cuenta1);

        System.out.println("___Saldo final___");
        System.out.println(proceso1.mostrarSaldo());

    }
}
