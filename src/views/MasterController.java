package views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import application.Main;
import domain.RoomVO;
import domain.UserVO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.JDBCUtil;

public class MasterController {
	private Pane root; //자신의 가장 윗쪽의 팬을 저장
	
	private ArrayList<RoomVO> roomlist = new ArrayList<>();
	
	@FXML
	private Button makeRoomBtn;
	@FXML
	private Button getOutBtn;
	@FXML
	private Button reloadBtn;
	
	@FXML
	public Label userName;
	@FXML
	public Label userWinOrLose;
	
	@FXML
	private VBox roomUIList;
	
	public int userWin;
	
	public int userLose;
	
	public void initialize() {
		System.out.println("메인 레이아웃 초기화");
		Main.app.masterController = this;
	}

	public Pane getRoot() {
		return root;
	}

	public void setRoot(Pane root) {
		this.root = root;
	}
	
	public void fadePopup() {
		Main.app.openPopup();
	}
	
	public void setUserInfo() {
		UserVO user = Main.app.getLoginUser();
		Platform.runLater(() -> {
			userName.setText(user.getName());
			userWinOrLose.setText(user.getInfo());
		});
		
	}
	
	public void reload() {
		reloadMafiaRoom();
		System.out.println(roomlist);
		try {
			makeFXML();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reloadMafiaRoom() {
		roomlist.clear(); //리스트를 전부 지우고 새로 가져오기
		Connection con = JDBCUtil.getConnection(); //DB연결 가져오기
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM `mafia_rooms`";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				roomlist.add(makeRoomVO(rs));
			}
			
		} catch (Exception e) {
			System.out.println("데이터베이스 쿼리중 오류 발생");
			e.printStackTrace();
		} finally {
			JDBCUtil.close(pstmt);
			JDBCUtil.close(con);
		}
	}
	
	private RoomVO makeRoomVO(ResultSet rs) throws Exception{
		RoomVO temp = new RoomVO();
//		temp.setRoomMaster(rs.getString("roomMaster"));
		temp.setRoomName(rs.getString("title"));
		temp.setRid(rs.getInt("id"));
		temp.setPwd(rs.getString("pwd"));
		temp.setCount(rs.getInt("count"));
		temp.setMaxCount(rs.getInt("maxcount"));
		temp.setCreated(rs.getString("created"));
		temp.setServer(rs.getString("server"));
		return temp;
	}
	
	public void makeFXML() throws Exception {
		roomUIList.getChildren().clear();
		for(RoomVO item : this.roomlist) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/RoomItemLayout.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			
			RoomItemController ic = loader.getController();
			
			
			int rid = item.getRid();
			String roomMaster = item.getRoomMaster();
			String roomName = item.getRoomName();
			String created = item.getCreated();
			String server = item.getServer();
			int count = item.getCount();
			int maxCount = item.getMaxCount();
		
			ic.setData(rid,roomName, count, maxCount, roomMaster, created, server);
			roomUIList.getChildren().add(root);
		}
	}
}
