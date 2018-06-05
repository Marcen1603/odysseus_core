package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * This command is used to call addQuery on the executor. This is typically
 * created from Odysseus Script
 * 
 * @author Marco Grawunder
 *
 */

public class AddQueryCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -161042295238718034L;

	private String queryText;
	private String parserID;
	private String transCfgName;
	private Context context;
	private List<IQueryBuildSetting<?>> addSettings;
	private boolean startQueries;

	public AddQueryCommand(String queryText, String parserID, ISession caller, String transCfgName, Context context,
			List<IQueryBuildSetting<?>> addSettings, boolean startQueries) {
		super(caller);
		this.queryText = queryText;
		this.parserID = parserID;
		this.transCfgName = transCfgName;
		this.context = context;
		this.addSettings = new ArrayList<>(addSettings);
		this.startQueries = startQueries;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		Collection<Integer> queries = executor.addQuery(queryText, parserID, getCaller(), transCfgName, context,
				addSettings);
		if (startQueries) {
			for (Integer q : queries) {
				executor.startQuery(q, getCaller());
			}
		}
		addCreatedQueryIds(queries);
	}

	public String getQueryText() {
		return this.queryText;
	}

	public String getParserId() {
		return this.parserID;
	}

	public String getTransCfgName() {
		return this.transCfgName;
	}

	public Context getContext() {
		return this.context;
	}

	public List<IQueryBuildSetting<?>> getAddSettings() {
		return this.addSettings;
	}

	public boolean startQueries() {
		return this.startQueries;
	}

}
