/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author MarkMilster
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CRYPT_SIMPLE", doc = "Cryptographic functionality", category = {
		LogicalOperatorCategory.ADVANCED })
public class SimpleCryptAO extends AbstractCryptAO {

	private static final long serialVersionUID = -8015847542104588689L;

	private String key;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	public SimpleCryptAO() {
		super();
	}

	public SimpleCryptAO(SimpleCryptAO cryptAO) {
		super(cryptAO);
		this.key = cryptAO.key;
	}

	@Parameter(type = StringParameter.class, name = "key", optional = true)
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public SimpleCryptAO clone() {
		return new SimpleCryptAO(this);
	}

}
