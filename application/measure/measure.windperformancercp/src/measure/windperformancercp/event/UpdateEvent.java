package measure.windperformancercp.event;

import java.util.ArrayList;

import measure.windperformancercp.views.IPresenter;


public class UpdateEvent extends AbstractEvent<IPresenter, ArrayList<?>> {
	
	public UpdateEvent(IPresenter source, UpdateEventType type, ArrayList<?> value) {
		super(source,type,value);
	}
	
	
	
}
