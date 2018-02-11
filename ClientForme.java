import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

public class ClientForme extends JFrame implements ActionListener {

    CommForme server;
    TabFormes formes;
    FormeFactory factory;

    boolean flagEND;
    protected Color theColor = new Color(0x00FF00);
    private static final String
            TITRE_APPLICATION = "app.frame.title",
            MENU_FICHIER_TITRE = "app.frame.menus.file.title",
            MENU_FICHIER_QUITTER = "app.frame.menus.file.exit",
            MENU_DESSIN_TITRE = "app.frame.menus.draw.title",
            MENU_DESSIN_DEMARRER = "app.frame.menus.draw.start",
            MENU_DESSIN_ARRETER = "app.frame.menus.draw.stop",
            MENU_AIDE_TITRE = "app.frame.menus.help.title",
            MENU_AIDE_PROPOS = "app.frame.menus.help.about";

    private static final char MENU_FICHIER_QUITTER_TOUCHE_RACC = KeyEvent.VK_Q;
    private static final int MENU_FICHIER_QUITTER_TOUCHE_MASK = ActionEvent.CTRL_MASK;

    private static final char MENU_DESSIN_DEMARRER_TOUCHE_RACC = KeyEvent.VK_D;
    private static final int MENU_DESSIN_DEMARRER_TOUCHE_MASK = ActionEvent.CTRL_MASK;

    private static final char MENU_DESSIN_ARRETER_TOUCHE_RACC = KeyEvent.VK_A;
    private static final int MENU_DESSIN_ARRETER_TOUCHE_MASK = ActionEvent.CTRL_MASK;

    private static final String
            MESSAGE_DIALOGUE_NO_DE_FORMES = "app.frame.dialog.shapeCount",
            MESSAGE_DIALOGUE_A_PROPOS = "app.frame.dialog.about";

    private static final int
            NOMBRE_DE_FORMES = 150,
            DELAI_ENTRE_FORMES_MSEC = 1000,
            DELAI_QUITTER_MSEC = 200;

    private static final int
            CANEVAS_LARGEUR = 500,
            CANEVAS_HAUTEUR = 500,
            MARGE_H = 50,
            MARGE_V = 60,
            FORME_MAX_LARGEUR = 200,
            FORME_MAX_HAUTEUR = 200;

    /*
     * Attribut pour le nombre de formes à dessiner de suite, initisialisé à la
	 * valeur constante, plutôt qu'un chiffre.
	 */
    // TODO: 2018-02-10 remove : not needed
    private int nombreDeFormes = NOMBRE_DE_FORMES;

    /*
     * Attribut qui représente une seule forme
	 */
    // TODO: 2018-02-10 mettre tableau de formes a la place
    private Ellipse2D.Double forme;

    /*
	 * Ces attributs sont utilisés pour gérer l'état des articles de menu
	 * qui permettent de démarrer et d'arreter un SwingWorker
	 */
    // TODO: 2018-02-10 maybe these will be used as is
    private boolean workerActive;
    private JMenuItem demarrerMenuItem;
    private JMenuItem arreterMenuItem;

    /**
     * <code>CustomCanvas</code> est une "inner" classe qui permet de dessiner
     * des objets dans l'interface Swing.
     * <p>
     * On utilise une inner classe pour faciliter la visibilites des
     * variables dans la classe exterieure.
     * <p>
     * Voir
     * <a href="http://java.sun.com/docs/books/tutorial/uiswing/painting/overview.html">Overview of Custom Painting</a>,
     * une partie du tutoriel Java de Sun.
     */
    class CustomCanvas extends JPanel {

        public CustomCanvas() {
            setSize(getPreferredSize());
            setMinimumSize(getPreferredSize());
            ClientForme.CustomCanvas.this.setBackground(Color.white);
        }

        /**
         * <code>getPreferredSize</code> retourne la dimension du JPanel
         *
         * @return a <code>Dimension</code> value
         */
        public Dimension getPreferredSize() {
            return new Dimension(CANEVAS_LARGEUR, CANEVAS_HAUTEUR);
        }

        /**
         * <code>paintComponent</code> contient le code pour le dessin
         * "fait sur commande"
         *
         * @param g a <code>Graphics</code> value
         */
        public void paintComponent(Graphics g) {
			/* dessiner le fonds (background) -- obligatoire */
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // theColor = une variable de la classe principale
            g2d.setColor(theColor);
            formes.dessine(g2d);
            // TODO: 2018-02-10 paint components of the table
        }
    }

    public ClientForme() {
        /*
		 * Creer un objet CustomCanvas (JPanel) et mettre un scrollpane autour,
		 * puis placer le tout dans le contenu du JFrame (SqueletteSwingApplication)
		 */
        server = new CommForme();
        formes = new TabFormes();
        factory = FormeFactory.getFactory();
        getContentPane().add(new JScrollPane(new CustomCanvas()));
    }

    public static void main(String[] args) {
        ClientForme frame = new ClientForme();

        frame.makeFileMenu();
        frame.makeDrawMenu();
        frame.makeHelpMenu();

		/* mettre à jour les articles dans le menu */
        frame.updateMenus();

        ApplicationSupport.launch(
                frame,
                ApplicationSupport.getResource("app.frame.title"),
                0,
                0,
                CANEVAS_LARGEUR + MARGE_H,
                CANEVAS_HAUTEUR + MARGE_V);

        // pour centrer la fenêtre ?
        frame.setLocationRelativeTo(null);

		/*
		 * Si jamais on en a besoin, on peut ajouter un listener pour recevoir
		 * un evenement generé lorsque le JFrame est ferme par l'usager ou le système
		 */
        // frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // frame.addWindowListener(new WindowAdapter()
        // {
        // public void windowClosing(WindowEvent e)
        // {
        // 		// faire du nettoyage, etc., si nécessaire...
        //
        // 		// quitter
        // 		System.exit(0);
        // 	}
        // 	public void windowOpened(WindowEvent e)
        // 	{
        // 	}
        // });

    }

    /*
	 * Créer des menus avec ApplicationSupport.addMenu()
	 */
    // menu fichier
    private JMenu makeFileMenu() {
        JMenu fileMenu =
                ApplicationSupport.addMenu(
                        this,
                        MENU_FICHIER_TITRE,
                        new String[]{MENU_FICHIER_QUITTER});

        // ajouter un listener pour quand l'usager termine l'application
        fileMenu.getItem(0).addActionListener(this);
        // touche raccourci
        fileMenu.getItem(0).setAccelerator(
                KeyStroke.getKeyStroke(
                        MENU_FICHIER_QUITTER_TOUCHE_RACC,
                        MENU_FICHIER_QUITTER_TOUCHE_MASK));

        return fileMenu;
    }

    // menu dessiner
    private JMenu makeDrawMenu() {
        JMenu drawMenu =
                ApplicationSupport.addMenu(
                        this,
                        MENU_DESSIN_TITRE,
                        new String[]{MENU_DESSIN_DEMARRER, MENU_DESSIN_ARRETER});

        // ajouter un listener pour démarrer (item 0)
        this.demarrerMenuItem = drawMenu.getItem(0);
        this.demarrerMenuItem.addActionListener(this);
        // touche raccourci
        this.demarrerMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(
                        MENU_DESSIN_DEMARRER_TOUCHE_RACC,
                        MENU_DESSIN_DEMARRER_TOUCHE_MASK));

        // ajouter un listener pour arrêter (item 1)
        this.arreterMenuItem = drawMenu.getItem(1);
        this.arreterMenuItem.addActionListener(this);
        // touche raccourci
        this.arreterMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(
                        MENU_DESSIN_ARRETER_TOUCHE_RACC,
                        MENU_DESSIN_ARRETER_TOUCHE_MASK));

        return drawMenu;
    }

    // menu aide
    private JMenu makeHelpMenu() {
        JMenu helpMenu =
                ApplicationSupport.addMenu(
                        this,
                        MENU_AIDE_TITRE,
                        new String[]{MENU_AIDE_PROPOS});

        // ajouter un listener pour item 0 (à propos de)
        helpMenu.getItem(0).addActionListener(this);
        // aucune touche raccourci

        return helpMenu;
    }

    /*
	 * Cette méthode va activer ou désactiver les articles de menu selon l'état
	 */
    private void updateMenus() {
        demarrerMenuItem.setEnabled(!this.workerActive);
        arreterMenuItem.setEnabled(this.workerActive);
    }

    /*
	 * Toutes les actions des menus sont effectuées ici, grâce aux
	 * EventListeners
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        // enlever les commentaires ci-dessous pour voir la dynamique de cette méthode
        //		String s =
        //			"Action event detected."
        //				+ "\n"
        //				+ "    Event source: "
        //				+ source.getText();
        //		System.out.print(s + "\n");

		/*
		 * Puisque tous les articles de menu sont dirigés vers cette même méthode,
		 * il faut identifier la source de l'ActionEvent, puis on peut effectuer
		 * l'action selon cette source.
		 */

        // ici, on traite l'article "Démarrer"
        if (source
                .getText()
                .equals(ApplicationSupport.getResource(MENU_DESSIN_DEMARRER))
                && !this.workerActive) {
			/*
			 * Demander à l'usager combien de fois il veut que la forme change
			 */


            this.formes = new TabFormes();
            this.server.envoieGET();
            /*
             * On "dessine" ensuite les formes. C'est une activité passive
             * plutôt qu'active, car il y a une forme definie dans le programme, mais
             * elle va évoluer dans le temps, en se dessinant avec chaque changement.
             * Ainsi, c'est une sorte d'animation qui prendra un certain temps.
             *
             * Avec Java Swing, le Thread qui traite les
             * appels à la méthode actionPerformed est le même qui répond à tous les
             * évenements de la GUI. Si on lui donnait à faire ensuite un traitement
             * long (l'animation), il ne pourrait plus répondre aux évenements.
             * Pour éviter ce problème, on délègue le travail long à un thread
             * special selon une stratégie proposée par Sun. C'est le SwingWorker.
             * Pour plus d'informations sur cette approche, voir
             * http://java.sun.com/products/jfc/tsc/articles/threads/threads2.html
             * ou chercher "SwingWorker" dans le tutorial Java de Sun.
             */
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    //...code that might take a while to execute is here...
                    dessinerFormes();
                    workerActive = false;
                    updateMenus();
                    return new Integer(0);
                }
            };
            worker.start(); //required for SwingWorker 3
            this.workerActive = true;

        } else
			/*
			 * Traiter l'article de menu qui arrête le SwingWorker
			 * On met à false la valeur de l'attribut "workerActive" car la méthode
			 * dessinerFormes() va le tester dans sa boucle
			 */
            if (source
                    .getText()
                    .equals(ApplicationSupport.getResource(MENU_DESSIN_ARRETER))
                    && this.workerActive) {
                this.workerActive = false;
                server.envoieFIN();
                server.terminerConnection();
            } else
				/*
				 * Traiter l'article "A propos de ..." ici
				 */
                if (source
                        .getText()
                        .equals(ApplicationSupport.getResource(MENU_AIDE_PROPOS))) {
                    JOptionPane.showMessageDialog(
                            this,
                            ApplicationSupport.getResource(
                                    MESSAGE_DIALOGUE_A_PROPOS),
                            ApplicationSupport.getResource(MENU_AIDE_PROPOS),
                            JOptionPane.INFORMATION_MESSAGE);
                } else
					/*
					 * Traiter l'article "Quitter" ici
					 * On devrait arrêter le SwingWorker par principe, s'il est activé
					 */
                    if (source
                            .getText()
                            .equals(
                                    ApplicationSupport.getResource(
                                            MENU_FICHIER_QUITTER))) {
                        if (this.workerActive) {
                            this.workerActive = false;
                            try {
                                // pause courte pour permettre le worker de terminer
                                Thread.sleep(DELAI_QUITTER_MSEC);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        System.exit(0);
                    }

        updateMenus(); // changer l'état des menus selon les fanions
    }

    /*
	 * Cette méthode fait varier la forme à dessiner. D'abord l'objet qui
	 * représente la forme est changée, puis on force le système à la redessiner,
	 * puis on attend un court délai.
	 * C'est ici où la logique de l'animation est centrée.
	 * Le tout est répété un certain nombre de fois.
	 */
    protected void dessinerFormes() {
        // TODO: 2018-02-10 timing for GET sending and END sending
        server.connectionServer();
        String commande = server.recoiechaine();
        assert commande.equals("commande>");
        formes.add(factory.makeForme(server.recoiechaine()));
        this.repaint();
        try {
            // pause de N millisecondes
            Thread.sleep(DELAI_ENTRE_FORMES_MSEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
