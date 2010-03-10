package de.uniol.inf.is.odysseus.broker.transaction;

public class TransactionType {
	public enum Read{
		OneTime, Continuous, Cyclic
	}
	
	public enum Write{
		Continuous, Timestamp, Cyclic 
	}
}
