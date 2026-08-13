package gal.sdc.usc.risk.salida;

import gal.sdc.usc.risk.application.port.GameOutput;

public interface Consola extends GameOutput {
  void imprimir(Object mensaje);

  void imprimirSalto();

  String leer();
}
