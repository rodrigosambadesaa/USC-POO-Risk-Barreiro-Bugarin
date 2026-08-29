package gal.sdc.usc.risk.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.domain.valores.Continentes;
import gal.sdc.usc.risk.domain.valores.Equipamientos;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.domain.valores.Paises;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import gal.sdc.usc.risk.excepciones.ExcepcionRISK;
import gal.sdc.usc.risk.util.Colores;
import gal.sdc.usc.risk.util.Dado;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class DomainModelTest {

  @Test
  void dadoPorDefectoSoloProduceValoresValidos() {
    Dado dado = new Dado();
    for (int index = 0; index < 100; index++) {
      int value = dado.roll();
      assertTrue(value >= 1 && value <= 6);
    }
  }

  @Test
  void buildersValidanInvariantesEnLugarDeDevolverNull() {
    assertThrows(NullPointerException.class, () -> new Celda.Builder().withX(1).build());
    assertThrows(
        IllegalArgumentException.class, () -> new Celda.Builder().withX(-1).withY(0).build());
    assertThrows(NullPointerException.class, () -> new Jugador.Builder("Ada").build());
    assertThrows(NullPointerException.class, () -> new Carta.Builder().build());
    assertThrows(NullPointerException.class, () -> new Mision.Builder(null).build());
    assertThrows(
        IllegalArgumentException.class, () -> new Ejercito.Builder().withCantidad(-1).build());
  }

  @Test
  void ejercitosAplicanPolimorfismoPorColorYTransferenciasSeguras() {
    Ejercito azul = new Ejercito.Builder(Colores.Color.AZUL).withCantidad(5).build();
    Ejercito rojo = new Ejercito.Builder(Colores.Color.ROJO).withCantidad(5).build();
    assertArrayEquals(new int[] {2, 4}, azul.ataque(new int[] {2, 3}));
    assertArrayEquals(new int[] {3, 3}, rojo.ataque(new int[] {2, 3}));

    Ejercito destino = new Ejercito.Builder().build();
    assertEquals(5, destino.recibir(azul, 99));
    assertEquals(0, azul.toInt());
    assertThrows(ExcepcionRISK.class, () -> destino.recibir(azul));
    assertThrows(IllegalArgumentException.class, () -> destino.recibir(destino, -1));
    assertThrows(NullPointerException.class, () -> destino.recibir(null));
  }

  @Test
  void cartasConservanPaisTipoYValorDeRearmeSinExponerLaColeccionDelJugador() {
    Pais alaska = country(Paises.ALASKA);
    Carta carta =
        new Carta.Builder().withPais(alaska).withSubEquipamiento(SubEquipamientos.FUSILERO).build();
    Jugador ada = new Jugador.Builder("Ada").withColor(Colores.Color.ROJO).build();

    ada.addCarta(carta);
    ada.addCarta(carta);

    assertEquals(alaska, carta.getPais());
    assertEquals("Fusilero&Alaska", carta.getNombre());
    assertTrue(carta.obtenerRearme() > 0);
    assertEquals(List.of(carta), ada.getCartas());
    assertThrows(UnsupportedOperationException.class, () -> ada.getCartas().clear());
    assertTrue(ada.removeCarta(carta));
    assertTrue(ada.getCartas().isEmpty());
  }

  @Test
  void paisesFronterasContinentesYPropiedadPermanecenCoherentes() {
    Pais alaska = country(Paises.ALASKA);
    Pais alberta = country(Paises.ALBERTA);
    Fronteras borders = new Fronteras.Builder().withSur(alberta).withMaritima(alberta).build();
    assertTrue(alaska.setFronteras(borders));
    assertFalse(alaska.setFronteras(new Fronteras.Builder().build()));
    assertEquals(2, borders.getTodas().size());
    assertEquals(0, new Fronteras.Builder().build().getTodas().size());
    assertThrows(UnsupportedOperationException.class, () -> borders.getMaritimas().clear());

    Continente america = northAmerica(alaska, alberta);
    alaska.setContinente(america);
    alberta.setContinente(america);
    Jugador ada = new Jugador.Builder("Ada").withColor(Colores.Color.ROJO).build();
    alaska.setJugador(ada);
    assertNull(america.getJugador());
    assertEquals(List.of(alaska), america.getPaisesPorJugador(ada));

    alberta.setJugador(ada);
    assertEquals(ada, america.getJugador());
    assertEquals(2, ada.getPaises().size());
    assertEquals(List.of(america), ada.getContinentes());

    alaska.setJugador(ada);
    assertEquals(0, alaska.getNumVecesConquistado());
    assertEquals(2, ada.getPaises().size());

    Jugador linus = new Jugador.Builder("Linus").withColor(Colores.Color.AZUL).build();
    alaska.setJugador(linus);
    assertNull(america.getJugador());
    assertEquals(1, alaska.getNumVecesConquistado());
    assertFalse(ada.getPaises().contains(alaska));

    alaska.setJugador(null);
    assertEquals(1, alaska.getNumVecesConquistado());
    assertFalse(linus.getPaises().contains(alaska));
  }

  @Test
  void continentesNoExponenSuMapaMutable() {
    Pais alaska = country(Paises.ALASKA);
    Continente america = northAmerica(alaska);

    america.getPaises().clear();

    assertEquals(1, america.getPaises().size());
  }

  @Test
  void mapaNoComparteColeccionesConBuilderNiGetters() {
    Pais alaska = country(Paises.ALASKA);
    Pais alberta = country(Paises.ALBERTA);
    Mapa.Builder builder = new Mapa.Builder().withPais(alaska);
    Mapa mapa = builder.build(true);

    builder.withPais(alberta);
    mapa.getPaisesPorCeldas().clear();

    assertEquals(1, mapa.getPaisesPorCeldas().size());
    assertEquals(alaska, mapa.getPaisPorNombre(" ALASKA ".strip()));
    assertTrue(mapa.getPaisesPorJugador(null).isEmpty());
    assertThrows(
        IllegalArgumentException.class,
        () -> new Mapa.Builder().withPais(alaska).withPais(country(Paises.ALASKA)));
  }

  @Test
  void conversoresDeDominioSonEstrictosNulosYNoDependenDelLocale() {
    assertEquals(Paises.ALASKA, Paises.toPaises(" alaska "));
    assertEquals(Paises.GROELANDIA, Paises.toPaises("GROENLAN"));
    assertNull(Paises.toPaises(null));

    assertEquals(Equipamientos.CABALLERIA, Equipamientos.toEquipamientos(" caballería "));
    assertNull(Equipamientos.toEquipamientos(null));
    assertEquals(SubEquipamientos.FUSILERO, SubEquipamientos.toSubEquipamientos(" FUSILERO "));
    assertNull(SubEquipamientos.toSubEquipamientos(null));

    assertEquals(Misiones.M31, Misiones.toMisiones(" m31 "));
    assertNull(Misiones.toMisiones("prefijo-M31"));
    assertNull(Misiones.toMisiones(null));
  }

  @Test
  void misionesCumplenContratoDeValor() {
    Mision one = new Mision.Builder(Misiones.M31).build();
    Mision same = new Mision.Builder(Misiones.M31).build();
    assertEquals(one, same);
    assertEquals(one.hashCode(), same.hashCode());
    assertEquals(1, new HashSet<>(java.util.List.of(one, same)).size());
    assertEquals("M31", one.getCodigo());
  }

  private static Continente northAmerica(Pais... paises) {
    Continente.Builder builder =
        new Continente.Builder(Continentes.AMERICANORTE)
            .withNombre("América del Norte")
            .withAbreviatura("AméricaNorte")
            .withColor(Colores.Color.VIOLETA)
            .withEjercitosRearme(5);
    for (Pais pais : paises) {
      builder.withPais(pais);
    }
    return builder.build();
  }

  private static Pais country(Paises id) {
    return new Pais.Builder(id)
        .withNombre(id.getNombre())
        .withAbreviatura(id.getAbreviatura())
        .withCelda(new Celda.Builder().withX(id.getX()).withY(id.getY()).build())
        .build();
  }
}
