package views;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RoomItemController {
	@FXML
	private Label id;

	@FXML
	private Label title;
	
	@FXML
	private Label count;
	@FXML
	private Label maxCount;
	
	@FXML
	private Label username;
	
	@FXML
	private Label date;
	
	@FXML
	private Label server;
	
	
	public void setData(int id, String title, int count, int maxCount, String username, String date, String server) {
//		System.out.println(number + "," + addressname + "," + ispublic + "," + username + "," +  date);
		this.id.setText(id + "");
		this.title.setText(title);
		this.count.setText(count + "");
		this.maxCount.setText(maxCount + "");
		this.server.setText(server + "");
//		this.username.setText("네임");
		this.date.setText(date);
	}
	
	public void gotoRoom() {
		Main.app.roomLoad(this.id.getText(), server.getText());
		Main.app.loadPage("roomPage");
	}
}
