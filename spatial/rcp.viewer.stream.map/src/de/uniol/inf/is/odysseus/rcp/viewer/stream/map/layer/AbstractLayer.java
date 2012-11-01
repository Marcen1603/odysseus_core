package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public abstract class AbstractLayer implements ILayer {

    private static final long serialVersionUID = -8420177483408915474L;
	protected String name;
	protected String srid;
	protected LayerConfiguration configuration;
	protected boolean active = false;
	
	public AbstractLayer(LayerConfiguration configuration){
		this.configuration = configuration;
		this.name = configuration.getName();
	}
	
	@Override
	public String getName() {
		return name; 
	}
	
    public String getSRID() {
	    return srid;
    }

	public void setSrid(String srid) {
		this.srid = srid;
	}
	
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
	public LayerConfiguration getConfiguration(){
		return configuration;
	}
	
	@Override
	public void setConfiguration(LayerConfiguration configuration){
		this.configuration = configuration;
	}
	
	@Override
	public boolean isActive(){
		return active;
	}
	
}
