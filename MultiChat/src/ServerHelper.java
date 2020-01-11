import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerHelper extends Thread {

	private Socket incoming;
	private int roomNumber;


	public ServerHelper(Socket incoming) {
		this.incoming = incoming;
	}

	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incoming.getInputStream()));
			PrintWriter out;
			out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			String msg="";
			if(TCPMultiServer.getListOfHashMap().size()==0) {
				out.println("No chatrooms available");
				out.flush();

			}
			else {
				//writing available chat rooms
				for(int i1=0;i1<TCPMultiServer.getListOfHashMap().size();i1++) {
					msg+=("Chat Room "+(i1+1)+ " has ");
					if(TCPMultiServer.getListOfHashMap().get(i1).isEmpty()) {

						msg+=("no one\r");
					}
					else {
						for(String s:TCPMultiServer.getListOfHashMap().get(i1).values()) {
							msg+=(s+" ");
						}
						msg+="\r";
					}
				}
				msg+="Please enter your name: ";
				out.println(msg);
				out.flush();
				out.println("break");
				out.flush();
				String name=in.readLine();
				roomNumber=0;
				while(roomNumber<=0||roomNumber>TCPMultiServer.getListOfHashMap().size()) {
					out.println("Please a number to join a chat room: ");
					out.flush();
					roomNumber=Integer.parseInt(in.readLine());
				}
				out.println("Joining chat room #"+roomNumber+" with name "+name);
				out.flush();
				TCPMultiServer.getListOfHashMap().get(roomNumber-1).put(incoming, name);

			}
			while (true) {
				String str = in.readLine();
				System.out.println("Server received : " + str);
				if (str == null) {
					break;

				} 
				else if(str.startsWith("switch_channel")) {
					String[] tokens = str.split(" ");
					out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
					if(tokens[0].equals("switch_channel")&&tokens[1].matches("\\d+")) {
						int check=Integer.parseInt(tokens[1]);
						if(check>=1&&check<=TCPMultiServer.getListOfHashMap().size()&&check!=roomNumber) {
							String name=TCPMultiServer.getListOfHashMap().get(roomNumber-1).get(incoming);
							TCPMultiServer.getListOfHashMap().get(roomNumber-1).remove(incoming);
							msg="Switching to channel number "+check+" containing ";
							if(TCPMultiServer.getListOfHashMap().get(check-1).isEmpty()) {

								msg+=("no one\r");
							}
							else {
								for(String names:TCPMultiServer.getListOfHashMap().get(check-1).values()) {
									msg+=(names+" ");

								}
							}
							roomNumber=check;
							out.println(msg);
							out.flush();
							TCPMultiServer.getListOfHashMap().get(roomNumber-1).put(incoming, name);

						}
						else {
							out.println("Try again...");
							out.flush();
						}
					}
					else {
						out.println("Try again...");
						out.flush();
					}

				}
				else {

					for(Socket s: TCPMultiServer.getListOfHashMap().get(roomNumber-1).keySet()) {//TCPMultiServer.getHashMap().keySet()) {
						if(s.getPort() != incoming.getPort()) {
							out = new PrintWriter(new OutputStreamWriter(
									s.getOutputStream()));
							out.println(TCPMultiServer.getListOfHashMap().get(roomNumber-1).get(incoming)+ ": " + str);
							//out.println(TCPMultiServer.getHashMap().get(incoming) + ": " + str);
							out.flush();
						}
					}
				}
			}






			incoming.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
