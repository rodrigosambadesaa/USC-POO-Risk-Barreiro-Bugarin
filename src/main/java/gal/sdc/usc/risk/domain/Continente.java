package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.valores.Continentes;
import gal.sdc.usc.risk.util.Colores.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Continente {
  private final Continentes identificador;
  private final String nombre;
  private final String abreviatura;
  private final Color color;
  private final Integer ejercitosRearme;
  private final HashMap<String, Pais> paises;

  private Continente(
      Continentes continente,
      String nombre,
      String abreviatura,
      Color color,
      Integer ejercitosRearme,
      HashMap<String, Pais> paises) {
    this.identificador = continente;
    this.nombre = nombre;
    this.abreviatura = abreviatura;
    this.color = color;
    this.ejercitosRearme = ejercitosRearme;
    this.paises = paises;
  }

  public Continentes getContinente() {
    return this.identificador;
  }

  public String getNombre() {
    return this.nombre;
  }

  public String getAbreviatura() {
    return this.abreviatura;
  }

  public Color getColor() {
    return this.color;
  }

  public Integer getEjercitosRearme() {
    return this.ejercitosRearme;
  }

  public HashMap<String, Pais> getPaises() {
    return new LinkedHashMap<>(this.paises);
  }

  public List<Pais> getPaisesPorJugador(Jugador jugador) {
    if (jugador == null) {
      return List.of();
    }
    return this.paises.values().stream()
        .filter(pais -> jugador.equals(pais.getJugador()))
        .toList();
  }

  public List<Pais> getPaisesFrontera() {
    return this.paises.values().stream()
        .filter(pais -> pais.getFronteras() != null)
        .filter(
            pais ->
                pais.getFronteras().getTodas().stream()
                    .anyMatch(
                        pais1 ->
                            pais1.getContinente() != null && pais1.getContinente() != this))
        .toList();
  }

  public Integer getNumEjercitos() {
    Integer i = 0;
    for (Pais pais : this.paises.values()) {
      i += pais.getEjercito().toInt();
    }
    return i;
  }

  public Jugador getJugador() {
    Jugador propietario = null;
    for (Pais pais : this.paises.values()) {
      Jugador actual = pais.getJugador();
      if (actual == null) {
        return null;
      }
      if (propietario == null) {
        propietario = actual;
      } else if (propietario != actual) {
        return null;
      }
    }
    return propietario;
  }

  @Override
  public String toString() {
    return "Continente{"
        + "identificador="
        + identificador
        + ", nombre='"
        + nombre
        + '\''
        + ", abreviatura='"
        + abreviatura
        + '\''
        + ", color="
        + color
        + ", ejercitosRearme="
        + ejercitosRearme
        + ", paises="
        + paises
        + '}';
  }

  public static class Builder {
    private final Continentes continente;
    private final HashMap<String, Pais> paises;
    private String nombre;
    private String abreviatura;
    private Color color;
    private Integer ejercitosRearme;

    public Builder(Continentes continente) {
      this.continente = continente;
      this.paises = new LinkedHashMap<>();
    }

    public Builder withNombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public Builder withAbreviatura(String abreviatura) {
      this.abreviatura = abreviatura;
      return this;
    }

    public Builder withColor(Color color) {
      this.color = color;
      return this;
    }

    public Builder withEjercitosRearme(Integer ejercitos) {
      this.ejercitosRearme = ejercitos;
      return this;
    }

    public Builder withPais(Pais pais) {
      Pais paisValido = Objects.requireNonNull(pais, "El país del continente es obligatorio");
      this.paises.put(paisValido.getAbreviatura(), paisValido);
      return this;
    }

    public int totalPaises() {
      return this.paises.size();
    }

    public Continente build() {
      Objects.requireNonNull(continente, "El identificador del continente es obligatorio");
      Objects.requireNonNull(nombre, "El nombre del continente es obligatorio");
      Objects.requireNonNull(abreviatura, "La abreviatura del continente es obligatoria");
      Objects.requireNonNull(color, "El color del continente es obligatorio");
      Objects.requireNonNull(ejercitosRearme, "El rearme del continente es obligatorio");
      if (nombre.isBlank()) {
        throw new IllegalArgumentException("El nombre del continente no puede estar vacío");
      }
      if (abreviatura.isBlank()) {
        throw new IllegalArgumentException("La abreviatura del continente no puede estar vacía");
      }
      if (ejercitosRearme < 0) {
        throw new IllegalArgumentException("El rearme del continente no puede ser negativo");
      }
      if (paises.isEmpty()) {
        throw new IllegalStateException("El continente debe contener al menos un país");
      }
      return new Continente(
          continente, nombre, abreviatura, color, ejercitosRearme, new LinkedHashMap<>(paises));
    }
  }
}
