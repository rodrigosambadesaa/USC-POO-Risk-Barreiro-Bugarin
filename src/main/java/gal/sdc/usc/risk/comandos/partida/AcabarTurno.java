package gal.sdc.usc.risk.comandos.partida;

import gal.sdc.usc.risk.application.VictoryEvaluator;
import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.domain.Jugador;
import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.salida.Resultado;
import gal.sdc.usc.risk.salida.SalidaObjeto;
import java.util.Map;

@Comando(estado = Estado.JUGANDO, comando = Comandos.ACABAR_TURNO)
public class AcabarTurno extends Partida implements IComando {
  @Override
  public void ejecutar(String[] comandos) {
    if (new VictoryEvaluator()
        .hasWon(super.getJugadorTurno(), super.getJugadores().values(), super.getMapa())) {
      super.acabarPartida();
      Resultado.victoria(super.getJugadorTurno());
      return;
    }

    boolean finPrimeraRonda = true;
    for (Map.Entry<String, Jugador> stringJugadorEntry : super.getJugadores().entrySet()) {
      if (stringJugadorEntry.getValue().getEjercitosPendientes().toInt() != 0) {
        finPrimeraRonda = false;
      }
    }
    if (finPrimeraRonda) {
      super.iniciar();
    }

    super.moverTurno();
    if (super.isJugando()) {
      super.getComandos().iniciarTurno(super.getJugadorTurno());
    } else {
      super.getComandos().habilitarRepartirEjercitos();
      super.getComandos().deshabilitarAcabarTurno();
    }

    SalidaObjeto salida = new SalidaObjeto();
    salida.put("nombre", super.getJugadorTurno().getNombre());
    salida.put("numeroEjercitosRearmar", super.getJugadorTurno().getEjercitosPendientes().toInt());
    Resultado.correcto(salida);
  }

  @Override
  public String ayuda() {
    return "acabar turno";
  }
}
