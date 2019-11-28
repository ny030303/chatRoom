package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.JDBCUtil;
import util.Util;

public class Server extends Thread {
	// initialize socket and input stream
	private ServerSocket serverSocket = null;
	private int port = 4040;
	private String title;
	private int maxCount;
	private String pwd;
	private String serverIp;

	HashMap<String, ConnectClient> uidMap = new HashMap<>();
	List<ConnectClient> clients = new ArrayList<>();

	public Server(String title, int maxCount, String pwd) {
		this.title = title;
		this.maxCount = maxCount;
		this.pwd = pwd;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		System.out.println(Util.getMyAddress());
		while (!Util.portAvailable(port)) {
			port++; // 4040부터 빈 포트를 찾음.
		}

		try {
			// 서버 시작
			serverSocket = new ServerSocket(port);
			System.out.println("Server started. port=" + port);

			serverIp = Util.getMyAddress() + ":" + port;
			String sql = "INSERT INTO mafia_rooms (title,count,maxcount,pwd,server) VALUES "
					+ String.format("('%s',0,%d,password('%s'),'%s')", title, maxCount, pwd, serverIp);
			JDBCUtil.sqlMafiaRoom(sql);

			while( true) {
				System.out.println("클라이언트 연결 대기중...");
				Socket socket = serverSocket.accept();
				System.out.println(Util.getTime() + socket.getInetAddress() + " 클라이언트 연결 요청.");
				ConnectClient client = new ConnectClient(socket, this);
				clients.add(client); // 클라이언트 맵에 추가하고 스레드 시작
				client.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendToAllClient(String msg, ConnectClient fromClient) {
		clients.forEach(client -> {
			// if (client != fromClient) 
			{
				client.send("[" + fromClient.getUserName() + "] " + msg);
			}
		});
	}

	public void updateMafiaUser(String clientUid, ConnectClient fromClient) {
		if (clientUid != null) {
			uidMap.put(clientUid, fromClient);
		}
		else {
			uidMap.forEach((key, value) -> {
				if( value == fromClient ) {
					uidMap.remove(key);					
				}
			});
		}
		JDBCUtil.sqlMafiaRoom(
				String.format("Update mafia_rooms set count=%d where server = '%s'", uidMap.size(), serverIp));
		
	}

	public void close() {
		JDBCUtil.deleteMafiaRoom(serverIp);
		try {
			System.out.println("서버를 종료합니다.");
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}