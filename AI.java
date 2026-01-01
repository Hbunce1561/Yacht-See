/*public class AI extends Yacht{

    @Override
    public int scoringMetric(String s){
        scoringMetric(null);
    }
    @Override
    public void roll() {
        // TODO Auto-generated method stub
        super.roll();
    }
}
  public void AIturn() {
        loadDice();
        roll();
        System.out.println("----Opponent turn----\nOpponent Roll: ");
        
        AIScore();
        printScore(opponent);
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
        */