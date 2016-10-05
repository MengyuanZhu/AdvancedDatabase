import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

/**
 * Trivial client for the date server.
 */
public class DateClient {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
        
		OutputStreamWriter osw;
   		String str = "Hello World";
        Socket s = new Socket("127.0.0.1", 6969);
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		out.println(str);
		
        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        
		System.out.println(answer);
        System.exit(0);
    }
}
