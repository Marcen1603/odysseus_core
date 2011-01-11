package windperformancercp.event;

import java.util.ArrayList;

import windperformancercp.model.sources.IDialogResult;
import windperformancercp.views.IPresenter;

public class UpdateEvent extends AbstractEvent<IPresenter, ArrayList<?>> {
	
	public UpdateEvent(IPresenter source, UpdateEventType type, ArrayList<?> value) {
		super(source,type,value);
	}
	
	
	
}
