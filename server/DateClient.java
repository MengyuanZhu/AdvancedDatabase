import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.PrintWriter;
//import org.json.simple.JSONObject;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Arrays;
/**
 * Trivial client for the date server.
 */
public class DateClient {

  
    public static void main(String[] args) throws IOException {
        //JSONObject obj = new JSONObject();
	String location="atlanta";
	String longitude="33.7490";
	String latitude="84.3880";
   
	OutputStreamWriter osw;
   	String str = location+","+longitude+","+latitude;


        Socket s = new Socket("target.gsu.edu", 6969);
	PrintWriter out = new PrintWriter(s.getOutputStream(), true);
	out.println(str);
		
        BufferedReader input =  new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        List<String> items = Arrays.asList(answer.split("\\s*,\\s*"));
	for (int i=0;i<items.size();i=i+3){
		System.out.println(items.get(i).substring(0,items.get(i).indexOf("_")));
		System.out.println(items.get(1));
		System.out.println(items.get(2));
	}
        System.exit(0);
    }
}
