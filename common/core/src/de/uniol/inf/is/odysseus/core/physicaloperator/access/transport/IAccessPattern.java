package de.uniol.inf.is.odysseus.core.physicaloperator.access.transport;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public enum IAccessPattern {
    /** Data push from external sources */
    PUSH,
    /** Data pull from external sources */
    PULL,
    /** Data push from external sources requiring acknowledgement */
    ROBUST_PUSH,
    /** Data pull from external sources requiring acknowledgement */
    ROBUST_PULL
}
