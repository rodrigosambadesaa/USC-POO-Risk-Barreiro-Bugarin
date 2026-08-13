package gal.sdc.usc.risk.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.comandos.EjecutorListener;
import gal.sdc.usc.risk.comandos.generico.Ayuda;
import gal.sdc.usc.risk.support.CapturingOutput;
import gal.sdc.usc.risk.util.Dado;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class GameAndCommandsTest {

  @Test
  void dosPartidasNoCompartenMapaJugadoresComandosNiTranscripcion() {
    Game first = game(1);
    Game second = game(2);

    GameContext.run(
        first,
        () -> {
          Ejecutor.comando("crear mapa");
          Ejecutor.comando("crear Ada ROJO");
        });
    GameContext.run(second, () -> Ejecutor.comando("crear mapa"));

    assertNotSame(first.mapa(), second.mapa());
    assertTrue(first.jugadores().containsKey("Ada"));
    assertTrue(second.jugadores().isEmpty());
    assertNotSame(first.comandos(), second.comandos());
    assertTrue(first.transcript().contains("crear Ada ROJO"));
    assertFalse(second.transcript().contains("Ada"));
  }

  @Test
  void contextoRestauraLaPartidaAnteriorYSeLimpiaAunqueFalle() {
    Game outer = game(1);
    Game inner = game(2);
    GameContext.run(
        outer,
        () -> {
          assertSame(outer, GameContext.current());
          assertThrows(
              IllegalStateException.class,
              () ->
                  GameContext.run(
                      inner,
                      () -> {
                        throw new IllegalStateException("boom");
                      }));
          assertSame(outer, GameContext.current());
        });
    assertThrows(NullPointerException.class, GameContext::current);
  }

  @Test
  void registroTipadoCreaInstanciasNuevasYRechazaTiposAusentes() {
    Game game = game(1);
    GameContext.run(
        game,
        () -> {
          assertTrue(game.registry().create(Ayuda.class) instanceof Ayuda);
          assertNotSame(game.registry().create(Ayuda.class), game.registry().create(Ayuda.class));
        });
    assertThrows(NullPointerException.class, () -> game.registry().create(null));
  }

  @Test
  void parserDistingueComandoIncorrectoYComandoNoPermitido() {
    Game game = game(1);
    GameContext.run(
        game,
        () -> {
          Ejecutor.comando("esto no es risk");
          Ejecutor.comando("crear Ada ROJO");
        });
    assertTrue(game.transcript().contains("código de error: 101"));
    assertTrue(game.transcript().contains("código de error: 99"));
  }

  @Test
  void listenerSeEjecutaSinCrearThreadsOcultos() {
    Game game = game(1);
    AtomicReference<Thread> callbackThread = new AtomicReference<>();
    Thread callingThread = Thread.currentThread();
    GameContext.run(
        game,
        () ->
            Ejecutor.comando(
                "crear mapa",
                new EjecutorListener() {
                  @Override
                  public void onComandoEjecutado() {
                    callbackThread.set(Thread.currentThread());
                  }
                }));
    assertSame(callingThread, callbackThread.get());
  }

  @Test
  void partidasConcurrentesMantienenContextosAislados() throws InterruptedException {
    Game first = game(1);
    Game second = game(2);
    CountDownLatch start = new CountDownLatch(1);
    Thread one = new Thread(() -> runAfter(start, first, "Uno"));
    Thread two = new Thread(() -> runAfter(start, second, "Dos"));
    one.start();
    two.start();
    start.countDown();
    one.join();
    two.join();

    assertTrue(first.transcript().contains("Uno"));
    assertFalse(first.transcript().contains("Dos"));
    assertTrue(second.transcript().contains("Dos"));
    assertFalse(second.transcript().contains("Uno"));
  }

  private static void runAfter(CountDownLatch start, Game game, String player) {
    try {
      start.await();
      GameContext.run(
          game,
          () -> {
            Ejecutor.comando("crear mapa");
            Ejecutor.comando("crear " + player + " ROJO");
          });
    } catch (InterruptedException error) {
      Thread.currentThread().interrupt();
      throw new AssertionError(error);
    }
  }

  private static Game game(long seed) {
    return new Game(new CapturingOutput(), new Dado(new Random(seed)));
  }
}
