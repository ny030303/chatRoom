package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.control.Alert.AlertType;

public class JDBCUtil {
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("드라이버 파일을 찾을 수 없습니다.");
			return null;
		}
		// 데이터베이스명, 유저명, 비밀번호 변경
		String connectionString = "jdbc:mysql://gondr.asuscomm.com/yy_10107" + "?useUnicode=true"
				+ "&characterEncoding=utf8" + "&useSSL=false" + "&serverTimezone=Asia/Seoul";
		String userId = "yy_10107";
		String password = "jj7323751";

		Connection con = null;

		try {
			con = DriverManager.getConnection(connectionString, userId, password);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("DB 연결중 오류 발생");
		}

		return con;
	}
	
	
	public static int sqlMafiaRoom(String sql) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int rs = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeUpdate();
			return rs;

		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 처리중 오류 발생 인터넷 연결 확인", AlertType.ERROR);
		} finally {
			close(pstmt);
			close(conn);
		}
		return 0;
	}
	
	public static void deleteMafiaRoom(String server) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		int rs = 0;
		try {
			pstmt = conn.prepareStatement("delete from mafia_rooms where server = '" + server + "'");
			rs = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Util.showAlert("에러", "데이터베이스 처리중 오류 발생 인터넷 연결 확인", AlertType.ERROR);
		} finally {
			close(pstmt);
			close(conn);
		}		
	}

	public static List<Object> fetchAll(String qry, Function<ResultSet, Object> function) {
		List<Object> retObjects = new ArrayList<Object>();
		PreparedStatement statement = null;
		ResultSet resultset = null;
		Connection dbConn = getConnection();
		if (dbConn != null) {
			try {
				statement = (PreparedStatement) dbConn.prepareStatement(qry);
				resultset = statement.executeQuery();
				while (resultset.next()) {
					Object applyObject = function.apply(resultset);
					if (applyObject != null)
						retObjects.add(applyObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close(resultset);
				close(statement);
			}
		}
		return retObjects;
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
		}
	}

	public static void close(PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
		}
	}

	public static void close(Connection co) {
		try {
			if (co != null)
				co.close();
		} catch (Exception e) {
		}
	}
}