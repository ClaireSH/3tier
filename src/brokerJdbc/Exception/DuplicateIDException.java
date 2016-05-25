package brokerJdbc.Exception;
// id 중복 등록 막음
public class DuplicateIDException extends Exception{

	public DuplicateIDException(String message) {
		super(message);
	}

	public DuplicateIDException() {
		super("동일한 ID의 레코드가 존재합니다.");
	}
	

}
