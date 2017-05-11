package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.AbstractSecurityPunctuation;

public class SPAnalyzerPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	List<AbstractSecurityPunctuation> buffer;
	private static final Logger LOG = LoggerFactory.getLogger(SPAnalyzerPO.class);

	public SPAnalyzerPO() {
		super();
		this.buffer = new ArrayList<AbstractSecurityPunctuation>();
	}

	public void processSecurityPunctuation(AbstractSecurityPunctuation punctuation) {
		if (buffer.isEmpty()) {
			buffer.add(punctuation);
			return;
			
			//timestamp verlgiechen
		} else if (buffer.get(buffer.size() - 1).equals(punctuation)) {

			punctuation.getSRP().union(buffer.get(buffer.size() - 1).getSRP());
			LOG.info(punctuation.toString());
			this.buffer.clear();
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
 * Die SPs werden solange zur�ckgehalten, bis ein Tupel verschickt ankommt
 */
	@Override
	protected void process_next(T object, int port) {
		for(AbstractSecurityPunctuation sp:this.buffer){
			sendPunctuation(sp);
		}this.buffer.clear();
		transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof AbstractSecurityPunctuation) {
			processSecurityPunctuation((AbstractSecurityPunctuation) punctuation);
		} else
			sendPunctuation(punctuation);

	}

}
