package de.uniol.inf.is.odysseus.pattern.util;

/**
 * Repräsentiert den Pattern-Typ. 
 * @author Michael Falk
 */
public enum PatternType {
	ALL, ANY, ABSENCE,
	FUNCTOR,
	ALWAYS, SOMETIMES,
	RELATIVE_N_HIGHEST, RELATIVE_N_LOWEST,
	FIRST_N, LAST_N,
	INCREASING, DECREASING,
	STABLE, NON_STABLE, MIXED,
	NON_INCREASING, NON_DECREASING;
}
