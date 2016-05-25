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
public interface DatabaseIn {
	/**
	 * custommer 테이블에서 SSN의 존재 유무를 확인
	 * @return 매개변수로 주어진 ssn이 존재하면 true 를 그렇지 않으면 false를 반환
	 * @param ssn 존재 유무를 확인하고자 하는 고객의 ssn
	 * javadoc 로 컴파일 하면 api document가 만들어짐. 
	 * @throws SQLException 
	 * */
	public boolean ssnExists(String ssn) throws SQLException;
	
	
	 public boolean ssnExist(String ssn);
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
	public Customer getCustomer(String ssn) throws RecordNotFoundException, SQLException;
	
	public Stock getStock(String symbol)throws RecordNotFoundException;
	
	
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
	public void addCustomer(Customer c)throws DuplicateIDException;
	/**
	 * 매개변수로 주어진 ssn에 해당하는 고객 정보 및 보유주식 정보를 삭제 
	 * 트랜잭션 처리 해줘야함. 
	 * @param ssn
	 * @throws RecordNotFoundException
	 */
	public void deleteCustomer(String ssn)throws RecordNotFoundException;
	/**어떤 객체를 매번 새로 생성하고 버리기보다 효율적으로 관리하기 위해
	 *  Connection 계속 만들고 버리는 것은 소모적인 작업. 
	 *  Object pool: 일정수의 객체를 미리  생성해 모아 놓고 빌려줌. 버리지 않음. 
	 * 	
	 */
	/**
	 * 매개변수로 주어진 새로운 고객정보를 갱신한다. 
	 * @param c 새로운 고객 정보를 가지고 있는 Customer 객체
	 * @throws RecordNotFoundException
	 */
	
	public void updateCustomer(Customer c)throws RecordNotFoundException;
	/**
 	 * 매개변수로 주어진 ssn의 고객이 보유하고 있는 주식의 목록을 조회하여 반환
	 * @param ssn
	 * @return
	 * @throws RecordNotFoundException
	 */
	
	public ArrayList<Shares> getPortfolio(String ssn)throws RecordNotFoundException;
	
	/**
	 * Customer 테이블에 등록된 모든 고객정보를 조회한다. 
	 * @return 등록되어 있는 모든 고객정보 목록
	 */
	
	public ArrayList<Customer> getAllCustomers();
	
	public ArrayList<Stock> getAllStocks();
	
	public boolean existShares(Shares s);
	
	
	
	/**
	 * 매개변수로 전달된 주식을 매입 (Shares 테이블에 레코드 입력)
	 * 매입하고자 하는 주식에 대한 보유 현황이 없을 경우에는 새로운 레코드를 삽입
	 * 이미 보유하고 있는 주식에 대한 추가 매입인 경우에는 레코드를 업데이트 한다. 
	 * @param s 매입하고자 하는 주식에 대한 정보를 갖는 Shares 객체 
	 * ssn,  symbol, quantity
	 */
	public void buyShares(Shares s);
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
	public void sellShares(Shares s)throws InvalidTransactionException, RecordNotFoundException; 
	
	

}

