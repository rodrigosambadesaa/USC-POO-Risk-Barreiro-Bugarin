package gal.sdc.usc.risk.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.comandos.partida.CambiarCartas;
import gal.sdc.usc.risk.domain.Carta;
import gal.sdc.usc.risk.domain.Celda;
import gal.sdc.usc.risk.domain.Continente;
import gal.sdc.usc.risk.domain.Ejercito;
import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.domain.Mapa;
import gal.sdc.usc.risk.domain.Mision;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.valores.Continentes;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.domain.valores.Paises;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import gal.sdc.usc.risk.util.Colores;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class VictoryAndCardsTest {
  private final VictoryEvaluator evaluator = new VictoryEvaluator();

  @Test
  void misionesTerritorialesCompruebanSusLimitesExactos() {
    Jugador occupation = player("Ocupación", Colores.Color.ROJO, Misiones.M1);
    List<Pais> countries = countries(24);
    countries.forEach(country -> country.setJugador(occupation));
    assertTrue(evaluator.hasWon(occupation, List.of(occupation), emptyMap()));

    Jugador reinforced = player("Rearme", Colores.Color.AZUL, Misiones.M2);
    List<Pais> reinforcedCountries = countries(18);
    reinforcedCountries.forEach(
        country -> {
          country.setJugador(reinforced);
          country.getEjercito().recibir(new Ejercito.Builder().withCantidad(2).build());
        });
    assertTrue(evaluator.hasWon(reinforced, List.of(reinforced), emptyMap()));
    new Ejercito.Builder().build().recibir(reinforcedCountries.get(0).getEjercito(), 1);
    assertFalse(evaluator.hasWon(reinforced, List.of(reinforced), emptyMap()));
  }

  @Test
  void misionContinentalYDestruccionUsanElEstadoDeDominio() {
    Jugador continental = player("Continentes", Colores.Color.ROJO, Misiones.M31);
    Pais asiaCountry = country(Paises.SIBERIA);
    Pais southAmericaCountry = country(Paises.VENEZUELA);
    Continente asia = continent(Continentes.ASIA, asiaCountry);
    Continente southAmerica = continent(Continentes.AMERICASUR, southAmericaCountry);
    asiaCountry.setContinente(asia);
    southAmericaCountry.setContinente(southAmerica);
    asiaCountry.setJugador(continental);
    southAmericaCountry.setJugador(continental);
    Mapa map =
        new Mapa.Builder()
            .withContinente(asia)
            .withContinente(southAmerica)
            .withPais(asiaCountry)
            .withPais(southAmericaCountry)
            .build(true);
    assertTrue(evaluator.hasWon(continental, List.of(continental), map));

    Jugador hunter = player("Cazador", Colores.Color.ROJO, Misiones.M42);
    Jugador target = player("Objetivo", Colores.Color.AZUL, Misiones.M1);
    assertTrue(evaluator.hasWon(hunter, List.of(hunter, target), emptyMap()));
    country(Paises.ALASKA).setJugador(target);
    assertFalse(evaluator.hasWon(hunter, List.of(hunter, target), emptyMap()));
  }

  @Test
  void cambioDeCartasCalculaCombinacionYBonificacionPorPropiedad() {
    Jugador player = player("Cartas", Colores.Color.VERDE, Misiones.M1);
    List<Pais> countries = countries(3);
    countries.forEach(country -> country.setJugador(player));
    Mapa.Builder mapBuilder = new Mapa.Builder();
    countries.forEach(mapBuilder::withPais);
    Mapa map = mapBuilder.build(true);
    List<SubEquipamientos> equipment =
        List.of(
            SubEquipamientos.GRANADERO, SubEquipamientos.DECABALLO, SubEquipamientos.DECAMPANHA);
    List<String> cardNames = new ArrayList<>();
    for (int index = 0; index < equipment.size(); index++) {
      Carta card =
          new Carta.Builder()
              .withPais(countries.get(index))
              .withSubEquipamiento(equipment.get(index))
              .build();
      player.getCartas().add(card);
      cardNames.add(card.getNombre());
    }

    assertEquals(19, CambiarCartas.calcularCambiosString(cardNames, player, map));
  }

  private static Jugador player(String name, Colores.Color color, Misiones mission) {
    Jugador player = new Jugador.Builder(name).withColor(color).build();
    player.setMision(new Mision.Builder(mission).build());
    return player;
  }

  private static List<Pais> countries(int amount) {
    Paises[] values = Paises.values();
    List<Pais> countries = new ArrayList<>();
    for (int index = 0; index < amount; index++) {
      countries.add(country(values[index]));
    }
    return countries;
  }

  private static Pais country(Paises id) {
    return new Pais.Builder(id)
        .withNombre(id.getNombre())
        .withAbreviatura(id.getAbreviatura())
        .withCelda(new Celda.Builder().withX(id.getX()).withY(id.getY()).build())
        .build();
  }

  private static Continente continent(Continentes id, Pais country) {
    return new Continente.Builder(id)
        .withNombre(id.getNombre())
        .withAbreviatura(id.getAbreviatura())
        .withColor(id.getColor())
        .withEjercitosRearme(id.getEjercitos())
        .withPais(country)
        .build();
  }

  private static Mapa emptyMap() {
    return new Mapa.Builder().build(true);
  }
}
