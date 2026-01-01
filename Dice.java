import java.util.*;;
public class Dice {
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
