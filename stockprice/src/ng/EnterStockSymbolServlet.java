package ng;


import java.io.IOException;
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
	private static final Logger log = Logger.getLogger(EnterStockSymbolServlet.class.getName());

    @SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        String symbol = req.getParameter("symbol");
        
        String action = req.getParameter("action");
        if(("delete").equals(action))
        {
        	Long id =Long.valueOf(req.getParameter("id"));
        	resp.getWriter().println("You are about to delete "+symbol+" from your portfolio");
        	PersistenceManager pm = PMF.get().getPersistenceManager();
        	Query query = pm.newQuery("select from " + StockItem.class.getName() +" where id == "+id) ;
        	List<StockItem> StockItems = (List<StockItem>) query.execute();
        	StockItem s = ((StockItem)StockItems.get(0));
        	pm.deletePersistent(s);
        }
        else{
        Date date = new Date();
        
        StockItem greeting = new StockItem(user.getNickname(), symbol, date);
        log.info(user.getEmail() + " Created " + symbol + " on " + date);

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(greeting);
        } finally {
            pm.close();
        }
        }
        resp.sendRedirect("/portfolio.jsp");
    }
}