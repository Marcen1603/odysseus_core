package windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import windperformancercp.model.sources.ISource;
import windperformancercp.model.sources.SourceModel;


public class SourceModelEvent extends AbstractEvent<SourceModel, ISource>{
	
	public SourceModelEvent(SourceModel source, SourceModelEventType type, ISource value){
		super(source, type, value);		
	}
	
		
}
