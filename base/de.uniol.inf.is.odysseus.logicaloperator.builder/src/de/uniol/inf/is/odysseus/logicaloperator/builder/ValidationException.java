package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -1602573291200567065L;
	private ArrayList<Exception> exceptions;

	public ValidationException(String operator, List<Exception> e) {
		super(getString(operator, e));
		this.exceptions = new ArrayList<Exception>(e);
	}

	private static String getString(String operator, List<Exception> exceptions) {
		StringBuilder builder = new StringBuilder();
		builder.append("validation error in ").append(operator).append(":");
		for (Exception e : exceptions) {
			builder.append('\n');
			builder.append(e);
		}
		return builder.toString();
	}

	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}
}
