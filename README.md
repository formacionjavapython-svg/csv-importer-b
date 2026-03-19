# 🚀 CSV Importer Lab (Java Puro)

Aplicación de consola en Java que importa archivos CSV de transacciones financieras, valida datos, previene vulnerabilidades de seguridad y genera un resumen de resultados.

---

## 🧠 Descripción

Este proyecto implementa un importador CSV seguro utilizando:

* Programación Orientada a Objetos (POO)
* Validación de datos
* Pipeline funcional (parse → validate → accumulate)
* Prevención de ataques de **Path Traversal**
* Pruebas sin frameworks (TestRunner propio)
* CI/CD con GitHub Actions

---

## 🧱 Estructura del Proyecto

```
src/
 ├── dto/
 ├── service/
 ├── test/
 │    └── TestRunner.java
 ├── Money.java
 ├── Transaction.java
 └── TxType.java
```

---

## ⚙️ Requisitos

* Java 17 o superior
* Git

---

## ▶️ Ejecución local

### 1. Compilar

```
javac -d out $(find src -name "*.java")
```

### 2. Ejecutar

```
java -cp out test.TestRunner
```

---

## 📄 Formato del CSV

Ejemplo de archivo `data.csv`:

```
1,IN,100.50,MXN,Salario
2,OUT,50.00,MXN,Comida
3,IN,200.00,MXN,Bono
4,OUT,abc,MXN,Error
```

---

## 🔐 Seguridad

El sistema previene ataques de **Path Traversal**:

* Validación de rutas
* Uso de `Path.normalize()`
* Restricción a un directorio base (`IMPORT_BASE_DIR`)

---

## 🧪 Pruebas

Se implementa un `TestRunner` personalizado sin JUnit que valida:

* ✔️ Procesamiento correcto de datos
* ✔️ Manejo de archivos inexistentes
* ✔️ Protección contra path traversal

Ejemplo de salida:

```
Passed: 3
Failed: 0
```

---

## 🔄 CI/CD

Se configuró GitHub Actions para:

* Compilar el proyecto automáticamente
* Ejecutar pruebas en cada push
* Validar funcionamiento continuo

Archivo:

```
.github/workflows/build.yml
```

---

## 📊 Resultado esperado

```
Valid: 3
Invalid: 1
Total IN: 300.50
Total OUT: 50.00
```

---

## 💡 Conceptos aplicados

* Encapsulamiento
* Inmutabilidad (`final`)
* Uso de `BigDecimal` para dinero
* Manejo de excepciones
* Validación defensiva

---

## 👨‍💻 Autor

Proyecto desarrollado como laboratorio práctico de Java enfocado en buenas prácticas, seguridad y automatización.

---

## 🚀 Mejoras futuras

* Exportar reporte a archivo (`test-report.txt`)
* Integrar Checkstyle en CI
* Agregar CodeQL (análisis de seguridad)
* Convertir a API REST

---
