package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;

public class LogicalOperatorResponse extends Response {

	
	private ILogicalOperator responseValue;

	public LogicalOperatorResponse() {
		super();
	}

	public LogicalOperatorResponse(ILogicalOperator value, boolean success) {
		super(success);
		this.responseValue = value;
	}

	public ILogicalOperator getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(ILogicalOperator value) {
		this.responseValue = value;
	}
}
