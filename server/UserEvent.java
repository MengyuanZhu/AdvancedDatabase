import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Arrays;
/**
 * Trivial client for the date server.
 */
public class UserEvent {


public static void main(String[] args) throws IOException {

        String state=String.valueOf(0);
        String username="mzhu7";
        String password="123";
        String pokemonName = "Pikachu";
        String hp = String.valueOf(500);
        String weight = String.valueOf(10);
        String type = "grass";
        String height = String.valueOf(1);
        String attack = String.valueOf(100);
        String defense = String.valueOf(200);


        OutputStreamWriter osw;
        String str = state+","+username+","+password; //if state is 5
        str = state+","+username; //if state is 0
        str = state+","+username+","+pokemonName+","+hp+","+weight+","+type+","+height+","+attack+","+defense;  //if state is 1
        Socket s = new Socket("target.gsu.edu", 6970);
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        out.println(str);

        BufferedReader input =  new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        System.out.println(answer);


        System.exit(0);
}
}
