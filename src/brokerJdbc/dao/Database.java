package brokerJdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import brokerJdbc.Exception.DuplicateIDException;
import brokerJdbc.Exception.InvalidTransactionException;
import brokerJdbc.Exception.RecordNotFoundException;
import brokerJdbc.vo.Customer;
import brokerJdbc.vo.Shares;
import brokerJdbc.vo.Stock;

//업무로직을 담는 메소드
public class Database {
	/**
	 * custommer 테이블에서 SSN의 존재 유무를 확인
	 * @return 매개변수로 주어진 ssn이 존재하면 true 를 그렇지 않으면 false를 반환
	 * @param ssn 존재 유무를 확인하고자 하는 고객의 ssn
	 * javadoc 로 컴파일 하면 api document가 만들어짐. 
	 * @throws SQLException 
	 * */
	public boolean ssnExists(String ssn) throws SQLException{
		boolean result = false;
		//select 
		Connection con = ConnectionManager.getConnection();
		
		Statement stmt = null;
		//PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet sn =null;
		try {
			stmt = con.createStatement();
			//String sql = "select ssn from customer where ssn =?";
			String sql = "SELECT * FROM customer WHERE ssn = '"+ssn+"'";
			sn = stmt.executeQuery(sql);
			//ResultSet rs = 
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		
		
		
		
		
		return result;
	}
	
	
	 public boolean ssnExist(String ssn) {
	      boolean result = false;
	      // Customer cus = new Customer();
	      /**
	       * 커넥션 얻어오기
	       * SQL문 String에 넣기
	       * PreparedStatement 에 con.prepareStatement(sql);
	       * pstat.setString(1, ~~);
	       * int row = pstat.executeUpdate(); 
	       * 
	       */
	      Connection con = ConnectionManager.getConnection();
	      String sql = "Select ssn from customer where ssn = ?";

	      try {
	         PreparedStatement pstat = con.prepareStatement(sql);
	        // pstat.setString(1, "customer");
	         pstat.setString(1, ssn);//TODO ssn 그냥 넣어도 되던데 난 왜 

//	         ResultSet rs = pstat.executeQuery(sql);
//	         result = rs.next();
//
	          int row = pstat.executeUpdate();//반환값은 int이다
	          System.out.println(row +"개 레코드 있습니다.");
	          if(row==1){
	        	  result = true;
	          }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         ConnectionManager.close(con);
	      }
	      return result;

	   }
	/**
	 * 매개 변수로 주어진 ssn 에 해당하는 고객을 조회하여 반환
	 * @param ssn - 조회하고자 하는 고객의 ssn
	 * @return 조회 결과를 갖는 Customer 객체
	 * @throws RecordNotFoundException 조회하고자 하는 고객의 ssn이 존재하지 안을 경우 발생
	 * @throws SQLException 
	 */
	 /**
	  * driver 로딩
      * 커넥션 얻어오기
      * SQL문 String에 넣기
      * PreparedStatement 에 con.prepareStatement(sql);
      * pstat.setString(1, ~~);
      * int row = pstat.executeUpdate(); 
      * 
      */
	public Customer getCustomer(String ssn) throws RecordNotFoundException, SQLException{
		if(!ssnExist(ssn)) throw new RecordNotFoundException();
		Customer c = null ;
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT * FROM customer WHERE ssn = ?";
		//예외처리 
		//1. 컴파일 타임 sql, IO, Interrupted ... 4가지   
		//2. 런타임 두 가지가 있다. 런타임 컴파일러가 체크 하지 않고 사용가자 필요시 예외처리  
		
		try{
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ssn); //****실행 전에 '?' 파라미터 지정!!!
		ResultSet rs = pstmt.executeQuery();
		//커서 최소 한 번 이동해야함.
		if(rs.next()){
			String cust_name = rs.getString("cust_name");
			String address = rs.getString("address");
			c = new Customer(ssn, cust_name , address);
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		return c;
	}
	
	public Stock getStock(String symbol)throws RecordNotFoundException{
		String price ="";
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT price FROM stock WHERE symbol = ?";
		try{
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, symbol);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
		price =	rs.getString("price");
			
		}else{
			throw new RecordNotFoundException();
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		
		Stock st = new Stock(symbol, price);
		return st;
	}
	
	
	/**
	 * 매개변수로 주어진 새로운 고객 정보를 등록한다.
	 * @param c 등록하고자 하는 새로운 고객 정보를 가지고 있는 Customer 객체
	 * @throws DuplicateIDException 등록하고자 하는 고객의 ssn이 이미 존재할 경우 발생
	 * 
	 * driver 로딩
     * 커넥션 얻어오기
     * SQL문 String에 넣기
     * PreparedStatement 에 con.prepareStatement(sql);
     * pstat.setString(1, ~~);
     * int row = pstat.executeUpdate(); 
     * 
     */
	public String addCustomer(Customer c)throws DuplicateIDException{
		//1. c 가 존재하면 에러 발생
		if(ssnExist(c.getSsn())) throw new DuplicateIDException();
		String result = null;
		Connection con = ConnectionManager.getConnection();
		String sql = "INSERT INTO customer VALUES(?,?,?)";
		try{
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, c.getSsn());
		pstmt.setString(2, c.getCust_name());
		pstmt.setString(3, c.getAddress());
		int row = pstmt.executeUpdate();
		System.out.println(row +"명 고객 정보 입력됨.");
		if(row ==1) result = row+"명 고객 정보 입력됨.";
		
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		return result;
		
	}
	/**
	 * 매개변수로 주어진 ssn에 해당하는 고객 정보 및 보유주식 정보를 삭제 
	 * 트랜잭션 처리 해줘야함. 
	 * @param ssn
	 * @throws RecordNotFoundException
	 */
	public String deleteCustomer(String ssn)throws RecordNotFoundException{
		if(!ssnExist(ssn)) throw new RecordNotFoundException();
		
		String rs= null;
		Connection con = ConnectionManager.getConnection();
		String sql = "DELETE FROM customer WHERE ssn = ?";
		String sql1 = "DELETE FROM shares WHERE ssn = ?";
		try{
		con.setAutoCommit(false);
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ssn);
		int row = pstmt.executeUpdate();
		System.out.println(row+"개 회원 정보 삭제");
		if(row!=0){rs = row+"명 고객 정보 삭제됨."; }
		//if(true) throw new SQLException();
		pstmt = con.prepareStatement(sql1);
		pstmt.setString(1, ssn);
		//int row = pstmt.executeUpdate();
		int row1 = pstmt.executeUpdate();
		//System.out.println(row+"개 레코드와 그 객체의 보유주식 "+row1+"개 삭제 됨.");
		
		System.out.println(row1+"개 보유주식 정보 삭제");
		con.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		return rs;
	}
	/**어떤 객체를 매번 새로 생성하고 버리기보다 효율적으로 관리하기 위해
	 *  Connection 계속 만들고 버리는 것은 소모적인 작업. 
	 *  Object pool: 일정수의 객체를 미리  생성해 모아 놓고 빌려줌. 버리지 않음. 
	 * 	
	 */
	/**
	 * 매개변수로 주어진 새로운 고객정보를 갱신한다. 
	 * @param c 새로운 고객 정보를 가지고 있는 Customer 객체
	 * @return 
	 * @throws RecordNotFoundException
	 */
	
	public String updateCustomer(Customer c)throws RecordNotFoundException{
	 if(!ssnExist(c.getSsn())) throw new RecordNotFoundException();
	 Connection con = ConnectionManager.getConnection();
	
	 String rs = null;
	 String sql = "UPDATE customer SET cust_name = ?, address =? WHERE ssn = ?";
	 try{
	 PreparedStatement pstmt = con.prepareStatement(sql);
	 pstmt.setString(1, c.getCust_name());
	 pstmt.setString(2, c.getAddress());
	 pstmt.setString(3, c.getSsn());
	 int row = pstmt.executeUpdate();
	 if(row !=0){
		 rs = "업데이트 성공";
	 }
	 System.out.println(row );
	 
	 }catch(SQLException e){
		 e.printStackTrace();
	 }finally{
		 ConnectionManager.close(con);
	 }
	return null;
	}
	/**
 	 * 매개변수로 주어진 ssn의 고객이 보유하고 있는 주식의 목록을 조회하여 반환
	 * @param ssn
	 * @return
	 * @throws RecordNotFoundException
	 */
	
	public ArrayList<Shares> getPortfolio(String ssn)throws RecordNotFoundException{
		//특정 고객이 보유하고 있는 모든 주식의 정보 
		if(!ssnExist(ssn)) throw new RecordNotFoundException();
		ArrayList<Shares> list = new ArrayList<>();
		Connection con = ConnectionManager.getConnection();
		String sql ="SELECT * FROM shares WHERE ssn= ?";
		try{
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, ssn);
		ResultSet rs = pstmt.executeQuery();
		Shares sh = null;
			while (rs.next()) {
			String symbol = rs.getString("symbol");
			String quantity = rs.getString("quantity");
			 sh = new Shares(ssn, symbol, quantity);
			list.add(sh);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		
		return list;
	}
	
	/**
	 * Customer 테이블에 등록된 모든 고객정보를 조회한다. 
	 * @return 등록되어 있는 모든 고객정보 목록
	 */
	
	public ArrayList<Customer> getAllCustomers(){
		ArrayList<Customer> list = new ArrayList<>();
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT * FROM customer";
		try{
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			String ssn = rs.getString("ssn");
			String cust_name = rs.getString("cust_name");
			String address = rs.getString("address");
		Customer c = new Customer(ssn, cust_name, address);
		list.add(c);
			
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		return list;
	}
	
	public ArrayList<Stock> getAllStocks(){
		ArrayList<Stock> list = new ArrayList<>();
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT * FROM Stock";
		try{
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			String symbol = rs.getString("symbol");
			String price = rs.getString("price");
		Stock st = new Stock(symbol, price );
		list.add(st);
			
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		
		return list;
		
	}
	
	public boolean existShares(Shares s){
		boolean result = false;
		Connection con = ConnectionManager.getConnection();
		String sql = "SELECT ssn FROM shares WHERE ssn = ? AND symbol= ?";
		try{
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, s.getSsn());
		pstmt.setString(2, s.getSymbol());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){
			result = true;
		}
		
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
		ConnectionManager.close(con);
		}
		return result;
	}
	
	
	
	/**
	 * 매개변수로 전달된 주식을 매입 (Shares 테이블에 레코드 입력)
	 * 매입하고자 하는 주식에 대한 보유 현황이 없을 경우에는 새로운 레코드를 삽입
	 * 이미 보유하고 있는 주식에 대한 추가 매입인 경우에는 레코드를 업데이트 한다. 
	 * @param s 매입하고자 하는 주식에 대한 정보를 갖는 Shares 객체 
	 * ssn,  symbol, quantity
	 */
	public String buyShares(Shares s){
		Connection con = ConnectionManager.getConnection();
		String sql;
		String rs=null;
		if(existShares(s)){// 존재 하면
		sql ="UPDATE shares SET quantity = quantity + ? WHERE ssn = ? AND symbol = ?";
		try{
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, s.getQuantity());
			stmt.setString(2, s.getSsn());
			stmt.setString(3, s.getSymbol());
			
			int row = stmt.executeUpdate();
			System.out.println(row +"개 정보 추가");
			if(row ==1){rs = row+"개 주식을 샀습니다. ";}
		}catch(SQLException e ){
				e.printStackTrace();
			}finally{
				ConnectionManager.close(con);
			}
		}else {//존재 하지 않으면
		sql = "INSERT into shares Values(?, ?, ? ) ";
		try{
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, s.getSsn());
		stmt.setString(2, s.getSymbol());
		stmt.setString(3, s.getQuantity());
		
		int row = stmt.executeUpdate();
		System.out.println(row +"개 정보 삽입");
		}catch(SQLException e ){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		
		}
		return rs;
	}
	/**
	 * 매개변수로 전달된 보유주식을 매도한다. 
	 * - 보유하고 있는 주식보다 적은 수량을 매도하는 경우에는 보유수량을 차감 후 업데이트
	 * - 보유하고 있는 주식과 같은 수량(전량)을 매도하는 경우에는 포트폴리오 삭제 (Shares 테이블에 해당 레코드를 삭제)
	 * - 보유하고 있는 주식보다 많은 수량을 매도하고자 하는 경우에는 예외를 발생
	 * - 매도하려고 하는 주식에 대한 정보를 가지고 있지 않은 경우에도 예외를 발생
	 * 
	 * @param s 매도하고자 하는 주식에 대한 정보를 갖는 Shares 객체
	 * @throws InvalidTransactionException 보유 수량보다 많은 수량을 매도하고자 하거나 또는 
	 * 보유하고 있지도 않은 경우 발생
	 */
	@SuppressWarnings("resource")
	public String sellShares(Shares s)throws InvalidTransactionException, RecordNotFoundException{
		Connection con = ConnectionManager.getConnection();
		String res = null;
		String sql ="SELECT quantity FROM shares WHERE ssn = ? AND symbol = ? ";
		try{
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, s.getSsn());
		stmt.setString(2, s.getSymbol());
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
		String quan =	rs.getString("quantity");
		int ownQuan = Integer.parseInt(quan);
		int getQuan = Integer.parseInt(s.getQuantity());
		
		if(ownQuan > getQuan){
		sql = "UPDATE shares SET quantity = quantity - ? WHERE ssn = ? AND symbol = ?";
		stmt = con.prepareStatement(sql);
		stmt.setString(1, s.getQuantity());
		stmt.setString(2, s.getSsn());
		stmt.setString(3, s.getSymbol());
		int row = stmt.executeUpdate();
		res = row+"개 정보가 update 되었습니다.";
		System.out.println(row + "개 정보가 update 되었습니다.");
		}else if(ownQuan == getQuan){
		sql = "DELETE FROM SHARES WHERE ssn = ? AND symbol = ?";	
		stmt = con.prepareStatement(sql);
		stmt.setString(1, s.getSsn());
		stmt.setString(2, s.getSymbol());
		int row  = stmt.executeUpdate();
		res = row + "개 정보를 shares 테이블에서 삭제헀습니다.";
		System.out.println(row + "개 정보를 shares 테이블에서 삭제");
		}else if(ownQuan < getQuan){
			//throw new InvalidTransactionException();
		}
		}else{
		//	throw new RecordNotFoundException();
		}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ConnectionManager.close(con);
		}
		return res;
	} 
	
	
public static void main(String[] args) throws SQLException, RecordNotFoundException, DuplicateIDException, InvalidTransactionException {
//	Database db = new Database();
//	System.out.println(db.ssnExist("111-111"));
//	System.out.println(db.getCustomer("111-111"));
//	Customer c = new Customer("111-120","김진홍","여의도");
//	db.addCustomer(c);
//	db.deleteCustomer("111-120");
	
//	db.updateCustomer(c);
//	ArrayList<Shares> list =  db.getPortfolio("111-112");
//	for(Shares sh : list){
//		System.out.println(sh);
//	}
//	ArrayList<Customer> culist = db.getAllCustomers();
//	for(Customer c : culist){
//		System.out.println(c.toString());
//	}
//	ArrayList<Stock> stlist = db.getAllStocks();
//	for(Stock st : stlist){
//		System.out.println(st.toString());
//	}
//	Shares s = new Shares("111-111","SUNW","10");
//	db.buyShares(s);
//	db.sellShares(s);
	
	
}
}

