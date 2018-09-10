package de.uniol.inf.is.odysseus.systemload_relational.physical;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.systemload.ISystemLoad;
import de.uniol.inf.is.odysseus.systemload.logicaloperator.SystemLoadToPayloadAO;;

public class SystemLoadToPayloadPO<M extends ISystemLoad, T extends Tuple<M>>
		extends AbstractPipe<T, T> {

	final boolean append;
	final String loadname;
	
	public SystemLoadToPayloadPO(SystemLoadToPayloadAO ao) {
		this.append = ao.isAppend();
		this.loadname = ao.getLoadname();
	}

	@Override
	public OutputMode getOutputMode() {
		if (append){
			return OutputMode.MODIFIED_INPUT;
		}
		
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		final int inputSize;
		if (append) {
			inputSize = object.size();
		} else {
			inputSize = 0;
		}
		final Tuple<ISystemLoad> out;
		out = new Tuple<ISystemLoad>(inputSize + 3, false);

		if (append) {
			System.arraycopy(object.getAttributes(), 0, out.getAttributes(), 0,
					inputSize);
		}

		out.setAttribute(inputSize, object.getMetadata().getCpuLoad(loadname));
		out.setAttribute(inputSize + 1, object.getMetadata().getMemLoad(loadname));
		out.setAttribute(inputSize + 2, object.getMetadata().getNetLoad(loadname));

		out.setMetadata((ISystemLoad) object.getMetadata().clone());
		out.setRequiresDeepClone(object.requiresDeepClone());
		transfer((T) out);
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

}
