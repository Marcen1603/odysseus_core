package de.uniol.inf.is.odysseus.core.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class ScriptExecuteThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ScriptExecuteThread.class);

	private static final int MAX_TRIES = 10;
	private static final long WAIT_TIME_MILLIS = 10 * 1000;

	private final IExecutor executor;
	private final String queryText;
	private final ISession user;
	private final Context context;

	public ScriptExecuteThread(IExecutor executor, String queryText, ISession user, Optional<String> fileUrl) {
		this.executor = executor;
		this.queryText = queryText;
		this.user = user;
		context = Context.empty();
		if (fileUrl.isPresent()) {
			context.put("BUNDLE-ROOT", fileUrl.get());
		}
		setDaemon(true);
		setName("Script execution thread");
	}

	@Override
	public void run() {
		LOG.debug("Begin script execution...");
		int tries = 0;


		while (true) {
			try {
				executor.addQuery(queryText, "OdysseusScript", user, context);
				LOG.debug("Script executed");
				break;
			} catch (Throwable t) {
				tries++;
				if (tries == MAX_TRIES) {
					throw new RuntimeException("Script failed " + MAX_TRIES + " times", t);
				}

				if (LOG.isDebugEnabled()) {
					LOG.debug("Could not start script (try {}, waiting {} ms)", new Object[] { tries, WAIT_TIME_MILLIS, t });
				} else {
					LOG.error("Script failed in try {}. Waiting {} ms... (error was: {})", new Object[] { tries, WAIT_TIME_MILLIS, t.getClass().getSimpleName() });
				}

				setName("Script execution thread (fails: " + tries + ")");
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
