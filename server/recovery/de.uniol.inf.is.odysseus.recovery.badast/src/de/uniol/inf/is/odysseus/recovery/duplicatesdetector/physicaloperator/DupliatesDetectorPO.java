package de.uniol.inf.is.odysseus.recovery.duplicatesdetector.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.recovery.TrustUpdatePunctuation;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Operator to wait for a {@link TrustUpdatePunctuation}. All elements before
 * will get their trust values changed, because they might be duplicates.
 * 
 * @author Michael Brand
 *
 */
public class DupliatesDetectorPO<StreamObject extends IStreamObject<IMetaAttribute>>
		extends AbstractPipe<StreamObject, StreamObject> {

	/**
	 * True, if a {@link TrustUpdatePunctuation} has been received. Elements are
	 * no longer possible duplicates.
	 */
	private boolean punctuationReceived;

	/**
	 * The trust value to be used for duplicates.
	 */
	private final double trust;

	/**
	 * Creates a new {@link DupliatesDetectorPO}.
	 * 
	 * @param value
	 *            The trust value to be used for duplicates.
	 */
	public DupliatesDetectorPO(double value) {
		this.trust = value;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof TrustUpdatePunctuation) {
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
		if (!this.punctuationReceived) {
			((ITrust) object.getMetadata()).setTrust(this.trust);
		}
		this.transfer(object, port);
	}

}