package de.uniol.inf.is.odysseus.util;

/**
 * @author Jonas Jacobi
 */
public abstract class ToStringVisitor<T> implements INodeVisitor<T, String> {

	private StringBuilder builder;
	private boolean wasup;

	public ToStringVisitor() {
		reset();
	}

	public void descend(T to) {
		if (this.wasup) {
			this.builder.append(',');
		} else {
			this.builder.append('(');
		}
		this.wasup = false;
	};

	@Override
	public void ascend(T sub) {
		if (this.wasup) {
			this.builder.append(" )");
		}
		this.wasup = true;
	}

	public String getResult() {
		if (this.wasup) {
			this.builder.append(" )");
		}
		return this.builder.toString();
	}

	public void reset() {
		this.builder = new StringBuilder();
		this.wasup = false;
	}
	
	protected StringBuilder getBuilder() {
		return builder;
	}
}
