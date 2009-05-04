package ng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

public class util {

	public static String getStringSegment(String str, String startstr,
			int gap1, String endstr, int gap2) {
		// System.out.print(endstr);
		// System.out.println(str);

		int baseIndex = str.indexOf(startstr);
		if (baseIndex == -1) {
			return "-999";
		} else {
			// System.out.print(baseIndex);
			int len = str.indexOf(endstr, baseIndex + gap1) - baseIndex - gap1
					- gap2;
			// System.out.print(len);
			return str.substring(baseIndex + gap1, baseIndex + gap1 + len);
		}
	}
	public static ArrayList<Holdings> consolidate(List<Holdings> StockItems)
	{
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		
		boolean found=false;
		ArrayList<Holdings> consolidated=new ArrayList();
		for(Holdings t: StockItems){
			found = false;
			for(Holdings old: consolidated){
				if(old.getStockCode().equals(t.getStockCode())){
					double prevInvestment = old.getAvgPrice() * old.getQuantity();
					double currentInvestment = t.getAvgPrice() * t.getQuantity();
					double avgPrice = (prevInvestment + currentInvestment)
							/ (old.getQuantity() + t.getQuantity());
					DecimalFormat df = new DecimalFormat("0.00");
					String a = df.format(avgPrice);
					double AA = Double.parseDouble(a);
			
					old.setAvgPrice(AA);
				//	System.out.println("avg price:" + s.getInvPrice());
					old.setQuantity(old.getQuantity() + t.getQuantity());
				//	System.out.println("total qty :" + s.getQuantity());
					found = true;
					
				}
			}
			
			if(found == false)
				consolidated.add(t);
		}
	//	pm.close();

		return consolidated;
		
	}
	public static String getNameXML(String q) {

		String page = "";
		String source = "";

		try {
			URL url = new URL(
					"http://getquote.icicidirect.com/trading/equity/trading_stock_code.asp?Symbol="
							+ q);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			// resp.getWriter().println(url);

			while ((line = reader.readLine()) != null) {
				// ...
				page = page + line;
			}
			reader.close();

		} catch (MalformedURLException e) {
			// ...
		} catch (IOException e) {
			// ...
		}

		source = source + "<?xml version='1.0' encoding='ISO-8859-1'?>";
		source = source + "<data>";
		while (page.indexOf("JavaScript:UpdateSymbol") > -1) {
			source = source
					+ "<company><ticker>"
					+ ng.util.getStringSegment(page, "JavaScript:UpdateSymbol",
							25, ",", 1) + "</ticker>";
			page = page.substring(page.indexOf("JavaScript:UpdateSymbol") + 30);
			source = source
					+ "<name>"
					+ ng.util.getStringSegment(page, "<td", 38, "</td", 0)
							.replaceAll("&", "&amp;") + "</name></company>";
		}
		source = source + "</data>";
		return source;
	}

	public static String getPriceXML(String q) {

		String page = "";
		String source = "";
		WebPageFetcher fetcher = null;
		try {
			fetcher = new WebPageFetcher(
					new URL(
							"http://getquote.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol="
									+ q));
			page = fetcher.getPageContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		source = source + "<?xml version='1.0' encoding='ISO-8859-1'?>";
		source = source + "<data>";
		source = source + "<company>"
				+ ng.util.getStringSegment(page, "  NAME", 144, "td", 2)
				+ "</company>";
		source = source + "<time>"
				+ ng.util.getStringSegment(page, "  TIME", 82, "td", 2)
				+ "</time>";
		source = source + "<price>"
				+ ng.util.getStringSegment(page, "  PRICE", 86, "td", 2)
				+ "</price>";
		source = source + "</data>";

		// System.out.println(source+" "+page);
		return source;
	}

}
