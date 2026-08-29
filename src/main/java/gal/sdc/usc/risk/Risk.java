package gal.sdc.usc.risk;

import gal.sdc.usc.risk.application.Game;
import gal.sdc.usc.risk.jugar.Menu;
import gal.sdc.usc.risk.salida.ConsolaNormal;
import gal.sdc.usc.risk.util.Dado;
import java.util.Random;

public final class Risk {
  private Risk() {}

  public static void main(String[] args) {
    Long seed = Long.getLong("risk.seed");
    Dado dado = seed == null ? new Dado() : new Dado(new Random(seed));
    Menu.jugar(new Game(new ConsolaNormal(), dado));
  }
}
