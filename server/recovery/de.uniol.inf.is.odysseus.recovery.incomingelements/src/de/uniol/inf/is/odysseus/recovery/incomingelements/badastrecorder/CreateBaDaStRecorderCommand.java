package de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.incomingelements.BaDaStSender;

// FIXME Interface IBaDaStRecorder is known, but no concrete implementation. They are within the BaDaSt application bundle.
/**
 * {@code IExecutorCommand} to create and start an {@code IBaDaStRecorder} for a
 * given source.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class CreateBaDaStRecorderCommand extends AbstractExecutorCommand {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1179560987235141601L;

	/**
	 * The recorder type.
	 */
	private final String mType;

	/**
	 * The full source name.
	 */
	private final Resource mSourcename;

	/**
	 * The keys for all needed recorder parameters.
	 */
	private final Collection<String> mParameterKeys;

	/**
	 * Creates a new {@code IExecutorCommand} to create and start an
	 * {@code IBaDaStRecorder} for a given source.
	 * 
	 * @param caller
	 *            The current session.
	 * @param type
	 *            The recorder type.
	 * @param sourcename
	 *            The full source name.
	 * @param parameterKeys
	 *            The keys for all needed recorder parameters.
	 */
	public CreateBaDaStRecorderCommand(ISession caller, String type, Resource sourcename,
			Collection<String> parameterKeys) {
		super(caller);
		this.mType = type;
		this.mSourcename = sourcename;
		this.mParameterKeys = parameterKeys;
	}

	/**
	 * Retrieves the access to the source from {@code IDataDictionary}.
	 * 
	 * @param dd
	 *            An {@code IDataDictionary}.
	 * @return The {@code AbstractAccessAO} representing the stream or view.
	 * @throws DataDictionaryException
	 *             if there is no stream or view within the
	 *             {@code IDataDictionary} with the given name.
	 */
	private AbstractAccessAO getSourceAccess(IDataDictionary dd) throws DataDictionaryException {
		ILogicalOperator sourceAccess = dd.getViewOrStream(this.mSourcename.getResourceName(), getCaller());
		if (sourceAccess == null) {
			throw new DataDictionaryException("CreateBaDaStRecorderCommand: " + this.mSourcename.getResourceName()
					+ " is an unknown source name!");
		}
		// Note: this must not be the direct source, can be a rename or
		// something else.
		return getSourceAccessRecursive(sourceAccess);
	}

	/**
	 * Search recursively for an {@code AbstractAccessAO} with a given source
	 * name (depth-first-search).
	 * 
	 * @param topOperator
	 *            The operator to check and to search from towards sources.
	 * @return The found {@code AbstractAccessAO} or null.
	 */
	private AbstractAccessAO getSourceAccessRecursive(ILogicalOperator topOperator) {
		if (AbstractAccessAO.class.isInstance(topOperator)
				&& ((AbstractAccessAO) topOperator).getAccessAOName().equals(this.mSourcename)) {
			// Break condition success
			return (AbstractAccessAO) topOperator;
		}

		for (LogicalSubscription subToSource : topOperator.getSubscribedToSource()) {
			AbstractAccessAO sourceAccess = getSourceAccessRecursive(subToSource.getTarget());
			if (sourceAccess != null) {
				// Break condition success
				return sourceAccess;
			}
		}

		// Break condition failure
		return null;
	}

	/**
	 * Puts together the configuration for the {@code IBaDaStRecorder} to
	 * create.
	 * 
	 * @param sourceAccessParameters
	 *            The parameter mapping of the source access (
	 *            {@code ILogicalOperator}).
	 * @return The configuration as {@code Properties} object.
	 */
	private Properties createRecorderConfig(Map<String, String> sourceAccessParameters) {
		Properties config = new Properties();
		config.put(BaDaStRecorderKeyword.KEY_TYPE, this.mType);
		config.put(BaDaStRecorderKeyword.KEY_SOURCENAME, this.mSourcename.toString());
		for (String key : this.mParameterKeys) {
			config.put(key, sourceAccessParameters.get(key));
		}
		return config;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		// Check, if DataDictionary contains source name
		AbstractAccessAO sourceAccess = getSourceAccess(dd);
		if (sourceAccess == null) {
			throw new DataDictionaryException("CreateBaDaStRecorderCommand: " + this.mSourcename.getResourceName()
					+ " is an unknown source name!");
		}
		// Check, if source definition contains all needed options
		// (parameters of ABaDaStRecorder)
		Map<String, String> sourceAccessParameters = sourceAccess.getOptionsMap();
		for (String recorderParameter : this.mParameterKeys) {
			if (!sourceAccessParameters.keySet().contains(recorderParameter)) {
				throw new RuntimeException("CreateBaDaStRecorderCommand: " + this.mSourcename.getResourceName()
						+ " misses the parameter " + recorderParameter + "!");
			}
		}
		// Check, if there is already an recorder running for that source
		String recorderName = BaDaStRecorderRegistry.getRecorder(this.mSourcename.toString());
		if (recorderName != null) {
			// Nothing to do. Recorder is running
			return;
		}
		// Put together to configuration for the recorder
		Properties config = createRecorderConfig(sourceAccessParameters);
		recorderName = BaDaStSender.sendCreateCommand(config);
		if (recorderName == null) {
			throw new RuntimeException("CreateBaDaStRecorderCommand: Could not create BaDaSt recorder!");
		}
		BaDaStRecorderRegistry.register(this.mSourcename.toString(), recorderName);
		BaDaStSender.sendStartCommand(recorderName);
	}

}
