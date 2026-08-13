package gal.sdc.usc.risk.comandos.preparacion;

import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Ejecutor;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.excepciones.Errores;
import gal.sdc.usc.risk.infrastructure.resources.ResourceStore;
import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.salida.Resultado;
import java.io.BufferedReader;
import java.io.IOException;

@Comando(estado = Estado.PREPARACION, comando = Comandos.ASIGNAR_MISIONES)
public class AsignarMisiones extends Partida implements IComando {
  @Override
  public void ejecutar(String[] comandos) {
    if (super.getMapa() == null) {
      Resultado.error(Errores.MAPA_NO_CREADO);
      return;
    }
    if (super.getJugadores().size() < 3 || super.getJugadores().size() > 6) {
      Resultado.error(Errores.JUGADORES_NO_CREADOS);
      return;
    }

    try {
      BufferedReader bufferLector;
      bufferLector = ResourceStore.reader(comandos[2]);
      String linea;

      String[] partes;
      while ((linea = bufferLector.readLine()) != null) {
        partes = linea.split(";");
        if (partes.length == 2) {
          Ejecutor.comando("asignar mision " + partes[0].trim() + " " + partes[1].trim(), false);
        }
      }
      bufferLector.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String ayuda() {
    return "asignar misiones <nombre_fichero>";
  }
}
