package de.uniol.inf.is.odysseus.core.logicaloperator;

/**
 * An enum to define input requirements
 * 
 * @author Marco Grawunder
 *
 */

public enum InputOrderRequirement {
	STRICT /* The operator need strict input ordering */, 
	NONE /* the operator does not care about order */, 
	SELFHANDLED /*the operator has an order handling*/
};