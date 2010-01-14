package de.uniol.inf.is.odysseus.p2p.queryhandling;

public enum Lifecycle {
	// Changed OPEN --> NEW, CLOSED --> TERMINATED
	NEW, SPLIT, DISTRIBUTION, GRANTED, RUNNING, TERMINATED, FAILED, SUCCESS
}
