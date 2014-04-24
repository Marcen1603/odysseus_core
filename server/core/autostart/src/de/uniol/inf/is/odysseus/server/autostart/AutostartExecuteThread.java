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
	private static final long WAIT_TIME_MILLIS = 10 * 1000;

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
		LOG.debug("Begin autostart execution...");
		int tries = 0;

		while (true) {
			try {
				executor.addQuery(queryText, "OdysseusScript", user, "Standard", Context.empty());
				LOG.debug("Autostart script executed");
				break;
			} catch (Throwable t) {
				tries++;
				if (tries == MAX_TRIES) {
					throw new RuntimeException("Autostart script failed " + MAX_TRIES + " times", t);
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("Could not start autostart script (try {}, waiting {} ms)", new Object[] { tries, WAIT_TIME_MILLIS, t });
				} else {
					LOG.error("Autostart script failed in try {}. Waiting {} ms... (error was: {})", new Object[] { tries, WAIT_TIME_MILLIS, t.getClass().getSimpleName() });
				}

				setName("Autostart execution thread (fails: " + tries + ")");
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
