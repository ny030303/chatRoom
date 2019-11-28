package views;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupController implements Initializable {
//	@FXML
//	private ComboBox peopleCntBox;
	
	@FXML
	private TextField roomTitle;
	
	@FXML
	private ComboBox people;

	@FXML
	private TextField roomPwd;
	
	private Stage me;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		people.getItems().addAll("5", "10", "15", "20");
	}
	
	
	public void setDialogStage(Stage me) {
		this.me = me;
	}
	
//	
//	public void setScore(int score) {
//		this.score = score;
//		lblScore.setText("당신의 점수는 " + score + "입니다.");
//	}
	
	public void record() {
//		if(title.getText().isEmpty()) {
//			Util.showAlert("재확인", "제목을 적어주세요.", AlertType.ERROR);
//			return;
//		}
//		
		System.out.println("------- record");
		
//		Main.app.slideOut(Main.app.mainPage);
		
		Main.app.makeLoad("master", roomTitle.getText(), people.getValue().toString(), roomPwd.getText());
		Main.app.loadPage("roomPage");
		
//		Connection con = JDBCUtil.getConnection(); //DB연결 가져오기
//		PreparedStatement pstmt = null;
//		String sql = "INSERT INTO  (`name`, `score`) VALUES(?, ?)";
//		try {
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, txtName.getText());
//			pstmt.setInt(2, score);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			System.out.println("데이터베이스 쿼리중 오류 발생");
//			e.printStackTrace();
//		} finally {
//			JDBCUtil.close(pstmt);
//			JDBCUtil.close(con);
//		}
//		
//		
//		this.me.close();
	}

}
