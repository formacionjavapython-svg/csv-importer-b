# CSV Importer - B06_Josue Israel Vasquez Martinez

Proyecto sencillo en Java para importar transacciones desde un archivo CSV, aplicando Programación Orientada a Objetos, validación básica, control de rutas seguras y pruebas sin JUnit.

## Objetivo

Desarrollar un programa simple y limpio que permita:

- Leer un archivo CSV de transacciones
- Procesar registros de entrada y salida
- Validar datos
- Calcular totales
- Detectar errores
- Evitar rutas inseguras (`path traversal`)
- Ejecutar pruebas sin dependencias externas

## Estructura del proyecto

```bash
csv-importer/
├── checkstyle.xml
├── data/
│   └── transactions.csv
├── testdata/
│   ├── totals.csv
│   └── valid-invalid.csv
├── src/
│   ├── Importer.java
│   ├── ImporterForTests.java
│   ├── ImportResult.java
│   ├── Main.java
│   ├── Money.java
│   ├── TestRunner.java
│   ├── Transaction.java
│   └── TxType.java
└── test-report.txt
```

## Descripción general del diseño POO

El proyecto está dividido en clases con responsabilidades bien definidas.

### 1. TxType.java

```java
// clase TxType.java definimos variables de entrada salida
```

Esta clase es un `enum` y define los tipos de transacción permitidos:

- `IN` = entrada de dinero
- `OUT` = salida de dinero

Se utiliza para evitar errores por escribir tipos manualmente como texto libre.

### 2. Money.java

```java
// clase Money.java encapsula el monto y la moneda
```

Esta clase representa una cantidad monetaria.

Contiene:

- `amount` = monto
- `currency` = moneda

También valida:

- que el monto no esté vacío
- que la moneda no esté vacía
- que el monto no sea negativo

Ejemplo:

```java
Money money = new Money("100.50", "MXN");
```

### 3. Transaction.java

```java
// clase Transaction.java representa una transaccion del archivo csv
```

Esta clase representa una transacción individual.

Cada fila válida del CSV se convierte en un objeto `Transaction`.

Contiene:

- `id`
- `type`
- `money`
- `description`

Ejemplo:

```java
Transaction tx = new Transaction("TX001", TxType.IN, money, "Deposito cliente");
```

### 4. ImportResult.java

```java
// clase ImportResult.java guarda el resumen del proceso de importacion
```

Esta clase almacena el resultado de la importación.

Contiene:

- cantidad de registros válidos
- cantidad de registros inválidos
- total de entradas
- total de salidas
- lista de errores

Esto permite regresar un resultado ordenado al final del proceso.

### 5. Importer.java

```java
// clase Importer.java lee el csv, valida rutas y procesa lineas
```

Esta es la clase principal del proceso de importación.

Se encarga de:

- validar la ruta del archivo
- leer el CSV
- ignorar encabezados
- procesar cada línea
- crear objetos `Transaction`
- registrar errores
- devolver un `ImportResult`

### 6. ImporterForTests.java

```java
// clase ImporterForTests.java se usa para cambiar la carpeta base en pruebas
```

Esta clase hereda de `Importer`.

Su función es permitir que las pruebas trabajen sobre la carpeta `testdata/` en lugar de `data/`.

### 7. Main.java

```java
// clase Main.java ejecuta la importacion normal del archivo transactions.csv
```

Es el punto de entrada principal del programa.

Desde aquí se ejecuta la importación real del archivo:

```text
data/transactions.csv
```

Después muestra:

- número de válidos
- número de inválidos
- total de ingresos
- total de egresos
- errores encontrados

### 8. TestRunner.java

```java
// clase TestRunner.java ejecuta pruebas sin usar junit
```

Esta clase funciona como un framework de pruebas básico hecho manualmente.

Permite:

- ejecutar casos de prueba
- capturar excepciones
- mostrar si la prueba pasó o falló
- generar un reporte en consola y archivo

## Explicación de los archivos CSV

### 1. data/transactions.csv

```java
// csv transactions.csv sirve para la ejecucion principal del sistema
```

Este archivo se usa en `Main.java` para la ejecución normal del programa.

Ejemplo:

```csv
id,type,amount,currency,description
TX001,IN,1000.50,MXN,Deposito cliente
TX002,OUT,250.00,MXN,Compra material
TX003,IN,500.00,MXN,Venta local
TX004,OUT,-50.00,MXN,Monto invalido
TX005,ABC,100.00,MXN,Tipo invalido
```

### 2. testdata/totals.csv

```java
// csv totals.csv sirve para totales de datos
```

Este archivo se usa en pruebas para verificar que los totales calculados sean correctos.

Ejemplo:

```csv
id,type,amount,currency,description
B001,IN,100.00,MXN,Ingreso uno
B002,IN,200.00,MXN,Ingreso dos
B003,OUT,50.00,MXN,Egreso uno
B004,OUT,30.00,MXN,Egreso dos
```

Resultado esperado:

- total entradas = `300.00`
- total salidas = `80.00`

### 3. testdata/valid-invalid.csv

```java
// csv valid-invalid.csv sirve para validar conteo de registros correctos e incorrectos
```

Este archivo se usa para comprobar que el sistema detecte correctamente cuántos registros son válidos y cuántos inválidos.

Ejemplo:

```csv
id,type,amount,currency,description
A001,IN,200.00,MXN,Ingreso correcto
A002,OUT,50.00,MXN,Egreso correcto
A003,OUT,-10.00,MXN,Monto invalido
A004,OTHER,100.00,MXN,Tipo invalido
A005,IN,300.00,MXN,Otro ingreso correcto
```

Resultado esperado:

- válidos = `3`
- inválidos = `2`



## Flujo del sistema

### Flujo principal

1. `Main.java` llama a `Importer`
2. `Importer` valida la ruta del archivo
3. Se abre el CSV
4. Se ignora la primera línea porque es encabezado
5. Cada línea se separa por comas
6. Se crea un objeto `Money`
7. Se crea un objeto `Transaction`
8. Se acumulan totales en `ImportResult`
9. Si una línea tiene error, se registra
10. Al final se imprime el resumen

## Salida esperada del programa principal

Al ejecutar `Main.java` con el archivo `transactions.csv`, la salida esperada es:

```bash
=== RESUMEN DE IMPORTACION ===
Registros validos: 3
Registros invalidos: 2
Total ingresos: 1500.50
Total egresos: 250.00
=== ERRORES ===
Linea 5: El monto no puede ser negativo
Linea 6: Tipo invalido: ABC
```

## Pruebas sin JUnit

### Objetivo

Se implementó un `TestRunner` personalizado para comprender mejor cómo funciona la lógica de un framework de pruebas, pero sin usar librerías externas.

### Casos de prueba implementados

#### Caso 1: Bloqueo de Path Traversal

```java
// testPathTraversalBlocked verifica que rutas peligrosas sean bloqueadas
```

Verifica que rutas como:

- `../etc/passwd`
- `C:\Windows\System32`

sean rechazadas.

#### Caso 2: Conteo de válidos e inválidos

```java
// testValidInvalidCount verifica que el conteo de registros validos e invalidos sea correcto
```

Usa el archivo:

```text
testdata/valid-invalid.csv
```

Valida que:

- registros válidos = `3`
- registros inválidos = `2`

#### Caso 3: Totales correctos

```java
// testCorrectTotals verifica que los totales de entrada y salida sean correctos
```

Usa el archivo:

```text
testdata/totals.csv
```

Valida que:

- total entradas = `300.00`
- total salidas = `80.00`

## Salida esperada de las pruebas

Al ejecutar `TestRunner.java`, la salida esperada es:

```bash
[PASS] Caso 1: Bloqueo de Path Traversal
[PASS] Caso 2: Conteo Validos/Invalidos
[PASS] Caso 3: Totales Correctos
=== REPORTE DE PRUEBAS ===
Pruebas aprobadas: 3
Pruebas fallidas: 0
Archivo de reporte generado: test-report.txt
```

## Archivo de reporte de pruebas

Después de ejecutar `TestRunner`, se genera automáticamente:

```text
test-report.txt
```

Contenido esperado:

```text
Pruebas aprobadas: 3
Pruebas fallidas: 0
```

## Calidad de código con Checkstyle

### ¿Qué es checkstyle.xml?

```java
// archivo checkstyle.xml define reglas de estilo y calidad del codigo
```

Es un archivo de configuración que usa la herramienta Checkstyle para revisar automáticamente el estilo del proyecto.

No forma parte de la lógica del programa.

No se importa en Java.

No se ejecuta desde `Main`.

Solo sirve para revisar si el código cumple ciertas reglas.

### Reglas aplicadas

- Longitud máxima de línea: 120 caracteres
- Nombres de clases en PascalCase
- Variables y métodos en camelCase
- Indentación consistente
- Uso de `final` en variables locales
- Uso de llaves en bloques

### Buenas prácticas aplicadas en el proyecto

- Métodos pequeños
- Una responsabilidad por método
- Early return
- Validación temprana
- Mensajes claros en excepciones
- Inmutabilidad con `final`

## Compilación y ejecución

### Compilar

```bash
javac src/*.java
```

### Ejecutar programa principal

```bash
java -cp src Main
```

### Ejecutar pruebas

```bash
java -cp src TestRunner
```

## Relación entre clases y archivos

### Ejecución normal

`Main.java` usa:

- `Importer.java`
- `ImportResult.java`
- `Transaction.java`
- `Money.java`
- `TxType.java`
- `data/transactions.csv`

### Ejecución de pruebas

`TestRunner.java` usa:

- `ImporterForTests.java`
- `Importer.java`
- `ImportResult.java`
- `Transaction.java`
- `Money.java`
- `TxType.java`
- `testdata/totals.csv`
- `testdata/valid-invalid.csv`

## Explicación breve del proyecto

Este proyecto implementa un importador de transacciones en Java usando Programación Orientada a Objetos.

Cada clase tiene una responsabilidad clara:

- `TxType` define tipos de entrada y salida
- `Money` encapsula monto y moneda
- `Transaction` representa cada registro del CSV
- `ImportResult` guarda resultados y errores
- `Importer` orquesta el proceso de importación
- `TestRunner` ejecuta pruebas básicas sin JUnit

Además, el sistema incluye validación contra `path traversal`, control de calidad con `Checkstyle` y pruebas sobre archivos CSV controlados.

## Comentarios de apoyo por archivo

### TxType.java

```java
// clase TxType.java definimos variables de entrada salida
```

### Money.java

```java
// clase Money.java valida y representa montos monetarios
```

### Transaction.java

```java
// clase Transaction.java modela una transaccion individual
```

### ImportResult.java

```java
// clase ImportResult.java resume resultados validos invalidos y errores
```

### Importer.java

```java
// clase Importer.java procesa el archivo csv y valida rutas seguras
```

### ImporterForTests.java

```java
// clase ImporterForTests.java redirige la lectura de archivos a testdata
```

### Main.java

```java
// clase Main.java ejecuta el flujo principal del sistema
```

### TestRunner.java

```java
// clase TestRunner.java corre pruebas manuales y genera reporte
```

### transactions.csv

```java
// csv transactions.csv sirve como entrada principal del programa
```

### totals.csv

```java
// csv totals.csv sirve para totales de datos
```

### valid-invalid.csv

```java
// csv valid-invalid.csv sirve para conteo de validos e invalidos
```

### checkstyle.xml

```java
// archivo checkstyle.xml revisa estilo y calidad del codigo
```

## Conclusión

Se construyó un sistema sencillo pero ordenado para importar transacciones desde archivos CSV en Java.

El proyecto permite demostrar:

- uso de POO
- validación de datos
- manejo de errores
- separación de responsabilidades
- seguridad de rutas
- pruebas sin JUnit
- calidad de código con Checkstyle

Es una solución básica, clara y fácil de explicar en un contexto académico o de certificación.