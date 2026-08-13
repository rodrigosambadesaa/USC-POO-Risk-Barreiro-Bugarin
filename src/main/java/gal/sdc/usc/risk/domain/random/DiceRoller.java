package gal.sdc.usc.risk.domain.random;

/** Fuente de azar inyectable para tiradas de dados de seis caras. */
@FunctionalInterface
public interface DiceRoller {
  int roll();
}
