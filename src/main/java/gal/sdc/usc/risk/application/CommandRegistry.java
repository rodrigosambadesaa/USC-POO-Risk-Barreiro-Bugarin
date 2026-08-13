package gal.sdc.usc.risk.application;

import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.comandos.generico.Ayuda;
import gal.sdc.usc.risk.comandos.generico.ObtenerColor;
import gal.sdc.usc.risk.comandos.generico.ObtenerContinente;
import gal.sdc.usc.risk.comandos.generico.ObtenerFrontera;
import gal.sdc.usc.risk.comandos.generico.ObtenerPaises;
import gal.sdc.usc.risk.comandos.generico.Salir;
import gal.sdc.usc.risk.comandos.generico.VerMapa;
import gal.sdc.usc.risk.comandos.partida.AcabarTurno;
import gal.sdc.usc.risk.comandos.partida.AsignarCarta;
import gal.sdc.usc.risk.comandos.partida.AtacarPais;
import gal.sdc.usc.risk.comandos.partida.AtacarPaisDados;
import gal.sdc.usc.risk.comandos.partida.CambiarCartas;
import gal.sdc.usc.risk.comandos.partida.CambiarCartasTodas;
import gal.sdc.usc.risk.comandos.partida.DescribirContinente;
import gal.sdc.usc.risk.comandos.partida.DescribirJugador;
import gal.sdc.usc.risk.comandos.partida.DescribirPais;
import gal.sdc.usc.risk.comandos.partida.Jugador;
import gal.sdc.usc.risk.comandos.partida.Rearmar;
import gal.sdc.usc.risk.comandos.preparacion.AsignarMision;
import gal.sdc.usc.risk.comandos.preparacion.AsignarMisiones;
import gal.sdc.usc.risk.comandos.preparacion.AsignarPais;
import gal.sdc.usc.risk.comandos.preparacion.AsignarPaises;
import gal.sdc.usc.risk.comandos.preparacion.CrearJugador;
import gal.sdc.usc.risk.comandos.preparacion.CrearJugadores;
import gal.sdc.usc.risk.comandos.preparacion.CrearMapa;
import gal.sdc.usc.risk.comandos.preparacion.RepartirEjercito;
import gal.sdc.usc.risk.comandos.preparacion.RepartirEjercitos;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/** Registro exhaustivo y tipado; añadir un comando exige declarar su factoría aquí. */
public final class CommandRegistry {
  private final Map<Class<? extends IComando>, Supplier<? extends IComando>> factories =
      new LinkedHashMap<>();

  public CommandRegistry() {
    register(Ayuda.class, Ayuda::new);
    register(Salir.class, Salir::new);
    register(VerMapa.class, VerMapa::new);
    register(ObtenerColor.class, ObtenerColor::new);
    register(ObtenerContinente.class, ObtenerContinente::new);
    register(ObtenerFrontera.class, ObtenerFrontera::new);
    register(ObtenerPaises.class, ObtenerPaises::new);
    register(AcabarTurno.class, AcabarTurno::new);
    register(AsignarCarta.class, AsignarCarta::new);
    register(AtacarPais.class, AtacarPais::new);
    register(AtacarPaisDados.class, AtacarPaisDados::new);
    register(CambiarCartas.class, CambiarCartas::new);
    register(CambiarCartasTodas.class, CambiarCartasTodas::new);
    register(DescribirContinente.class, DescribirContinente::new);
    register(DescribirJugador.class, DescribirJugador::new);
    register(DescribirPais.class, DescribirPais::new);
    register(Jugador.class, Jugador::new);
    register(Rearmar.class, Rearmar::new);
    register(AsignarMision.class, AsignarMision::new);
    register(AsignarMisiones.class, AsignarMisiones::new);
    register(AsignarPais.class, AsignarPais::new);
    register(AsignarPaises.class, AsignarPaises::new);
    register(CrearJugador.class, CrearJugador::new);
    register(CrearJugadores.class, CrearJugadores::new);
    register(CrearMapa.class, CrearMapa::new);
    register(RepartirEjercito.class, RepartirEjercito::new);
    register(RepartirEjercitos.class, RepartirEjercitos::new);
  }

  private <T extends IComando> void register(Class<T> type, Supplier<T> factory) {
    if (factories.put(type, factory) != null) {
      throw new IllegalStateException("Comando duplicado: " + type.getName());
    }
  }

  public IComando create(Class<? extends IComando> type) {
    Supplier<? extends IComando> factory = factories.get(Objects.requireNonNull(type, "type"));
    if (factory == null) {
      throw new IllegalArgumentException("Comando no registrado: " + type.getName());
    }
    return factory.get();
  }
}
