package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import gal.sdc.usc.risk.util.Colores.Color;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class Jugador {
  private final String nombre;
  private final Color color;
  private final Ejercito ejercitosPendientes;
  private final List<Carta> cartas;
  private final List<Pais> paises;
  private Mision mision = null;

  private Jugador(String nombre, Color color) {
    this.nombre = nombre;
    this.color = color;
    this.ejercitosPendientes = new Ejercito.Builder(color).build();
    this.cartas = new ArrayList<>();
    this.paises = new ArrayList<>();
  }

  public String getNombre() {
    return nombre;
  }

  public Color getColor() {
    return color;
  }

  public Mision getMision() {
    return mision;
  }

  public List<Pais> getPaises() {
    return List.copyOf(paises);
  }

  public List<Continente> getContinentes() {
    LinkedHashSet<Continente> continentes = new LinkedHashSet<>();
    for (Pais pais : paises) {
      Continente continente = pais.getContinente();
      if (continente != null && this.equals(continente.getJugador())) {
        continentes.add(continente);
      }
    }
    return List.copyOf(continentes);
  }

  void addPais(Pais pais) {
    if (!paises.contains(pais)) {
      paises.add(pais);
    }
  }

  void removePais(Pais pais) {
    paises.remove(pais);
  }

  public Ejercito getEjercitosPendientes() {
    return this.ejercitosPendientes;
  }

  public List<Carta> getCartas() {
    return cartas;
  }

  public Carta getCarta(SubEquipamientos subEquipamiento, Pais pais) {
    for (Carta carta : this.getCartas()) {
      if (carta.getSubEquipamiento().equals(subEquipamiento) && carta.getPais().equals(pais)) {
        return carta;
      }
    }
    return null;
  }

  public Integer getNumEjercitos() {
    Integer i = 0;
    for (Pais pais : this.getPaises()) {
      i += pais.getEjercito().toInt();
    }
    return i;
  }

  public boolean setMision(Mision mision) {
    if (this.mision == null) {
      this.mision = mision;
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "Jugador{"
        + "nombre='"
        + nombre
        + '\''
        + ", color="
        + color
        + ", ejercitosPendientes="
        + ejercitosPendientes
        + ", cartas="
        + cartas
        + ", mision="
        + mision
        + '}';
  }

  public static class Builder {
    private final String nombre;
    private Color color;

    public Builder(String nombre) {
      this.nombre = nombre;
    }

    public Builder withColor(Color color) {
      this.color = color;
      return this;
    }

    public Jugador build() {
      Objects.requireNonNull(nombre, "El nombre del jugador es obligatorio");
      Objects.requireNonNull(color, "El color del jugador es obligatorio");
      if (nombre.isBlank()) {
        throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
      }
      return new Jugador(nombre, color);
    }
  }
}
