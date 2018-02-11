import java.awt.*;
import java.util.LinkedList;

public class TabFormes {

    private int tabLength = 10;
    private LinkedList<Forme> tableau;

    public TabFormes() {
        this.tableau = new LinkedList<Forme>();
    }

    public void add(Forme f) {
        if (tableau.size() > tabLength) {
            tableau.remove(0);
            tableau.add(f);
        } else
            tableau.add(f);

    }

    public void remove(int indx) {
        tableau.remove(indx);
    }

    public int getSize() {
        return tableau.size();
    }

    public Forme getForme(int indx)
    {
        return tableau.get(indx);
    }

    public void dessine(Graphics2D g2)
    {
        for (Forme f : tableau) {
            f.dessine(g2);
        }
    }

}
