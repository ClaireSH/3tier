package brokerJdbc.vo;

import java.io.Serializable;

public class Customer implements Serializable {
		private String ssn;
		private String cust_name;
		private String address;
		
		
		public Customer(String ssn, String cust_name, String address) {
			super();
			this.ssn = ssn;
			this.cust_name = cust_name;
			this.address = address;
		}
		public String getSsn() {
			return ssn;
		}
		public void setSsn(String ssn) {
			this.ssn = ssn;
		}
		public String getCust_name() {
			return cust_name;
		}
		public void setCust_name(String cust_name) {
			this.cust_name = cust_name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		@Override
		public String toString() {
			return ssn +" "+ cust_name ;
		}
		
	
}
