package measure.windperformancercp.event;

public enum QueryEventType implements IEventType {
	AddQuery,
	DeleteQuery,
	SuccessfulConntect,
	FailureConnect,
	SuccessfulDisconnect,
	FailureDisconnect
}
