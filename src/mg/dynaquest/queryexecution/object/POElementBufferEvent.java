package mg.dynaquest.queryexecution.object;

import mg.dynaquest.queryexecution.event.POEvent;

abstract public class POElementBufferEvent extends POEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5560888777535208371L;

	POElementBufferEvent(POElementBuffer source) {
		super(source);
	}
}