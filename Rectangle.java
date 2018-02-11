import java.awt.*;


public class Rectangle extends Forme {

    protected int x1;
    protected int y1;
    protected int x2;
    protected int y2;

    public Rectangle(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        theColor = new Color(0x00FF00);
    }

    @Override
    public void dessine(Graphics2D g2) {
        g2.setColor(theColor);
        g2.fillRect(x1, y1, x2 - x1, y2 - y1);
    }
}
