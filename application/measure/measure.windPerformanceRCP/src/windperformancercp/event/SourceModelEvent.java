package windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import windperformancercp.model.IModel;
import windperformancercp.model.sources.ISource;


public class SourceModelEvent extends AbstractEvent<IModel, ISource>{
	
	public SourceModelEvent(IModel model, SourceModelEventType type, ISource value){
		super(model, type, value);		
	}
		
}
