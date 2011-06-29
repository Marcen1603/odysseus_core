package de.uniol.inf.is.odysseus.slamodel.unit;

/**
 * possible time units for defining evaluation windows and time-based metrics.
 * 
 * @author Thomas Vogelgesang
 * 
 * abbreviations of units are used because some of them are already defined as
 * parser tokens, so they can't be read as identifier
 */
public enum TimeUnit {
	ms, s, m, h, d, months;
}
