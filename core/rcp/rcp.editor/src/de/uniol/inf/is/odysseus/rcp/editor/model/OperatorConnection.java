package de.uniol.inf.is.odysseus.rcp.editor.model;

import java.io.Serializable;

public class OperatorConnection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Operator source = null;
	private Operator target = null;
	
	public OperatorConnection() {
		
	}
	
	public void setSource( Operator src ) {
		source = src;
	}
	
	public void setTarget( Operator tgt ) {
		target = tgt;
	}
	
	public Operator getSource() {
		return source;
	}
	
	public Operator getTarget() {
		return target;
	}
}
