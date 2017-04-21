package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.Cryptor;

/**
 * This is the abstract Class of the CryptAO. Every CryptAO should extend this.
 * It serves the necessary Parameters.
 * 
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
	private Integer punctuationDelay;

	/**
	 * Default Constructor.
	 */
	public AbstractCryptAO() {
		super();
	}

	/**
	 * Copy Constructor.
	 * 
	 * @param abstractCryptAO
	 *            The AbstractCryptAO to copy.
	 */
	public AbstractCryptAO(AbstractCryptAO abstractCryptAO) {
		super(abstractCryptAO);
		this.algorithm = abstractCryptAO.algorithm;
		this.initVector = abstractCryptAO.initVector;
		this.mode = abstractCryptAO.mode;
		this.attributes = abstractCryptAO.attributes;
		this.punctuationDelay = abstractCryptAO.punctuationDelay;
		// this.outputSchema = abstractCryptAO.outputSchema;
	}

	/**
	 * Returns the Algorithm, that will be used to crypt.
	 * 
	 * @return The Algorithm, that will be used to crypt
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * Returns the initVector, that will be used in the cryptAlgorithm.
	 * 
	 * @return The initVector, that will be used in the cryptAlgorithm.
	 */
	public String getInitVector() {
		return initVector;
	}

	/**
	 * The mode will be either ENCRYPT or DECRYPT
	 * 
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Sets the parameter Algorithm.
	 * 
	 * @param algorithm
	 *            The Algorithm, that will be used for crypting.
	 */
	@Parameter(type = StringParameter.class, name = "algorithm", optional = false)
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Sets the parameter InitVector.
	 * 
	 * @param initVector
	 *            The InitVector, that will be used in the cryptAlgorithm.
	 */
	@Parameter(type = StringParameter.class, name = "initVector", optional = true)
	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}

	/**
	 * Sets the parameter Mode.
	 * 
	 * @param mode
	 *            Has to be either EMCRYPT or DECRYPT.
	 */
	@Parameter(type = StringParameter.class, name = "mode", optional = false)
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * Returns the punctuationDelay. After every punctuationDelay crypted
	 * Object, a CryptPunctuation will be sent.
	 * 
	 * @return The PunctuationDelay
	 */
	public Integer getPunctuationDelay() {
		return this.punctuationDelay;
	}

	/**
	 * Sets the parameter punctuationDelay. After every punctuationDelay crypted
	 * Object, a CryptPunctuation will be sent.
	 * 
	 * @param punctuationDelay
	 *            The amount of Objects to crypt, before you sent a
	 *            CryptPunctuation. <br>
	 *            If you want to sent it every time, you have to use 1.
	 */
	@Parameter(type = IntegerParameter.class, name = "punctuationDelay", optional = true)
	public void setPunctuationDelay(Integer punctuationDelay) {
		this.punctuationDelay = punctuationDelay;
	}

	/**
	 * Sets the outputSchema of the crypted Attributes. <br>
	 * The given attributes will be crypted. <br>
	 * At Encrypting Mode the Datatype does'nt matter.<br>
	 * At Decrypting Mode you have to specify the datatype of the decrypted
	 * Attributes.
	 * 
	 * @param attributes
	 */
	@Parameter(type = CreateSDFAttributeParameter.class, name = "SchemaOfCrypted", isList = true, optional = false, doc = "The output schema. You only have to set the Schema of the crypted Attributes")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema output = super.getOutputSchemaIntern(pos);
		List<SDFAttribute> newAttributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute atr : output.getAttributes()) {
			boolean found = false;
			for (SDFAttribute userAtr : this.attributes) {
				if (userAtr.getAttributeName().equals(atr.getAttributeName())) {
					// found mathing Attribute
					// atr will be crypted
					found = true;
					if (this.mode.equals(ENCRYPT_MODE)) {
						if (this.getAlgorithm().equals(Cryptor.OPE_LONG_ALGORITHM)) {
							newAttributes.add(atr.clone(SDFDatatype.LONG));
						} else {
							newAttributes.add(atr.clone(SDFDatatype.STRING));
						}
					} else if (this.mode.equals(DECRYPT_MODE)) {
						newAttributes.add(atr.clone(userAtr.getDatatype()));
					}
					break;
				}
			}
			if (!found) {
				// atr won't be crypted
				newAttributes.add(atr);
			}
		}

		SDFSchema newSchema = SDFSchemaFactory.createNewWithAttributes(newAttributes, output);
		return newSchema;
	}

}
