package de.uniol.inf.is.odysseus.incubation.crypt.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.crypt.provider.ICryptor;

/**
 * @author MarkMilster
 *
 */
public class SimpleCryptPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private ICryptor cryptor;
	private List<SDFAttribute> inputSchema;
	private List<SDFAttribute> restrictionList;

	public SimpleCryptPO(ICryptor cryptor, List<SDFAttribute> inputSchema, List<SDFAttribute> restrictionList) {
		super();
		this.cryptor = cryptor;
		this.inputSchema = inputSchema;
		this.restrictionList = restrictionList;
		this.cryptor.init();
	}

	public SimpleCryptPO(SimpleCryptPO<T> cryptPO) {
		super(cryptPO);
		this.cryptor = cryptPO.getCryptor();
		this.inputSchema = cryptPO.inputSchema;
		this.restrictionList = cryptPO.restrictionList;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		this.sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	// TODO in metadaten uebertragen, welches attribut mit welchem verfahren
	// verschluesselt wurde, und welcher key (also receiverID und streamID (ist
	// receiverID notwendig? nein weil jeder receiver kennt seine eigene ID, ist
	// streamID notwendig? nein, kann man auch absprechen opder doch
	// uebertragen, oder irgendwie die richtige aus odysseus nutzen (queryID)))
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void process_next(T object, int port) {
		if (object instanceof Tuple) {
			Tuple tuple = (Tuple) object;
			// TODO Doku: mit dem attributes parameter muss man angeben, welches
			// Attribut ver/entschlüsselt werden soll
			// TODO Ausblick: beim decrypting attributes auch in metadaten mit
			// übertragen genau wie alles andere (momentan ist es
			// absprachesache)
			// Tuple restrictedTuple = tuple.restrict(this.restrictionList,
			// true);

			for (int i = 0; i < tuple.getAttributes().length; i++) {
				// TODO Ausblick: in Metadaten speichern, welche attribute
				// verschluesselt wurden und wie --> dann braucht man den
				// algorithmus gar nicht angeben, genau so receiverID
				// automatisch erkennen, zb aus benutzerdaten, und streamID
				// automatisch mit in metadaten uebertragen (genau wie
				// algorithmus)--> dann braucht man gar keine Parameter mehr
				// beim Decrypting (Nutzerfreundlich, da haufiger als
				// encrypting)

				Object attributeValue = tuple.getAttribute(i);
				SDFAttribute attribute = this.inputSchema.get(i);
				for (SDFAttribute retAtr : this.restrictionList) {
					if (retAtr.equals(attribute)) {
						attributeValue = this.cryptor.cryptObject(attributeValue);
						break;
					}
				}

				// if (this.restrictionList.contains(attribute)) {
				// attribute = this.cryptor.cryptObject(attribute);
				// }
				tuple.setAttribute(i, attributeValue);
			}
			transfer((T) tuple);
		} else if (object instanceof KeyValueObject) {
			KeyValueObject keyValue = (KeyValueObject) object;
			Object[] keys = keyValue.getAttributes().keySet().toArray();
			for (int i = 0; i < keyValue.getAttributes().entrySet().size(); i++) {
				Object obj = keyValue.getAttribute(keys[i].toString());
				Object crypted = this.cryptor.cryptObject(obj);
				keyValue.setAttribute(keys[i].toString(), crypted);
			}
			transfer((T) keyValue);
		}
		// Map map = object.getGetValueMap();
		// Object[] entry = map.entrySet().toArray();
		// Object[] key = map.keySet().toArray();
		// for (int i = 0; i < entry.length; i++) {
		// Object crypted = this.cryptor.cryptObject(entry[i]);
		// object.setKeyValue(key[i].toString(), crypted);
		// }
		// transfer(object);
	}

	/**
	 * @return the cryptor
	 */
	public ICryptor getCryptor() {
		return cryptor;
	}

}
