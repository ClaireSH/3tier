package broker.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

import broker.protocol.Command;
import brokerJdbc.Exception.DuplicateIDException;
import brokerJdbc.Exception.InvalidTransactionException;
import brokerJdbc.Exception.RecordNotFoundException;
import brokerJdbc.dao.DatabaseIn;
import brokerJdbc.vo.Customer;
import brokerJdbc.vo.Shares;
import brokerJdbc.vo.Stock;

/**
 * 
 * @author SCMaster
 *	DB 모든 메소드 선언
 *	Database 객체를 broker client로 바꿔주고 input output 만들기
 */
public class BrokerClient implements DatabaseIn {
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public BrokerClient(){
		try {
			Socket socket = new Socket("127.0.0.1", 7777);
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}//catch
		
	}//constructor
	@Override
	public boolean ssnExists(String ssn) throws SQLException {
		//Cmd에 담아서 보내고 처리 결과 받아오기 
		return false;
	}
	@Override
	public boolean ssnExist(String ssn) {
		return false;
	}
	@Override
	public Customer getCustomer(String ssn) throws RecordNotFoundException, SQLException {
		//검색 메소드 
		Customer cu = null;
		Command cmd = new Command(Command.GET_CUSTOMER);
		cmd.setArgs(ssn);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		if(recmd.getResult() !=null)cu = (Customer) recmd.getResult();
		if(recmd.getResult() == null){
		throw new RecordNotFoundException("요청하신 고객의 정보를 찾을 수 없습니다.");}
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cu;
	}
	@Override
	public Stock getStock(String symbol) throws RecordNotFoundException {
	//symbol 
		Stock st = null;
		Command cmd = new Command(Command.GET_STOCK);
		cmd.setArgs(symbol);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		if(recmd.getResult() !=null)st = (Stock) recmd.getResult();
		if(recmd.getResult() == null){
		throw new RecordNotFoundException("요청하신 고객의 정보를 찾을 수 없습니다.");}
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return st;
	}
	@Override
	public void addCustomer(Customer c) throws DuplicateIDException {
		//customer 추가 메소드 
		Command cmd = new Command(Command.ADD_CUSTOMER);
		cmd.setArgs(c);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		if(recmd.getResult() == null){
		int status = recmd.getStatus();//-20 이다.
		throw new DuplicateIDException("동일한 SSN이 이미 존재합니다.");
		}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deleteCustomer(String ssn) throws RecordNotFoundException {
		//delete Customer 메소드
		Command cmd = new Command(Command.DELETE_CUSTOMER);
		cmd.setArgs(ssn);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		if(recmd.getResult() == null){
		int status = recmd.getStatus();//-10 이다.
		throw new RecordNotFoundException("요청하신 고객의 정보를 찾을 수 없습니다.");
	
		}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateCustomer(Customer c) throws RecordNotFoundException {
		//수정 메소드 
		Command cmd = new Command(Command.UPDATE_CUSTOMER);
		cmd.setArgs(c);
		try {
			oos.writeObject(cmd);
		ois.readObject();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public ArrayList<Shares> getPortfolio(String ssn) throws RecordNotFoundException {
		// Getportfolio 메소드 ssn 을 보내주면 배열을 받아와서 return
				ArrayList<Shares> shList = null;
				Command cmd = new Command(Command.GET_PORTFOLIO);
				cmd.setArgs(ssn);
				try {
					oos.writeObject(cmd);
				Command recmd = (Command)ois.readObject();
				shList = (ArrayList<Shares>) recmd.getResult();
				if(recmd.getResult() == null){
				int status = recmd.getStatus();//-10 이다.
				}
				
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
		return shList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Customer> getAllCustomers() {
	//모든 고객의 customer 객체를 배열에 담아서 가지고 온다. 예외는 없음
		ArrayList<Customer> cusList = null;
		Command cmd = new Command(Command.GET_ALL_CUSTOMERS);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		cusList = (ArrayList<Customer>) recmd.getResult();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cusList;
	}
	@Override
	public ArrayList<Stock> getAllStocks() {
		ArrayList<Stock> stocList = null;
		Command cmd = new Command(Command.GET_ALL_STOCKS);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		stocList = (ArrayList<Stock>) recmd.getResult();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return stocList;
	}
	@Override
	public boolean existShares(Shares s) {
		ArrayList<Stock> stocList = null;
		Command cmd = new Command(Command.GET_ALL_CUSTOMERS);
		try {
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		stocList = (ArrayList<Stock>) recmd.getResult();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	@Override
	public void buyShares(Shares s) {
		Command cmd = new Command(Command.BUY_SHARES);
		try {
			cmd.setArgs(s);
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		String rs = (String) recmd.getResult();
		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void sellShares(Shares s) throws InvalidTransactionException, RecordNotFoundException {
		Command cmd = new Command(Command.SELL_SHARES);
		try {
			cmd.setArgs(s);
			oos.writeObject(cmd);
		Command recmd = (Command)ois.readObject();
		String rs = (String) recmd.getResult();
		if (rs== null){
			throw new InvalidTransactionException();
		}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
	
