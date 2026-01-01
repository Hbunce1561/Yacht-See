import java.util.ArrayList;
import java.util.Scanner;
public class Yacht {
    private ArrayList<Dice> dice;
    private Scanner sc;
    private int remainingRolls;
    private Score player;

    public Yacht() {
        this.dice = new ArrayList<>();
        loadDice();
        this.remainingRolls = 2;
        sc = new Scanner(System.in);
        this.player = new Score();
    }
    public void clearConsole(){
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
                printScore(player);
                loadDice();
            }
            if (!player.score.containsValue(null)) {
                player.calculateBonuses();
                System.out.println("----Pre-Bonus Totals:----\nYou: " + player.getTotal());
                break;
            }
/* + "  |  Opponent: "
                        + opponent.getTotal()
                        + "\n----Bonuses----\nYou: " + player.getBonuses() + "  |  Opponent: " + opponent.getBonuses()
                        + "\n----Final score----\nYou: " + player.getBonusTotal() + "  |  Opponent: " + opponent.getBonusTotal()); */
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

    public void scoreStart() {
        String border = " ----------------------------";
        while (true) {
            System.out.println("\nWhich would you like to score this as?\n\n"+border+"\n");
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
            System.out.println("\n"+border+"\n");
            String s = sc.nextLine();
            SCORE_CATEGORIES cat = convertToCategories(s);
            if(cat == null){
                System.out.println("Invalid input");
            }
            else{
               if(this.player.score.get(cat) == null){
                this.player.addScore(cat, scoringMetric(cat));
                break;
               }
               else{
                System.out.println("This category has already been played, please choose another.");

               }
            }
        }
        }
    

    private SCORE_CATEGORIES convertToCategories(String s){
        return constants.inputMap.get(s.toLowerCase());
       
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
        switch (categories) {
            case ONES:
               this.player.bonusYCheck(rollResults);
                return rollResults[0];
            case TWOS:
               this.player.bonusYCheck(rollResults);
                return rollResults[1] * 2;
            case THREES:
               this.player.bonusYCheck(rollResults);
                return rollResults[2] * 3;
            case FOURS:
               this.player.bonusYCheck(rollResults);
                return rollResults[3] * 4;
            case FIVES:
               this.player.bonusYCheck(rollResults);
                return rollResults[4] * 5;
            case SIXES:
               this.player.bonusYCheck(rollResults);
                return rollResults[5] * 6;
            case THREE_OF_A_KIND:
               this.player.bonusYCheck(rollResults);
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
            case FOUR_OF_A_KIND:
                this.player.bonusYCheck(rollResults);
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
            case FULL_HOUSE:
               this.player.bonusYCheck(rollResults);
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
                this.player.bonusYCheck(rollResults);
                for (int i = 0; i < 3; i++) {
                    if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                            && rollResults[i + 3] >= 1) {
                        return 30;
                    }
                }
                return 0;
            case LARGE_STRAIGHT:
                this.player.bonusYCheck(rollResults);
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
                for (int i = 1; i < rollResults.length + 1; i++) {
                    chanceScore += rollResults[i - 1] * i;
                }
                this.player.bonusYCheck(rollResults);
                return chanceScore;
        }
        return 0;
    }

    

}

