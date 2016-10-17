package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * This logical operator provides the function for cryptographic of parameters.
 * 
 * @author MarkMilster
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CRYPT_COMMAND", doc = "Encrypting Parameters for a query on crypted datastream", category = {
		LogicalOperatorCategory.ADVANCED })
public class CryptCommandAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2829044762236991290L;

	private Map<String, Object> parameter;
	private String algorithm;
	private String initVector;
	private int receiverID;
	private int streamID;

	/**
	 * The id of the receiver to load his asymmetric key
	 * 
	 * @return the receiverID
	 */
	public int getReceiverID() {
		return receiverID;
	}

	/**
	 * The id of the receiver to load his asymmetric key
	 * 
	 * @param receiverID
	 *            the receiverID to set
	 */
	@Parameter(type = IntegerParameter.class, name = "receiverID", optional = false)
	public void setReceiverID(int receiverID) {
		this.receiverID = receiverID;
	}

	/**
	 * Returns the id of the stream, which is crypted
	 * 
	 * @return the streamID
	 */
	public int getStreamID() {
		return streamID;
	}

	/**
	 * Sets the id of the stream, which is crypted
	 * 
	 * @param streamID
	 *            the streamID to set
	 */
	@Parameter(type = IntegerParameter.class, name = "streamID", optional = false)
	public void setStreamID(int streamID) {
		this.streamID = streamID;
	}

	/**
	 * Default constructor.
	 */
	public CryptCommandAO() {
		super();
	}

	/**
	 * Copy Constructor.
	 * 
	 * @param cryptCommandAO
	 *            The cryptCommandAO, which will be copied
	 */
	public CryptCommandAO(CryptCommandAO cryptCommandAO) {
		super(cryptCommandAO);
		this.parameter = cryptCommandAO.parameter;
		this.algorithm = cryptCommandAO.algorithm;
		this.initVector = cryptCommandAO.initVector;
		this.streamID = cryptCommandAO.streamID;
		this.receiverID = cryptCommandAO.receiverID;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CryptCommandAO(this);
	}

	/**
	 * Returns the map of parameters, which will be crypted.
	 * 
	 * @return The key represents the occurrence of the parameter. <br>
	 *         The value is the parameter, which will be crypted
	 */
	public Map<String, Object> getParameter() {
		return parameter;
	}

	/**
	 * Sets the map of parameters, which will be crypted.
	 * 
	 * @param parameter
	 *            The key represents the occurrence of the parameter. <br>
	 *            The value is the parameter, which will be crypted
	 */
	@Parameter(type = StringParameter.class, name = "parameter", optional = false, isMap = true)
	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}

	/**
	 * Returns the algorithm, which will be used for crypting.
	 * 
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * Sets the algorithm, which will be used for crypting.
	 * 
	 * @param algorithm
	 *            the algorithm to set
	 */
	@Parameter(type = StringParameter.class, name = "algorithm", optional = false)
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Returns the initVector, which will be used for crypting.
	 * 
	 * @return the initVector
	 */
	public String getInitVector() {
		return initVector;
	}

	/**
	 * Sets the initVector, which will be used for crypting.
	 * 
	 * @param initVector
	 *            the initVector to set
	 */
	@Parameter(type = StringParameter.class, name = "initVector", optional = true)
	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}

}
