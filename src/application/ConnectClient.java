package application;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ConnectClient extends Thread {
	private Server server = null;
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	String userId = "unknown";

	public ConnectClient(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}
	
	public String getUserName() {
		return userId;
	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (in != null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			String line = "";
			while (in != null) {
				try {
					line = in.readUTF();
					System.out.println("Server RECV:" + line);
					if (line.startsWith("#uid#")) {
						userId = line.substring(5);
						server.updateMafiaUser(userId, this);
						server.sendToAllClient(userId + "님이 접속하셨습니다.", this);
					} else {
						server.sendToAllClient(line, this);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					if( e.getMessage().indexOf("Connection reset")>= 0 ) break;
				}
			}
			System.out.println("Closing connection");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		server.updateMafiaUser(null, this); /* 연결 끊김 */
	}
}
