package model;

/**
 * Alle Möglichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Pattern-Typ und true zurück.
 * EXPRESSIONS gibt Tupel mit über return-Expressions festgelegten Attributen zurück.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS;
}
