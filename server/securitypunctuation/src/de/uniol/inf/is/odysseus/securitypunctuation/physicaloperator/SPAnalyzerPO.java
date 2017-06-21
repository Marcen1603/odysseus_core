  package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.DataDescriptionPart;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;

public class SPAnalyzerPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	List<ISecurityPunctuation> buffer;
	private static final Logger LOG = LoggerFactory.getLogger(SPAnalyzerPO.class);

	public SPAnalyzerPO() {
		super();
		this.buffer = new ArrayList<ISecurityPunctuation>();
	}

	public void processSecurityPunctuation(ISecurityPunctuation punctuation) {
		if (buffer.isEmpty()) {
			buffer.add(punctuation);
			return;

		} else if (buffer.get(buffer.size() - 1).getTime().equals(punctuation.getTime())) {

			for (ISecurityPunctuation sp : this.buffer) {

				if (sp.getImmutable() == punctuation.getImmutable() && sp.getSign() == punctuation.getSign()) {
					// if the DDPs of the SPs are the same, they get unioned,
					// i.e. the Roles in their SRP are unioned
					if (punctuation.getDDP().equals(sp.getDDP())) {
						sp.getSRP().union(punctuation.getSRP());
						return;
					}
					// if the roles of the SPs are the same and the tupleRange
					// are the same, the SPs are intersected, resulting in a
					// union of the attributes in the DDP
					else if (punctuation.getSRP().equals(sp.getSRP())
							&& (punctuation.getDDP().getTupleRange()[0] == sp.getDDP().getTupleRange()[0]
									&& punctuation.getDDP().getTupleRange()[1] == sp.getDDP().getTupleRange()[1])) {
						
						punctuation=punctuation.intersect(sp,sp.getTime());
						punctuation.setDDP(new DataDescriptionPart(
								String.valueOf(sp.getDDP().getTupleRange()[0]) + ","
										+ String.valueOf(sp.getDDP().getTupleRange()[1]),
								punctuation.getDDP().getAttributes()));
					
						this.buffer.add(punctuation);
						this.buffer.remove(sp);
						return;
					}
				}

			}
			// this.buffer.clear();
			this.buffer.add(punctuation);
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
