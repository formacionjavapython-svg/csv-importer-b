# CSV Importer - Importador Seguro de Transacciones

Aplicación de consola en Java que importa y procesa transacciones financieras desde archivos CSV con validaciones de seguridad y diseño orientado a objetos.

##  Características

- **Diseño POO**: Clases `Money`, `Transaction`, `ImportResult`, `Importer`
- **Seguridad**: Protección contra path traversal
- **Validación**: Pipeline funcional (parse → validate → accumulate)
- **Sin dependencias**: Java puro, sin frameworks externos
- **Testing**: TestRunner personalizado sin JUnit

## 📋 Requisitos

- Java 17 JDK o superior
- Git

##  Instalación y Uso

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/csv-importer-lab.git
cd csv-importer-lab
```

### 2. Compilar

**Desde IntelliJ IDEA:**
- Abrir el proyecto
- Build → Build Project

**Desde terminal (Windows):**
```batch
javac -d out src\main.java\*.java
```

**Desde terminal (Linux/Mac):**
```bash
javac -d out src/main.java/*.java
```

### 3. Ejecutar

**Desde IntelliJ:**
- Click derecho en `Main.java` → Run

**Desde terminal (Windows):**
```batch
java -cp out Main
```

**Desde terminal (Linux/Mac):**
```bash
java -cp out Main
```

### 4. Ejecutar Tests

**Desde IntelliJ:**
- Click derecho en `TestRunner.java` → Run

**Desde terminal:**
```bash
java -cp out TestRunner
```

## Estructura del Proyecto
```
csv-importer-b/
├── src/
│   ├── main.java/
│   │   ├── Main.java           # Punto de entrada
│   │   ├── Importer.java       # Orquestador con seguridad
│   │   ├── ImportResult.java   # Resultado del proceso
│   │   ├── Transaction.java    # Modelo de transacción
│   │   ├── Money.java          # Valor monetario
│   │   └── TxType.java         # Enum IN/OUT
│   └── test.java.data/
│       └── test.java/
│           └── TestRunner.java # Framework de testing
├── data/
│   └── transactions.csv        # Archivo de ejemplo
└── README.md
```

##  Formato del CSV
```csv
id,type,amount,currency,description
TX001,IN,1500.50,MXN,Venta de producto A
TX002,OUT,800.00,MXN,Compra de insumos
```



##  Seguridad

- **Path Traversal Protection**: Valida rutas para prevenir acceso a archivos fuera del directorio permitido
- **Directorio Base**: Configurable mediante variable de entorno `IMPORT_BASE_DIR`
- **Validación de Entrada**: Múltiples capas de validación en Money, Transaction e Importer

### Configurar Directorio Base

**Windows:**
```batch
set IMPORT_BASE_DIR=C:\ruta\a\datos
java -cp out Main
```

**Linux/Mac:**
```bash
export IMPORT_BASE_DIR=/ruta/a/datos
java -cp out Main
```

##  Tests

El proyecto incluye un TestRunner personalizado que valida:
-  Validación de Money (montos válidos/inválidos)
-  Parseo de transacciones

**Ejecutar tests:**
```bash
java -cp out TestRunner
```

**Resultado esperado:**
```
=== Starting Tests ===
✓ PASS: Money Validation
✓ PASS: Transaction Parsing

=== Test Results ===
Total: 2
Passed: 2
Failed: 0
Success Rate: 100%
```

##  Ejemplo de Salida
```
Using base directory: data
=== Import Summary ===
Valid records: 4
Invalid records: 1
Total IN: 4751.25 MXN
Total OUT: 800.00 MXN

Errors:
  - Line 5: Invalid format
```

##  Tecnologías

- Java 17
- BigDecimal para cálculos financieros precisos
- NIO Path API para manejo seguro de archivos
- Sin dependencias externas


Ing. Adolfo Simbron M. - Laboratorio de Certificación Java

##  Licencia

Este proyecto es parte de un laboratorio educativo.
