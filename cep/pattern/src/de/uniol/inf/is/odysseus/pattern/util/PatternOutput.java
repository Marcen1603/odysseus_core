package de.uniol.inf.is.odysseus.pattern.util;

/**
 * Alle Möglichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Timestamp, den Pattern-Typ und true aus.
 * INPUT legt das Ausgabeschema auf einer der Inputports. Benötigt weiterhin den Parameter inputport (0..n).
 * EXPRESSIONS gibt Tupel mit über return-Expressions festgelegten Attributen. Kann null-Werte produzieren.
 * TUPLE_CONTAINER gibt die Tupel in einem Tupel-Container aus. Pro Tuple ein Element. Vorteil: Ausgabe ist schema-unabhängig.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, INPUT, EXPRESSIONS, TUPLE_CONTAINER;
}
