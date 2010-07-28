package de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManagerListener;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;


public class ModelManager<C> implements IModelManager<C> {

	private List<IGraphModel<C>> models = new ArrayList<IGraphModel<C>>();
	private IGraphModel<C> activeModel = null;
	private List<IModelManagerListener<C>> listeners = new ArrayList<IModelManagerListener<C>>();
	
	private static final Logger logger = LoggerFactory.getLogger( ModelManager.class );
	
	@Override
	public void addModel( IGraphModel<C> model ) {
		synchronized( models ) {
			if( !models.contains(model)) {
				models.add(model);
				fireModelAddedEvent(model);
				logger.info("New Model added: " + model.toString());
			}
		}
	}
	
	@Override
	public void removeModel( IGraphModel<C> model ) {
		synchronized( models ) {
			if( models.contains(model)) {
				models.remove(model);
				fireModelRemovedEvent(model);
				logger.info("Model removed: " + model.toString());
			}
		}
	}
	
	@Override
	public List<IGraphModel<C>> getModels() {
		return Collections.unmodifiableList(models);
	}
	
	@Override
	public void removeAllModels() {
		synchronized( models ) {
			for( IGraphModel<C> p : models ) 
				removeModel(p);
		}
	}
	
	@Override
	public void addListener( IModelManagerListener<C> listener ) {
		synchronized( listeners ) {
			if( !listeners.contains(listener))
				listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener( IModelManagerListener<C> listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	private void fireModelAddedEvent( IGraphModel<C> provider ) {
		synchronized( listeners ) {
			for( IModelManagerListener<C> l : listeners ) {
				try {
					l.modelAdded(this, provider);
				} catch( Exception ex ) {
					logger.error("Fehler in Listener", ex);
				}
			}
		}
	}

	private void fireModelRemovedEvent( IGraphModel<C> provider ) {
		synchronized( listeners ) {
			for( IModelManagerListener<C> l : listeners ) {
				if( l != null ) {
					try {
						l.modelRemoved(this, provider);
					} catch( Exception ex ) {
						logger.error("Fehler in Listener", ex);
					}
				}
			}
		}
	}
	
	private void fireActiveModelChangedEvent( IGraphModel<C> provider ) {
		synchronized( listeners ) {
			for( IModelManagerListener<C> l : listeners ) {
				if( l != null ) {
					try {
						l.activeModelChanged(this, provider);
					} catch( Exception ex ) {
						logger.error("Fehler in Listener", ex);
					}
				}
			}
		}
	}
	
	@Override
	public IGraphModel<C> getActiveModel() {
		return activeModel;
	}

	@Override
	public void setActiveModel(IGraphModel<C> model) {
		if( model != activeModel ) {
			activeModel = model;
			activeModel.setName("Active Graph");
			fireActiveModelChangedEvent(model);
		}
	}

}
