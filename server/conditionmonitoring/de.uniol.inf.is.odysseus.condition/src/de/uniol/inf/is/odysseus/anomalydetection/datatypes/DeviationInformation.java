package de.uniol.inf.is.odysseus.anomalydetection.datatypes;

/**
 * A small helper object to store the deviation information for the different
 * groups
 */
public class DeviationInformation {

	// For all
	public double standardDeviation;
	public double mean;

	// For online calculation
	public long n;
	public double m2;

	// For window (approximate) calculation
	public double k;
	public double sumWindow;
	public double sumWindowSqr;

	// For offline calculation
	public double sum1;
	public double sum2;
}
