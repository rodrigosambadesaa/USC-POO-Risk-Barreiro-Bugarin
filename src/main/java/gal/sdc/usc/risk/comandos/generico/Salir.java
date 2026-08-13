package gal.sdc.usc.risk.comandos.generico;

import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.jugar.Partida;

@Comando(estado = Estado.CUALQUIERA, comando = Comandos.SALIR)
public class Salir extends Partida implements IComando {
  @Override
  public void ejecutar(String[] comandos) {
    super.acabarPartida();
  }

  @Override
  public String ayuda() {
    return "salir";
  }
}
