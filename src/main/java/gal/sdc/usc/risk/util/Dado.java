package gal.sdc.usc.risk.util;

import gal.sdc.usc.risk.domain.random.DiceRoller;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

public final class Dado implements DiceRoller {
  private final RandomGenerator random;

  public Dado(RandomGenerator random) {
    this.random = Objects.requireNonNull(random, "random");
  }

  public Dado() {
    this(new Random());
  }

  @Override
  public int roll() {
    return random.nextInt(6) + 1;
  }
}
