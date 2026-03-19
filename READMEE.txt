
                          CSV IMPORTER                                
            Sistema de Lectura y Procesamiento de CSV               



¿QUÉ ES?

Sistema Java simple que lee un archivo CSV, procesa transacciones
bancarias (ingresos y egresos), y calcula totales automáticamente.


¿PARA QUÉ SIRVE?

Importa datos de transacciones desde un archivo CSV, las organiza
en objetos Java, y proporciona un resumen con totales de ingresos,
egresos y balance general.


¿QUÉ HACE?

1. Lee archivo "datos/transacciones.csv"
2. Parsea 5 columnas: ID, TIPO, MONTO, MONEDA, DESCRIPCION
3. Crea objetos Transaction con los datos
4. Suma todos los INGRESOS (IN)
5. Suma todos los EGRESOS (OUT)
6. Calcula BALANCE (ingresos - egresos)
7. Muestra resumen en consola


CLASES PRINCIPALES

Money.java: Almacena un monto con su moneda.
  - Atributos: amount (double), currency (String)

Transaction.java: Representa una transacción individual.
  - Atributos: id, type, money, description

Importer.java: Lee y procesa el CSV.
  - Método importCSV(): Lee archivo y retorna lista de transacciones
  - Método getTotalIN(): Suma ingresos
  - Método getTotalOUT(): Suma egresos

Main.java: Punto de entrada que ejecuta el programa.
  - Crea Importer, lee CSV, muestra resultados

TestRunner.java: Ejecuta 8 pruebas automatizadas.
  - Prueba lectura correcta del CSV
  - Verifica cálculos exactos
  - Valida seguridad básica


CÓMO COMPILAR

javac -encoding UTF-8 src/*.java


CÓMO EJECUTAR PROGRAMA

java -cp src Main

Muestra: Todas las transacciones y totales de ingresos/egresos


CÓMO EJECUTAR PRUEBAS

java -cp src TestRunner

Muestra: 8 pruebas automatizadas (8/8 pasan)


RESULTADO ESPERADO (PROGRAMA)

=== CSV IMPORTER ===

Leyendo: datos/transacciones.csv
Se leyeron 5 transacciones

Transacciones:
TX001 | IN | 1000.0 MXN | Venta producto
TX002 | OUT | 500.0 MXN | Pago proveedor
TX003 | IN | 2000.0 MXN | Comisión
TX004 | OUT | 300.0 MXN | Gastos
TX005 | IN | 1500.0 MXN | Servicio

=== RESUMEN ===
Total procesadas: 5
Total INGRESOS: 4500.0
Total EGRESOS: 800.0
BALANCE: 3700.0


RESULTADO ESPERADO (PRUEBAS)

=== PRUEBAS SIMPLIFICADAS ===

PRUEBA 1: Path Traversal
   Rechaza ../
   Acepta datos/archivo.csv

PRUEBA 2: CSV Válido
  Lee 5 transacciones
  Parsea ID correctamente

PRUEBA 3: Conteo Válidas/Inválidas
  Cuenta 5 transacciones válidas
  Todos los tipos son válidos (IN/OUT)

PRUEBA 4: Totales Correctos
  Total INGRESOS = 4500.0
  Total EGRESOS = 800.0

=== RESULTADOS ===
Pasadas: 8/8
TODAS PASARON


ESTRUCTURA DEL ARCHIVO CSV

Formato: ID,TIPO,MONTO,MONEDA,DESCRIPCION
Ejemplo: TX001,IN,1000.00,MXN,Venta producto

Requisitos:
- Exactamente 5 columnas
- TIPO solo puede ser "IN" (ingreso) u "OUT" (egreso)
- MONTO usa punto (.) no coma (,)
- Primera línea es encabezado
- Resto son datos


CONCEPTOS JAVA UTILIZADOS

Clases y objetos, constructores, métodos, ArrayList, for-each,
lectura de archivos, String.split(), Double.parseDouble(),
excepciones (try-catch), pruebas unitarias básicas.


ESTADO DEL PROYECTO

5 clases implementadas: BIEN
8 pruebas automatizadas: BIEN (8/8 pasan)
Funcionalidad completa: BIEN
Listo para usar: BIEN


