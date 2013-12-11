package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class AbstractQueryDistributionParameter extends Setting<String> implements IQueryBuildSetting<String> {

	private final List<String> parameters = Lists.newArrayList();

	protected AbstractQueryDistributionParameter(String value, List<String> parameters) {
		super(value);
		
		Preconditions.checkNotNull(parameters, "Parameter list of query part allocator parameter must not be null!");
		this.parameters.addAll(parameters);
	}

	public final ImmutableList<String> getParameters() {
		return ImmutableList.copyOf(parameters);
	}
}
