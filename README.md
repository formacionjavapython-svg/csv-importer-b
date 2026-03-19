# CSV Importer Lab

Este proyecto es una implementación didáctica de un **importador de transacciones financieras en CSV** escrita en Java puro. El objetivo principal es demostrar buenas prácticas de **POO**, seguir principios de diseño funcional y aplicar medidas básicas de **seguridad** y **calidad de código** sin depender de frameworks ni bibliotecas externas.

## Objetivo

Construir una aplicación de consola que lea un archivo CSV desde un directorio permitido, valide y procese cada línea como una transacción (ingreso o egreso) y genere un resumen con:

- Número de registros válidos e inválidos.
- Totales de ingresos (`IN`) y egresos (`OUT`).
- Lista de errores descriptivos para las filas inválidas.

El diseño se inspira en el laboratorio de la Universidad Veracruzana【439643924922804†L13-L34】 e incluye pruebas automáticas y verificación con Checkstyle para mantener un código limpio【439643924922804†L171-L188】.

## Estructura del proyecto

```
csv-importer-lab/
├── src
│   ├── main/java/com/csvimporter
│   │   ├── Importer.java        # Orquesta la lectura, validación y acumulación
│   │   ├── ImportResult.java    # Acumula resultados y errores
│   │   ├── Money.java           # Valor monetario con monto y moneda
│   │   ├── Transaction.java     # Representa una transacción financiera
│   │   ├── TxType.java          # Enum con tipos de transacción (IN, OUT)
│   │   └── Main.java            # Punto de entrada del programa
│   └── test/java/com/csvimporter
│       └── TestRunner.java      # Framework casero de pruebas
├── checkstyle.xml               # Reglas básicas de estilo de código
└── README_user.md               # Guía detallada para comprender el proyecto
```

## Prerrequisitos

- **Java 17** o superior instalado en tu sistema【439643924922804†L36-L40】.
- **Git** configurado para clonar el repositorio【439643924922804†L44-L49】.
- Opcional: una cuenta en **GitHub** para configurar CI con Actions【439643924922804†L50-L55】.

## Compilación y ejecución

1. Clona este repositorio y navega al directorio del proyecto:
   ```bash
   git clone https://github.com/tu-usuario/csv-importer-lab.git
   cd csv-importer-lab
   ```

2. Compila el proyecto usando `javac` (no se requiere Maven ni Gradle). Asegúrate de crear un directorio `bin` para los `.class`:
   ```bash
   mkdir -p bin
   javac -d bin src/main/java/com/csvimporter/*.java
   ```

3. Establece la variable de entorno `IMPORT_BASE_DIR` con el directorio donde se encuentran tus archivos CSV. Por ejemplo:
   ```bash
   export IMPORT_BASE_DIR=/ruta/a/csvs
   ```

4. Ejecuta la aplicación pasando como argumento el nombre del archivo (relativo a `IMPORT_BASE_DIR`):
   ```bash
   java -cp bin com.csvimporter.Main transacciones.csv
   ```
   El programa imprimirá los conteos y totales, además de listar las líneas inválidas.

## Ejecución de pruebas

Este proyecto incluye un pequeño **framework de pruebas** en `TestRunner.java` que valida:

- Detección de intentos de path traversal (rutas con `..`, absolutas o con caracteres ilegales)【439643924922804†L145-L168】.
- Conteo correcto de registros válidos e inválidos.
- Cálculo correcto de totales de ingresos y egresos.

Para ejecutar las pruebas:

```bash
javac -d bin src/main/java/com/csvimporter/*.java
java -cp bin com.csvimporter.TestRunner
```

Al finalizar, se imprimirá un resumen y se generará un archivo `test-report.txt` con los resultados.

## Calidad de código y Checkstyle

El archivo `checkstyle.xml` define reglas básicas de estilo que promueven un código consistente, como longitud máxima de línea, tamaño de métodos, nombres y sangría【439643924922804†L171-L188】. Puedes verificar tu código con la herramienta `checkstyle` si la tienes instalada:

```bash
checkstyle -c checkstyle.xml src/main/java/com/csvimporter/*.java
```

## CI/CD con GitHub Actions

Si deseas automatizar compilación, pruebas y análisis de estilo, puedes configurar **GitHub Actions**. Crea un archivo `.github/workflows/ci.yml` similar al siguiente:

```yaml
name: Java CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: 17
          distribution: "temurin"
      - name: Compile
        run: |
          mkdir -p bin
          javac -d bin src/main/java/com/csvimporter/*.java
      - name: Run tests
        run: java -cp bin com.csvimporter.TestRunner
      - name: Checkstyle
        run: checkstyle -c checkstyle.xml src/main/java/com/csvimporter/*.java || true
```

Esta configuración se ejecutará en cada push o pull request. El paso de Checkstyle se marca como `|| true` para que no falle el build; ajusta según tus necesidades.

## Licencia

Este proyecto se entrega para fines académicos y no incluye una licencia específica. Puedes modificarlo y adaptarlo libremente.