package brokerJdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//TODO: 싱글턴 모델 공부
//singleton 으로 만들거임. 클래스의 객체가 하나만 만들어지도록 하는 것.
//이 클래스를 외부에서는 생성하지 못하도록 해줘야 함. 
public class ConnectionManager {
	//utility 클래스
	private static String url  = "jdbc:oracle:thin:@localhost:1521:XE";
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String user ="hr";
	private static String password = "hr";
	
	//로딩타임 때 객체가 생성되에 하려면? Static
	private static ConnectionManager cm = new ConnectionManager();
	
	private ConnectionManager(){} // 생성자를 외부에서 생성하지 못하는 것은 외부에서 생성하지 못하는 것
	
	public static ConnectionManager getInstance(){
		return cm;
	}
	//static initialiser 클래스 로딩타임 때 단 한 번 실행 됨. 
	
	static{
		try {
			Class.forName(driver); // driver 로딩 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static Connection getConnection(){
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void close(Connection con){
		try {
			if(con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
/*
 * ConnectionManager.getInstance() 로 가져다 쓰면 됨. 
 * connection 을 가져다 쓰려면 getConnectionManager.getConnection 씀
 * con close 해주려면 ConnectionManager.close()
 */