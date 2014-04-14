package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class KeyValueProjectPO<T extends KeyValueObject<?>> extends AbstractPipe<T, T> {
	private List<SDFAttribute> paths;

	public KeyValueProjectPO(List<SDFAttribute> paths) {
		super();
		this.paths = paths;
	}
	
	public KeyValueProjectPO(KeyValueProjectPO<T> po) {
		super(po);
		this.paths = po.paths;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		KeyValueObject<IMetaAttribute> newObject = new KeyValueObject<IMetaAttribute>();
		for(SDFAttribute path: this.paths) {
			// Sch�ner m�glich?
			String pathURI = path.getURI();
			String pathQualName = path.getQualName();
			if(object.getAttribute(pathURI) != null) {
				newObject.setAttribute(pathURI, object.getAttribute(pathURI));
			} else if(object.getAttribute(pathQualName) != null) {
				newObject.setAttribute(pathQualName, object.getAttribute(pathQualName));
			}
		}
        newObject.setMetadata(object.getMetadata().clone());
		if(!newObject.isEmpty()) {
			transfer((T) newObject);
		}
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new KeyValueProjectPO<T>(this);
	}

}
