package ng;

import java.io.IOException;
import javax.servlet.http.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;


@SuppressWarnings("serial")
public class StockNameServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String ticker = req.getParameter("q");
		resp.setContentType("text/xml");
		 String page ="";
		
		
		
        try {
            URL url = new URL("http://getquote.icicidirect.com/trading/equity/trading_stock_code.asp?Symbol="+ticker);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
           
         //   resp.getWriter().println(url);

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

    resp.getWriter().println("<?xml version='1.0' encoding='ISO-8859-1'?>");     
    resp.getWriter().println("<data>");    
    while (page.indexOf("JavaScript:UpdateSymbol") > -1)
    {
    	resp.getWriter().println("<company><ticker>"+ng.util.getString(page, "JavaScript:UpdateSymbol" , 25, ",", 1)+"</ticker>");
    	page = page.substring(page.indexOf("JavaScript:UpdateSymbol")+30);
    	resp.getWriter().println("<name>"+ng.util.getString(page, "<td" , 38, "</td", 0).replaceAll("&","&amp;")+"</name></company>");
    }
    resp.getWriter().println("</data>");
    
	}
	

}

