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
		String page = "";

		try {
			URL url = new URL(
					"http://getquote.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol="
							+ ticker);
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
		// Index upto which all the data are same and company specific data
		// starts

		// resp.getWriter().println(page);
		resp.getWriter().println("<?xml version='1.0' encoding='ISO-8859-1'?>");
		resp.getWriter().println("<data>");
		resp.getWriter().println(
				"<company>"
						+ ng.util.getStringSegment(page,
								"STOCK                     NAME", 169, "td", 2)
						+ "</company>");
		resp.getWriter().println(
				"<time>"
						+ ng.util.getStringSegment(page,
								"LAST TRADED                     TIME", 112,
								"td", 2) + "</time>");
		resp.getWriter().println(
				"<price>"
						+ ng.util.getStringSegment(page,
								"LAST TRADE                     PRICE", 111,
								"td", 2) + "</price>");
		resp.getWriter().println("</data>");

	}

}
