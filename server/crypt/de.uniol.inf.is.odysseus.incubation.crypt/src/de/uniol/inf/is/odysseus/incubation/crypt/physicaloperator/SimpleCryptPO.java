package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator.punctuation.CryptPunctuation;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;

//TODO output shema setzen
/**
 * The physical operator to crypt datastreams
 * 
 * @author MarkMilster
 *
 */
public class SimpleCryptPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private static final int DEFAULT_PUNCTUATION_DELAY = 10;

	private Integer punctuationDelay = DEFAULT_PUNCTUATION_DELAY;
	private int counter = 0;

	protected ICryptor cryptor;
	protected List<SDFAttribute> inputSchema;
	protected List<SDFAttribute> restrictionList;

	/**
	 * Constructor
	 * 
	 * @param cryptor
	 *            The crypto, which will be used for crypting the datastreams
	 * @param inputSchema
	 *            the inputSchema of the dataStream
	 * @param restrictionList
	 *            The restriction List of the attributes, which will be crypted
	 * @param punctuationDelay
	 *            The delay of crypted elements, after which the
	 *            CryptPunctuation will be send.
	 */
	public SimpleCryptPO(ICryptor cryptor, List<SDFAttribute> inputSchema, List<SDFAttribute> restrictionList,
			Integer punctuationDelay) {
		super();
		this.cryptor = cryptor;
		this.inputSchema = inputSchema;
		this.restrictionList = restrictionList;
		if (punctuationDelay != null) {
			this.punctuationDelay = punctuationDelay;
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param cryptPO
	 *            The SimpleCryptPO, which will be copied.
	 */
	public SimpleCryptPO(SimpleCryptPO<T> cryptPO) {
		super(cryptPO);
		this.cryptor = cryptPO.getCryptor();
		this.inputSchema = cryptPO.inputSchema;
		this.restrictionList = cryptPO.restrictionList;
		this.punctuationDelay = cryptPO.punctuationDelay;
	}

	@Override
	protected void process_open() {
		this.cryptor.init();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void process_next(T object, int port) {
		if (object instanceof Tuple) {
			Tuple tuple = (Tuple) object;
			// TODO Ausblick: beim decrypting attributes auch in metadaten mit
			// übertragen genau wie alles andere (momentan ist es
			// absprachesache)
			// Tuple restrictedTuple = tuple.restrict(this.restrictionList,
			// true);

			CryptPunctuation punctuation = null;
			for (int i = 0; i < tuple.getAttributes().length; i++) {
				// TODO Ausblick: in Metadaten speichern, welche attribute
				// verschluesselt wurden und wie --> dann braucht man den
				// algorithmus gar nicht angeben, genau so receiverID
				// automatisch erkennen, zb aus benutzerdaten, und streamID
				// automatisch mit in metadaten uebertragen (genau wie
				// algorithmus)--> dann braucht man gar keine Parameter mehr
				// beim Decrypting (Nutzerfreundlich, da haufiger als
				// encrypting)

				if (this.cryptor.getMode() == Cipher.ENCRYPT_MODE) {
					PointInTime point = ((IStreamObject<? extends ITimeInterval>) object).getMetadata().getStart();
					if (this.counter % this.punctuationDelay == 0) {
						punctuation = CryptPunctuation.createNewCryptPunctuation(point);
					}
				}
				Object attributeValue = tuple.getAttribute(i);
				SDFAttribute attribute = this.inputSchema.get(i);
				for (SDFAttribute retAtr : this.restrictionList) {
					if (retAtr.equals(attribute)) {
						if (punctuation != null) {
							punctuation.getCryptedAttributes().add(retAtr);
							punctuation.getAlgorithms().add(this.cryptor.getAlgorithm());
						}
						// if (attributeValue instanceof ByteBuffer) {
						// // this should be happen at DECRYPTING
						// attributeValue =
						// ByteConverter.byteBufferToBytes((ByteBuffer)
						// attributeValue);
						// }
						attributeValue = this.cryptor.cryptObjectViaString(attributeValue);

						// Encrypting: STRING; Decrypting: AnyObject
						SDFDatatype outType = SDFDatatype.getType(attributeValue.getClass().getSimpleName());

						// if (attributeValue instanceof byte[]) {
						// // this should be happen at ENCRYPTING
						// attributeValue = ByteBuffer.wrap((byte[])
						// attributeValue);
						// System.out.println(attributeValue.toString());
						// outType = SDFDatatype.getType("BYTEBUFFER");
						// }

						if (outType != null) {
							// parsed to a other type as object
							SDFSchema output = this.getOutputSchema();
							List<SDFAttribute> newOutput = new ArrayList<SDFAttribute>();
							for (int u = 0; u < output.size(); u++) {
								if (u == i) {
									newOutput.add(output.get(u).clone(outType));
								} else {
									newOutput.add(output.get(u));
								}

							}
							// TODO newOutput noch wirklich als OutputSchema
							// setzen
						}
						break;
					}
				}
				tuple.setAttribute(i, attributeValue);

			}
			if (punctuation != null) {
				this.sendPunctuation(punctuation);
			}
			transfer((T) tuple);

		} 
		// else if (object instanceof KeyValueObject) {
		// KeyValueObject keyValue = (KeyValueObject) object;
		// Object[] keys = keyValue.getAttributes().keySet().toArray();
		// for (int i = 0; i < keyValue.getAttributes().entrySet().size(); i++)
		// {
		// Object obj = keyValue.getAttribute(keys[i].toString());
		// Object crypted = this.cryptor.cryptObject(obj);
		// keyValue.setAttribute(keys[i].toString(), crypted);
		// }
		// transfer((T) keyValue);
		// }
	}

	/**
	 * Returns the cryptor, which will be used for crypting the datastream.
	 * 
	 * @return the cryptor
	 */
	public ICryptor getCryptor() {
		return cryptor;
	}

}
