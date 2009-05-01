package ng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.jdo.Query;
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
			if (StockItems.size() == 0) {
				System.out.println("1");
				Transaction greeting = new Transaction(user.getNickname(), symbol,
						date, quantity, invPrice);
				log
						.info(user.getEmail() + " Created " + symbol + " on "
								+ date);
				try {
					pm.makePersistent(greeting);
				} finally {
					pm.close();
				}
			} else {
				System.out.println("2");
				Transaction s = (StockItems.get(0));
				double prevInvestment = s.getInvPrice() * s.getQuantity();
				double currentInvestment = invPrice * quantity;
				double avgPrice = (prevInvestment + currentInvestment)
						/ (s.getQuantity() + quantity);
				DecimalFormat df = new DecimalFormat("0.00");
				String a = df.format(avgPrice);
				double AA = Double.parseDouble(a);

				s.setInvPrice(AA);
				System.out.println("avg price:" + s.getInvPrice());
				s.setQuantity(s.getQuantity() + quantity);
				System.out.println("total qty :" + s.getQuantity());
				try {
					pm.makePersistent(s);
				} finally {
					pm.close();
				}
			}
			// PersistenceManager pm = PMF.get().getPersistenceManager();

		}
		resp.sendRedirect("/portfolio.jsp");
	}
}