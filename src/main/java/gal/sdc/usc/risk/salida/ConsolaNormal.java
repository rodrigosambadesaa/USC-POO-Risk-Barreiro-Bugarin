package gal.sdc.usc.risk.salida;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class ConsolaNormal implements Consola {
  private final Scanner scanner =
      new Scanner(System.in, StandardCharsets.UTF_8).useLocale(Locale.forLanguageTag("es-ES"));
  private final PrintStream ps = new PrintStream(System.out, true, StandardCharsets.UTF_8);

  @Override
  public void imprimir(Object o) {
    ps.print(o.toString());
  }

  @Override
  public void imprimirSalto() {
    this.imprimir("\n");
  }

  @Override
  public String leer() {
    return scanner.nextLine();
  }
}
