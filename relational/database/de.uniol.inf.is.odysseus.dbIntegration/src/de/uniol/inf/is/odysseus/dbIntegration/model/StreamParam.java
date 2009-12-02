package de.uniol.inf.is.odysseus.dbIntegration.model;

import java.util.List;

public class StreamParam {

	private List<Object> params;
	
	public StreamParam(List<Object> params) {
		super();
		this.params = params;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
	
	
}
