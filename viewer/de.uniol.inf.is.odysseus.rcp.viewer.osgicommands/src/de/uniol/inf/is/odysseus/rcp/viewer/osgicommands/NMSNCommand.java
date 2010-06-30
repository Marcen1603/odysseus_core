package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class NMSNCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IAdvancedExecutor exec = Activator.getExecutor();
		if( exec != null ) {
			String[] q = new String[8];
			q[0] = "CREATE STREAM nexmark:person2 (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440";
			q[4] = "CREATE STREAM nexmark:person2_v (timestamp LONG,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) FROM (SELECT * FROM nexmark:person2 [UNBOUNDED ON timestamp])";
			q[1] = "CREATE STREAM nexmark:bid2 (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442";
			q[5] = "CREATE STREAM nexmark:bid2_v (timestamp LONG,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) FROM (SELECT * FROM nexmark:bid2 [UNBOUNDED ON timestamp])";
			q[2] = "CREATE STREAM nexmark:auction2 (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441";
			q[6] = "CREATE STREAM nexmark:auction2_v (timestamp LONG,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) FROM (SELECT * FROM nexmark:auction2 [UNBOUNDED ON timestamp])";
			q[3] = "CREATE STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443";
			q[7] = "CREATE STREAM nexmark:category2_v (id INTEGER, name STRING, description STRING, parentid INTEGER) FROM (SELECT * FROM nexmark:category2 [UNBOUNDED])";
			for (String s : q) {
				try {
					// TODO: User einfuegen, der diese Query ausführt
					User user = new User("TODO.SetUser");
					 
					exec.addQuery(s, "CQL", user, Activator.getTrafoConfigParam());
				} catch (PlanManagementException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
