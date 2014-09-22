package de.uniol.inf.is.odysseus.peer.distribute.util;

import java.util.List;

import com.google.common.base.Preconditions;

public final class InterfaceParametersPair<T extends INamedInterface> {

	private final T interf;
	private final List<String> parameters;
	
	public InterfaceParametersPair( T interf, List<String> parameters ) {
		Preconditions.checkNotNull(interf, "Named interface must not be null!");
		Preconditions.checkNotNull(parameters, "parameters must not be null!");
		
		this.interf = interf;
		this.parameters = parameters;
	}
	
	public T getInterface() {
		return interf;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
}
