package ng;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

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

}
