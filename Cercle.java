import java.awt.*;

public class Cercle extends Ovale {

    public Cercle(int centreX, int centreY, int rayon) {
        super(centreX, centreY, rayon, rayon);
        theColor = new Color(0x0000FF);
    }
}
