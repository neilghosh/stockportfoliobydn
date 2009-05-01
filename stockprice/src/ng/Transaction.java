package ng;

import java.io.StringReader;
import java.text.NumberFormat;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Transaction {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String user;

	@Persistent
	private String stockCode;

	@Persistent
	private Date date;

	@Persistent
	private int quantity;

	@Persistent
	private double invPrice;
	
	@Persistent
	private String transactionType;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getInvPrice() {
		return invPrice;
	}

	public void setInvPrice(double invPrice) {
		this.invPrice = invPrice;
	}

	public Transaction(String user, String stockCode, Date date, int quantity,
			double invPrice) {

		this.user = user;
		this.stockCode = stockCode;
		this.date = date;
		this.quantity = quantity;
		this.invPrice = invPrice;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setCurrentPrice() {

	}

	public double getStockPrice() {
		double price = 0.0d;
		String PriceStr = "";
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc = factory.newDocumentBuilder()
					.parse(
							new InputSource(new StringReader(util
									.getPrice(stockCode))));
			doc.getDocumentElement().normalize();

			NodeList nodeLst = doc.getElementsByTagName("price");
			Element priceElement = (Element) nodeLst.item(0);
			NodeList prc = priceElement.getChildNodes();
			// price = Double.parseDouble(((prc.item(0)).getNodeValue()));

			NumberFormat nf = NumberFormat.getInstance();
			price = nf.parse(((prc.item(0)).getNodeValue())).doubleValue();

		} catch (java.text.ParseException ex) {
			System.out.print(ex.getMessage());
		} catch (Exception e) {
			System.out.print(e);
		}

		return price;
	}

}