package windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import windperformancercp.model.ISource;

//public class SourceEvent extends AbstractEvent{
public class SourceEvent implements IEvent{
	private ISource source;
	private SourceEventType type;
	
	public SourceEvent(ISource source, SourceEventType type){
		//TODO
		//super(sourceID,type);		
		this.source = source;
		this.type = type;
	}
	
		
	public String toString(){
		return this.type+" from "+source;
	}

	@Override
	public IEventType getEventType() {
		return type;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Object getSender() {
		// TODO Auto-generated method stub
		return source;
	}
}
