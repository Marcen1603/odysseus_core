package measure.windperformancercp.event;

//import de.uniol.inf.is.odysseus.event.AbstractEvent;
//import org.eclipse.swt.events.TypedEvent;

import measure.windperformancercp.model.IModel;
import measure.windperformancercp.model.sources.IDialogResult;


public class ModelEvent extends AbstractEvent<IModel, IDialogResult>{
	
	public ModelEvent(IModel model, ModelEventType type, IDialogResult value){
		super(model, type, value);		
	}
		
}
