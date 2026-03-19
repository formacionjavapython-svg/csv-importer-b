# Importador CSV Seguro - Java Puro

## || Descripción del Proyecto ||
Este proyecto es una aplicación de consola desarrollada en Java puro (sin dependencias de terceros como Maven o Gradle) construida para el laboratorio de evaluación de Axity. Su objetivo es leer, validar y procesar transacciones financieras desde un archivo CSV, generando un reporte final de registros e ingresos/egresos.

## || Características Principales ||
* **Desarrollo Seguro:** Implementa protección contra ataques de *Path Traversal* utilizando variables de entorno (`IMPORT_BASE_DIR`) para restringir la lectura de archivos a una "allowlist" (directorio seguro).
* **Diseño Orientado a Objetos (POO):** Responsabilidades claramente definidas en clases como `Money`, `Transaction` e `Importer`, aplicando inmutabilidad (`final`) y *Null Safety*.
* **Pipeline Funcional:** Flujo de procesamiento dividido en tres etapas claras: *Parse*, *Validate* y *Accumulate*.
* **Calidad de Código:** Integración de `Checkstyle` para asegurar estándares de nombrado, longitud de métodos (máx. 30 líneas) y formato general.
* **Pruebas Nativas:** Framework de pruebas personalizado (`TestRunner.java`) sin depender de JUnit, validando la seguridad de rutas y la exactitud matemática.

## || Prerrequisitos ||
* Java 17 JDK o superior.
* Git.

## || Instrucciones de Ejecución ||

### 1. Configurar el entorno seguro
Antes de ejecutar el programa, es obligatorio definir el directorio base permitido donde residirán los archivos CSV:

**Windows (PowerShell):**
```powershell
$env:IMPORT_BASE_DIR="C:\ruta\absoluta\a\tu\proyecto\Imports"