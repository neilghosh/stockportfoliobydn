package ng;


import java.io.StringReader;
import java.text.NumberFormat;

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
public class Holdings {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String user;

	@Persistent
	private String stockCode; 

	@Persistent
	private int quantity;

	@Persistent
	private double avgPrice;

	public Holdings(String user, String stockCode, int quantity, double avgPrice) {
		
		this.user = user;
		this.stockCode = stockCode;
		this.quantity = quantity;
		this.avgPrice = avgPrice;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public double getStockPrice() {
		double price = 0.0d;
		String PriceStr = "";
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document doc = factory.newDocumentBuilder().parse(
					new InputSource(new StringReader(util
							.getPriceXML(stockCode))));
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
