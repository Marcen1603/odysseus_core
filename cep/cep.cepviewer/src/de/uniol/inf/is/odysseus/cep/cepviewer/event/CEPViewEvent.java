package de.uniol.inf.is.odysseus.cep.cepviewer.event;
import java.util.EventObject;

@SuppressWarnings("serial")
public class CEPViewEvent extends EventObject {
	
	public static final int ITEM_SELECTED = 0;
	public static final int STATE_SELECTED = 1;
	
	private Object content;
	private int type;

	public CEPViewEvent(Object source, int type, Object content) {
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
