package de.uniol.inf.is.odysseus.core.metadata;

public class StreamString<M extends IMetaAttribute> extends AbstractStreamObject<M> {

	private static final long serialVersionUID = -8427379583652445078L;
	String content;
	
	public StreamString(String content){
		this.content = content;
	}
	
	@Override
	public String toString() {
		return ""+content;
	}
		
	@Override
	public AbstractStreamObject<M> clone() {
		return this;
	}


}
