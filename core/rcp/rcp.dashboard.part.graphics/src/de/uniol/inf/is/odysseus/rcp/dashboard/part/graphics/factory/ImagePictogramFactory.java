package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.ImagePictogram;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

public class ImagePictogramFactory implements CreationFactory {
	
	public Object getNewObject() {
		return new ImagePictogram();
	}

	public Object getObjectType() {
		return Pictogram.class;
	}
}
