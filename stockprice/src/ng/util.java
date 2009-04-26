package ng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class util {

	public static String getString(String str, String startstr, int gap1,
			String endstr, int gap2) {
		int baseIndex = str.indexOf(startstr);
		if (baseIndex == -1) {
			return "N/A";
		} else {
			//System.out.print(baseIndex);
			int len = str.indexOf(endstr, baseIndex + gap1) - baseIndex - gap1
					- gap2;
			//System.out.print(len);
			return str.substring(baseIndex + gap1, baseIndex + gap1 + len);
		}
	}

	public static String getName(String q) {

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
		source = source +"<data>";
		while (page.indexOf("JavaScript:UpdateSymbol") > -1) {
			source = source +"<company><ticker>"
					+ ng.util.getString(page, "JavaScript:UpdateSymbol", 25,
							",", 1) + "</ticker>";
			page = page.substring(page.indexOf("JavaScript:UpdateSymbol") + 30);
			source = source +"<name>"
					+ ng.util.getString(page, "<td", 38, "</td", 0).replaceAll(
							"&", "&amp;") + "</name></company>";
		}
		source = source +"</data>";
		return source;
	}


	public static String getPrice(String q) {

		String page = "";
		String source = "";

		   try {
	            URL url = new URL("http://getquote.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol="+q);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
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
	    //Index upto which all the data are same and company specific data starts     

	   // resp.getWriter().println(page);
	    source = source + "<?xml version='1.0' encoding='ISO-8859-1'?>";     
	    source = source + "<data>";    
	    source = source + "<company>"+ng.util.getString (page,"STOCK                     NAME", 169, "td",2)+"</company>";  
	    source = source + "<time>"+ng.util.getString (page,"LAST TRADED                     TIME", 112, "td",2)+"</time>";  
	    source = source + "<price>"+ng.util.getString (page,"LAST TRADE                     PRICE", 111, "td",2)+"</price>";
	    source = source + "</data>";
	    
		
		
		
		
		return source;
	}









}
