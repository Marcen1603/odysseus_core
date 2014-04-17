package de.uniol.inf.is.odysseus.server.autostart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class AutostartExecuteThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(AutostartExecuteThread.class);
	
	private static final int MAX_TRIES = 10;
	private static final long WAIT_TIME_MILLIS = 10 * 10000;
	
	private final IExecutor executor;
	private final String queryText;
	private final ISession user;
	
	public AutostartExecuteThread(IExecutor executor, String queryText) {
		this.executor = executor;
		this.queryText = queryText;
		this.user = UserManagementProvider.getSessionmanagement().loginSuperUser(null);
		
		setDaemon(true);
		setName("Autostart execution thread");
	}

	@Override
	public void run() {
		int tries = 0;
		
		while( true ) {
			try {
				executor.addQuery(queryText, "OdysseusScript", user, "Standard", Context.empty());
				break;
			} catch( Throwable t ) {
				tries++;
				if( tries == MAX_TRIES ) {
					throw new RuntimeException("Autostart script failed " + MAX_TRIES + " times", t);
				}
				
				LOG.error("Autostart script failed in try {}. Waiting {} ms...", tries, WAIT_TIME_MILLIS);
				LOG.debug("Exception in autostart", t);
				
				setName("Autostart execution thread (fails: " + tries + ")"); // to see in debug-view in eclipse
				
				tryWait();
			}
		}
	}

	private static void tryWait() {
		try {
			Thread.sleep(WAIT_TIME_MILLIS);
		} catch (InterruptedException e) {
		}
	}
}
