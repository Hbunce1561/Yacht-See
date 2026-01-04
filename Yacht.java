import java.util.ArrayList;
import java.util.Scanner;

public class Yacht {
    protected ArrayList<Dice> dice;
    private Scanner sc;
    protected int remainingRolls;
    protected Score player;

    public Yacht() {
        this.dice = loadDice();
        this.remainingRolls = 2;
        sc = new Scanner(System.in);
        this.player = new Score();
    }

    public void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    public void play() {
        while (true) {
            if (this.remainingRolls > 0) {
                roll();
                printDice();
                keepDice();
            } else {
                clearConsole();
                roll();
                printDice();
                scoreStart();
                clearConsole();
                printScore();
                clearKeep();
                resetRollCount();
            }
            if (!this.player.score.containsValue(null)) {
                this.player.calculateBonuses();
                break;
            }
        }
    }

    public void printScoreTotal() {
        System.out.println("----Pre-Bonus Totals:----\n" + this.player.getTotal() + "\n----Bonuses----\n"
                + this.player.getBonuses() + "\n----Final score----\n" + this.player.getBonusTotal());

    }

    public int getTotalScore() {
        return this.player.getBonusTotal();
    }

    public ArrayList<Dice> loadDice() {
        ArrayList<Dice> d = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            d.add(new Dice());
        }
        return d;
    }

    public void roll() {

        for (int i = 0; i < 5; i++) {
            if (this.dice.get(i).getKeep() == false) {
                this.dice.get(i).rollFace();
            }
        }

    }

    public void clearKeep() {
        for (int i = 0; i < 5; i++) {
            this.dice.get(i).setKeep(false);
        }
    }

    public void resetRollCount() {
        this.remainingRolls = 2;
    }

    public void printDice() {

        String border = "            +---+---+---+---+---+";
        System.out.println(border);
        System.out.print("Die value:  |");
        for (Dice d : this.dice) {
            System.out.printf(" %d |", d.getFace());
        }
        System.out.println("\n" + border);

        System.out.print("Die number: |");
        for (int i = 0; i < this.dice.size(); i++) {
            System.out.printf(" %d |", i + 1);
        }
        System.out.println();
        System.out.println(border);
    }

    public void keepDice() {
        boolean inputCheck = false;
        while (!inputCheck) {
            System.out.println("Which dice would you like to keep? \n(E.g. 1, 2, 3; all; none)");
            inputCheck = rollInputClean(sc.nextLine());
        }

    }

    public void scoreStart() {
        String border = " ----------------------------";
        while (true) {
            System.out.println("\nWhich would you like to score this as?\n\n" + border + "\n");
            int i = 0;
            for (SCORE_CATEGORIES categories : SCORE_CATEGORIES.values()) {
                if (this.player.score.get(categories) == null) {
                    System.out.print(categories + "  |  ");
                    i++;
                    if (i % 3 == 0) {
                        System.out.println();
                    }
                }

            }
            System.out.println("\n" + border + "\n");
            String s = sc.nextLine();
            SCORE_CATEGORIES cat = convertToCategories(s);
            if (cat == null) {
                System.out.println("Invalid input");
            } else {
                if (this.player.score.get(cat) == null) {
                    this.player.addScore(cat, scoringMetric(cat));
                    break;
                } else {
                    System.out.println("This category has already been played, please choose another.");

                }
            }
        }
    }

    private SCORE_CATEGORIES convertToCategories(String s) {
        return constants.inputMap.get(s.toLowerCase());

    }

    public void printScore() {
        System.out.println("\nTotal: " + this.player.getTotal() + "\n");
    }

    public boolean rollInputClean(String s) {
        try {
            s = s.toLowerCase();
            s = s.replaceAll(" ", "");
            if (s.equals("all")) {
                for (int i = 0; i < 5; i++) {
                    this.dice.get(i).setKeep(true);
                }
                this.remainingRolls = 0;
                return true;
            } else if (s.equals("none")) {
                clearKeep();
                this.remainingRolls--;
                return true;
            }
            clearKeep();
            String arr[] = s.split(",");
            for (int i = 0; i < arr.length; i++) {
                this.dice.get(Integer.parseInt(arr[i]) - 1).setKeep(true);
            }
            this.remainingRolls--;
            return true;
        } catch (Exception E) {
            System.out.println("Invalid input: Please enter numbers 1-5 separated by commas, 'all', or 'none'.");
            return false;
        }
    }

    public int scoringMetric(SCORE_CATEGORIES categories) {
        int rollResults[] = new int[6];
        for (Dice d : this.dice) {
            switch (d.getFace()) {
                case 1:
                    rollResults[0]++;
                    break;
                case 2:
                    rollResults[1]++;
                    break;
                case 3:
                    rollResults[2]++;
                    break;
                case 4:
                    rollResults[3]++;
                    break;
                case 5:
                    rollResults[4]++;
                    break;
                case 6:
                    rollResults[5]++;
                    break;
            }
        }
        this.player.bonusYCheck(rollResults);
        switch (categories) {
            case ONES:
                return rollResults[0];
            case TWOS:
                return rollResults[1] * 2;
            case THREES:
                return rollResults[2] * 3;
            case FOURS:
                return rollResults[3] * 4;
            case FIVES:
                return rollResults[4] * 5;
            case SIXES:
                return rollResults[5] * 6;
            case THREE_OF_A_KIND:
                for (int i = 0; i < rollResults.length; i++) {
                    if (rollResults[i] >= 3) {
                        int toak = 0;
                        for (Dice d : this.dice) {
                            toak += d.getFace();
                        }
                        return toak;
                    }
                }
                return 0;
            case FOUR_OF_A_KIND:
                for (int i = 0; i < rollResults.length; i++) {
                    if (rollResults[i] >= 4) {
                        int foak = 0;
                        for (Dice d : this.dice) {
                            foak += d.getFace();
                        }
                        return foak;
                    }
                }
                return 0;
            case FULL_HOUSE:
                boolean pair = false;
                boolean trips = false;
                for (int i = 0; i < rollResults.length; i++) {
                    if (rollResults[i] == 2) {
                        pair = true;
                    }
                    if (rollResults[i] == 3) {
                        trips = true;
                    }
                }
                if (pair && trips) {
                    return 25;
                }
                return 0;
            case SMALL_STRAIGHT:
                for (int i = 0; i < 3; i++) {
                    if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                            && rollResults[i + 3] >= 1) {
                        return 30;
                    }
                }
                return 0;
            case LARGE_STRAIGHT:
                for (int i = 0; i < 2; i++) {
                    if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                            && rollResults[i + 3] >= 1 && rollResults[i + 4] >= 1) {
                        return 40;
                    }
                }
                return 0;
            case YAHTZEE:
                for (int count : rollResults) {
                    if (count == 5) {
                        return 50;
                    }
                }
                return 0;
            case CHANCE:
                int chanceScore = 0;
                for (Dice d : this.dice) {
                    chanceScore += d.getFace();
                }
                return chanceScore;
        }
        return 0;
    }

}
