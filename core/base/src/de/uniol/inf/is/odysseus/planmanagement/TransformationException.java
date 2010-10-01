package de.uniol.inf.is.odysseus.planmanagement;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;

public class TransformationException extends Exception {

	private static final long serialVersionUID = -8930049065359776237L;
	private List<ILogicalOperator> untranslatedOperators;
	private TransformationConfiguration config;

	public TransformationException() {
	}

	public TransformationException(String message) {
		super(message);
	}

	public TransformationException(Throwable cause) {
		super(cause);
	}

	public TransformationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformationException(TransformationConfiguration config,
			List<ILogicalOperator> errors) {
		this.config = config;
		this.untranslatedOperators = errors;
	}

	public List<ILogicalOperator> getUntranslatedOperators() {
		return untranslatedOperators;
	}

	public TransformationConfiguration getConfig() {
		return config;
	}
	
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder("transformation failed");
		if (untranslatedOperators != null) {
			builder.append("; unable to transform: ");
			for(ILogicalOperator op : this.untranslatedOperators) {
				builder.append(op.toString());
			}
		}
		if (this.config != null) {
			builder.append("; configuration used " + config.toString());
		}
		return builder.toString(); 
	}

}
