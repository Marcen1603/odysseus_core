package de.uniol.inf.is.odysseus.pattern.model;

/**
 * Alle M�glichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * EXPRESSIONS gibt Tupel mit �ber return-Expressions festgelegten Attributen.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS;
}
