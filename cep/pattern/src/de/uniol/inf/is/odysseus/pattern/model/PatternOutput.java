package de.uniol.inf.is.odysseus.pattern.model;

/**
 * Alle M�glichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * EXPRESSIONS gibt Tupel mit �ber return-Expressions festgelegten Attributen. Kann null-Werte produzieren.
 * TUPLE_CONTAINER gibt die Tupel in einem Tupel-Container aus. Pro Tuple ein Element. Vorteil: Ausgabe ist schema-unabh�ngig.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS, TUPLE_CONTAINER;
}
