package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation;

import java.util.ArrayList;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * This Punctuation contains a Map with cryptedPredicates.<br>
 * It could be used to set cryptedPredicates into an operator.
 * 
 * @author MarkMilster
 *
 */
public class CryptedPredicatePunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = 42424242L;

	final static byte NUMBER = 3;

	public static final transient SDFSchema schema;

	protected HashMap<String, CryptedValue> cryptedPredicates;

	static {
		ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("CryptedPredicatePunctuation", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("CryptedPredicatePunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("CryptedPredicatePunctuation", "cryptedPredicates", SDFDatatype.OBJECT));
		schema = SDFSchemaFactory.createNewSchema("CryptedPredicatePunctuation", Tuple.class, attributes);
	}

	/**
	 * Constructs a cryptedPredicatePunctuation which holds the crypted
	 * Predicates
	 * 
	 * @param point
	 *            the point, where this punctuation was created
	 */
	private CryptedPredicatePunctuation(long point) {
		super(point);
		this.cryptedPredicates = new HashMap<>();
	}

	/**
	 * Constructs a cryptedPredicatePunctuation which holds the crypted
	 * Predicates
	 * 
	 * @param point
	 *            the point, where this punctuation was created
	 */
	private CryptedPredicatePunctuation(PointInTime point) {
		super(point);
		this.cryptedPredicates = new HashMap<>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptedPredicatePunctuation
	 *            the CryptededPredicatePunctuation, which will be copied
	 */
	private CryptedPredicatePunctuation(CryptedPredicatePunctuation cryptedPredicatePunctuation) {
		super(cryptedPredicatePunctuation);
		this.cryptedPredicates = new HashMap<>();
	}

	@Override
	public AbstractPunctuation clone() {
		return new CryptedPredicatePunctuation(this);
	}

	@Override
	public AbstractPunctuation clone(PointInTime point) {
		CryptedPredicatePunctuation punc = new CryptedPredicatePunctuation(point);
		return punc;
	}

	@Override
	public byte getNumber() {
		return NUMBER;
	}

	@Override
	public String toString() {
		return "CryptedPredicatePunctuation " + getTime();
	}

	/**
	 * Creates a new CryptedPredicatePunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptedPredicatePunctuation at the specified point
	 */
	static public CryptedPredicatePunctuation createNewCryptedPredicatePunctuation(long point) {
		return new CryptedPredicatePunctuation(point);
	}

	/**
	 * Creates a new CryptedPredicatePunctuation at the point given in the
	 * metadata of a other CryptedPredicatePunctuation
	 * 
	 * @param input
	 *            The metadata of a other CryptedPredicatePunctuation
	 * @return A new CryptedPredicatePunctuation with the same point as the
	 *         given metadata
	 */
	static public CryptedPredicatePunctuation createNewInstance(Tuple<?> input) {
		return new CryptedPredicatePunctuation(new PointInTime((long) input.getAttribute(1)));
	}

	/**
	 * Creates a new CryptedPredicatePunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptedPredicatePunctuation at the specified point
	 */
	static public CryptedPredicatePunctuation createNewCryptedPredicatePunctuation(PointInTime point) {
		return new CryptedPredicatePunctuation(point);
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getValue() {
		Tuple<?> ret = new Tuple(3, true);
		ret.setAttribute(0, NUMBER);
		ret.setAttribute(1, getTime().getMainPoint());
		ret.setAttribute(2, this.cryptedPredicates);
		return ret;
	}

	/**
	 * Returns the cryptedPredicates
	 * 
	 * @return the cryptedPredicates
	 */
	public HashMap<String, CryptedValue> getCryptedPredicates() {
		return cryptedPredicates;
	}

	/**
	 * Sets the cryptedPredicates
	 * 
	 * @param cryptedPredicates
	 *            the cryptedPredicates to set
	 */
	public void setCryptedPredicates(HashMap<String, CryptedValue> cryptedPredicates) {
		this.cryptedPredicates = cryptedPredicates;
	}

}
