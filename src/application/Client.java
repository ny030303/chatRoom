package application;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
	interface RecvCallback {
		void receive();
	}

	private Consumer<String> cbReceiveMsg = null;

	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;

	private String address;
	private int port;
	private String userName;

	public Client(String address, int port, String userName, Consumer<String> callback) {
		this.address = address;
		this.port = port;
		this.userName = userName;
		this.cbReceiveMsg = callback;
	}

	public void send(String msg) {
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void connect() {
		System.out.println(String.format("서버(%s:%d)에 연결중입니다...", address, port));
		try {
			socket = new Socket(address, port);
			System.out.println("연결되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}

	public void close() {
		try {
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		System.out.println("서버 연결이 끊겼습니다.");
	}

	@Override
	public void run() {
		try {
			connect();
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			send("#uid#" + userName);
			
			String line = "";
			while (in != null) {
				try {
					line = in.readUTF();
					System.out.println("Client RECV:" + line);
					if (cbReceiveMsg != null) {

						cbReceiveMsg.accept(line);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if( e.getMessage().indexOf("Connection reset")>= 0 ) break;
				}
			}
			System.out.println("Closing connection");
			cbReceiveMsg.accept("Closing connection");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}