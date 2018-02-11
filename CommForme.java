
public class CommForme extends Comm {

    public static final String FIN_DE_CONNECTION = "END";
    public static final String DEBUT_DE_CONNECTION = "END";

    public CommForme() {
        super();
    }

    public void envoieGET() {
        envoiechaine(DEBUT_DE_CONNECTION);
    }

    public void envoieFIN() {
        envoiechaine(FIN_DE_CONNECTION);
    }

}
