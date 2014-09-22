package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class AbstractQueryDistributionParameter extends Setting<Object> implements IQueryBuildSetting<Object> {

	private final List<InterfaceNameParametersPair> pairs = Lists.newArrayList();

	protected AbstractQueryDistributionParameter() {
		super(null);
		
		setValue(pairs);
	}
	
	protected AbstractQueryDistributionParameter( String interfaceName, List<String> parameters ) {
		this();
		
		add(interfaceName, parameters);
	}
	
	public final void add( String interfaceName, List<String> parameters ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(interfaceName), "interfaceName must not be null or empty!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		pairs.add(new InterfaceNameParametersPair(interfaceName, parameters));
	}

	public final ImmutableList<InterfaceNameParametersPair> getPairs() {
		return ImmutableList.copyOf(pairs);
	}
	
	@Override
	public Object getValue() {
		return getPairs();
	}
}
