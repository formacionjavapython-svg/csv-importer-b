package test.javascripts;



import main.java.Money;

public class TestRunner extends Money {
    public TestRunner(String cantidad, String moneda) {
        super(cantidad, moneda);
    }

    public static void main(String[] args) {
        // Aquí inician pruebas unitarias
        testMoney();
        System.out.println("Running tests...");
        System.out.println("All tests passed");
    }

    private static void testMoney() {
        Money m1 = new Money("100.00", "MXN");
        Money m2 = new Money("50.00", "MXN");

        if (!m1.add(m2).toString().equals("150.00 MXN")) {
            throw new RuntimeException("Money addition failed");
        }
    }

}
