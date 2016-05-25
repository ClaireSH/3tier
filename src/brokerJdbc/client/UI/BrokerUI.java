package brokerJdbc.client.UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import broker.client.BrokerClient;
import brokerJdbc.Exception.DuplicateIDException;
import brokerJdbc.Exception.InvalidTransactionException;
import brokerJdbc.Exception.RecordNotFoundException;
import brokerJdbc.dao.Database;
import brokerJdbc.vo.Customer;
import brokerJdbc.vo.Shares;
import brokerJdbc.vo.Stock;

import javax.swing.border.BevelBorder;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.FlowLayout;

public class BrokerUI extends JFrame implements ActionListener {

	private JFrame frame;
	private JTextField tf_stock;
	private JTextField tf_quantity;
	private JTextField tf_name;
	private JTextField tf_ssn;
	private JTextField tf_symbol;
	private JTextField tf_price;
	private DefaultListModel<Customer> dflm1;
	private DefaultListModel<Shares> dflm;
	private DefaultListModel<String> dflm3;
	//private Database db;
	private JButton btn_add;
	private JButton btn_delete;
	private JButton btn_update;
	private JButton btn_apply;
	private JButton btn_cancel;
	private JButton btn_buy ;
	private JButton btn_sell;
	private JButton btn_gcsp;
	private JTextArea ta_address;
	private String cm;
	private String scssn="";
	private String cu;
	private Customer cus;
	private JList<Customer> list_1;
	private BrokerClient db;
	
	
	public static void main(String[] args) {
		BrokerUI window =null;
				try {
					window = new BrokerUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					window.showMessage(e.getMessage());
				}
			}
		

	public BrokerUI() {
		db = new BrokerClient();  
		initialize();
		showAllStocks();
		initCustomerButton(true);
		showAllCustomer();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(150, 300,800, 344);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 51, 153));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_4.setForeground(new Color(102, 153, 255));
		panel_2.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_12 = new JPanel();
		panel_12.setForeground(new Color(102, 153, 255));
		panel_4.add(panel_12, BorderLayout.NORTH);
		
		JLabel lblCustomerInformation = new JLabel("Customer Information");
		panel_12.add(lblCustomerInformation);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBackground(new Color(102, 153, 51));
		panel_13.setForeground(new Color(153, 204, 255));
		panel_4.add(panel_13);
		
		btn_add = new JButton("ADD");
		btn_add.addActionListener(this);
		btn_add.setActionCommand("add");
		
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tf_name.setText("");
				tf_ssn.setText("");
				ta_address.setText("");
			}
		});
		panel_13.add(btn_add);
		
		btn_delete = new JButton("DELETE");
		btn_delete.addActionListener(this);
		btn_delete.setActionCommand("delete");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_13.add(btn_delete);
		
		btn_update = new JButton("UPDATE");
		panel_13.add(btn_update);
		btn_update.addActionListener(this);
		btn_update.setActionCommand("update");
		
		
		btn_apply = new JButton("apply");
		btn_apply.setEnabled(false);
		btn_apply.setActionCommand("apply");
		btn_apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//name tf 에 뭔가 있을 때
				//add를 누른 후 입력하고 apply 를 누를 경우> textfield에 입력한 거 가져와서 객체생성후 insert 하기
				if(cm.equals("add")){
				try{
				String name = tf_name.getText();
				String ssn = tf_ssn.getText();
				String address = ta_address.getText();
				Customer c = new Customer(ssn, name, address);
				try {
					db.addCustomer(c);
				} catch (DuplicateIDException e1) {
					e1.printStackTrace();
					showMessage(e1.getMessage());
				}}catch(NullPointerException e2){
					System.out.println("칸이 비어있습니다!");
					showMessage(e2.getMessage());
				}
				initCustomerButton(true);
				showAllCustomer();
				videTF();
				}else if(cm.equals("delete")){
					int option = JOptionPane.showConfirmDialog(null, scssn+" 고객 정보를 삭제하겠습니까?","고객삭제",JOptionPane.YES_NO_OPTION);
					String ssn = tf_ssn.getText();
					try {
						db.deleteCustomer(ssn);
					} catch (RecordNotFoundException e1) {
						e1.printStackTrace();
						showMessage(e1.getMessage());
					}
					initCustomerButton(true);
					showAllCustomer();
					videTF();
				}else if(cm.equals("update")){
					String name = tf_name.getText();
					String ssn = tf_ssn.getText();
					String address = ta_address.getText();
					Customer c = new Customer(ssn, name, address);
					try {
						db.updateCustomer(c);
					} catch (RecordNotFoundException e1) {
						e1.printStackTrace();
						showMessage(e1.getMessage());
					}
					initCustomerButton(true);
					videTF();
					showAllCustomer();
				}
			}
		});
		panel_13.add(btn_apply);
		
		btn_cancel = new JButton("cancel");
		btn_cancel.setEnabled(false);
		panel_13.add(btn_cancel);
		btn_cancel.setActionCommand("cancel");
		
		
		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_7 = new JPanel();
		panel_5.add(panel_7, BorderLayout.WEST);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_9.getLayout();
		flowLayout.setVgap(20);
		panel_9.setBackground(new Color(204, 153, 102));
		panel_7.add(panel_9, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Name");
		panel_9.add(lblNewLabel);
		
		tf_name = new JTextField();
		tf_name.setEditable(false);
		panel_9.add(tf_name);
		tf_name.setToolTipText("30");
		tf_name.setColumns(10);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(new Color(204, 153, 51));
		panel_7.add(panel_10, BorderLayout.CENTER);
		
		JLabel lblBbn = new JLabel("SSN");
		panel_10.add(lblBbn);
		
		tf_ssn = new JTextField();
		tf_ssn.setEditable(false);
		panel_10.add(tf_ssn);
		tf_ssn.setColumns(10);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBackground(new Color(153, 102, 0));
		panel_7.add(panel_11, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_1 = new JLabel("Address");
		panel_11.add(lblNewLabel_1);
		
		ta_address = new JTextArea();
		ta_address.setEditable(false);
		ta_address.setColumns(10);
		ta_address.setRows(3);
		ta_address.setTabSize(30);
		panel_11.add(ta_address);
		
		JPanel panel_8 = new JPanel();
		panel_5.add(panel_8, BorderLayout.CENTER);
		
		dflm = new DefaultListModel<>();
		panel_8.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new LineBorder(new Color(75, 0, 130), 2));
		panel_16.setBackground(new Color(102, 102, 153));
		panel_8.add(panel_16);
		panel_16.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(178, 193, -175, -165);
		panel_16.add(scrollPane);
		
		JLabel lblNewLabel_2 = new JLabel("Stock Portfolio");
		lblNewLabel_2.setFont(new Font("Script MT Bold", Font.BOLD, 12));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(0, 0, 180, 24);
		panel_16.add(lblNewLabel_2);
		
		JList list = new JList();
		list.setBounds(20, 22, 153, 160);
		panel_16.add(list);
		list.setModel(dflm);
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int ind = list.getSelectedIndex();
				Shares cu =dflm.getElementAt(ind);
				System.out.println(cu);
				String symbol = cu.getSymbol();
				tf_stock.setText(symbol);
				tf_quantity.setEditable(true);
				btn_sell.setEnabled(true);

				
			}
		});

				
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new LineBorder(new Color(218, 165, 32), 2));
		panel_17.setBackground(new Color(153, 153, 204));
		panel_8.add(panel_17);
		
		JLabel lblNewLabel_3 = new JLabel("All Customers");
		lblNewLabel_3.setFont(new Font("Script MT Bold", Font.BOLD, 12));
		panel_17.add(lblNewLabel_3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_17.add(scrollPane_1);
		
		list_1 = new JList<>();
		list_1.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(204, 255, 0), null, null, null));
		//panel_15.add(list_1, BorderLayout.WEST);
		scrollPane_1.setViewportView(list_1);
		dflm1 = new DefaultListModel<>();
		list_1.setModel(dflm1);
		list_1.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				tf_quantity.setText("");
				btn_buy.setEnabled(false);
				btn_sell.setEnabled(false);
				dflm.clear();
				int ind = list_1.getSelectedIndex();
				cus =dflm1.getElementAt(ind);
				System.out.println(cus);
				scssn = cus.getSsn();
				tf_name.setText(cus.getCust_name());
				tf_ssn.setText(cus.getSsn());
				ta_address.setText(cus.getAddress());
				try {
					ArrayList<Shares> shareList  = db.getPortfolio(scssn);
					for(Shares sh:shareList){
					dflm.addElement(sh);
					
					}
				} catch (RecordNotFoundException e1) {
					e1.printStackTrace();
					showMessage(e1.getMessage());
				}
			}
			
			
		});
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(102, 153, 204));
		panel_2.add(panel_6, BorderLayout.SOUTH);
		
		btn_buy = new JButton("Buy");
		btn_buy.setEnabled(false);
		btn_buy.setFont(new Font("굴림", Font.BOLD, 13));
		btn_buy.setForeground(new Color(255, 51, 0));
		panel_6.add(btn_buy);
		
		btn_buy.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String quantity = tf_quantity.getText();
				String symbol = tf_stock.getText();
				Shares shares = new Shares(scssn, symbol, quantity);
				db.buyShares(shares);
				showPortfolio();
				
			}
			
			
		});
	
		tf_stock = new JTextField();
		tf_stock.setEditable(false);
		panel_6.add(tf_stock);
		tf_stock.setColumns(10);
		
		tf_quantity = new JTextField();
		tf_quantity.setEditable(false);
		panel_6.add(tf_quantity);
		tf_quantity.setColumns(10);
		tf_quantity.getDocument().addDocumentListener(new DocumentListener() {
	         @Override
	         public void changedUpdate(DocumentEvent arg0) {
	        	  
	        	 String st =null;
				try {
					st = arg0.getDocument().getText(0, arg0.getDocument().getLength());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println("Change VAlue Inserted! Length: "+arg0.getDocument().getLength());
	            if(Pattern.matches("^[0-9]+$", st)){
	            	System.out.println("숫자");
	            	}else{
	            	if(!st.equals("")){
						System.out.println("숫자 아님");
						JOptionPane.showMessageDialog(null , "숫자만 입력해 주세요.");
							}
	            	}
	         }
	         @Override
	         public void insertUpdate(DocumentEvent arg0) {
	          	  
	        	 String st =null;
				try {
					st = arg0.getDocument().getText(0, arg0.getDocument().getLength());
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            System.out.println("insert VAlue Inserted! Length: "+arg0.getDocument().getLength());
	            if(Pattern.matches("^[0-9]+$", st)){
	            	System.out.println("숫자");
	            	}else{
	            		if(!st.equals("")){
	            	System.out.println("숫자 아님");
	            	JOptionPane.showMessageDialog(null , "숫자만 입력해 주세요.");
	            		}
	            	}
	         }
	         @Override
	         public void removeUpdate(DocumentEvent arg0) {
	          	  
//	        	 String st =null;
//				try {
//					st = arg0.getDocument().getText(0, arg0.getDocument().getLength());
//				} catch (BadLocationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	            System.out.println("remove VAlue Inserted! Length: "+arg0.getDocument().getLength());
//	            if(Pattern.matches("^[0-9]+$", st)){
//	            	System.out.println("숫자");
//	            	}else{
//	            		if(!st.equals("")){
//	    	            	System.out.println("숫자 아님");
//	    	            	JOptionPane.showMessageDialog(null , "숫자만 입력해 주세요.");
//	    	            		}
//	            	}
	            
	         }
	         });
		tf_quantity.addKeyListener(new KeyAdapter() {
		
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				System.out.println(tf_quantity.getText().length());
				if( tf_stock.getText() != null && !tf_quantity.getText().equals("")){
				btn_buy.setEnabled(true);
				btn_sell.setEnabled(true);
				}else{
					btn_buy.setEnabled(false);
					btn_sell.setEnabled(false);
				}
			}
			
		});
		
		
		btn_sell = new JButton("Sell");
		btn_sell.setEnabled(false);
		btn_sell.setFont(new Font("굴림", Font.BOLD, 13));
		btn_sell.setForeground(new Color(0, 51, 204));
		panel_6.add(btn_sell);
		btn_sell.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Scssn
				String symbol = tf_stock.getText();
				String quantity = tf_quantity.getText();
				String ssn = scssn;
				
				Shares sh = new Shares(ssn, symbol, quantity);
				try {
					db.sellShares(sh);
					tf_quantity.setText("");
					showPortfolio();
				} catch (InvalidTransactionException e1) {
					e1.printStackTrace();
					showMessage(e1.getMessage());
				} catch (RecordNotFoundException e1) {
					e1.printStackTrace();
					showMessage(e1.getMessage());
				}
			}
		});
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_4 = new JLabel("Stock Information");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel_4, BorderLayout.NORTH);
		
		JPanel panel_18 = new JPanel();
		panel_18.setBackground(new Color(70, 130, 180));
		panel_3.add(panel_18, BorderLayout.CENTER);
		panel_18.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_5 = new JLabel("Available Stocks");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBackground(new Color(102, 153, 153));
		panel_18.add(lblNewLabel_5, BorderLayout.NORTH);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_18.add(scrollPane_2);
		JList<String> list_2 = new JList<>();
		list_2.setBackground(new Color(189, 183, 107));
		list_2.setForeground(new Color(139, 0, 0));
		scrollPane_2.setViewportView(list_2);
		dflm3 = new DefaultListModel<>();
		list_2.setModel(dflm3);
		list_2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				tf_quantity.setText("");
				int ind = list_2.getSelectedIndex();
				cu =dflm3.getElementAt(ind);
				System.out.println(cu);
				//dflm.clear();
				tf_stock.setText(cu);
				btn_buy.setEnabled(false);
				tf_quantity.setEditable(true);
			}
			
		});
		

		
		JPanel panel_19 = new JPanel();
		panel_3.add(panel_19, BorderLayout.SOUTH);
		panel_19.setLayout(new GridLayout(3, 1, 0, 0));
		
		btn_gcsp = new JButton("Get Current Stock Price");
		btn_gcsp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Stock st;
				try {
					st = db.getStock(cu);
					String price = st.getPrice();
					tf_symbol.setText(cu);
					tf_price.setText(price);
				} catch (RecordNotFoundException e1) {
					e1.printStackTrace();
					showMessage(e1.getMessage());
				}
				
			}
		});
		panel_19.add(btn_gcsp);
		
		JPanel panel_20 = new JPanel();
		panel_20.setBackground(new Color(153, 204, 204));
		panel_19.add(panel_20);
		panel_20.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblStock = new JLabel("Stock");
		lblStock.setHorizontalAlignment(SwingConstants.LEFT);
		panel_20.add(lblStock);
		
		tf_symbol = new JTextField();
		tf_symbol.setEditable(false);
		panel_20.add(tf_symbol);
		tf_symbol.setColumns(10);
		
		JPanel panel_21 = new JPanel();
		panel_21.setBackground(new Color(153, 204, 204));
		panel_19.add(panel_21);
		panel_21.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblCurrentPrice = new JLabel("Current Price");
		lblCurrentPrice.setBackground(new Color(153, 204, 204));
		panel_21.add(lblCurrentPrice);
		
		tf_price = new JTextField();
		tf_price.setEditable(false);
		panel_21.add(tf_price);
		tf_price.setColumns(10);
		
		
		
	}
	
	public void showPortfolio(){
		dflm.clear();
		int ind = list_1.getSelectedIndex();
		cus =dflm1.getElementAt(ind);
		System.out.println(cu);
		scssn = cus.getSsn();
		try {
			ArrayList<Shares> shareList  = db.getPortfolio(scssn);
			for(Shares sh:shareList){
			dflm.addElement(sh);
			
			}
		} catch (RecordNotFoundException e1) {
			e1.printStackTrace();
			showMessage(e1.getMessage());
		}
		
		}
	
	
	public String dilimiter(String msg){
		String[] parts = msg.split("\\s+");
		String part1 = parts[0];
		return part1;
	}
	
	public void videTF(){
		tf_name.setText("");
		tf_ssn.setText("");
		ta_address.setText("");
	}
	
	public void showAllStocks(){
		ArrayList<Stock> stockList = db.getAllStocks();
		for(Stock st: stockList){
			//String price = st.getPrice();
			String symbol = st.getSymbol();
			dflm3.addElement(symbol);
		}
	}
	
	public void showAllCustomer(){
		dflm1.clear();
		ArrayList<Customer> allCus = db.getAllCustomers();
		for(Customer cus: allCus){
			//String cust_name = cus.getCust_name();
			//String ssn = cus.getSsn();
			dflm1.addElement(cus);
		}
	}
	
	public void initCustomerButton(boolean status){
			btn_add.setEnabled(status);
			btn_delete.setEnabled(status);
			btn_update.setEnabled(status);
			btn_apply.setEnabled(!status);
			btn_cancel.setEnabled(!status);
			tf_name.setEditable(!status);
			tf_ssn.setEditable(!status);
			ta_address.setEditable(!status);
			
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		//apply 를 어떻게 해야 할까? 입력한 다음에 
		cm = e.getActionCommand();
		System.out.println(cm);
		if(cm.equals("add")||cm.equals("update")){
			initCustomerButton(false);
			if(cm.equals("update")) ta_address.setEditable(true);
		}else if(cm.equals("delete")){
			initCustomerButton(false);
			tf_name.setEditable(false);
			ta_address.setEditable(false);
			
		}else if(cm.equals("cancel")){
			
			initCustomerButton(true);
		}else if(cm.equals("apply")){
			System.out.println(cm);
			//name tf 에 뭔가 있을 때
			//add를 누른 후 입력하고 apply 를 누를 경우> textfield에 입력한 거 가져와서 객체생성후 insert 하기
			try{
			String name = tf_name.getText();
			String ssn = tf_ssn.getText();
			String address = ta_address.getText();
			Customer c = new Customer(ssn, name, address);
			try {
				db.addCustomer(c);
			} catch (DuplicateIDException e1) {
				e1.printStackTrace();
			}}catch(NullPointerException e2){
				System.out.println("칸이 비어있습니다!");
				showMessage(e2.getMessage());
			}
			initCustomerButton(true);
			videTF();
		}else if(!tf_name.contains(null)){
			//ssn 만 활성화 됨. delete
			String ssn = tf_ssn.getText();
			try {
				db.deleteCustomer(ssn);
			} catch (RecordNotFoundException e1) {
				e1.printStackTrace();
				showMessage(e1.getMessage());
			}
		}
		
		
	}
	
	public void showMessage(String message){
		JOptionPane.showMessageDialog(this, message );
		
	}
	
	
//	public class MouseEventHandler extends MouseAdapter{
//		
//	}
	
}
