package de.uniol.inf.is.odysseus.cep.epa.event;
import java.util.EventObject;

@SuppressWarnings("serial")
public class CEPEvent extends EventObject {
	
	public static final int ADD_MASCHINE = 0;
	public static final int CHANGE_STATE = 1;
	public static final int MACHINE_ABORTED = 2;
	public static final int SPLIT_MACHINE = 3;

	
	private Object content;
	private int type;

	public CEPEvent(Object source, int type, Object content) {
		super(source);
		this.type = type;
		this.content = content;
	}
	
	public int getType() {
		return this.type;
	}
	
	public Object getContent() {
		return this.content;
	}

}
