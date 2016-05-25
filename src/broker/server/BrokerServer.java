package broker.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer {

	public BrokerServer(){
		try {
			ServerSocket server = new ServerSocket(7777);
			System.out.println("Broker 서버 시작!");
			
			while(true){
			Socket client = server.accept();
			System.out.println(client.getInetAddress().getHostAddress()+"클라이언트 접속");
			//접속한 client 아이피 찍힘
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			BrokerServerThread bst = new BrokerServerThread(ois, oos);
			Thread t = new Thread(bst);
			t.start();
			}//while
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		new BrokerServer();
	}

}
