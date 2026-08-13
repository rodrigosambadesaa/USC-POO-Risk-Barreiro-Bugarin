package gal.sdc.usc.risk.comandos.partida;

import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.domain.Continente;
import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.excepciones.Errores;
import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.salida.Resultado;
import gal.sdc.usc.risk.salida.SalidaDupla;
import gal.sdc.usc.risk.salida.SalidaObjeto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Comando(estado = Estado.JUGANDO, comando = Comandos.DESCRIBIR_CONTINENTE)
public class DescribirContinente extends Partida implements IComando {
  @Override
  public void ejecutar(String[] comandos) {
    Continente continente = super.getMapa().getContinentePorNombre(comandos[2]);

    if (continente == null) {
      Resultado.error(Errores.CONTINENTE_NO_EXISTE);
      return;
    }

    SalidaObjeto salida = new SalidaObjeto();
    salida.put("nombre", continente.getNombre());
    salida.put("abreviatura", continente.getAbreviatura());

    Map<Jugador, Integer> jugadoresEjercitos = new LinkedHashMap<>();
    for (Pais pais : continente.getPaises().values()) {
      jugadoresEjercitos.putIfAbsent(pais.getJugador(), 0);
      jugadoresEjercitos.put(
          pais.getJugador(),
          jugadoresEjercitos.get(pais.getJugador()) + pais.getEjercito().toInt());
    }
    List<SalidaDupla> jugadores = new ArrayList<>();
    for (Map.Entry<Jugador, Integer> jugador : jugadoresEjercitos.entrySet()) {
      jugadores.add(new SalidaDupla(jugador.getKey().getNombre(), jugador.getValue()));
    }
    salida.put("jugadores", jugadores);

    salida.put("numeroEjercitos", continente.getNumEjercitos());
    salida.put("rearmeContinente", continente.getEjercitosRearme());

    Resultado.correcto(salida);
  }

  @Override
  public String ayuda() {
    return "describir continente <abreviatura_continente>";
  }
}
