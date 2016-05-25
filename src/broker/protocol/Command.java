package broker.protocol;

import java.io.Serializable;
/**
 * Broker 프로그램에서 서버와 클라이언트 사이에 요청과 응답을 처리할 클래스 
 * 요청 시 필요한 요청 명령상수와 필요한 매개변수 값을 저장하여 서버에 전달되고
 * 서버에서는 요청을 처리 후 반환되는 결과 및 처리상태 (예외)를 저장하여 클라이언
 * 트에게 재전송 된다. 
 * @author SCMaster
 *
 */
import java.util.ArrayList;
public class Command implements Serializable{
	private int cmdValue; //요청을 구분할 명령상수값을 저장
	private Object[] args ={""};//요청을 처리할 때 필요한 매개변수값을 저장
	
	private Object result ;//서버에서 처리된 결과를 저장
	private int status; //서버에서 처리된 상태값을 저장 
	
	public static final int ADD_CUSTOMER = 10; //상수로 지정해 버림, 클래스 이름으로 바로 부를 수 있음. 
	public static final int DELETE_CUSTOMER = 20;
	public static final int UPDATE_CUSTOMER = 30;
	public static final int GET_CUSTOMER = 40;
	public static final int GET_ALL_CUSTOMERS = 50;
	public static final int GET_ALL_STOCKS = 60;
	public static final int BUY_SHARES= 70;
	public static final int SELL_SHARES =80;
	public static final int GET_PORTFOLIO = 90;
	public static final int GET_STOCK = 100;
	
	
	public static final int RECORD_NOTFOUND =-10;
	public static final int DUPLICATED_ID = -20;
	public static final int INVALID_TRANSACTION = -30;
	//STATUS 가 0보다 크면 정상 처리, 음수면 오류
	
	public Command(int cmdValue){
		this.cmdValue = cmdValue;
		
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object args) {
		this.args[0] = args;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCmdValue() {
		return cmdValue;
	}

}
