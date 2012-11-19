package de.uniol.inf.is.odysseus.core.metadata;

import de.uniol.inf.is.odysseus.core.Order;

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
	protected IStreamObject<M> process_merge(IStreamObject<M> left,
			IStreamObject<M> right, Order order) {
		if (order == Order.LeftRight){
			return new StreamString<M>(((StreamString<M>)left).content+((StreamString<M>)right).content);
		}else{
			return new StreamString<M>(((StreamString<M>)right).content+((StreamString<M>)left).content);
		}
	}
	
	@Override
	public AbstractStreamObject<M> clone() {
		return this;
	}


}
