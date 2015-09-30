package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;

public class PreTransformationHandlerParameter extends Setting<Object> implements IQueryBuildSetting<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5385581293870142221L;

	public static class HandlerParameterPair {
		
		public String name;
		public List<Pair<String, String>> parameters;
		
		public HandlerParameterPair( String name, List<Pair<String, String>> parameters ) {
			this.name = name;
			this.parameters = parameters;
		}
	}
	
	
	private final List<HandlerParameterPair> usedTransformationHandlers = Lists.newArrayList();

	public PreTransformationHandlerParameter() {
		super(null);
		
		setValue(usedTransformationHandlers);
	}
	
	public final void add( String interfaceName, List<Pair<String, String>> parameters ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(interfaceName), "PreTransformationHandlers's name must not be null or empty!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		usedTransformationHandlers.add(new HandlerParameterPair(interfaceName, parameters));
	}

	public final ImmutableList<HandlerParameterPair> getPairs() {
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
