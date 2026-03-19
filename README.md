Objetivo Principal
Construir una aplicación de consola en Java que lea archivos CSV desde un directorio permitido, valide y procese transacciones financieras,
y genere un resumen detallado de registros válidos e inválidos con totales de ingresos y egresos.

Criterios de Aceptación
• Implementar validación de rutas contra path traversal
• Aplicar diseño orientado a objetos con clases Money, Transaction e Importer
• Procesar CSV con pipeline funcional: parse → validate → accumulate
• Generar resumen: registros válidos/inválidos y totales IN/OUT
• Incluir pruebas sin JUnit y CI/CD con GitHub Actions
• Aplicar Checkstyle para calidad de código
