public class Main {
    public static void main(String args[]) {
        double avg =0;
        for(int i = 0; i <1000; i++){
        AI ai = new AI();
        ai.play();
        
        System.out.println("Computer: ");
        ai.printScoreTotal();
        avg += ai.getTotalScore();
        }
        System.out.println("Computer Avg: "+ avg/1000);

    }
}

enum SCORE_CATEGORIES {
    ONES,
    TWOS,
    THREES,
    FOURS,
    FIVES,
    SIXES,
    THREE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    SMALL_STRAIGHT,
    LARGE_STRAIGHT,
    YAHTZEE,
    CHANCE
}
