package measure.windperformancercp.event;

import measure.windperformancercp.model.query.IPerformanceQuery;


public class QueryEvent extends AbstractEvent<Object, IPerformanceQuery> {
	
	public QueryEvent(Object source, QueryEventType type, IPerformanceQuery value) {
			super(source,type,value);
		}
		
		public Object getPresenter(){
			return getSender();
		}
		
		public IEventType getAttEventType() {
			return (QueryEventType) getEventType();
		}
		
		public IPerformanceQuery getResult(){
			return (IPerformanceQuery) getValue();
		}
	
}
