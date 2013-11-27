package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.factory;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.AbstractPictogram;

public class PictogramFactory implements CreationFactory {
	
	private Class<? extends AbstractPictogram> type;

	public PictogramFactory(Class<? extends AbstractPictogram> type){
		this.type = type;
	}
	
	@Override
	public Object getNewObject() {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {		
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object getObjectType() {
		return AbstractPictogram.class;
	}
}
