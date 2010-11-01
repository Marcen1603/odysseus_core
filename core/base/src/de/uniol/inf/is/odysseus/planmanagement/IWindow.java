package de.uniol.inf.is.odysseus.planmanagement;

/**
 * Used by MigrationHelper to abstract from window appraoches.
 * 
 * @author Tobias Witt
 *
 */
public interface IWindow {
	public enum WindowContentType {
		ELEMENT_BASED,
		TIME_BASED,
		OTHER
	};
	public long getWindowSize();
	public WindowContentType getWindowContentType();
}
