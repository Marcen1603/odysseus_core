package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class PreTransformationHandlerParameter extends Setting<Object> implements IQueryBuildSetting<Object>{

	public static class Pair {
		
		public String name;
		public List<String> parameters;
		
		public Pair( String name, List<String> parameters ) {
			this.name = name;
			this.parameters = parameters;
		}
	}
	
	
	private final List<Pair> usedTransformationHandlers = Lists.newArrayList();

	public PreTransformationHandlerParameter() {
		super(null);
		
		setValue(usedTransformationHandlers);
	}
	
	public final void add( String interfaceName, List<String> parameters ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(interfaceName), "PreTransformationHandlers's name must not be null or empty!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		usedTransformationHandlers.add(new Pair(interfaceName, parameters));
	}

	public final ImmutableList<Pair> getPairs() {
		return ImmutableList.copyOf(usedTransformationHandlers);
	}
	
	public final boolean hasPairs() {
		return !usedTransformationHandlers.isEmpty();
	}
	
	@Override
	public Object getValue() {
		return getPairs();
	}
}
