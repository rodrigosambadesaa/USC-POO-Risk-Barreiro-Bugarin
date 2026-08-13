package gal.sdc.usc.risk.comandos.generico;

import gal.sdc.usc.risk.comandos.Comando;
import gal.sdc.usc.risk.comandos.Comandos;
import gal.sdc.usc.risk.comandos.Estado;
import gal.sdc.usc.risk.comandos.IComando;
import gal.sdc.usc.risk.jugar.Partida;
import gal.sdc.usc.risk.salida.SalidaObjeto;
import gal.sdc.usc.risk.util.Colores;

@Comando(estado = Estado.CUALQUIERA, comando = Comandos.AYUDA)
public class Ayuda extends Partida implements IComando {
  @Override
  public void ejecutar(String[] comandos) {
    SalidaObjeto salida = new SalidaObjeto();
    for (Class<? extends IComando> comando : super.getComandos().getLista()) {
      String ayuda = super.getRegistry().create(comando).ayuda();

      if (comando.isAnnotationPresent(Comando.class)) {
        Comando comandoA = comando.getAnnotation(Comando.class);
        salida.put(
            new Colores(
                    comando.getSimpleName(),
                    comandoA.estado().equals(Estado.CUALQUIERA)
                        ? Colores.Color.CELESTE
                        : Colores.Color.AZUL)
                .toString(),
            new Colores(ayuda, Colores.Color.VERDE));
      }
    }
    super.getConsola().imprimir(salida.toString());
    super.getConsola().imprimirSalto();
  }

  @Override
  public String ayuda() {
    return "ayuda";
  }
}
