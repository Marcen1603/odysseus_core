package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityRestrictionPart;

@SuppressWarnings({ "rawtypes" })
public class SPAnalyzerPO extends AbstractPipe<Tuple, Tuple> {
	List<AbstractSecurityPunctuation> buffer;

	public void processSecurityPunctuation(AbstractSecurityPunctuation punctuation, int port) {

		if (buffer.get(buffer.size() - 1).getTime() == punctuation.getTime()) {
			punctuation = union(buffer.get(buffer.size() - 1), punctuation);
		} else {
			this.buffer.clear();
			this.buffer.add(punctuation);
		}
		sendPunctuation(punctuation);

	}

	// Vergleicht ob die "neuere" SP gleich der alten ist und f�gt dann die
	// Rollen der beiden Sps zusammen
	private AbstractSecurityPunctuation union(AbstractSecurityPunctuation olderSP,
			AbstractSecurityPunctuation newerSP) {
		if (olderSP.compareSP(newerSP)) {
			
			olderSP.getSRP().mergeSRP(newerSP.getSRP());
		}

		return olderSP;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple object, int port) {
		transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			processSecurityPunctuation((AbstractSecurityPunctuation) punctuation, port);
		} else
			sendPunctuation(punctuation);

	}

}
