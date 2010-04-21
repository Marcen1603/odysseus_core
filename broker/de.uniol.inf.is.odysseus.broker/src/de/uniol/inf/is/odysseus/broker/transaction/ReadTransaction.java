package de.uniol.inf.is.odysseus.broker.transaction;

public enum ReadTransaction implements ITransaction{
	OneTime, Continuous, Cyclic
}
