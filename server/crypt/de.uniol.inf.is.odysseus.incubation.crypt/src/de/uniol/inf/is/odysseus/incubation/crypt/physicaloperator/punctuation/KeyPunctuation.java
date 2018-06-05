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
 * This Punctuation will be send of en Encryptor, if a new key is used
 * 
 * @author MarkMilster
 *
 */
public class KeyPunctuation extends AbstractPunctuation {

	private static final long serialVersionUID = -2335894573246260211L;

	final static byte NUMBER = 3;

	public static final transient SDFSchema schema;

	private ArrayList<Integer> receiverId;
	private int streamId;

	static {
		ArrayList<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("KeyPunctuation", "type", SDFDatatype.INTEGER));
		attributes.add(new SDFAttribute("KeyPunctuation", "point", SDFDatatype.TIMESTAMP));
		attributes.add(new SDFAttribute("KeyPunctuation", "receiverIds", SDFDatatype.LIST_INTEGER));
		attributes.add(new SDFAttribute("KeyPunctuation", "streamIds", SDFDatatype.INTEGER));
		schema = SDFSchemaFactory.createNewSchema("CryptPunctuation", Tuple.class, attributes);
	}

	/**
	 * Inits the objects
	 */
	private void init() {
		this.setReceiverId(new ArrayList<>());
	}

	/**
	 * Constructs a keyPunctuation with specifies the crypted data at the given
	 * point
	 * 
	 * @param point
	 *            the point, where this punctuation was crypted
	 */
	private KeyPunctuation(long point) {
		super(point);
		this.init();
	}

	/**
	 * Constructs a keyPunctuation with specifies the crypted data at the given
	 * point
	 * 
	 * @param point
	 *            the point, where this punctuation was crypted
	 */
	private KeyPunctuation(PointInTime point) {
		super(point);
		this.init();
	}

	/**
	 * Copy constructor.
	 * 
	 * @param keyPunctuation
	 *            The KeyPunctuation, which will be copied
	 */
	private KeyPunctuation(KeyPunctuation keyPunctuation) {
		super(keyPunctuation);
		this.init();
	}

	@Override
	public AbstractPunctuation clone() {
		return new KeyPunctuation(this);
	}

	@Override
	public AbstractPunctuation clone(PointInTime point) {
		KeyPunctuation punc = new KeyPunctuation(point);
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
	 * Creates a new KeyPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new KeyPunctuation at the specified point
	 */
	static public KeyPunctuation createNewKeyPunctuation(long point) {
		return new KeyPunctuation(point);
	}

	/**
	 * Creates a new KeyPunctuation at the point given in the metadata of a
	 * other KeyPunctuation
	 * 
	 * @param input
	 *            The metadata of a other CryptPunctuation
	 * @return A new CryptPunctuation with the same point as the given metadata
	 */
	static public KeyPunctuation createNewInstance(Tuple<?> input) {
		return new KeyPunctuation(new PointInTime((long) input.getAttribute(2)));
	}

	/**
	 * Creates a new KeyPunctuation at the specified point.
	 * 
	 * @param point
	 *            The point, when the punctuation will be created
	 * @return a new KeyPunctuation at the specified point
	 */
	static public KeyPunctuation createNewKeyPunctuation(PointInTime point) {
		return new KeyPunctuation(point);
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
		ret.setAttribute(2, this.getReceiverId());
		ret.setAttribute(3, this.getStreamId());

		return ret;
	}

	/**
	 * Returns the receiverIDs
	 * 
	 * @return the receiverId
	 */
	public ArrayList<Integer> getReceiverId() {
		return receiverId;
	}

	/**
	 * Sets the receiverID
	 * 
	 * @param receiverId
	 *            the receiverId to set
	 */
	public void setReceiverId(ArrayList<Integer> receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * Returns the streamID
	 * 
	 * @return the streamId
	 */
	public int getStreamId() {
		return streamId;
	}

	/**
	 * Sets the streamID
	 * 
	 * @param streamId
	 *            the streamId to set
	 */
	public void setStreamId(int streamId) {
		this.streamId = streamId;
	}

	/**
	 * Adds one receiverID to the list of receiverIDs
	 * 
	 * @param receiverId
	 *            The receiverID to add
	 */
	public void addReceiverId(int receiverId) {
		this.receiverId.add(Integer.valueOf(receiverId));
	}

}
