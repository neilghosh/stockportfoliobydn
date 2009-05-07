package ng;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import ng.Holdings;

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
			//Transaction greeting = new Transaction(user.getNickname(), s.getStockCode(),
				//	s.getDate(), s.getQuantity(), s.getInvPrice(), "Sell");
			pm.deletePersistent(s);
			resp.sendRedirect("/TransactionHistory.jsp");
		} else {
			Date date = new Date(req.getParameter("date"));
			int quantity = Integer.parseInt(req.getParameter("qty"));
			double invPrice = Double.parseDouble(req.getParameter("price"));
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery("select from "
					+ Holdings.class.getName() + " where stockCode == '"
					+ symbol + "' && user == '" + user.getNickname() + "'");
			List<Holdings> StockItems = (List<Holdings>) query.execute();
			Transaction greeting = new Transaction(user.getNickname(), symbol,
						date, quantity, invPrice, "Buy");
			Holdings newStock=null;
			if(StockItems.size()>0){
				Holdings s = StockItems.get(0);
				invPrice = (s.getAvgPrice()*s.getQuantity()+invPrice*quantity)/(s.getQuantity()+quantity);
				quantity = s.getQuantity()+quantity;
				s.setAvgPrice(invPrice);
				s.setQuantity(quantity);
			}
			else{
				 newStock = new Holdings(user.getNickname(), symbol, quantity, invPrice);
		

			}
				log
						.info(user.getEmail() + " Created " + symbol + " on "
								+ date);
				try {
					pm.makePersistent(greeting);
					pm.makePersistent(newStock);
				} finally {
					pm.close();
				}
			 
			// PersistenceManager pm = PMF.get().getPersistenceManager();
				resp.sendRedirect("/portfolio.jsp");
		}
	}
	
	
}