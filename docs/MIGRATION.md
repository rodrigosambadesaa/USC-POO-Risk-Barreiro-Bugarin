# Nota de migración desde el proyecto original

## Motivo

El proyecto original mezclaba reglas, presentación, recursos y estado global. La
CLI cargaba clases JavaFX, `Partida` almacenaba toda la partida en campos estáticos
y el script excluía la GUI aunque otras fuentes la importaban. La construcción
dependía de JARs Java 8 incluidos en el repositorio y el escenario no coincidía con
su gold standard.

La evidencia previa a cualquier refactor está congelada en `BASELINE.md`.

## Correspondencia estructural

| Antes | Ahora |
| --- | --- |
| `src/gal/...` | `src/main/java/gal/...` |
| `res/` | `src/main/resources/` |
| sin pruebas | `src/test/java` y `src/test/resources` |
| `tablero` | `domain` |
| estáticos en `Partida` | agregado por instancia `application.Game` |
| reflexión de comandos | `application.CommandRegistry` tipado |
| `Random` directo | puerto `domain.random.DiceRoller` |
| lectura de `res/` | `infrastructure.resources.ResourceStore` |
| `build.sh`/Ant/JARs | `pom.xml`, Maven y Docker |

Los paquetes `comandos`, `jugar`, `salida` y `gui` se conservan como fachadas de
compatibilidad para no cambiar órdenes, FXML, nombres públicos ni identidad visual.
El dominio ya no depende de ellos. CLI y JavaFX resuelven los mismos comandos sobre
una instancia `Game`.

## Correcciones de comportamiento

- La propiedad de un país actualiza de forma bidireccional las colecciones de los
  jugadores. Antes podían quedar referencias obsoletas después de una conquista;
  esto era un bug porque alteraba continentes, misiones y victoria.
- El escenario de consola ya no intenta inicializar JavaFX. El fallo anterior era
  técnico, no una regla del juego.
- El orden de mapas, jugadores y fronteras observables es estable. Se preservan los
  mismos datos; solo se elimina variación accidental entre JVM y sistemas.
- Los builders rechazan datos incompletos. Devolver `null` tras imprimir un mensaje
  aplazaba el fallo y causaba errores sin contexto.
- El canje, ataques, conquista, rearme, turnos, misiones y errores públicos quedan
  protegidos por pruebas. El gold standard nuevo representa la ejecución completa
  y exitosa de las 110 órdenes, no una ejecución truncada.

## Compatibilidad y operación

- Java mínimo: 21.
- Construcción: `mvn clean verify`.
- CLI: `java -jar target/risk-2.0.0-SNAPSHOT-all.jar`.
- GUI: `mvn javafx:run`.
- Recursos externos opcionales: `risk.resources.dir`, `risk.scenario` y
  `risk.output.dir`.

No existe migración de partidas guardadas porque el proyecto original no ofrecía
persistencia de partidas. Los CSV públicos y códigos de error se mantienen.
