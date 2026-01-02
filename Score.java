import java.util.*;

public class Score{
    public EnumMap<SCORE_CATEGORIES, Integer> score;
    private int totalScore;
    private int bonusY;
    private int bonusPoints;
    private int bonusTotalScore;

    public Score() {
        this.score = new EnumMap<>(SCORE_CATEGORIES.class);
        this.bonusY = 0;
        this.bonusPoints = 0;
        this.bonusTotalScore=0;
        loadScore();
    }

    public void addScore(SCORE_CATEGORIES s, int i) {
        this.score.put(s, i);
        this.totalScore += i;
    }

    private void loadScore() {
        for (SCORE_CATEGORIES sc : SCORE_CATEGORIES.values()) {
            this.score.put(sc, null);
        }
    }

    public void bonusYacht() {
        this.bonusY++;
    }

    public int getTotal() {
        return this.totalScore;
    }
    public int getBonusTotal(){
        return this.bonusTotalScore;
    }
    public int getBonuses() {
        return this.bonusPoints;
    }

    public void calculateBonuses() {
        if (this.score.get(SCORE_CATEGORIES.ONES) + this.score.get(SCORE_CATEGORIES.TWOS) + this.score.get(SCORE_CATEGORIES.THREES) + this.score.get(SCORE_CATEGORIES.FOURS)
                + this.score.get(SCORE_CATEGORIES.FIVES) + this.score.get(SCORE_CATEGORIES.SIXES) >= 63) {
            this.bonusPoints += 35;
        }
        this.bonusPoints += this.bonusY * 100;
        this.bonusTotalScore = this.bonusPoints + this.totalScore;
    }
    public void bonusYCheck(int i []) {
        for (int j = 0; j < i.length; j++) {
            if (i[j] == 5 && this.score.get(SCORE_CATEGORIES.YAHTZEE) != null) {
                bonusYacht();
            }
        }

    }
}
