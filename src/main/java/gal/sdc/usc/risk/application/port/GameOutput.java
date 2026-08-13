package gal.sdc.usc.risk.application.port;

/** Puerto de salida compartido por CLI, pruebas y JavaFX. */
public interface GameOutput {
  void imprimir(Object value);

  void imprimirSalto();

  String leer();
}
