package measure.windperformancercp.event;

import measure.windperformancercp.model.query.IPerformanceQuery;
import measure.windperformancercp.views.IPresenter;


public class QueryEvent extends AbstractEvent<IPresenter, IPerformanceQuery> {
	
	public QueryEvent(IPresenter source, QueryEventType type, IPerformanceQuery value) {
		//public InputDialogEvent(AbstractUIDialog source, InputDialogEventType type, IDialogResult value) {
			super(source,type,value);
		}
		
		public IPresenter getPresenter(){
			return getSender();
		}
		
		public IEventType getAttEventType() {
			return (QueryEventType) getEventType();
		}
		
		public IPerformanceQuery getResult(){
			return (IPerformanceQuery) getValue();
		}
	
}
