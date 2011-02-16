package measure.windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import measure.windperformancercp.model.IModel;
import measure.windperformancercp.model.sources.ISource;


public class SourceModelEvent extends AbstractEvent<IModel, ISource>{
	
	public SourceModelEvent(IModel model, SourceModelEventType type, ISource value){
		super(model, type, value);		
	}
		
}
