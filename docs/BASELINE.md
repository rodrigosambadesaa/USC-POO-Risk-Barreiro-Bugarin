# Línea base del proyecto original

Fecha: 13 de agosto de 2026. Entorno: Windows 11, Eclipse Temurin 21.0.8,
Maven 3.9.11 y Docker 29.6.2. El árbol de Git estaba limpio en `master`.

## Resultados observados antes del refactor

- `build.sh` no llegó a compilar: tenía finales CRLF interpretados por Bash y el
  JDK de Windows no estaba expuesto en ese entorno. Incluso corrigiendo el entorno,
  su conjunto de fuentes excluye `gui` mientras 13 clases conservan imports de GUI.
- La compilación directa del conjunto que pretendía usar `build.sh` falló con 36
  errores por esos acoplamientos y mostró 12 advertencias.
- La compilación completa con `javac -encoding UTF-8 -Xlint:all -cp "lib/*"`
  sí terminó, pero produjo 19 advertencias (APIs obsoletas, tipos raw/unchecked,
  clases serializables y contratos `equals`/`hashCode`).
- La compilación JavaFX dependía del `jfxrt.jar` de Java 8 versionado. JavaFX se
  cargaba también durante una ejecución puramente de consola.
- `res/comandos.csv` contenía 110 líneas. La ejecución heredada terminó con código
  0, pero JavaFX falló al asignar misiones (`No toolkit found`) y el flujo quedó en
  preparación. El archivo producido tuvo 771 líneas; `goldstandard.txt`, 557.
- `resultados.txt` ya estaba desalineado antes de ejecutar: 398 líneas frente a las
  557 del gold standard.
- Ant no estaba disponible. Esto no se considera un bloqueo: la migración usa Maven
  y dependencias declaradas.

Esta evidencia distingue los fallos históricos de las regresiones que pudiera
introducir la modernización. Los resultados válidos de modelos, transferencias y
parser quedaron cubiertos inicialmente por pruebas de caracterización.
