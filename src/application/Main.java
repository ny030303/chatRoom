package application;
	
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import domain.UserVO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.JDBCUtil;
import views.LoginController;
import views.MasterController;
import views.RegisterController;
import views.RoomController;
import views.PopupController;


public class Main extends Application {
	
	public static Main app;
	
	private Server server = null;
	private Client client = null;
	private UserVO loginUser = null;
	
	public StackPane mainPage = null;
	
	private Map<String, MasterController> controllerMap = new HashMap<>();
	
	private Stage popupStage;
	private PopupController popupController;
	public MasterController masterController;  
	
	
	private MasterController loadPageController(String fxmlName) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(fxmlName));
		Pane loadPage = null;
		try {
			loadPage = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MasterController rc = loader.getController();
		rc.setRoot(loadPage);		
		return rc;
	}

	@Override
	public void start(Stage primaryStage) {
		app = this; //싱글톤으로 만들어준다.
		try {
			controllerMap.put("main", loadPageController("/views/MainLayout.fxml"));
			controllerMap.put("login", loadPageController("/views/LoginLayout.fxml"));
			controllerMap.put("register", loadPageController("/views/RegisterLayout.fxml"));
			controllerMap.put("roomPage", loadPageController("/views/roomLayout.fxml"));
			

			mainPage = (StackPane) controllerMap.get("main").getRoot();			
			Scene scene = new Scene(mainPage);
			
			mainPage.getChildren().add(controllerMap.get("login").getRoot());
			
			scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			
	
			
			
			FXMLLoader scorePopupLoader = new FXMLLoader();
			scorePopupLoader.setLocation(getClass().getResource("/views/PopupLayout.fxml"));
			
			// 방 만들기 팝업 로드
			popupStage = new Stage();
			popupStage.setTitle("방 만들기");
			popupStage.initModality(Modality.WINDOW_MODAL);
			popupStage.initOwner(primaryStage);
			
			AnchorPane popup = scorePopupLoader.load();
			Scene popupScene = new Scene(popup);
			popupStage.setScene(popupScene);
//			
//			 popupController = scorePopupLoader.getController();
//			popupController.setDialogStage(popupStage);
			
			primaryStage.setOnCloseRequest(evt -> {
				System.out.println("primaryStage() - Close");
				if( server != null ) {
					server.close();					
				}
				// evt.consume(); // 종료하지 않음.
				System.exit(0);
			});
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("앱 로딩중 오류 발생");
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void openPopup() {
		popupStage.show();
	}
	
	public void loadPage(String name) {
		MasterController c = controllerMap.get(name);
		Pane pane = c.getRoot();
		mainPage.getChildren().add(pane);
		
		pane.setTranslateX(-800);
		pane.setOpacity(0);
		
		Timeline timeline = new Timeline();
		KeyValue toRight = new KeyValue(pane.translateXProperty(), 0);
		KeyValue fadeIn = new KeyValue(pane.opacityProperty(), 1);
		
		KeyFrame frame = new KeyFrame(Duration.millis(500), toRight, fadeIn);
		
		timeline.getKeyFrames().add(frame);
		timeline.play();
	}
	
	public void slideOut(Pane pane) {
		Timeline timeline = new Timeline();
		KeyValue toRight = new KeyValue(pane.translateXProperty(), 800);
		KeyValue fadeOut = new KeyValue(pane.opacityProperty(), 0);
		
		KeyFrame frame = new KeyFrame(Duration.millis(500), (e) -> {
			mainPage.getChildren().remove(pane);
		}, toRight, fadeOut);
		
		timeline.getKeyFrames().add(frame);
		timeline.play();
	}
	
	public void makeLoad(String id, String title, String maxCount, String pwd) {
		server = new Server(title, Integer.parseInt(maxCount), pwd);
		server.start();	
		
		roomLoad(id, "127.0.0.1" + ":" + server.getPort());
	}
	
	public void roomLoad(String userName, String server) {	
		popupStage.close();
		RoomController rc2 = (RoomController) controllerMap.get("roomPage");
		rc2.init();
		
		Consumer<String> cbRecv = recv -> {
			((RoomController) controllerMap.get("roomPage")).addChatBox(recv);
			System.out.println(recv);
		};
		
		String[] serverInfos = server.split(":");
		client = new Client(serverInfos[0], Integer.parseInt(serverInfos[1]), userName, cbRecv);
		client.start();
	}
	
	public void sendText(String msg) {
		client.send(msg);
	}

	public UserVO getLoginUser() {
		return loginUser;
	}
	
	public void setLoginUser(UserVO user) {
		loginUser = user;
		controllerMap.get("main").setUserInfo();
	}
	
}
