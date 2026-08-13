package gal.sdc.usc.risk.support;

import gal.sdc.usc.risk.application.port.GameOutput;

public final class CapturingOutput implements GameOutput {
  private final StringBuilder value = new StringBuilder();

  @Override
  public synchronized void imprimir(Object object) {
    value.append(object);
  }

  @Override
  public synchronized void imprimirSalto() {
    value.append('\n');
  }

  @Override
  public String leer() {
    throw new IllegalStateException("La prueba no admite entrada interactiva");
  }

  public synchronized String content() {
    return value.toString();
  }
}
