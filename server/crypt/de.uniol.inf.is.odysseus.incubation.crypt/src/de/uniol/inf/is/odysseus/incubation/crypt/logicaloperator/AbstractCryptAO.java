/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author MarkMilster
 *
 */
public abstract class AbstractCryptAO extends UnaryLogicalOp {
	
	public static final String ENCRYPT_MODE = "Encrypt";
	public static final String DECRYPT_MODE = "Decrypt";
	
	private static final long serialVersionUID = -3775485595649303579L;
	
	private String algorithm;
	private String initVector;
	private String mode;
	private List<SDFAttribute> attributes;
	
	public AbstractCryptAO() {
		super();
	}
	
	public AbstractCryptAO(AbstractCryptAO abstractCryptAO) {
		super(abstractCryptAO);
		this.algorithm = abstractCryptAO.algorithm;
		this.initVector = abstractCryptAO.initVector;
		this.mode = abstractCryptAO.mode;
		this.attributes = abstractCryptAO.attributes;
	}
	
	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * @return the initVector
	 */
	public String getInitVector() {
		return initVector;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	
	@Parameter(type = StringParameter.class, name = "algorithm", optional = false)
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Parameter(type = StringParameter.class, name = "initVector", optional = true)
	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}
	
	@Parameter(type = StringParameter.class, name = "mode", optional = false)
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the attributes
	 */
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "attributes", optional = true, isList=true)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

}
