import java.util.*;

public class main {
    public static void main(String args[]) {
        yacht y = new yacht();
        y.play();
    }
}

class constants {
     public static final String[] SCORE_MULTIS = {
            "Ones",
            "Twos",
            "Threes",
            "Fours",
            "Fives",
            "Sixes"};
    public static final String[] SCORE_CATEGORIES = {
            "Ones",
            "Twos",
            "Threes",
            "Fours",
            "Fives",
            "Sixes",
            "Three of a Kind",
            "Four of a Kind",
            "Full House",
            "Sm. Straight",
            "Lg. Straight",
            "Yahtzee",
            "Chance"
    };
}

class Score {
    public HashMap<String, Integer> score;
    private int totalScore;
    private int bonusY;
    private int bonusPoints;

    public Score() {
        this.score = new HashMap<>();
        this.bonusY = 0;
        this.bonusPoints = 0;
        loadScore();
    }

    public void addScore(String s, int i) {
        this.score.put(s, i);
        this.totalScore += i;
    }

    private void loadScore() {
        for (String category : constants.SCORE_CATEGORIES) {
            this.score.put(category, null);
        }
    }

    public void bonusYaht() {
        this.bonusY++;
    }

    public int getTotal() {
        return this.totalScore;
    }
    public int getBonusTotal(){
        return this.bonusPoints+this.totalScore;
    }
    public int getBonuses() {
        return this.bonusPoints;
    }

    public void calculateBonuses() {
        if (this.score.get("Ones") + this.score.get("Twos") + this.score.get("Threes") + this.score.get("Fours")
                + this.score.get("Fives") + this.score.get("Sixes") >= 63) {
            this.bonusPoints += 35;
        }
        this.bonusPoints += this.bonusY * 100;
    }
}

class Dice {
    private int face;
    private Random rand = new Random();
    private boolean keep;

    public Dice() {
        this.face = 0;
        this.setKeep(false);
    }

    public int getFace() {
        return this.face;
    }

    public boolean getKeep() {
        return this.keep;
    }

    public void rollFace() {
        this.face = rand.nextInt(6) + 1;
    }

    public void setKeep(boolean b) {
        this.keep = b;
    }
}

class yacht {
    private ArrayList<Dice> dice;
    private Scanner sc;
    private int remainingRolls;
    private Score player;
    private Score opponent;

    public yacht() {
        this.dice = new ArrayList<>();
        loadDice();
        this.remainingRolls = 2;
        sc = new Scanner(System.in);
        player = new Score();
        opponent = new Score();
    }

    public void play() {
        while (true) {
            if (this.remainingRolls > 0) {
                roll();
                printDice();
                keepDice();
            } else {
                System.out.print("\033[H\033[2J");
                roll();
                printDice();
                scoreStart();
                System.out.print("\033[H\033[2J");
                printScore(player);
                loadDice();
                AIturn();
            }
            if (!player.score.containsValue(null)) {
                player.calculateBonuses();
                opponent.calculateBonuses();
                System.out.println("----Pre-Bonus Totals:----\nYou: " + player.getTotal() + "  |  Opponent: "
                        + opponent.getTotal()
                        + "\n----Bonuses----\nYou: " + player.getBonuses() + "  |  Opponent: " + opponent.getBonuses()
                        + "\n----Final score----\nYou: " + player.getBonusTotal() + "  |  Opponent: " + opponent.getBonusTotal());
                break;
            }

        }
    }

    private void loadDice() {
        this.dice.clear();
        this.remainingRolls = 2;
        for (int i = 0; i < 5; i++) {
            this.dice.add(new Dice());
        }
    }

    public void roll() {

        for (int i = 0; i < 5; i++) {
            if (this.dice.get(i).getKeep() == false) {
                this.dice.get(i).rollFace();
            }
        }
        for (int i = 0; i < 5; i++) {
            this.dice.get(i).setKeep(false);
        }
    }

    public void printDice() {

        String border = "            +---+---+---+---+---+";
        System.out.println(border);
        System.out.print("Die value:  |");
        for (Dice d : dice) {
            System.out.printf(" %d |", d.getFace());
        }
        System.out.println("\n" + border);

        System.out.print("Die number: |");
        for (int i = 0; i < this.dice.size(); i++) {
            System.out.printf(" %d |", i+1);
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

    public void AIturn() {
        loadDice();
        roll();
        System.out.println("----Opponent turn----\nOpponent Roll: ");
        
        AIScore();
        printScore(opponent);
    }

    public void scoreStart() {
        boolean inputCheck = false;
        while (!inputCheck) {
            System.out.println("\nWhich would you like to score this as?\n");
            int i = 0;
            for (String categories : constants.SCORE_CATEGORIES) {

                if (player.score.get(categories) == null) {
                    System.out.print(categories + "  |  ");
                    i++;
                    if (i % 3 == 0) {
                        System.out.println();
                    }
                }

            }
            System.out.println();
            inputCheck = scoreInputClean(sc.nextLine());
        }
    }

    public void printScore(Score s) {
        System.out.println("\nTotal: " + s.getTotal() + "\n");
    }

    public boolean rollInputClean(String s) {
        try {
            s = s.toLowerCase();
            s = s.replaceAll(" ", "");
            if (s.equals("all")) {
                for (int i = 0; i < 5; i++) {
                    this.dice.get(i).setKeep(true);
                    ;
                }
                this.remainingRolls = 0;
                return true;
            } else if (s.equals("none")) {
                for (int i = 0; i < 5; i++) {
                    this.dice.get(i).setKeep(false);
                }
                this.remainingRolls--;

                return true;
            }
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

    public boolean scoreInputClean(String s) {
        try {
            s = s.toLowerCase();
            for (String categories : constants.SCORE_CATEGORIES) {
                if (s.equalsIgnoreCase(categories) && player.score.get(categories) == null) {
                    player.addScore(categories, scoringMetric(categories, player));
                    System.out.println("\n[" + categories + "]\n");
                    return true;
                } else if (s.equalsIgnoreCase(categories) && player.score.get(categories) != null) {
                    System.out.println("Category has already been played, please choose a different category");
                    return false;
                }
            }

            System.out.println("Invalid input");
            return false;
        } catch (Exception e) {
            System.out.println(e + "\nInvalid input");
            return false;
        }

    }
    public void AIScore() {

        int max = 0;
        int i = 0;
        String categoryMost = "";
        System.out.println();
        printDice();
        for (String categories : constants.SCORE_CATEGORIES) {

            if (opponent.score.get(categories) == null) {
                System.out.print(categories + "  |  ");
                i++;
                if (i % 3 == 0) {
                    System.out.println();
                }
            }
        }
        System.out.println();
        for (String categories : constants.SCORE_CATEGORIES) {
            if (opponent.score.get(categories) == null) {
                int score = scoringMetric(categories, opponent);
                if (score > max) {
                    max = score;
                    categoryMost = categories;
                }
                
            }
        }
        
        if (max == 0) {
            for (String categories : constants.SCORE_CATEGORIES) {
                if (opponent.score.get(categories) == null) {
                    categoryMost = categories;
                    break;
                }
            }
        }
        opponent.addScore(categoryMost, max);
        System.out.println("\n[" + categoryMost + "]\n");
            }
        

    public int scoringMetric(String s, Score pl) {
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
        switch (s) {
            case "Ones":
                bonusYCheck(rollResults, pl);
                return rollResults[0];
            case "Twos":
                bonusYCheck(rollResults, pl);
                return rollResults[1] * 2;
            case "Threes":
                bonusYCheck(rollResults, pl);
                return rollResults[2] * 3;
            case "Fours":
                bonusYCheck(rollResults, pl);
                return rollResults[3] * 4;
            case "Fives":
                bonusYCheck(rollResults, pl);
                return rollResults[4] * 5;
            case "Sixes":
                bonusYCheck(rollResults, pl);
                return rollResults[5] * 6;
            case "Three of a Kind":
                bonusYCheck(rollResults, pl);
                for (int i = 0; i < rollResults.length; i++) {
                    if (rollResults[i] == 3) {
                        int toak = 0;
                        for (int j = 0; j < rollResults.length; j++) {
                            toak += rollResults[j];
                        }
                        return toak;
                    }
                }
                return 0;
            case "Four of a Kind":
                bonusYCheck(rollResults, pl);
                for (int i = 0; i < rollResults.length; i++) {
                    if (rollResults[i] == 4) {
                        int foak = 0;
                        for (int j = 0; j < rollResults.length; j++) {
                            foak += rollResults[j];
                        }
                        return foak;
                    }
                }
                return 0;
            case "Full House":
                bonusYCheck(rollResults, pl);
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
            case "Sm. Straight":
                bonusYCheck(rollResults, pl);
                for (int i = 0; i < 3; i++) {
                    if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                            && rollResults[i + 3] >= 1) {
                        return 30;
                    }
                }
                return 0;
            case "Lg. Straight":
                bonusYCheck(rollResults, pl);
                for (int i = 0; i < 2; i++) {
                    if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                            && rollResults[i + 3] >= 1 && rollResults[i + 4] >= 1) {
                        return 40;
                    }
                }
                return 0;
            case "Yahtzee":
                for (int count : rollResults) {
                    if (count == 5) {
                        return 50;
                    }
                }
                return 0;
            case "Chance":
                int chanceScore = 0;
                for (int i = 1; i < rollResults.length + 1; i++) {
                    chanceScore += rollResults[i - 1] * i;
                }
                bonusYCheck(rollResults, pl);
                return chanceScore;
        }
        return 0;
    }

    private void bonusYCheck(int i[], Score pl) {
        for (int j = 0; j < i.length; j++) {
            if (i[j] == 5 && pl.score.get("Yahtzee") != null) {
                pl.bonusYaht();
            }
        }

    }

}
