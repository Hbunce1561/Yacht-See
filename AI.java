import java.util.ArrayList;

public class AI extends Yacht {
    private int rollResults[];
    private SCORE_CATEGORIES c;

    public AI() {
        super();
        this.c = null;
    }

    @Override
    public void play() {
        while (true) {
            if (this.remainingRolls > 0) {
                roll();
                keepDice();
                printDice();
            } else {
                roll();
                printDice();
                keepDice();
                scoreStart();
                printScore();
                clearKeep();
                resetRollCount();
                clearCat();
            }
            if (!this.player.score.containsValue(null)) {
                this.player.calculateBonuses();
                break;
            }
        }
    }

    private void clearCat() {
        this.c = null;
    }

    @Override
    public void roll() {
        rollResults = new int[] { 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 5; i++) {
            if (this.dice.get(i).getKeep() == false) {
                this.dice.get(i).rollFace();
            }

        }
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

    }

    @Override
    public void keepDice() {
        if (yachtCheck()) {
            setAllKeeps();
        } else if (lStraightCheck()) {
            if (this.player.score.get(SCORE_CATEGORIES.LARGE_STRAIGHT) == null) {
                setAllKeeps();
                this.c = SCORE_CATEGORIES.LARGE_STRAIGHT;
            } else if (this.player.score.get(SCORE_CATEGORIES.SMALL_STRAIGHT) == null) {
                setAllKeeps();
                this.c = SCORE_CATEGORIES.SMALL_STRAIGHT;
            }
        } else if (sStraightCheck()) {
            if (this.player.score.get(SCORE_CATEGORIES.LARGE_STRAIGHT) == null) {
                selectKeeps(runCheck());

            } else if (this.player.score.get(SCORE_CATEGORIES.SMALL_STRAIGHT) == null) {
                setAllKeeps();
                this.c = SCORE_CATEGORIES.SMALL_STRAIGHT;
            }

        } else if (fhCheck() && this.player.score.get(SCORE_CATEGORIES.FULL_HOUSE) == null) {
            setAllKeeps();
            this.c = SCORE_CATEGORIES.FULL_HOUSE;
        } else if (FOAKCheck() || TOAKCheck()) {
            selectKeeps(multCheck());
        } else {
            clearKeep();
            this.remainingRolls--;
        }
    }

    private void selectKeeps(ArrayList<Integer> d) {

        for (int i = 0; i < d.size(); i++) {
            for (int j = 0; j < this.dice.size(); j++) {
                if (this.dice.get(j).getFace() == d.get(i)) {
                    this.dice.get(j).setKeep(true);
                }
            }
        }
        this.remainingRolls--;

    }

    private ArrayList<Integer> multCheck() {
        ArrayList<Integer> d = new ArrayList<>();
        for (int i = 0; i< rollResults.length; i++) {
            if (rollResults[i] > 1) {
                d.add(i+1);
            }
        }
        return d;

    }

    private ArrayList<Integer> runCheck() {
        ArrayList<Integer> d = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                    && rollResults[i + 3] >= 1) {
                d.add(i + 1);
                d.add(i + 2);
                d.add(i + 3);
                d.add(i + 4);
            }
        }
        return d;

    }

    private void setAllKeeps() {
        for (int i = 0; i < 5; i++) {
            this.dice.get(i).setKeep(true);
        }
        this.remainingRolls = 0;
    }

    private boolean yBonusCheck() {
        if (this.player.score.get(SCORE_CATEGORIES.YAHTZEE) != null) {
            return true;
        }
        return false;
    }

    private boolean fhCheck() {
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
            return true;
        }
        return false;

    }

    private boolean TOAKCheck() {
        for (int i = 0; i < rollResults.length; i++) {
            if (rollResults[i] >= 3) {
                return true;
            }
        }
        return false;
    }

    private void printCat() {
        for (SCORE_CATEGORIES sc : this.player.score.keySet()) {
            System.out.println(sc + " | " + this.player.score.get(sc) + "\n");
        }
    }

    private boolean FOAKCheck() {
        for (int i = 0; i < rollResults.length; i++) {
            if (rollResults[i] >= 4) {
                return true;
            }
        }
        return false;
    }

    private boolean sStraightCheck() {
        for (int i = 0; i < 3; i++) {
            if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                    && rollResults[i + 3] >= 1) {
                return true;
            }
        }
        return false;
    }

    private boolean lStraightCheck() {
        for (int i = 0; i < 2; i++) {
            if (rollResults[i] >= 1 && rollResults[i + 1] >= 1 && rollResults[i + 2] >= 1
                    && rollResults[i + 3] >= 1 && rollResults[i + 4] >= 1) {
                return true;
            }
        }
        return false;
    }

    private boolean yachtCheck() {
        for (int count : rollResults) {
            if (count == 5) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void scoreStart() {
        if (yachtCheck() && this.player.score.get(SCORE_CATEGORIES.YAHTZEE) == null) {
                this.c = SCORE_CATEGORIES.YAHTZEE;
            } else if (fhCheck() && this.player.score.get(SCORE_CATEGORIES.FULL_HOUSE) == null) {
                this.c = SCORE_CATEGORIES.FULL_HOUSE;
            }
            else if (FOAKCheck() && this.player.score.get(SCORE_CATEGORIES.FOUR_OF_A_KIND) == null) {
                this.c = SCORE_CATEGORIES.FOUR_OF_A_KIND;
            } else if (TOAKCheck() && this.player.score.get(SCORE_CATEGORIES.THREE_OF_A_KIND) == null) {
                this.c = SCORE_CATEGORIES.THREE_OF_A_KIND;
            } else if (rollResults[5] > 2 && this.player.score.get(SCORE_CATEGORIES.SIXES) == null) {
                this.c = SCORE_CATEGORIES.SIXES;
            } else if (rollResults[4] > 2 && this.player.score.get(SCORE_CATEGORIES.FIVES) == null) {
                this.c = SCORE_CATEGORIES.FIVES;
            } else if (rollResults[3] > 3 && this.player.score.get(SCORE_CATEGORIES.FOURS) == null) {
                this.c = SCORE_CATEGORIES.FOURS;
            }
            else if (sStraightCheck()&&this.player.score.get(SCORE_CATEGORIES.SMALL_STRAIGHT) == null) {
                this.c = SCORE_CATEGORIES.SMALL_STRAIGHT;
            } else if (lStraightCheck()&&this.player.score.get(SCORE_CATEGORIES.LARGE_STRAIGHT) == null) {
                this.c = SCORE_CATEGORIES.LARGE_STRAIGHT;
            }
        if(c==null){
            if (this.player.score.get(SCORE_CATEGORIES.ONES) == null) {
                this.c = SCORE_CATEGORIES.ONES;
            } else if (this.player.score.get(SCORE_CATEGORIES.TWOS) == null) {
                this.c = SCORE_CATEGORIES.TWOS;
            } else if (this.player.score.get(SCORE_CATEGORIES.THREES) == null) {
                this.c = SCORE_CATEGORIES.THREES;
            } else if (this.player.score.get(SCORE_CATEGORIES.FOURS) == null) {
                this.c = SCORE_CATEGORIES.FOURS;
            } else if (this.player.score.get(SCORE_CATEGORIES.FIVES) == null) {
                this.c = SCORE_CATEGORIES.FIVES;
            } else if (this.player.score.get(SCORE_CATEGORIES.SIXES) == null) {
                this.c = SCORE_CATEGORIES.SIXES;
            } else if (this.player.score.get(SCORE_CATEGORIES.THREE_OF_A_KIND) == null) {
                this.c = SCORE_CATEGORIES.THREE_OF_A_KIND;
            } else if (this.player.score.get(SCORE_CATEGORIES.FOUR_OF_A_KIND) == null) {
                this.c = SCORE_CATEGORIES.FOUR_OF_A_KIND;
            } else if (this.player.score.get(SCORE_CATEGORIES.FULL_HOUSE) == null) {
                this.c = SCORE_CATEGORIES.FULL_HOUSE;
            }
            else if (this.player.score.get(SCORE_CATEGORIES.SMALL_STRAIGHT) == null) {
                this.c = SCORE_CATEGORIES.SMALL_STRAIGHT;
            } else if (this.player.score.get(SCORE_CATEGORIES.LARGE_STRAIGHT) == null) {
                this.c = SCORE_CATEGORIES.LARGE_STRAIGHT;
            } else if (this.player.score.get(SCORE_CATEGORIES.YAHTZEE) == null) {
                this.c = SCORE_CATEGORIES.YAHTZEE;
            } else if (this.player.score.get(SCORE_CATEGORIES.CHANCE) == null) {
                this.c = SCORE_CATEGORIES.CHANCE;
            }
        }
        this.player.addScore(this.c, scoringMetric(this.c));
        System.out.println("\n" + this.c + "\n");
    }

    @Override
    public int scoringMetric(SCORE_CATEGORIES categories) {
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
                int toak = 0;
                if (TOAKCheck()) {
                    for (Dice d : this.dice) {
                        toak += d.getFace();
                    }
                }
                return toak;
            case FOUR_OF_A_KIND:
                int foak = 0;
                if (FOAKCheck()) {
                    for (Dice d : this.dice) {
                        foak += d.getFace();
                    }
                }
                return foak;
            case FULL_HOUSE:
                if (fhCheck()) {
                    return 25;
                }
                return 0;
            case SMALL_STRAIGHT:
                if (sStraightCheck()) {
                    return 30;
                }
                return 0;
            case LARGE_STRAIGHT:
                if (lStraightCheck()) {
                    return 40;
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
