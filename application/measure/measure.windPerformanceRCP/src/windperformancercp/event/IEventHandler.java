package windperformancercp.event;

public interface IEventHandler {

	public void subscribe(IEventListener listener, IEventType type);

	public void unsubscribe(IEventListener listener, IEventType type);

	public void subscribeToAll(IEventListener listener);

	public void unSubscribeFromAll(IEventListener listener);

	public void fire(IEvent<?, ?> event);

}
