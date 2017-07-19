package de.uniol.inf.is.odysseus.spatial.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.IMovingObjectDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectDataStructureProvider;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MODataStructureAO;

public class MODataStructurePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private IMovingObjectDataStructure dataStructure;

	public MODataStructurePO(MODataStructureAO ao) {
		int geometryPosition = ao.getInputSchema().findAttributeIndex(ao.getGeometryAttribute());
		int idPosition = ao.getInputSchema().findAttributeIndex(ao.getIdAttribute());
		this.dataStructure = MovingObjectDataStructureProvider.getInstance().getOrCreateDataStructure(
				ao.getDataStructureName(), ao.getDataStructureType(), geometryPosition, idPosition,
				ao.getDistancePerMovingObject());
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		// Save the tuple in a dataStructure
//		this.dataStructure.add(object);
//
//		// And put out a tuple with the name of the dataStructure
//		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
//		tuple.setAttribute(0, dataStructure.getName());
//		tuple.setMetadata(object.getMetadata());
//		if (tuple instanceof IStreamObject<?>)
//			transfer((T) tuple);
	}

}
