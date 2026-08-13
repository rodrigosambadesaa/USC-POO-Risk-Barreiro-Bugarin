package gal.sdc.usc.risk.domain.ejercito.compuesto;

import gal.sdc.usc.risk.domain.ejercito.EjercitoBase;
import gal.sdc.usc.risk.util.Colores;

public class EjercitoCyan extends EjercitoBase {
  public EjercitoCyan(int cantidad) {
    super(cantidad, Colores.Color.CELESTE);
  }

  public int[] ataque(int[] valores) {
    if (valores.length == 1) {
      valores[0] += 2;
      return valores;
    }
    return valores;
  }
}
