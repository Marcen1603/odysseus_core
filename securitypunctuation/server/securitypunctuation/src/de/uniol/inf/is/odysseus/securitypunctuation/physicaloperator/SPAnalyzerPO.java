package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;

public class SPAnalyzerPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	List<ISecurityPunctuation> buffer;


	public SPAnalyzerPO() {
		super();
		this.buffer = new ArrayList<ISecurityPunctuation>();
	}

	public void processSecurityPunctuation(ISecurityPunctuation punctuation) {
		if (buffer.isEmpty()) {
			buffer.add(punctuation);
			return;

		} else if (buffer.get(buffer.size() - 1).getTime().equals(punctuation.getTime())) {
			this.buffer = punctuation.union(buffer);
		} else {
			this.buffer.clear();
			this.buffer.add(punctuation);
		}

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/*
	 * The SPs are stored until at least one tuple is sent
	 */
	@Override
	protected void process_next(T object, int port) {
		for (ISecurityPunctuation sp : this.buffer) {
			sendPunctuation(sp);
		}
		this.buffer.clear();
		transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			processSecurityPunctuation((ISecurityPunctuation) punctuation);
		} else
			sendPunctuation(punctuation);

	}

}
