import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Arrays;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

class Pokemon{
  public String pokemon;
  public String hp;
  public String weight;
  public String type;
  public String grass;
  public String height;
  public String attack;
  public String defense;
};


public class UserEvent {

  public static void main(String[] args) throws IOException {
          String state=String.valueOf(0);
          String username="mzhu7";
          String password="123";
          String pokemonName = "zmy";
          String hp = String.valueOf(500);
          String weight = String.valueOf(10);
          String type = "grass";
          String height = String.valueOf(1);
          String attack = String.valueOf(100);
          String defense = String.valueOf(200);

          OutputStreamWriter osw;
          String str = state+","+username+","+password; //if state is 5
          str = state+","+username; //if state is 0
          //str = state+","+username+","+pokemonName+","+hp+","+weight+","+type+","+height+","+attack+","+defense;  //if state is 1
          //str = state+","+username+","+pokemonName;  //if state is 3, update pokemon
          Socket s = new Socket("target.gsu.edu", 6970);
          PrintWriter out = new PrintWriter(s.getOutputStream(), true);
          out.println(str);

          BufferedReader input =  new BufferedReader(new InputStreamReader(s.getInputStream()));
          String answer = input.readLine();
          if (state.equals("0")) {
                  Gson gson = new Gson();
                  Pokemon[] pokemonData = gson.fromJson(answer, Pokemon[].class);
                  System.out.println(pokemonData[0].pokemon);
          }
          else{
                  System.out.println(answer);
          }
          System.exit(0);
  }
}
