package de.uniol.inf.is.odysseus.broker.transaction;


/**
 * The Enum ReadTransaction represents reading transactions.
 * 
 * @author Dennis Geesen
 */
public enum ReadTransaction implements ITransaction{
	
	/** One time reading transaction. */
	OneTime, 
	/** Continuously reading transaction. */
	Continuous, 
	/** The reading part of a cyclic transaction. */
	Cyclic
}
