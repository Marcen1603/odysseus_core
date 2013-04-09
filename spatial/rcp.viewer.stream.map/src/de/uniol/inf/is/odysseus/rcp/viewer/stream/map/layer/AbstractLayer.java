package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public abstract class AbstractLayer<C extends LayerConfiguration> implements ILayer {

    private static final long serialVersionUID = -8420177483408915474L;
	protected String name;
	protected int srid;
	protected C configuration;
	protected boolean active = false;
	
	public AbstractLayer(C configuration){
		this.configuration = configuration;
		if (this.configuration != null)
			this.name = configuration.getName();
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
		this.configuration.setName(name);
	}
	
	@Override
	public String getName() {
		return name; 
	}
	
    @Override
	public int getSRID() {
	    return srid;
    }

	public void setSrid(int srid) {
		this.srid = srid;
	}
	
	@Override
	public Style getStyle() {
		return null;
	}

	@Override
	public String getComplexName() {
		return name;
	}
	
	@Override
	public String getQualifiedName() {
		return name;
	}
	
	@Override
	public C getConfiguration(){
		return configuration;
	}

	@Override
	public abstract void setConfiguration(LayerConfiguration configuration);
	
	@Override
	public boolean isActive(){
		return active;
	}
	
	@Override
	public void setActive(boolean b){
		this.active = b;
	}
	
	@Override
	public boolean isGroup() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getComplexName();
	}
}
