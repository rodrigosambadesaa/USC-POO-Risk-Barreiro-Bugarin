package gal.sdc.usc.risk.jugar;

import gal.sdc.usc.risk.application.CommandRegistry;
import gal.sdc.usc.risk.application.Game;
import gal.sdc.usc.risk.application.GameContext;
import gal.sdc.usc.risk.application.port.GameOutput;
import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.domain.Carta;
import gal.sdc.usc.risk.domain.Continente;
import gal.sdc.usc.risk.domain.Ejercito;
import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.domain.Mapa;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.random.DiceRoller;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import gal.sdc.usc.risk.util.Colores;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class Partida {
  private final Game game;

  protected Partida() {
    this(GameContext.current());
  }

  protected Partida(Game game) {
    this.game = java.util.Objects.requireNonNull(game, "game");
  }

  protected Mapa getMapa() {
    return game.mapa();
  }

  protected void setMapa(Mapa nuevoMapa) {
    if (game.mapa() == null) {
      game.mapa(nuevoMapa);
    }
  }

  protected GameOutput getConsola() {
    return game.output();
  }

  protected void nuevoJugador(Jugador jugador) {
    game.jugadores().put(jugador.getNombre(), jugador);
    game.turnos().add(jugador);
  }

  protected Jugador getJugadorTurno() {
    return game.turnos().peek();
  }

  protected HashMap<String, Jugador> getJugadores() {
    return new LinkedHashMap<>(game.jugadores());
  }

  protected Jugador getJugadorPorNombre(String nombre) {
    if (nombre == null) {
      return null;
    }

    return game.jugadores().values().stream()
        .filter(jugador -> jugador.getNombre().equalsIgnoreCase(nombre))
        .findAny()
        .orElse(null);
  }

  protected Jugador getJugadorPorColor(Colores.Color color) {
    return this.getJugadoresPorColor().get(color);
  }

  protected HashMap<Colores.Color, Jugador> getJugadoresPorColor() {
    HashMap<Colores.Color, Jugador> jugadores = new HashMap<>();
    for (Jugador jugador : game.jugadores().values()) {
      jugadores.put(jugador.getColor(), jugador);
    }
    return jugadores;
  }

  protected HashMap<Misiones, Jugador> getJugadoresPorMision() {
    HashMap<Misiones, Jugador> jugadores = new HashMap<>();
    for (Jugador jugador : game.jugadores().values()) {
      if (jugador.getMision() != null) {
        jugadores.put(jugador.getMision().getIdentificador(), jugador);
      }
    }
    return jugadores;
  }

  protected boolean isJugando() {
    return game.jugando();
  }

  protected boolean haAcabado() {
    return game.acabada();
  }

  protected void acabarPartida() {
    game.acabada(true);
    this.getComandos().acabarPartida();
  }

  protected boolean iniciar() {
    if (game.jugando()) {
      return false;
    }
    game.jugando(true);
    for (SubEquipamientos subEquipamiento : SubEquipamientos.values()) {
      for (Pais pais : this.getMapa().getPaisesPorCeldas().values()) {
        game.cartas()
            .add(new Carta.Builder().withSubEquipamiento(subEquipamiento).withPais(pais).build());
      }
    }

    this.getComandos().iniciarPartida(this.getJugadorTurno());
    this.comprobacionesTurno();
    return true;
  }

  protected ComandosDisponibles getComandos() {
    return game.comandos();
  }

  protected CommandRegistry getRegistry() {
    return game.registry();
  }

  protected DiceRoller getDice() {
    return game.dice();
  }

  protected void devolverCarta(Carta carta) {
    game.cartas().add(carta);
  }

  protected Carta getCarta(SubEquipamientos subEquipamiento, Pais pais) {
    for (Carta carta : new ArrayList<>(game.cartas())) {
      if (carta.getSubEquipamiento().equals(subEquipamiento) && carta.getPais().equals(pais)) {
        game.cartas().remove(carta);
        return carta;
      }
    }
    return null;
  }

  protected List<Carta> getCartasMonton() {
    return List.copyOf(game.cartas());
  }

  protected boolean isHaConquistadoPais() {
    return game.conquistado();
  }

  protected void conquistadoPais() {
    game.conquistado(true);
  }

  protected boolean moverTurno() {
    game.conquistado(false);
    int jugadores = game.turnos().size();
    for (int intento = 0; intento < jugadores; intento++) {
      Jugador jugadorAnterior = game.turnos().poll();
      if (jugadorAnterior == null) {
        return false;
      }
      game.turnos().add(jugadorAnterior);

      Jugador siguiente = game.turnos().peek();
      if (siguiente != null && !siguiente.getPaises().isEmpty()) {
        if (this.isJugando()) {
          this.comprobacionesTurno();
        }
        return true;
      }
    }
    return false;
  }

  protected int calcularEjercitosPendientes(Jugador jugador) {
    int e = 0;

    // El jugador recibe el número de ejércitos que es el resultado de dividir el número de países
    // que pertenecen al jugador entre 3. Por ejemplo, si un jugador tiene 14 países, al iniciar su
    // turno recibe 4 países (el resultado entero de 14/3= 4).
    e += jugador.getPaises().size() / 3;

    // Si todos los países de un continente pertenecen a dicho jugador, recibe el número de
    // ejércitos indicados en la Tabla 4.
    for (Continente continente : jugador.getContinentes()) {
      e += continente.getEjercitosRearme();
    }

    return e;
  }

  protected void comprobacionesTurno() {
    int refuerzos = this.calcularEjercitosPendientes(this.getJugadorTurno());
    if (refuerzos > 0) {
      this.getJugadorTurno()
          .getEjercitosPendientes()
          .recibir(new Ejercito.Builder().withCantidad(refuerzos).build());
    }

    // Un jugador no puede disponer de más de 6 cartas de equipamiento. En ese caso el cambio se
    // realiza automáticamente escogiendo la combinación con mayor número de ejércitos.
    if (this.getJugadorTurno().getCartas().size() > 6) {
      Ejecutor.comando("cambiar cartas todas auto");
    }
  }

  protected Integer getEjercitosIniciales() {
    return switch (this.getJugadores().size()) {
      case 3 -> 35;
      case 4 -> 30;
      case 5 -> 25;
      case 6 -> 20;
      default -> 0;
    };
  }
}
