/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author MarkMilster
 *
 */
public class HybridCryptAO extends AbstractCryptAO {
	
	private static final long serialVersionUID = 6859117029462630361L;
	
	private String receiverKey;
	
	/**
	 * @return the receiver_key
	 */
	public String getReceiverKey() {
		return receiverKey;
	}
	
	public HybridCryptAO() {
		super();
	}
	
	public HybridCryptAO(HybridCryptAO hybridCryptAO) {
		super(hybridCryptAO);
		this.receiverKey = hybridCryptAO.receiverKey;
	}
	
	@Parameter(type = StringParameter.class, name = "receiverKey", optional = true)
	public void setReceiverKey(String receiverKey) {
		this.receiverKey = receiverKey;
	}

	@Override
	public HybridCryptAO clone() {
		return new HybridCryptAO(this);
	}
	
}
