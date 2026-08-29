package gal.sdc.usc.risk.jugar;

import gal.sdc.usc.risk.application.Game;
import gal.sdc.usc.risk.application.GameContext;
import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.infrastructure.resources.ResourceStore;
import gal.sdc.usc.risk.salida.Resultado;
import gal.sdc.usc.risk.util.Colores;
import gal.sdc.usc.risk.util.Colores.Color;
import java.io.BufferedReader;
import java.io.IOException;

public class Menu extends Partida {
  public static void jugar(Game game) {
    GameContext.run(game, Menu::new);
  }

  private Menu() {
    String orden;
    boolean hayFichero = false;
    boolean primero = true;

    try (BufferedReader bufferLector =
        ResourceStore.reader(System.getProperty("risk.scenario", "comandos.csv"))) {
      hayFichero = true;

      while ((orden = bufferLector.readLine()) != null) {
        orden = orden.strip();
        if (orden.isEmpty() || orden.startsWith("#") || orden.startsWith("//")) {
          continue;
        }

        if (!primero) {
          super.getConsola().imprimirSalto();
        } else {
          primero = false;
        }
        this.entrada();
        super.getConsola().imprimir(orden);
        super.getConsola().imprimirSalto();

        Ejecutor.comando(orden);
      }
    } catch (IOException e) {
      System.err.println("Archivo de comandos no encontrado, usando consola: " + e.getMessage());
    }

    if (!hayFichero) {
      while (true) {
        if (!primero) {
          super.getConsola().imprimirSalto();
        } else {
          primero = false;
        }
        this.entrada();
        orden = super.getConsola().leer();

        if (!orden.isEmpty()) {
          Ejecutor.comando(orden);
        }
      }
    }

    Resultado.Escritor.cerrar();
  }

  private void entrada() {
    String out = "";
    if (super.isJugando() || super.getComandos().isPaisesAsignados(super.getMapa())) {
      out +=
          "["
              + new Colores(super.getJugadorTurno().getNombre(), super.getJugadorTurno().getColor())
              + "] ";
    }
    out += new Colores("$> ", Color.AMARILLO);
    super.getConsola().imprimir(out);
  }
}
