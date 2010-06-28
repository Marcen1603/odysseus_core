package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterDefaultRoot;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class NMQNCommand extends AbstractHandler implements IHandler {

	private static final String[] queries = {
			"SELECT b.auction, DolToEur(b.price) AS euroPrice, b.bidder, b.datetime FROM nexmark:bid2 [UNBOUNDED] AS b",
			"SELECT auction, price FROM nexmark:bid2 WHERE auction=7 OR auction=20 OR auction=21 OR auction=59 OR auction=87",
			"SELECT p.name, p.city, p.state, a.id FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:person2 [UNBOUNDED] AS p WHERE a.seller=p.id AND a.category < 150 AND (p.state='Oregon' OR p.state='Idaho' OR p.state='California')",
			"SELECT AVG(q.final) FROM nexmark:category2 AS c, (SELECT MAX(b.price) AS final, a.category FROM nexmark:auction2 [UNBOUNDED] AS a, nexmark:bid2 [UNBOUNDED] AS b  WHERE a.id = b.auction AND b.datetime < a.expires AND  a.expires < Now() GROUP BY a.id, a.category) AS q WHERE q.category = c.id GROUP BY c.id",
			"SELECT b.auction, b.price, b.bidder	FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME] AS b,(SELECT MAX(price) AS max_price FROM nexmark:bid2 [SIZE 1000 ADVANCE 1000 TIME]) AS sub WHERE sub.max_price = b.price",
			"SELECT p.id, p.name, a.reserve FROM nexmark:person2 [SIZE 12 HOURS ADVANCE 1 TIME] AS p, nexmark:auction2 [SIZE 12 HOURS ADVANCE 1 TIME] AS a WHERE p.id = a.seller" };

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IAdvancedExecutor executor = Activator.getExecutor();
		if (executor != null) {
			for (String q : queries) {
				try {
					// TODO: User einfuegen, der diese Query ausf�hrt
					User user = new User("TODO.SetUser");
					executor.addQuery(q, "CQL", user, new ParameterDefaultRoot(new MySink()));
				} catch (PlanManagementException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("Kein ExecutorService gefunden");
			// TODO: Nachricht hier anzeigen
			return null;
		}
		return null;
	}

}
