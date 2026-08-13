package gal.sdc.usc.risk.application;

import gal.sdc.usc.risk.application.port.GameOutput;
import gal.sdc.usc.risk.domain.Carta;
import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.domain.Mapa;
import gal.sdc.usc.risk.domain.random.DiceRoller;
import gal.sdc.usc.risk.jugar.ComandosDisponibles;
import gal.sdc.usc.risk.util.Dado;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/** Estado y ciclo de vida de una partida independiente. */
public final class Game {
  private final ComandosDisponibles comandos;
  private final CommandRegistry registry;
  private final DiceRoller dice;
  private final GameOutput output;
  private final Queue<Jugador> turnos = new LinkedList<>();
  private final Map<String, Jugador> jugadores = new LinkedHashMap<>();
  private final List<Carta> cartas = new ArrayList<>();
  private Mapa mapa;
  private volatile boolean conquistado;
  private volatile boolean jugando;
  private volatile boolean acabada;
  private final StringBuilder transcript = new StringBuilder();

  public Game(GameOutput output) {
    this(output, new Dado());
  }

  public Game(GameOutput output, DiceRoller dice) {
    this.output = java.util.Objects.requireNonNull(output, "output");
    this.dice = java.util.Objects.requireNonNull(dice, "dice");
    this.comandos = new ComandosDisponibles();
    this.registry = new CommandRegistry();
  }

  public DiceRoller dice() {
    return dice;
  }

  public ComandosDisponibles comandos() {
    return comandos;
  }

  public CommandRegistry registry() {
    return registry;
  }

  public GameOutput output() {
    return output;
  }

  public Queue<Jugador> turnos() {
    return turnos;
  }

  public Map<String, Jugador> jugadores() {
    return jugadores;
  }

  public List<Carta> cartas() {
    return cartas;
  }

  public Mapa mapa() {
    return mapa;
  }

  public void mapa(Mapa mapa) {
    this.mapa = mapa;
  }

  public boolean conquistado() {
    return conquistado;
  }

  public void conquistado(boolean conquistado) {
    this.conquistado = conquistado;
  }

  public boolean jugando() {
    return jugando;
  }

  public void jugando(boolean jugando) {
    this.jugando = jugando;
  }

  public boolean acabada() {
    return acabada;
  }

  public void acabada(boolean acabada) {
    this.acabada = acabada;
  }

  public synchronized void recordCommand(String command) {
    transcript.append("$> ").append(command).append('\n');
  }

  public synchronized void recordResult(String result) {
    transcript.append(result).append('\n').append('\n');
  }

  public synchronized String transcript() {
    return transcript.toString();
  }
}
