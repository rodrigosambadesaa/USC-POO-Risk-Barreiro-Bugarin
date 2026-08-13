package gal.sdc.usc.risk.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gal.sdc.usc.risk.application.Game;
import gal.sdc.usc.risk.jugar.Menu;
import gal.sdc.usc.risk.support.CapturingOutput;
import gal.sdc.usc.risk.util.Dado;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConsoleScenarioRegressionTest {

  @Test
  void escenarioCompletoCoincideConGoldStandardCanonico(@TempDir Path output) throws Exception {
    String previous = System.getProperty("risk.output.dir");
    System.setProperty("risk.output.dir", output.toString());
    try {
      Game game = new Game(new CapturingOutput(), new Dado(new Random(20260813L)));
      Menu.jugar(game);

      String expected;
      try (var stream = getClass().getResourceAsStream("/goldstandard.txt")) {
        assertNotNull(stream);
        expected = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
      }
      assertEquals(normalize(expected), normalize(game.transcript() + "EOF\n"));
      assertEquals(
          normalize(expected), normalize(Files.readString(output.resolve("resultados.txt"))));
      assertTrue(game.transcript().contains("numeroEjercitosFinalesDestino"));
      assertTrue(game.transcript().contains("código de error: 125"));
      assertEquals(3, game.jugadores().size());
      assertEquals(
          42,
          game.mapa().getPaisesPorCeldas().values().stream()
              .filter(country -> country.getJugador() != null)
              .count());
      assertTrue(game.jugando());
      assertEquals("Hooker", game.turnos().peek().getNombre());
    } finally {
      restore("risk.output.dir", previous);
    }
  }

  @Test
  void recursosJavaFxEstanEmpaquetadosYFXMLBienFormado() throws Exception {
    var resource = getClass().getResource("/gal/sdc/usc/risk/gui/principal.fxml");
    assertNotNull(resource);
    var factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    try (var stream = resource.openStream()) {
      assertEquals(
          "StackPane",
          factory.newDocumentBuilder().parse(stream).getDocumentElement().getTagName());
    }
  }

  private static String normalize(String value) {
    return value.replace("\r\n", "\n");
  }

  private static void restore(String key, String value) {
    if (value == null) System.clearProperty(key);
    else System.setProperty(key, value);
  }
}
