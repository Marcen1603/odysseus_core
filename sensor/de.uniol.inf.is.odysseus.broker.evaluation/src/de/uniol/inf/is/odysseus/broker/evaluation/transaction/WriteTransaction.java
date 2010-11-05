package de.uniol.inf.is.odysseus.broker.evaluation.transaction;

/**
 * The Enum WriteTransaction represents writing transactions.
 * 
 * @author Dennis Geesen
 */
public enum WriteTransaction implements ITransaction{	
	
	/** Continuously writing transactions. */
	Continuous, 
	/** A queue transaction which writes timestamps. */
	Timestamp, 
	/** The writing part of an cyclic transaction. */
	Cyclic 
}
