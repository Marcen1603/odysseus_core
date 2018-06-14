package de.uniol.inf.is.odysseus.net.querydistribute.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.util.INamedInterface;

public final class InterfaceParametersPair<T extends INamedInterface> implements Serializable {

	private static final long serialVersionUID = -6565561845406123986L;
	
	private final T interf;
	private final List<String> parameters;
	
	public InterfaceParametersPair( T interf, List<String> parameters ) {
		Preconditions.checkNotNull(interf, "Named interface must not be null!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		this.interf = interf;
		// ArrayList is serializable
		this.parameters = new ArrayList<>(parameters);
	}
	
	public T getInterface() {
		return interf;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
}
