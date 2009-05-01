package ng;

import ng.WebPageFetcher;
import java.net.*;

public class testmain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String page = "";
		String source = "";
		WebPageFetcher fetcher = null;
		try {
			fetcher = new WebPageFetcher(
					new URL(
							"http://getquote.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol="
									+ "BSES"));
			page = fetcher.getPageContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// resp.getWriter().println(page);
		source = source + "<?xml version='1.0' encoding='ISO-8859-1'?>";
		source = source + "<data>";
		source = source + "<company>"
				+ ng.util.getStringSegment(page, "  NAME", 144, "td", 2)
				+ "</company>";
		source = source + "<time>"
				+ ng.util.getStringSegment(page, "  TIME", 82, "td", 2) + "</time>";
		source = source + "<price>"
				+ ng.util.getStringSegment(page, "  PRICE", 82, "td", 2) + "</price>";
		source = source + "</data>";

		System.out.println(source);

	}
}
