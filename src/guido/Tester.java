package guido;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Tester {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("fachera.sytes.net", 31007);
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println(ois.readObject());
			ois.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
