package de.uniol.inf.is.odysseus.peer.distribute.parameter;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class InterfaceNameParametersPair {

	private final String name;
	private final List<String> parameters;
	
	public InterfaceNameParametersPair( String name, List<String> parameters ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		this.name = name;
		this.parameters = parameters;
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
}
