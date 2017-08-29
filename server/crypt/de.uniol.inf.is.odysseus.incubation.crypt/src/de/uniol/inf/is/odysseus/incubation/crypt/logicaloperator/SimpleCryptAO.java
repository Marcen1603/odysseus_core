package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This logical operator provides the function for cryptographic algorithms.
 * 
 * @author MarkMilster
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CRYPT_SIMPLE", doc = "Cryptographic functionality", category = {
		LogicalOperatorCategory.ADVANCED })
public class SimpleCryptAO extends AbstractCryptAO {

	private static final long serialVersionUID = -8015847542104588689L;

	private String key;

	/**
	 * Returns the key in a String representation.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Default constructor
	 */
	public SimpleCryptAO() {
		super();
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptAO
	 *            The SimpleCryptAO to copy
	 */
	public SimpleCryptAO(SimpleCryptAO cryptAO) {
		super(cryptAO);
		this.key = cryptAO.key;
	}

	/**
	 * Sets the parameter Key
	 * 
	 * @param key
	 *            The key to use in the cryptographic algorithm
	 */
	@Parameter(type = StringParameter.class, name = "key", optional = true)
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public SimpleCryptAO clone() {
		return new SimpleCryptAO(this);
	}

}
