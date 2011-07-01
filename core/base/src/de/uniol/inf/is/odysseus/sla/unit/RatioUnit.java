package de.uniol.inf.is.odysseus.sla.unit;

/**
 * Possible units for ratio values.
 * 
 * percent: the value is given as 1/100, so the range should normally be 0 to
 * 100
 * 
 * absolute: the value is an absolute ratio, so the range should normally be 0
 * to 1
 * 
 * @author Thomas Vogelgesang
 * 
 */
public enum RatioUnit {

	percent, absolute;
}
