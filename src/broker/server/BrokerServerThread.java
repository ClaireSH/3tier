package broker.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import broker.protocol.Command;
import brokerJdbc.Exception.DuplicateIDException;
import brokerJdbc.Exception.InvalidTransactionException;
import brokerJdbc.Exception.RecordNotFoundException;
import brokerJdbc.dao.Database;
import brokerJdbc.vo.Customer;
import brokerJdbc.vo.Shares;
import brokerJdbc.vo.Stock;

public class BrokerServerThread implements Runnable {
	//넘겨 받은거 저장 할 곳!
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private boolean exit;
	private Database db;
	
	public BrokerServerThread(ObjectInputStream ois, ObjectOutputStream oos) {
		super();
		this.ois = ois;
		this.oos = oos;
		db = new Database();
	}
/**
 * readObject()가 무한루프로 command 객체 읽음. 
 * 1. 명령어 상수 값 읽기 
 * 2. 그에 따라 분기 처리 Switch
 * 3. 
 * 
 */
	@Override
	public void run(){
		while(!exit){//사용자 socket 끊으면 익셉션 터짐, 플래그변수로 관리
			try {
			Command cmd = (Command)ois.readObject();
			Customer c;
			String ssn;
			switch(cmd.getCmdValue()){
			case Command.ADD_CUSTOMER: 
				System.out.println("serverThread ADD");
			//ois 로 읽어와서 처리하고 다시 결과 보내주기 
				c = (Customer)cmd.getArgs()[0];
				try {
					String rs = db.addCustomer(c);
					cmd.setResult(rs);
					cmd.setStatus(-20);
					oos.writeObject(cmd);
				} catch (DuplicateIDException e) {
					e.printStackTrace();
				}
				break;
		
			case Command.DELETE_CUSTOMER:
				System.out.println("serverThread Delete cus");

				ssn = (String)cmd.getArgs()[0];
				try {
					String rs = db.deleteCustomer(ssn);
					cmd.setResult(rs);
					cmd.setStatus(-10);
					oos.writeObject(cmd);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				}
				
				break;
			case Command.UPDATE_CUSTOMER:
				System.out.println("serverThread update");

				c = (Customer)cmd.getArgs()[0];
				try {
					String rs = db.updateCustomer(c);
					cmd.setResult(rs);
					cmd.setStatus(-10);
					oos.writeObject(cmd);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case Command.GET_PORTFOLIO:
				System.out.println("serverThread getPortfolio");

				ssn = (String)cmd.getArgs()[0];
				try {
					ArrayList<Shares> sharesList = db.getPortfolio(ssn);
					cmd.setResult(sharesList);
					cmd.setStatus(-10);
					oos.writeObject(cmd);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				}
				break;
			case Command.GET_ALL_CUSTOMERS:
				System.out.println("serverThread get all cus");

				ArrayList<Customer> cusList = db.getAllCustomers();
				cmd.setResult(cusList);
				oos.writeObject(cmd);
				break;
			case Command.GET_ALL_STOCKS:
				System.out.println("serverThread Get All stocks");

				ArrayList<Stock> stocList = db.getAllStocks();
				cmd.setResult(stocList);
				oos.writeObject(cmd);
				break;
			case Command.GET_CUSTOMER:
				System.out.println("serverThread get customer");

				ssn = (String)cmd.getArgs()[0];
				try {
					c = db.getCustomer(ssn);
					cmd.setResult(c);
					cmd.setStatus(-10);
					oos.writeObject(cmd);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case Command.GET_STOCK://100
				ssn = (String)cmd.getArgs()[0];
				try {
					Stock st = db.getStock(ssn);
					cmd.setResult(st);
					cmd.setStatus(-10);
					oos.writeObject(cmd);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				}
			
				break;
			case Command.BUY_SHARES:
				Shares shs = (Shares)cmd.getArgs()[0];
				String rs = db.buyShares(shs);
				cmd.setResult(rs);
				cmd.setStatus(-10);
				oos.writeObject(cmd);
			
				break;
			case Command.SELL_SHARES:  
				Shares s = (Shares)cmd.getArgs()[0];
			try{
				String res = db.sellShares(s);
				cmd.setResult(res);
				cmd.setStatus(-10);
				oos.writeObject(cmd);
			}catch(InvalidTransactionException e){
				throw e;
			}
				break;
				default:
					break;
			}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				exit = true;
			}//catch
			catch (InvalidTransactionException e) {
				e.printStackTrace();
			} catch (RecordNotFoundException e) {
				e.printStackTrace();
			}
		
			
		}//while
	}//run

}
