import ca.etsmtl.log.util.IDLogger;

import java.util.StringTokenizer;

public class FormeFactory {

    private static final String OVALE = "OVALE";
    private static final String CERCLE = "CERCLE";
    private static final String RECTANGLE = "RECTANGLE";
    private static final String CARRE = "CARRE";
    private static final String LIGNE = "LIGNE";

    public static FormeFactory singleton = null;

    private FormeFactory() {
    }

    public static FormeFactory getFactory() {
        if (singleton == null) {
            return new FormeFactory();
        }
        return singleton;
    }

    public Forme makeForme(String description) {
        IDLogger idLogger = IDLogger.getInstance();
        StringTokenizer tokenizer = new StringTokenizer(description, "<>/ ", false);
        int cnt = 0;
        while (tokenizer.hasMoreTokens()) {
            if (cnt == 0) {
                idLogger.logID(Integer.parseInt(tokenizer.nextToken()));
            } else if (cnt == 1) {
                return createForme(tokenizer);
            }
            cnt++;
        }
        return null;
    }

    public Forme createForme(StringTokenizer token) {
        String type = token.nextToken();
        if (type.equals(CARRE)) {
            return new Carre(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken
                    ()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
        } else if (type.equals(CERCLE)) {
            return new Cercle(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
        } else if (type.equals(OVALE)) {
            return new Ovale(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken
                    ()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
        } else if (type.equals(RECTANGLE)) {
            return new Rectangle(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken
                    ()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
        } else if (type.equals(LIGNE)) {
            return new Ligne(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken
                    ()), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
        }
        return null;
    }

    public static void main(String[] args) {
        FormeFactory f = FormeFactory.getFactory();
        String forme = "12345<CERCLE>2 3 4</CERCLE>";
        System.out.println(f.makeForme(forme) instanceof Cercle);
    }
}
