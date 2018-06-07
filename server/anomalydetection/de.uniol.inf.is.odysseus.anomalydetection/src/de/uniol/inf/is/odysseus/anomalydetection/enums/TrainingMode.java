package de.uniol.inf.is.odysseus.anomalydetection.enums;

/**
 * Some operators have multiple training modes, typically these. Online:
 * Permanent training, does not forget old values Manual: The values are not
 * learned but given by the user Tuple based: Learns a given number of tuples,
 * e.g. 100 Window: Only uses the tuples in the window
 * 
 * @author Tobias Brandt
 *
 */
public enum TrainingMode {
	ONLINE, MANUAL, TUPLE_BASED, WINDOW
}
