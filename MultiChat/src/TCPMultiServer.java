
import java.net.*;
import java.util.*;

public class TCPMultiServer {
	//keep single room
	private static HashMap<Socket, String> hashMap = new HashMap<Socket, String>();
	//keep all rooms
	private static List<Map<Socket, String>> listOfHashMap=new ArrayList<>();

	private static int chatRoomNumber;

	public static void main(String[] args) {

		int i = 0;

		try {
			System.out.println("Server is started...");
			ServerSocket serverSocket = new ServerSocket(6789);
			Scanner scanner=new Scanner(System.in);
			while(chatRoomNumber<=0) {
				System.out.print("Please a number to create chat rooms: ");
				chatRoomNumber=scanner.nextInt();
			}
			scanner.close();
			System.out.println("Creating "+ chatRoomNumber +" chat rooms...");
			for(int a=0;a<chatRoomNumber;a++) {
				Map<Socket, String> hashMap=Collections.synchronizedMap(new HashMap<>());
				listOfHashMap.add(hashMap);

			}
			System.out.println("Ready to accept connections...");


			while (true) {
				Socket incoming = serverSocket.accept();


				new ServerHelper(incoming).start();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	public static List<Map<Socket, String>> getListOfHashMap(){
		return listOfHashMap;
	}
	public static void setListOfHashMap(List<Map<Socket, String>> list) {
		listOfHashMap=list;
	}

	public static synchronized HashMap<Socket, String> getHashMap() {
		return hashMap;
	}

	public static synchronized void setHashMap(HashMap<Socket, String> hashMap) {
		TCPMultiServer.hashMap = hashMap;
	}

}