package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -1602573291200567065L;
	private ArrayList<Exception> exceptions;

	public ValidationException(List<Exception> e) {
		super(getString(e));
		this.exceptions = new ArrayList<Exception>(e);
	}

	private static String getString(List<Exception> exceptions) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (Exception e : exceptions) {
			if (!first) {
				builder.append('\n');
			} else {
				first = false;
			}
			builder.append(e);
		}
		return builder.toString();
	}

	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}
}
