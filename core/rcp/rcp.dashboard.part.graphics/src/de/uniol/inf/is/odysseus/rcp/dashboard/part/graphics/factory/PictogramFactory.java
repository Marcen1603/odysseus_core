package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram;

public class PictogramFactory implements CreationFactory {
	
	private Class<? extends Pictogram> type;

	public PictogramFactory(Class<? extends Pictogram> type){
		this.type = type;
	}
	
	public Object getNewObject() {
		//return new ImagePictogram();
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {		
			e.printStackTrace();
		}
		return null;
	}

	public Object getObjectType() {
		return Pictogram.class;
	}
}
