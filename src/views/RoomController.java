package views;

import application.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RoomController extends MasterController {

	@FXML
	private Label roomTitle;

	@FXML
	private TextField roomInput;

	@FXML
	private Button sendBtn;

	@FXML
	private VBox chatBox;

	public void init() {
		System.out.println("RoomController() - init");
//		client = new EchoClientThread("127.0.0.1", 20005, "1234"); 
//		client.start();
	}

	public void getOutRoom() {
		Main.app.slideOut(getRoot());
	}

	public void sendText() {
		Main.app.sendText(roomInput.getText());
	}

	public void addChatBox(String msg) {
		Platform.runLater(() -> {
			chatBox.getChildren().add(new Text("user" + " : " + msg));
		});
	}
}
