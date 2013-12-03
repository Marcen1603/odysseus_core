package de.uniol.inf.is.odysseus.pattern.util;

/**
 * Alle M�glichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * INPUT legt das Ausgabeschema auf einer der Inputports. Ben�tigt weiterhin den Parameter inputport (0..n).
 * EXPRESSIONS gibt Tupel mit �ber return-Expressions festgelegten Attributen. Kann null-Werte produzieren.
 * TUPLE_CONTAINER gibt die Tupel in einem Tupel-Container aus. Pro Tuple ein Element. Vorteil: Ausgabe ist schema-unabh�ngig.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, INPUT, EXPRESSIONS, TUPLE_CONTAINER;
}
