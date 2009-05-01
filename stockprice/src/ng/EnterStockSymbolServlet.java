package ng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class EnterStockSymbolServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger
			.getLogger(EnterStockSymbolServlet.class.getName());

	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		String symbol = req.getParameter("symbol");
		String action = req.getParameter("action");

		if (("delete").equals(action)) {
			Long id = Long.valueOf(req.getParameter("id"));
			resp.getWriter().println(
					"You are about to delete " + symbol
							+ " from your portfolio");
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery("select from "
					+ Transaction.class.getName() + " where id == " + id);
			List<Transaction> StockItems = (List<Transaction>) query.execute();
			Transaction s = (StockItems.get(0));
			pm.deletePersistent(s);
		} else {
			Date date = new Date(req.getParameter("date"));
			int quantity = Integer.parseInt(req.getParameter("qty"));
			double invPrice = Double.parseDouble(req.getParameter("price"));
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery("select from "
					+ Transaction.class.getName() + " where stockCode == '"
					+ symbol + "' && user == '" + user.getNickname() + "'");
			List<Transaction> StockItems = (List<Transaction>) query.execute();
			Transaction greeting = new Transaction(user.getNickname(), symbol,
						date, quantity, invPrice, "Buy");
				log
						.info(user.getEmail() + " Created " + symbol + " on "
								+ date);
				try {
					pm.makePersistent(greeting);
				} finally {
					pm.close();
				}
			 
			// PersistenceManager pm = PMF.get().getPersistenceManager();

		}
		resp.sendRedirect("/portfolio.jsp");
	}
	
	
}