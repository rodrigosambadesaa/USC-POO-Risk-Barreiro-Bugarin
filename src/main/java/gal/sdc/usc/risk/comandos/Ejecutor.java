package gal.sdc.usc.risk.comandos;

import gal.sdc.usc.risk.application.Game;
import gal.sdc.usc.risk.application.GameContext;
import gal.sdc.usc.risk.excepciones.Errores;
import gal.sdc.usc.risk.excepciones.ExcepcionRISK;
import gal.sdc.usc.risk.salida.Resultado;

/** Ejecutor síncrono y determinista de comandos de una sesión. */
public final class Ejecutor {
  private Ejecutor() {}

  public static void comando(String text, EjecutorListener listener) {
    ejecutar(text, true, listener);
  }

  public static void comando(String text, boolean imprimir) {
    ejecutar(text, imprimir, null);
  }

  public static void comando(String text) {
    ejecutar(text, true, null);
  }

  private static void ejecutar(String text, boolean imprimir, EjecutorListener listener) {
    Game game = GameContext.current();
    GameContext.run(
        game,
        () -> {
          if (imprimir) {
            Resultado.Escritor.comando(text);
          }
          try {
            IComando command = resolver(game, text);
            if (command == null) {
              return;
            }
            command.ejecutar(text.trim().split("\\s+"));
            if (listener != null) {
              listener.onComandoEjecutado();
            }
          } catch (ExcepcionRISK error) {
            Resultado.error(error);
            if (listener != null) {
              listener.onComandoError(error);
            }
          }
        });
  }

  private static IComando resolver(Game game, String text) {
    String normalized = text.strip().toLowerCase(java.util.Locale.ROOT);
    for (Class<? extends IComando> type : game.comandos().getLista()) {
      Comando metadata = type.getAnnotation(Comando.class);
      if (metadata != null && normalized.matches(metadata.comando().getRegex())) {
        return game.registry().create(type);
      }
    }

    boolean known =
        java.util.Arrays.stream(Comandos.values())
            .anyMatch(command -> normalized.matches(command.getRegex()));
    Resultado.error(known ? Errores.COMANDO_NO_PERMITIDO : Errores.COMANDO_INCORRECTO);
    return null;
  }
}
