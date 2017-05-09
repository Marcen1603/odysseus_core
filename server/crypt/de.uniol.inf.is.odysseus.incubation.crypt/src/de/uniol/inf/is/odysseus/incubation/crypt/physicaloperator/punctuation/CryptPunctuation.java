package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * This punctuation contains information about the crypted data.
 * 
 * @author MarkMilster
 *
 */
public class CryptPunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = -7457813773892856504L;

	final static byte NUMBER = 3;

	public static final transient SDFSchema schema;

	private ArrayList<SDFAttribute> cryptedAttributes;
	private ArrayList<String> algorithms;

	static {
		ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("CryptPunctuation", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("CryptPunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("CryptPunctuation", "cryptedAttributes", SDFDatatype.LIST));
		attributes.add(new SDFAttribute("CryptPunctuation", "algorithms", SDFDatatype.LIST_STRING));
		schema = SDFSchemaFactory.createNewSchema("CryptPunctuation", Tuple.class, attributes);
	}

	/**
	 * Constructs a cryptPunctuation with specifies the crypted data at the
	 * given point
	 * 
	 * @param point
	 *            the point, where this punctuation was crypted
	 */
	private CryptPunctuation(long point) {
		super(point);
		cryptedAttributes = new ArrayList<SDFAttribute>();
		algorithms = new ArrayList<String>();
	}

	/**
	 * Constructs a cryptPunctuation with specifies the crypted data at the
	 * given point
	 * 
	 * @param point
	 *            the point, where this punctuation was crypted
	 */
	private CryptPunctuation(PointInTime point) {
		super(point);
		cryptedAttributes = new ArrayList<SDFAttribute>();
		algorithms = new ArrayList<String>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptPunctuation
	 *            the CryptPunctuation, which will be copied
	 */
	private CryptPunctuation(CryptPunctuation cryptPunctuation) {
		super(cryptPunctuation);
		cryptedAttributes = cryptPunctuation.getCryptedAttributes();
		algorithms = cryptPunctuation.getAlgorithms();
	}

	@Override
	public AbstractPunctuation clone() {
		return new CryptPunctuation(this);
	}

	@Override
	public AbstractPunctuation clone(PointInTime point) {
		CryptPunctuation punc = new CryptPunctuation(point);
		return punc;
	}

	@Override
	public byte getNumber() {
		return NUMBER;
	}

	@Override
	public String toString() {
		return "CryptPunctuation " + getTime();
	}

	/**
	 * Creates a new CryptPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptPunctuation at the specified point
	 */
	static public CryptPunctuation createNewCryptPunctuation(long point) {
		return new CryptPunctuation(point);
	}

	/**
	 * Creates a new CryptPunctuation at the point given in the metadata of a
	 * other CryptPunctuation
	 * 
	 * @param input
	 *            The metadata of a other CryptPunctuation
	 * @return A new CryptPunctuation with the same point as the given metadata
	 */
	static public CryptPunctuation createNewInstance(Tuple<?> input) {
		return new CryptPunctuation(new PointInTime((long) input.getAttribute(2)));
	}

	/**
	 * Creates a new CryptPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptPunctuation at the specified point
	 */
	static public CryptPunctuation createNewCryptPunctuation(PointInTime point) {
		return new CryptPunctuation(point);
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple(4, true);
		ret.setAttribute(0, NUMBER);
		ret.setAttribute(1, getTime().getMainPoint());
		ret.setAttribute(2, this.cryptedAttributes);
		ret.setAttribute(3, this.algorithms);
		return ret;
	}

	/**
	 * Returns the cryptedAttributes
	 * 
	 * @return the cryptedAttributes
	 */
	public ArrayList<SDFAttribute> getCryptedAttributes() {
		return cryptedAttributes;
	}

	/**
	 * Sets the cryptedAttributes
	 * 
	 * @param cryptedAttributes
	 *            the cryptedAttributes to set
	 */
	public void setCryptedAttributes(ArrayList<SDFAttribute> cryptedAttributes) {
		this.cryptedAttributes = cryptedAttributes;
	}

	/**
	 * Returns the algorithms
	 * 
	 * @return the algorithms
	 */
	public ArrayList<String> getAlgorithms() {
		return algorithms;
	}

	/**
	 * Sets the algorithms
	 * 
	 * @param algorithms
	 *            the algorithms to set
	 */
	public void setAlgorithms(ArrayList<String> algorithms) {
		this.algorithms = algorithms;
	}

}
