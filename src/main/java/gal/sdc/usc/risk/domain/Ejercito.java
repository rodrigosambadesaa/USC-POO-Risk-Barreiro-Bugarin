package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.ejercito.EjercitoNuevo;
import gal.sdc.usc.risk.domain.ejercito.base.EjercitoAmarillo;
import gal.sdc.usc.risk.domain.ejercito.base.EjercitoAzul;
import gal.sdc.usc.risk.domain.ejercito.base.EjercitoRojo;
import gal.sdc.usc.risk.domain.ejercito.compuesto.EjercitoCyan;
import gal.sdc.usc.risk.domain.ejercito.compuesto.EjercitoVerde;
import gal.sdc.usc.risk.domain.ejercito.compuesto.EjercitoVioleta;
import gal.sdc.usc.risk.excepciones.Errores;
import gal.sdc.usc.risk.util.Colores;
import java.util.Objects;

public abstract class Ejercito implements Comparable<Ejercito> {
  private Integer cantidad;
  private final Colores.Color color;

  protected Ejercito() {
    this(0, null);
  }

  protected Ejercito(int cantidad) {
    this(cantidad, null);
  }

  protected Ejercito(Colores.Color color) {
    this(0, color);
  }

  protected Ejercito(int cantidad, Colores.Color color) {
    if (cantidad < 0) {
      throw new IllegalArgumentException("La cantidad de ejércitos no puede ser negativa");
    }
    this.cantidad = cantidad;
    this.color = color;
  }

  public Integer recibir(Ejercito ejercito) {
    Ejercito origen = Objects.requireNonNull(ejercito, "El ejército de origen es obligatorio");
    return this.recibir(origen, origen.cantidad);
  }

  public Integer recibir(Ejercito ejercito, int cantidad) {
    return this.recibir(ejercito, cantidad, false);
  }

  public Integer recibir(Ejercito ejercito, int cantidad, boolean auto) {
    Ejercito origen = Objects.requireNonNull(ejercito, "El ejército de origen es obligatorio");
    if (cantidad < 0) {
      throw new IllegalArgumentException("La cantidad de ejércitos no puede ser negativa");
    }
    if (cantidad > origen.cantidad) {
      cantidad = origen.cantidad;
    }
    if (cantidad == 0) {
      if (!auto) {
        throw Objects.requireNonNull(Errores.EJERCITO_NO_DISPONIBLE.getExcepcion());
      }
      return 0;
    }

    this.add(cantidad);
    origen.del(cantidad);
    return cantidad;
  }

  private void add(int ejercitos) {
    this.cantidad += ejercitos;
  }

  private void del(int ejercitos) {
    if (ejercitos > this.cantidad) {
      ejercitos = this.cantidad;
    }
    this.cantidad -= ejercitos;
  }

  public Integer toInt() {
    return this.cantidad;
  }

  public abstract int[] ataque(int[] valores);

  public static class Builder {
    private final Colores.Color color;
    private int cantidad = 0;

    public Builder() {
      this(null);
    }

    public Builder(Colores.Color color) {
      this.color = color;
    }

    public Builder withCantidad(int cantidad) {
      this.cantidad = cantidad;
      return this;
    }

    public Ejercito build() {
      if (color == null) return new EjercitoNuevo(cantidad);

      switch (this.color) {
        case AZUL:
          return new EjercitoAzul(cantidad);
        case ROJO:
          return new EjercitoRojo(cantidad);
        case AMARILLO:
          return new EjercitoAmarillo(cantidad);
        case VIOLETA:
          return new EjercitoVioleta(cantidad);
        case CELESTE:
          return new EjercitoCyan(cantidad);
        case VERDE:
          return new EjercitoVerde(cantidad);
      }

      throw new IllegalArgumentException("Color de ejército no soportado: " + color);
    }
  }

  @Override
  public String toString() {
    return "Ejercito{" + "cantidad=" + cantidad + '}';
  }

  @Override
  public int compareTo(Ejercito o) {
    return cantidad.compareTo(o.cantidad);
  }
}
