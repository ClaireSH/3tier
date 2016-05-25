package brokerJdbc.Exception;

public class RecordNotFoundException extends Exception{
	
	
	
	public RecordNotFoundException(String message) {
		super(message);
		//(id+ "님을 찾을 수 없습니다.");
	}

	public RecordNotFoundException() {
		super("레코드를 찾을 수 없습니다.");
		//throws 예외 회피
		// throw new RecordnotFoundException  던져주면 됨  
	}
	

}
