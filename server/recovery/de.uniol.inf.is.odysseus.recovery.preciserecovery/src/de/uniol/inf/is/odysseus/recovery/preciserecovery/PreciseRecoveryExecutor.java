package de.uniol.inf.is.odysseus.recovery.preciserecovery;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.rollbackrecovery.RollbackRecoveryExecutor;

/**
 * The precise recovery executor represents a recovery strategy, which backups
 * and recovery query states (incl. sinks and sources), operator states (incl.
 * queue states), incoming data streams and the progress within outgoing data
 * streams. <br />
 * <br />
 * The precise recovery fulfills the requirements of completeness and
 * correctness.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class PreciseRecoveryExecutor extends RollbackRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(PreciseRecoveryExecutor.class);

	@Override
	public String getName() {
		return "PreciseRecovery";
	}

	/**
	 * The not initialized recovery component for the outgoing data stream
	 * elements (to be bound by OSGi).
	 */
	private static IRecoveryComponent cOutgoingElementsComponent;

	/**
	 * Binds a recovery component for the outgoing data stream elements.
	 *
	 * @param component
	 *            The component to bind, if its name matches "Outgoing Elements"
	 *            .
	 */
	public static void bindComponent(IRecoveryComponent component) {
		if (component.getName().equals("Outgoing Elements")) {
			cOutgoingElementsComponent = component;
		}
	}

	/**
	 * Unbinds a recovery component
	 * 
	 * @param component
	 *            The component to unbind, if it has been bound before.
	 */
	public static void unbindComponent(IRecoveryComponent component) {
		if (component.equals(cOutgoingElementsComponent)) {
			cOutgoingElementsComponent = null;
		}
	}

	/**
	 * The initialized recovery component for the outgoing data stream elements.
	 */
	private IRecoveryComponent mOutgoingElementsComponent;
	
	/**
	 * Empty default constructor for OSGi-DS.
	 */
	public PreciseRecoveryExecutor() {}

	/**
	 * Constructor, which initializes all bound recovery components.
	 * 
	 * @param config
	 *            The configuration for the executor.
	 */
	public PreciseRecoveryExecutor(Properties config) {
		super(config);
		this.mOutgoingElementsComponent = cOutgoingElementsComponent.newInstance(config);
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		return new PreciseRecoveryExecutor(config);
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		List<ILogicalQuery> modifiedQueries = super.recover(qbConfig, caller, queries);

		if (this.mOutgoingElementsComponent == null) {
			cLog.error("PreciseRecovery executor misses Outgoing elements recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mOutgoingElementsComponent.recover(qbConfig, caller, modifiedQueries);

		return modifiedQueries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		List<ILogicalQuery> modifiedQueries = super.activateBackup(qbConfig, caller, queries);

		if (this.mOutgoingElementsComponent == null) {
			cLog.error("PreciseRecovery executor misses Outgoing elements recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mOutgoingElementsComponent.activateBackup(qbConfig, caller, modifiedQueries);

		return modifiedQueries;
	}

}