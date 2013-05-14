package de.uniol.inf.is.odysseus.pattern.model;

/**
 * Repräsentiert den Pattern-Typ. 
 * @author Michael Falk
 */
public enum PatternType {
	ALL, ANY, ABSENCE,
	COUNT, FUNCTOR,
	ALWAYS, SOMETIMES,
	VALUE_MAX, VALUE_MIN,
	RELATIVE_N_HIGHEST, RELATIVE_N_LOWEST,
	FIRST_N_PATTERN, LAST_N_PATTERN;
}
