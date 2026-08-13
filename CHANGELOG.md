# Changelog

Todos los cambios relevantes se documentan en este archivo siguiendo una variante
de Keep a Changelog.

## [2.0.0] - 2026-08-13

### Añadido

- Construcción Maven reproducible sobre Java 21 y JavaFX 21.
- Capas `domain`, `application`, `adapters` e `infrastructure`.
- Suite JUnit 5 de caracterización, dominio, aplicación, concurrencia e integración.
- Regresión determinista con escenario y gold standard canónicos alineados.
- JaCoCo con umbrales, Checkstyle, Spotless, SpotBugs y advertencias como errores.
- Docker multi-stage, servicios Compose de CLI y pruebas y CI Linux/Windows.
- Servicio GUI web con JavaFX sobre Xvfb y acceso noVNC por el puerto 6080.

### Cambiado

- Estado estático de `Partida` reemplazado por instancias independientes de `Game`.
- Registro reflexivo sustituido por factorías de comandos tipadas.
- Azar encapsulado tras `DiceRoller` y semilla estable en el escenario canónico.
- Recursos movidos a classpath Maven y todas las lecturas/escrituras fijadas a UTF-8.
- Evaluación de victoria extraída a un caso de uso puro y comprobable.
- Builders e invariantes endurecidos con excepciones claras.

### Corregido

- Dependencia de JavaFX en la consola y fallo `No toolkit found` durante escenarios.
- Pools de un solo hilo creados y abandonados por cada comando.
- Contratos `equals`/`hashCode`, conversiones inseguras y APIs obsoletas.
- Propiedad de países, orden no determinista, locale, ANSI y finales de línea.
- Desalineación entre `comandos.csv`, `resultados.txt`, evaluador y gold standard.

### Eliminado

- JARs binarios versionados, incluido `jfxrt.jar`.
- FontAwesomeFX 8.9, incompatible en ejecución con JavaFX 21; los iconos usan
  glifos Unicode equivalentes.
- Scripts Ant/NetBeans/Replit incompatibles, reemplazados por Maven y Docker.
