/**
 * 
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

public enum WindowType {
    PERIODIC_TIME_WINDOW, // Zeit mit SLIDE
    PERIODIC_TUPLE_WINDOW, // Tupel mit SLIDE
    
    SLIDING_TIME_WINDOW, // Zeit mit ADVANCE (ADVANCE == 1)
    JUMPING_TIME_WINDOW, // Zeit mit ADVANCE
    FIXED_TIME_WINDOW, // Zeit mit ADVANCE (ADVANCE == RANGE)
    
    SLIDING_TUPLE_WINDOW, // ElementBasiertes (Advance == 1)
    JUMPING_TUPLE_WINDOW, // ElementBasiertes mit Advance (im Moment nur CQL)
    START_END_PREDICATE_WINDOW} // Start-/Ende-Praedikatfenster (im Moment nur Streaming SPARQL)