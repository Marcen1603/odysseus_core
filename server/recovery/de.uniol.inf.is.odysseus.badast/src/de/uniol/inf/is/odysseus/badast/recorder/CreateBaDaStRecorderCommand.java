package de.uniol.inf.is.odysseus.badast.recorder;

import java.util.Collection;
import java.util.Properties;

import de.uniol.inf.is.odysseus.badast.BaDaStSender;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * {@code IExecutorCommand} to create and start an {@code IBaDaStRecorder} for a
 * given source.
 *
 * @author Michael Brand
 *
 */
public class CreateBaDaStRecorderCommand extends AbstractExecutorCommand {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4132981191968220429L;

	/**
	 * The recorder type.
	 */
	private final String type;

	/**
	 * The full source name.
	 */
	private final Resource sourcename;

	/**
	 * The full view name.
	 */
	private final Resource viewname;

	/**
	 * The keys for all needed recorder parameters.
	 */
	private final Collection<String> parameterKeys;

	/**
	 * Creates a new {@code IExecutorCommand} to create and start an
	 * {@code IBaDaStRecorder} for a given source.
	 *
	 * @param session
	 *            The current session.
	 * @param type
	 *            The recorder type.
	 * @param sourcename
	 *            The full source name.
	 * @param viewname
	 *            The full view name.
	 * @param parameterKeys
	 *            The keys for all needed recorder parameters.
	 */
	public CreateBaDaStRecorderCommand(ISession session, String type, Resource sourcename, Resource viewname,
			Collection<String> parameterKeys) {
		super(session);
		this.type = type;
		this.sourcename = sourcename;
		this.viewname = viewname;
		this.parameterKeys = parameterKeys;
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
		ILogicalOperator view = dd.getViewOrStream(this.viewname.getResourceName(), getCaller()).getRoot();
		if (view == null) {
			throw new DataDictionaryException(
					"CreateBaDaStRecorderCommand: " + this.viewname.getResourceName() + " is an unknown view name!");
		}
		return getSourceAccessRecursive(view, dd);
	}

	/**
	 * Search recursively for an {@code AbstractAccessAO} with a given source
	 * name (depth-first-search).
	 *
	 * @param topOperator
	 *            The operator to check and to search from towards sources.
	 * @return The found {@code AbstractAccessAO} or null.
	 */
	private AbstractAccessAO getSourceAccessRecursive(ILogicalOperator topOperator, IDataDictionary dd) {
		if (topOperator instanceof AbstractAccessAO
				&& ((AbstractAccessAO) topOperator).getAccessAOName().equals(this.sourcename)) {
			// Break condition success
			return (AbstractAccessAO) topOperator;
		} else if (topOperator instanceof StreamAO) {
			AbstractAccessAO sourceAccess = getSourceAccessRecursive(
					dd.getStreamForTransformation(this.viewname, getCaller()).getRoot(), dd);
			if (sourceAccess != null) {
				// Break condition success
				return sourceAccess;
			}
		}

		for (LogicalSubscription subToSource : topOperator.getSubscribedToSource()) {
			AbstractAccessAO sourceAccess = getSourceAccessRecursive(subToSource.getSource(), dd);
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
	private Properties createRecorderConfig(OptionMap sourceAccessParameters) {
		Properties config = new Properties();
		config.put(IBaDaStRecorder.TYPE_CONFIG, this.type);
		config.put(IBaDaStRecorder.SOURCENAME_CONFIG, this.sourcename.toString());
		config.put(IBaDaStRecorder.VIEWNAME_CONFIG, this.viewname.toString());
		for (String key : this.parameterKeys) {
			config.put(key, sourceAccessParameters.get(key));
		}
		return config;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		AbstractAccessAO sourceAccess = getSourceAccess(dd);
		if (sourceAccess == null) {
			throw new DataDictionaryException("CreateBaDaStRecorderCommand: " + this.sourcename.getResourceName()
					+ " is an unknown source name!");
		}

		// Check, if source definition contains all needed options
		// (parameters of ABaDaStRecorder)
		OptionMap sourceAccessParameters = sourceAccess.getOptionsMap();
		for (String recorderParameter : this.parameterKeys) {
			if (!sourceAccessParameters.containsKey(recorderParameter)) {
				throw new RuntimeException("CreateBaDaStRecorderCommand: " + this.sourcename.getResourceName()
						+ " misses the parameter " + recorderParameter + "!");
			}
		}

		// Check, if there is already an recorder running for that source
		String recorderName = BaDaStRecorderRegistry.getRecorder(this.sourcename.toString());
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
		BaDaStRecorderRegistry.register(this.sourcename.toString(), recorderName);
		BaDaStSender.sendStartCommand(recorderName);
	}

}
