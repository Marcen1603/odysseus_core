package de.uniol.inf.is.odysseus.jmxexampleoperator;

/**
 *
 * @author Christian Kuka
 * @version 1.0
 */
public interface ConfigurationMBean {

    /**
     * Disable processing of tuples
     * @return The state of an operator
     */
    boolean stopProcessing();

    /**
     * Enable processing of tuples
     * @return The state of the operator
     */
    boolean startProcessing();

    /**
     * Get total number of processed tuples
     * @return Processed tuples
     */
    int getProcessedTuple();
}
