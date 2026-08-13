package gal.sdc.usc.risk.application;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Delimita la sesión durante una invocación de los comandos heredados. El ThreadLocal no contiene
 * estado global del juego: solo transporta la dependencia explícita a través de código antiguo
 * mientras los adaptadores se ejecutan.
 */
public final class GameContext {
  private static final ThreadLocal<Game> CURRENT = new ThreadLocal<>();

  private GameContext() {}

  public static Game current() {
    return Objects.requireNonNull(CURRENT.get(), "No hay una partida asociada al hilo actual");
  }

  public static void attach(Game game) {
    CURRENT.set(Objects.requireNonNull(game, "game"));
  }

  public static void detach() {
    CURRENT.remove();
  }

  public static void run(Game game, Runnable action) {
    call(
        game,
        () -> {
          action.run();
          return null;
        });
  }

  public static <T> T call(Game game, Supplier<T> action) {
    Objects.requireNonNull(game, "game");
    Objects.requireNonNull(action, "action");
    Game previous = CURRENT.get();
    CURRENT.set(game);
    try {
      return action.get();
    } finally {
      if (previous == null) {
        CURRENT.remove();
      } else {
        CURRENT.set(previous);
      }
    }
  }
}
