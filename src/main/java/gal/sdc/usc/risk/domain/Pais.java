package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.valores.Paises;
import gal.sdc.usc.risk.util.Colores;
import java.util.Objects;

public class Pais {
  public static final int MAX_LENGTH_NOMBRE = 10;

  private final Paises identificador;
  private final String nombre;
  private final String abreviatura;
  private final Celda celda;
  private final Ejercito ejercito;
  private Continente continente;
  private Fronteras fronteras = null;
  private Jugador jugador = null;
  private Integer numVecesConquistado;

  private Pais(Paises identificador, String nombre, String abreviatura, Celda celda) {
    this.identificador = identificador;
    this.nombre = nombre;
    this.abreviatura = abreviatura;
    this.celda = celda;
    this.ejercito = new Ejercito.Builder().build();
    this.numVecesConquistado = 0;
  }

  public boolean setContinente(Continente continente) {
    if (this.continente != null) {
      return false;
    }
    this.continente = continente;
    return true;
  }

  public String getNombre() {
    return nombre;
  }

  public String getAbreviatura() {
    return this.abreviatura;
  }

  public Continente getContinente() {
    return this.continente;
  }

  public Colores.Color getColor() {
    if (continente == null) {
      return null;
    }
    return this.continente.getColor();
  }

  public Celda getCelda() {
    return this.celda;
  }

  public boolean setFronteras(Fronteras fronteras) {
    if (this.fronteras == null) {
      this.fronteras = fronteras;
      return true;
    }
    return false;
  }

  public Fronteras getFronteras() {
    return fronteras;
  }

  public Jugador getJugador() {
    return jugador;
  }

  public void setJugador(Jugador jugador) {
    if (this.jugador == jugador) {
      return;
    }

    Jugador anterior = this.jugador;
    if (anterior != null) {
      anterior.removePais(this);
    }

    this.jugador = jugador;
    if (jugador != null) {
      jugador.addPais(this);
      if (anterior != null) {
        numVecesConquistado += 1;
      }
    }
  }

  public Ejercito getEjercito() {
    return ejercito;
  }

  public Integer getNumVecesConquistado() {
    return numVecesConquistado;
  }

  @Override
  public String toString() {
    return "Pais{"
        + "identificador="
        + identificador
        + ", nombre='"
        + nombre
        + '\''
        + ", abreviatura='"
        + abreviatura
        + '\''
        + ", celda="
        + celda
        + ", ejercito="
        + ejercito
        + ", continente="
        + continente
        + ", fronteras="
        + fronteras
        + ", jugador="
        + jugador
        + ", numVecesConquistado="
        + numVecesConquistado
        + '}';
  }

  public static class Builder {
    private final Paises pais;
    private String nombre;
    private String abreviatura;
    private Celda celda = null;

    public Builder(Paises pais) {
      this.pais = pais;
    }

    public Builder withNombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public Builder withAbreviatura(String nombre) {
      this.abreviatura = nombre;
      return this;
    }

    public Builder withCelda(Celda celda) {
      this.celda = celda;
      return this;
    }

    public Pais build() {
      Objects.requireNonNull(pais, "El identificador del país es obligatorio");
      Objects.requireNonNull(nombre, "El nombre del país es obligatorio");
      Objects.requireNonNull(abreviatura, "La abreviatura del país es obligatoria");
      Objects.requireNonNull(celda, "La celda del país es obligatoria");
      return new Pais(pais, nombre, abreviatura, celda);
    }
  }
}
