package gal.sdc.usc.risk;

import gal.sdc.usc.risk.evaluacion.Evaluation;
import gal.sdc.usc.risk.evaluacion.Result;
import gal.sdc.usc.risk.infrastructure.resources.ResourceStore;
import java.io.IOException;

public class Evaluacion {
  public static void main(String[] args) throws IOException {
    Result result = new Result(ResourceStore.outputFile("resultados.txt").getAbsolutePath());
    try (var reader = ResourceStore.reader("goldstandard.txt")) {
      Result goldStandard = Result.from(reader);
      Evaluation eval = new Evaluation(result, goldStandard);
      System.out.println(eval);
    }
  }
}
