package gal.sdc.usc.risk.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.domain.valores.Continentes;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.domain.valores.Paises;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import gal.sdc.usc.risk.excepciones.ExcepcionRISK;
import gal.sdc.usc.risk.util.Colores;
import gal.sdc.usc.risk.util.Dado;
import java.util.HashSet;
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
  }

  @Test
  void cartasConservanPaisTipoYValorDeRearme() {
    Pais alaska = country(Paises.ALASKA);
    Carta carta =
        new Carta.Builder().withPais(alaska).withSubEquipamiento(SubEquipamientos.FUSILERO).build();
    assertEquals(alaska, carta.getPais());
    assertEquals("Fusilero&Alaska", carta.getNombre());
    assertTrue(carta.obtenerRearme() > 0);
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

    Continente america =
        new Continente.Builder(Continentes.AMERICANORTE)
            .withNombre("América del Norte")
            .withAbreviatura("AméricaNorte")
            .withColor(Colores.Color.VIOLETA)
            .withEjercitosRearme(5)
            .withPais(alaska)
            .withPais(alberta)
            .build();
    alaska.setContinente(america);
    alberta.setContinente(america);
    Jugador ada = new Jugador.Builder("Ada").withColor(Colores.Color.ROJO).build();
    alaska.setJugador(ada);
    alberta.setJugador(ada);
    assertEquals(ada, america.getJugador());
    assertEquals(2, ada.getPaises().size());
    assertEquals(java.util.List.of(america), ada.getContinentes());

    Jugador linus = new Jugador.Builder("Linus").withColor(Colores.Color.AZUL).build();
    alaska.setJugador(linus);
    assertNull(america.getJugador());
    assertEquals(1, alaska.getNumVecesConquistado());
    assertFalse(ada.getPaises().contains(alaska));
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

  private static Pais country(Paises id) {
    return new Pais.Builder(id)
        .withNombre(id.getNombre())
        .withAbreviatura(id.getAbreviatura())
        .withCelda(new Celda.Builder().withX(id.getX()).withY(id.getY()).build())
        .build();
  }
}
