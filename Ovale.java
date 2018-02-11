import java.awt.*;

public class Ovale extends Forme {
    protected int centreX, centreY, rayonH, rayonV;

    public Ovale(int centreX, int centreY, int rayonH, int rayonV) {
        this.centreX = centreX;
        this.centreY = centreY;
        this.rayonH = rayonH;
        this.rayonV = rayonV;
        theColor = new Color(0xFF0000);
    }

    @Override
    public void dessine(Graphics2D g2) {
        g2.setColor(theColor);
        g2.fillOval(centreX - rayonH, centreY - rayonV, 2 * rayonH, 2 * rayonV);

    }
}
