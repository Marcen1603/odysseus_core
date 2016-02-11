package de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.physicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.TrustPunctuation;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Operator to wait for a {@link TrustPunctuation}. All elements before will get
 * their trust values changed.
 * 
 * @author Michael
 *
 */
public class TrustPunctuationReaderPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> {

	/**
	 * True, if a {@link TrustPunctuation} has been received. Elements will not
	 * longer changed.
	 */
	boolean punctuationReceived;

	/**
	 * The trust value to be used before a {@link TrustPunctuation} arrives.
	 */
	private final double trust;

	/**
	 * Creates a new {@link TrustPunctuationReaderPO}.
	 * 
	 * @param value
	 *            The trust value to be used before a {@link TrustPunctuation}
	 *            arrives.
	 * @param recMode
	 *            True, if trust shall be changed.
	 */
	public TrustPunctuationReaderPO(double value, boolean recMode) {
		this.trust = value;
		this.punctuationReceived = !recMode;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof TrustPunctuation) {
			this.punctuationReceived = true;
		}
		this.sendPunctuation(punctuation, port);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(StreamObject object, int port) {
		if (!this.punctuationReceived && Arrays.asList(object.getMetadata().getClasses()).contains(ITrust.class)) {
			((ITrust) object.getMetadata()).setTrust(this.trust);
		}
		this.transfer(object, port);
	}

}