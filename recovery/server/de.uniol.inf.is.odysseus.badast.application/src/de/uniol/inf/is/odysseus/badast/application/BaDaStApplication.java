package de.uniol.inf.is.odysseus.badast.application;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.IPubSubSystem;

/**
 * BaDaSt - Backup of Data Streams application. <br />
 * <br />
 * Starts first a publish subscribe system and a BaDaSt server. <br />
 * <br />
 * Type help in the OSGi console to see all BaDaSt commands.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStApplication implements IApplication {

	/**
	 * The logger for this class.
	 */
	static final Logger LOG = LoggerFactory.getLogger(BaDaStApplication.class);

	/**
	 * The only instance of this class.
	 */
	private static BaDaStApplication instance;

	/**
	 * Gets the only instance of this class.
	 */
	public static BaDaStApplication getInstance() {
		return instance;
	}

	/**
	 * The publish subscribe system to use.
	 */
	private static IPubSubSystem boundPubSub;

	/**
	 * Binds the publish subscribe system to use.
	 * 
	 * @param sys
	 */
	public static void bindPubSub(IPubSubSystem sys) {
		boundPubSub = sys;
	}

	/**
	 * Unbinds the publish subsribe system.
	 */
	public static void unbindPubSub(IPubSubSystem sys) {
		if (sys == boundPubSub) {
			boundPubSub = null;
		}
	}

	/**
	 * The publish subscribe system to use.
	 */
	private IPubSubSystem pubSub;

	/**
	 * The BaDaSt server
	 */
	private BaDaStServer badastServer = new BaDaStServer();

	@Override
	public void stop() {
		if (this.pubSub != null) {
			this.pubSub.interrupt();
		}
		this.badastServer.interrupt();
	}

	public void start() throws Exception {
		if (this.pubSub != null) {
			this.stop();
		}
		this.pubSub = boundPubSub.newInstance();
		this.pubSub.run();
		this.badastServer.start();
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		PropertyConfigurator.configure(BaDaStApplication.class.getClassLoader().getResource("log4j.properties"));
		this.start();
		instance = this;
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

}