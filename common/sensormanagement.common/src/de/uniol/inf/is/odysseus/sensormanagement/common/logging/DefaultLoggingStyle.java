package de.uniol.inf.is.odysseus.sensormanagement.common.logging;

// This logging style logs all elements whose timestamp difference to the previous element is >= minTimeDiff.
// Also, if a sample rate is set, elements are sampled according to this rate
public class DefaultLoggingStyle extends AbstractLoggingStyle
{
	public double minTimeDiff = 0.0;	// 0.0 -> Each element passes. 1.0 -> At least 1s between begin timestamps 
	public int sampleRate = 1;			// 1 -> Every element passes. 2 -> 1st passes, 2nd not, 3rd passes... 
}
