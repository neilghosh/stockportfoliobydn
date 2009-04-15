package ng;

import java.io.IOException;
import javax.servlet.http.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@SuppressWarnings("serial")
public class StockPriceServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String ticker = req.getParameter("q");
		resp.setContentType("text/xml");
		 String page ="";
		
		
		
        try {
            URL url = new URL("http://getquote.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol="+ticker);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
           
            resp.getWriter().println(url);

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
/*
    int baseIndex = page.indexOf("STOCK                     NAME", 0);	
    int companylength = page.indexOf("td", baseIndex+169)-baseIndex-169-2; 
    System.out.print(companylength);
    //print company name
    resp.getWriter().println(page.substring(baseIndex+169, baseIndex +169+companylength));
    resp.getWriter().println(page.substring(baseIndex +169+companylength+907, baseIndex +169+companylength+918));
    resp.getWriter().println(page.substring(baseIndex +169+companylength+1482, baseIndex +169+companylength+1169));
   // int proceIndex = page.indexOf("LAST TRADE                     PRICE");
  */
    resp.getWriter().println(page);
    resp.getWriter().println("<?xml version='1.0' encoding='ISO-8859-1'?>");     
    resp.getWriter().println("<data>");    
    resp.getWriter().println("<company>"+ng.util.getString (page,"STOCK                     NAME", 169, "td",2)+"</company>");  
    resp.getWriter().println("<time>"+ng.util.getString (page,"LAST TRADED                     TIME", 112, "td",2)+"</time>");  
    resp.getWriter().println("<price>"+ng.util.getString (page,"LAST TRADE                     PRICE", 111, "td",2)+"</price>");
    resp.getWriter().println("</data>");
    
	}
	

}

