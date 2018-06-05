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
public class CryptedPredicatesPunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = 42424242L;

	final static byte NUMBER = 3;

	public static final transient SDFSchema schema;

	protected HashMap<String, CryptedValue> cryptedPredicates;

	static {
		ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("CryptedPredicatesPunctuation", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("CryptedPredicatesPunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("CryptedPredicatesPunctuation", "cryptedPredicates", SDFDatatype.OBJECT));
		schema = SDFSchemaFactory.createNewSchema("CryptedPredicatePunctuation", Tuple.class, attributes);
	}

	/**
	 * Constructs a cryptedPredicatesPunctuation which holds the crypted
	 * Predicates
	 * 
	 * @param point
	 *            the point, where this punctuation was created
	 */
	private CryptedPredicatesPunctuation(long point) {
		super(point);
		this.cryptedPredicates = new HashMap<>();
	}

	/**
	 * Constructs a cryptedPredicatesPunctuation which holds the crypted
	 * Predicates
	 * 
	 * @param point
	 *            the point, where this punctuation was created
	 */
	private CryptedPredicatesPunctuation(PointInTime point) {
		super(point);
		this.cryptedPredicates = new HashMap<>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param cryptedPredicatesPunctuation
	 *            the CryptededPredicatePunctuation, which will be copied
	 */
	private CryptedPredicatesPunctuation(CryptedPredicatesPunctuation cryptedPredicatePunctuation) {
		super(cryptedPredicatePunctuation);
		this.cryptedPredicates = new HashMap<>();
	}

	@Override
	public AbstractPunctuation clone() {
		return new CryptedPredicatesPunctuation(this);
	}

	@Override
	public AbstractPunctuation clone(PointInTime point) {
		CryptedPredicatesPunctuation punc = new CryptedPredicatesPunctuation(point);
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
	 * Creates a new CryptedPredicatesPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptedPredicatesPunctuation at the specified point
	 */
	static public CryptedPredicatesPunctuation createNewCryptedPredicatePunctuation(long point) {
		return new CryptedPredicatesPunctuation(point);
	}

	/**
	 * Creates a new CryptedPredicatesPunctuation at the point given in the
	 * metadata of a other CryptedPredicatesPunctuation
	 * 
	 * @param input
	 *            The metadata of a other CryptedPredicatesPunctuation
	 * @return A new CryptedPredicatesPunctuation with the same point as the
	 *         given metadata
	 */
	static public CryptedPredicatesPunctuation createNewInstance(Tuple<?> input) {
		return new CryptedPredicatesPunctuation(new PointInTime((long) input.getAttribute(1)));
	}

	/**
	 * Creates a new CryptedPredicatesPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new CryptedPredicatesPunctuation at the specified point
	 */
	static public CryptedPredicatesPunctuation createNewCryptedPredicatesPunctuation(PointInTime point) {
		return new CryptedPredicatesPunctuation(point);
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
