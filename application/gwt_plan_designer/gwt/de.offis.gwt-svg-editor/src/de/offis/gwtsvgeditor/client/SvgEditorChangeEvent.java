package de.offis.gwtsvgeditor.client;

/**
 * Event to indicate different actions. 
 * (link created, module created, link removed, module removed)
 *
 * @author Alexander Funk
 * 
 */
public class SvgEditorChangeEvent {
	
	public static final int CREATED_LINK = 0;
	public static final int CREATED_MODULE = 1;
	public static final int REMOVED_LINK = 2;
	public static final int REMOVED_MODULE = 3;
	
	private int type = -1;
	
	public SvgEditorChangeEvent(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
