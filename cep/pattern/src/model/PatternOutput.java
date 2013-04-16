package model;

/**
 * Alle M�glichkeiten der Ausgabe von erkannten Events.
 * 
 * SIMPLE gibt nur den Pattern-Typ und true zur�ck.
 * EXPRESSIONS gibt Tupel mit �ber return-Expressions festgelegten Attributen zur�ck.
 * 
 * @author Michael Falk
 */
public enum PatternOutput {
	SIMPLE, EXPRESSIONS;
}
