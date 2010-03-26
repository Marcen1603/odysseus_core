package de.uniol.inf.is.odysseus.base;

/**
 * Used by MigrationHelper to abstract from window appraoches.
 * 
 * @author Tobias Witt
 *
 */
public interface IWindow {
	public enum Type {
		ELEMENT_BASED,
		TIME_BASED,
		OTHER
	};
	public long getWindowSize();
	public Type getWindowType();
}
