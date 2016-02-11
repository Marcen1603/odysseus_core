package de.uniol.inf.is.odysseus.recovery.rollbackrecovery;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * The rollback recovery executor represents a recovery strategy, which backups
 * and recovery query states (incl. sinks and sources), operator states (incl.
 * queue states), and incoming data streams. <br />
 * <br />
 * The rollback recovery fulfills the requirement of completeness, but not the
 * requirement of correctness.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class RollbackRecoveryExecutor extends AbstractRecoveryExecutor {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(RollbackRecoveryExecutor.class);

	@Override
	public String getName() {
		return "RollbackRecovery";
	}

	/**
	 * The not initialized recovery component for the protection point handling
	 * (to be bound by OSGi).
	 */
	private static IRecoveryComponent cProtectionPointsComponent;

	/**
	 * The not initialized recovery component for the incoming data stream
	 * elements (to be bound by OSGi).
	 */
	private static IRecoveryComponent cIncomingElementsComponent;

	/**
	 * The not initialized recovery component for the operator and queue states
	 * (to be bound by OSGi).
	 */
	private static IRecoveryComponent cOperatorStateComponent;

	/**
	 * Binds either a recovery component for the protection point handling, for
	 * the incoming data stream elements or for the operator states.
	 *
	 * @param component
	 *            The component to bind, if its name matches "Protection Points"
	 *            , "Incoming Elements" or "Operator State".
	 */
	public static void bindComponent(IRecoveryComponent component) {
		if (component.getName().equals("Protection Points")) {
			cProtectionPointsComponent = component;
		} else if (component.getName().equals("Incoming Elements")) {
			cIncomingElementsComponent = component;
		} else if (component.getName().equals("Operator State")) {
			cOperatorStateComponent = component;
		}
	}

	/**
	 * Unbinds a recovery component
	 * 
	 * @param component
	 *            The component to unbind, if it has been bound before.
	 */
	public static void unbindComponent(IRecoveryComponent component) {
		if (component.equals(cProtectionPointsComponent)) {
			cProtectionPointsComponent = null;
		} else if (component.equals(cIncomingElementsComponent)) {
			cIncomingElementsComponent = null;
		} else if (component.getName().equals("Operator State")) {
			cOperatorStateComponent = null;
		}
	}

	/**
	 * The initialized recovery component for the protection point handling.
	 */
	private IRecoveryComponent mProtectionPointsComponent;

	/**
	 * The initialized recovery component for the incoming data stream elements.
	 */
	private IRecoveryComponent mSourceRecoveryComponent;

	/**
	 * The initialized recovery component for the operator states.
	 */
	private IRecoveryComponent mOperatorStateComponent;

	/**
	 * Empty default constructor for OSGi-DS.
	 */
	public RollbackRecoveryExecutor() {
	}

	/**
	 * Constructor, which initializes all bound recovery components.
	 * 
	 * @param config
	 *            The configuration for the executor.
	 */
	public RollbackRecoveryExecutor(Properties config) {
		config.put("decreasedTrust", new Double(0.5));
		this.mConfig = config;
		this.mProtectionPointsComponent = cProtectionPointsComponent.newInstance(config);
		this.mSourceRecoveryComponent = cIncomingElementsComponent.newInstance(config);
		this.mOperatorStateComponent = cOperatorStateComponent.newInstance(config);
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		return new RollbackRecoveryExecutor(config);
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		List<ILogicalQuery> modifiedQueries = Lists.newArrayList(queries);
		if (this.mProtectionPointsComponent == null) {
			cLog.error("RollBackRecovery executor misses Protection Points recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mProtectionPointsComponent.recover(qbConfig, caller, modifiedQueries);

		if (this.mSourceRecoveryComponent == null) {
			cLog.error("RollBackRecovery executor misses Incoming Elements recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mSourceRecoveryComponent.recover(qbConfig, caller, modifiedQueries);

		if (this.mOperatorStateComponent == null) {
			cLog.error("RollBackRecovery executor misses Operator State recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mOperatorStateComponent.recover(qbConfig, caller, modifiedQueries);

		return modifiedQueries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		List<ILogicalQuery> modifiedQueries = Lists.newArrayList(queries);
		if (this.mProtectionPointsComponent == null) {
			cLog.error("RollBackRecovery executor misses Protection Points recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mProtectionPointsComponent.activateBackup(qbConfig, caller, modifiedQueries);

		if (this.mSourceRecoveryComponent == null) {
			cLog.error("RollBackRecovery executor misses Incoming Elements recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mSourceRecoveryComponent.activateBackup(qbConfig, caller, modifiedQueries);

		if (this.mOperatorStateComponent == null) {
			cLog.error("RollBackRecovery executor misses Operator State recovery component!");
			return modifiedQueries;
		}
		modifiedQueries = this.mOperatorStateComponent.activateBackup(qbConfig, caller, modifiedQueries);

		return modifiedQueries;
	}

}