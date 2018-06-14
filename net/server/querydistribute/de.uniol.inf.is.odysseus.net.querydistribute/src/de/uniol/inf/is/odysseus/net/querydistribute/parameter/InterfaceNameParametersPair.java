package de.uniol.inf.is.odysseus.net.querydistribute.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class InterfaceNameParametersPair implements Serializable {

	private static final long serialVersionUID = -3413067693521733317L;
	
	private final String name;
	private final List<String> parameters;
	
	public InterfaceNameParametersPair( String name, List<String> parameters ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "name must not be null or empty!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		this.name = name;
		// ArrayList is serializable
		this.parameters = new ArrayList<>(parameters);
	}
	
	public String getName() {
		return name;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
}
