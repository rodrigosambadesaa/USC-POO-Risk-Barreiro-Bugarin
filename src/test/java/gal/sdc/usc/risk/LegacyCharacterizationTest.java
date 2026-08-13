package gal.sdc.usc.risk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.correccion.Parseador;
import gal.sdc.usc.risk.domain.Celda;
import gal.sdc.usc.risk.domain.Ejercito;
import gal.sdc.usc.risk.domain.Mision;
import gal.sdc.usc.risk.domain.valores.Misiones;
import gal.sdc.usc.risk.util.Colores;
import gal.sdc.usc.risk.util.Dado;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

/** Protege comportamiento válido observado antes de desacoplar la implementación original. */
class LegacyCharacterizationTest {

  @Test
  void celdasRepresentanCoordenadasYRespetanLimitesDelTablero() {
    Celda origen = new Celda.Builder().withX(0).withY(0).build();
    Celda mismaCoordenada = new Celda.Builder().withX(0).withY(0).build();

    assertEquals(origen, mismaCoordenada);
    assertEquals(origen.hashCode(), mismaCoordenada.hashCode());
    assertNull(origen.getNorte());
    assertNull(origen.getOeste());
    assertEquals(1, origen.getEste().getX());
    assertEquals(1, origen.getSur().getY());
  }

  @Test
  void transferenciaDeEjercitosConservaLaCantidadTotal() {
    Ejercito reserva = new Ejercito.Builder(Colores.Color.ROJO).withCantidad(7).build();
    Ejercito pais = new Ejercito.Builder(Colores.Color.ROJO).withCantidad(2).build();

    assertEquals(4, pais.recibir(reserva, 4));
    assertEquals(3, reserva.toInt());
    assertEquals(6, pais.toInt());
  }

  @Test
  void parserNormalizaTextoSinDependerDelLocale() {
    assertEquals("arbolnandu", Parseador.Texto.adaptar(" Árbol Ñandú "));
    assertSame(Boolean.TRUE, Parseador.textoAObjeto("TRUE"));
    assertEquals(-42, Parseador.textoAObjeto("-42"));
  }

  @Test
  void dadoSiempreProduceValoresRiskValidos() {
    Dado dado = new Dado(new java.util.Random(1234));
    for (int i = 0; i < 500; i++) {
      int value = dado.roll();
      assertTrue(value >= 1 && value <= 6);
    }
  }

  @Test
  void misionesConElMismoIdentificadorSonIguales() {
    Mision primera = new Mision.Builder(Misiones.M1).build();
    Mision segunda = new Mision.Builder(Misiones.M1).build();
    Mision distinta = new Mision.Builder(Misiones.M2).build();

    assertEquals(primera, segunda);
    assertNotEquals(primera, distinta);
    assertEquals(1, new HashSet<>(java.util.List.of(primera, segunda)).size());
  }
}
