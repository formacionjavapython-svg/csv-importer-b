# Proyecto de aprendizaje - Formación Java/Python SVG
Arturo Pérez Gómez
# CSV Importer B 📊

Importador de transacciones financieras desde archivos CSV con validaciones y seguridad contra path traversal.

## 📋 Descripción

Aplicación Java que procesa archivos CSV con formato específico, valida los datos y genera un resumen de transacciones (ingresos/egresos).

## ✨ Características

- ✅ Lectura de archivos CSV
- ✅ Validación de datos (formato, tipos, valores negativos)
- ✅ Cálculo automático de ingresos (IN) y egresos (OUT)
- ✅ Balance neto de transacciones
- ✅ Seguridad contra path traversal
- ✅ Detección y reporte de errores

## 📁 Formato CSV

El archivo debe tener el siguiente formato con 4 columnas:

```csv
Id,Tipo_Entrada,Moneda,Valor
1,IN,MXN,100
2,OUT,USD,200

