package practicaltest01.eim.systems.cs.pub.ro.licenta;

import java.util.Random;

public class RandomPicker {

    Random randGenerator;
    private int randInt;
    public boolean decision;
    private String difficulty;

    enum Difficulty {
        Easy,
        Medium,
        Hard,
        Nightmare
    }

    public RandomPicker(String difficulty) {
        this.difficulty = difficulty;
        decision = selectChoice();
    }

    public boolean isBetween(int number, int low, int high) {
        if (number >= low && number <= high)
            return true;
        else
            return false;
    }


    public boolean selectChoice() {
        if (difficulty.equals(Difficulty.Easy.toString())) {
            randGenerator = new Random();
            randInt = randGenerator.nextInt(11);

            if (isBetween(randInt, 0, 2)) {
                return true;
            } else {
                return false;
            }

        } else if (difficulty.equals(Difficulty.Medium.toString())) {
            randGenerator = new Random();
            randInt = randGenerator.nextInt(11);

            if (isBetween(randInt, 0, 4)) {
                return true;
            } else {
                return false;
            }

        } else if (difficulty.equals(Difficulty.Hard.toString())) {
            randGenerator = new Random();
            randInt = randGenerator.nextInt(11);

            if (isBetween(randInt, 0, 6)) {
                return true;
            } else {
                return false;
            }

        } else if (difficulty.equals(Difficulty.Nightmare.toString())) {
            randGenerator = new Random();
            randInt = randGenerator.nextInt(11);

            if (isBetween(randInt, 0, 8)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
