import java.awt.*;

public class Ligne extends Forme{

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Ligne(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        theColor = new Color(0x00FFFF);
    }

    @Override
    public void dessine(Graphics2D g2) {
        g2.setColor(theColor);
        g2.drawLine(x1,y1,x2,y2);
        
    }
}
