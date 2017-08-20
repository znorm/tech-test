package tech.test.tools;

import java.util.Random;

public class RandomGenerator {

    private Random random;

    public RandomGenerator(){
        random = new Random();
    }

    public double getBrandFactor(double min, double max) {
        return (random.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
    }

    public static double getRandom() {
        return Math.random() * 3;
    }
}
