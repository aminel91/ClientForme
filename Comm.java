import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Comm {


    private Socket client;
    private PrintStream output;
    private BufferedReader input;

    public void connectionServer() {
        try {

            client = new Socket("localhost", 10000);

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintStream(client.getOutputStream());

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void envoiechaine(String chaine) {
        if (output == null || input == null || client == null) {
            connectionServer();
        }
        try {
            output = new PrintStream(client.getOutputStream());
            output.println(chaine);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String recoiechaine() {
        if (output == null || input == null || client == null) {
            connectionServer();
        }
        String data = null;
        try {
            data = input.readLine();

        } catch (IOException e) {
            System.out.println(e);
        }
        return data;
    }

    public void terminerConnection() {
        if (!(output == null || input == null || client == null)) {
            try {
                input.close();
                output.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
