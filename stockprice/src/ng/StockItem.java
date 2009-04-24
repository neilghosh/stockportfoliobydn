package ng;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StockItem {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String user;

	@Persistent
	private String stockCode;

	@Persistent
	private Date date;

	public StockItem(String user, String stockCode, Date date) {

		this.user = user;
		this.stockCode = stockCode;
		this.date = date;
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

	public double getStockPrice() {
		double price = 0.0d;
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db
					.parse("http://testjavaneil.appspot.com/stockprice?q="
							+ stockCode);
			doc.getDocumentElement().normalize();

			NodeList nodeLst = doc.getElementsByTagName("price");
			Element priceElement = (Element) nodeLst.item(0);
			NodeList prc = priceElement.getChildNodes();
			price = Double.parseDouble((((Node) prc.item(0)).getNodeValue()));
		} catch (Exception e) {
			System.out.print("Ex..");
		}
		return price;
	}
}