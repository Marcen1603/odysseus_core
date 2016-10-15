package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.IMovingObjectDataStructure;

/**
 * This operator fills a given data structure with data from the incoming
 * stream. The data structure is a spatial data structure. The name of the data
 * structure can be used to access it.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class STDataStructurePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;

	public STDataStructurePO(IMovingObjectDataStructure dataStructure) {
		this.dataStructure = dataStructure;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// Save the tuple in a dataStructure
		this.dataStructure.add(object);

		// And put out a tuple with the name of the dataStructure
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
		tuple.setAttribute(0, dataStructure.getName());
		tuple.setMetadata(object.getMetadata());
		if (tuple instanceof IStreamObject<?>)
			transfer((T) tuple);
	}

}
