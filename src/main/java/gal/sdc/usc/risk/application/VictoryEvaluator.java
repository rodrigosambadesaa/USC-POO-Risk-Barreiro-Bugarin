package gal.sdc.usc.risk.application;

import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.domain.Mapa;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.valores.Continentes;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.util.Colores;
import java.util.Collection;
import java.util.Objects;

/** Evalúa las condiciones de victoria sin depender de consola, JavaFX ni estado global. */
public final class VictoryEvaluator {
  public boolean hasWon(Jugador current, Collection<Jugador> players, Mapa map) {
    Objects.requireNonNull(current, "current");
    Objects.requireNonNull(players, "players");
    Objects.requireNonNull(map, "map");
    if (current.getMision() == null) {
      return false;
    }

    Misiones mission = current.getMision().getIdentificador();
    return switch (mission) {
      case M1 -> current.getPaises().size() >= 24;
      case M2 -> countriesWithAtLeastTwoArmies(current) >= 18;
      case M31 -> owns(current, map, Continentes.ASIA, Continentes.AMERICASUR);
      case M32 -> owns(current, map, Continentes.ASIA, Continentes.AFRICA);
      case M33 -> owns(current, map, Continentes.AMERICANORTE, Continentes.AFRICA);
      case M34 -> owns(current, map, Continentes.AMERICANORTE, Continentes.OCEANIA);
      case M41 -> destroyedOrFallback(current, players, Colores.Color.AMARILLO);
      case M42 -> destroyedOrFallback(current, players, Colores.Color.AZUL);
      case M43 -> destroyedOrFallback(current, players, Colores.Color.CELESTE);
      case M44 -> destroyedOrFallback(current, players, Colores.Color.ROJO);
      case M45 -> destroyedOrFallback(current, players, Colores.Color.VERDE);
      case M46 -> destroyedOrFallback(current, players, Colores.Color.VIOLETA);
    };
  }

  private static long countriesWithAtLeastTwoArmies(Jugador player) {
    return player.getPaises().stream()
        .map(Pais::getEjercito)
        .filter(army -> army.toInt() >= 2)
        .count();
  }

  private static boolean owns(Jugador player, Mapa map, Continentes first, Continentes second) {
    return player.getContinentes().contains(map.getContinentePorNombre(first.getNombre()))
        && player.getContinentes().contains(map.getContinentePorNombre(second.getNombre()));
  }

  private static boolean destroyedOrFallback(
      Jugador current, Collection<Jugador> players, Colores.Color targetColor) {
    Jugador target =
        players.stream()
            .filter(player -> player.getColor() == targetColor)
            .findFirst()
            .orElse(null);
    return target == null || target.equals(current)
        ? current.getPaises().size() >= 24
        : target.getPaises().isEmpty();
  }
}
