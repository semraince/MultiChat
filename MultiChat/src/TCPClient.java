import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String[] args) {
		try {
			System.out.println("Client is working");
			String host = "localhost";
			Socket socket = new Socket(host, 6789);
			BufferedReader inFromUser = new BufferedReader(
					new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			System.out.println("Connected to server");
			String str;
			String comeToServer;
			while (!(str = in.readLine()).equals("break")) {
				System.out.println(str);
			}
			
			str = inFromUser.readLine();
			out.println(str);
			out.flush();
			comeToServer=in.readLine();
			System.out.println(comeToServer);
			str = inFromUser.readLine();
			out.println(str);
			out.flush();
			comeToServer=in.readLine();
			System.out.println(comeToServer);
			new ClientPrinter(in).start();
			while (true) {
				str = inFromUser.readLine();
				out.println(str);
				out.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}