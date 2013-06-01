package de.uniol.inf.is.odysseus.pattern.model;

/**
 * Alle Möglichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * EXPRESSIONS gibt Tupel mit über return-Expressions festgelegten Attributen. Kann null-Werte produzieren.
 * TUPLE_CONTAINER gibt die Tupel in einem Tupel-Container aus. Pro Tuple ein Element. Vorteil: Ausgabe ist schema-unabhängig.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS, TUPLE_CONTAINER;
}
