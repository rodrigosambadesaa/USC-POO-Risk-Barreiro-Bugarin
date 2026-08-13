package gal.sdc.usc.risk.adapters.cli;

import gal.sdc.usc.risk.Risk;

/** Punto de entrada explícito del adaptador de consola. */
public final class CliApplication {
  private CliApplication() {}

  public static void main(String[] args) {
    Risk.main(args);
  }
}
