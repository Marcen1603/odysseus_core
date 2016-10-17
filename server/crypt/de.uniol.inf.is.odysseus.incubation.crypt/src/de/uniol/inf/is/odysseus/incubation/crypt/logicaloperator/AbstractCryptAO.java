package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

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
	 * Returns the List of Attributes, that will be crypted.
	 * 
	 * @return The List of Attributes, that will be crypted.
	 */
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the parameter Attributes.
	 * 
	 * @param attributes
	 *            The List of Attributes, that will be crypted.
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "attributes", optional = true, isList = true)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
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

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema output = super.getOutputSchemaIntern(pos);
		for (SDFAttribute atr : output.getAttributes()) {
			if (this.attributes.contains(atr)) {
				if (this.mode.equals(ENCRYPT_MODE)) {
					atr.clone(SDFDatatype.STRING);
					// TODO das atr auch wirklich setzen
				} else if (this.mode.equals(DECRYPT_MODE)) {
					atr.clone(SDFDatatype.OBJECT);
					// TODO richtigen datentyp nach parsing rausfinden! --> also
					// das ganze output schema erst im PO setzen? (wenn das hier also rausfaellt, dann muss die if Encrypt abfrage doch vor der schleife)
				}
			}
		}

		SDFSchema input = super.getInputSchema();
		List<SDFAttribute> newOutput = new ArrayList<SDFAttribute>();
		// check, if types are equal
		for (int i = 0; i < output.size(); i++) {
			newOutput.add(output.get(i).clone(input.get(i).getDatatype()));
		}
		return super.getOutputSchemaIntern(pos);
	}

}
