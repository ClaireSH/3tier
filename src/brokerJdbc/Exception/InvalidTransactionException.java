package brokerJdbc.Exception;
//보유 주식 보다 많이 팔려고 할 경우

public class InvalidTransactionException extends Exception{

	public InvalidTransactionException() {
		super("부적절한 거래가 이뤄졌습니다.");
	}

	public InvalidTransactionException(String message) {
		super(message);
	}
 
}
