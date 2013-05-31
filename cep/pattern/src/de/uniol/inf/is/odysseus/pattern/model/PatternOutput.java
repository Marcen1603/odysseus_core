package de.uniol.inf.is.odysseus.pattern.model;

/**
 * Alle Möglichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * EXPRESSIONS gibt Tupel mit über return-Expressions festgelegten Attributen.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS;
}
