public class Main {
    public static void main(String args[]) {
        Yacht player = new Yacht();
        AI ai = new AI();
       // player.play();
        ai.play();
       // System.out.println("\nYou: ");
       // player.printScoreTotal();
        System.out.println("Computer: ");
        ai.printScoreTotal();
       // if (ai.getTotalScore() > player.getTotalScore()) {
         //   System.out.println("You Lose!\nBetter luck next time...");
        //} else if (ai.getTotalScore() < player.getTotalScore()) {
         //   System.out.println("You Win!\n");
        //} else {
         //   System.out.println("A Tie!\nEveryone wins!");
        //}

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