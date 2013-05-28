package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.util.LinkedList;

import org.eclipse.swt.graphics.GC;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.GroupLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class GroupLayer extends LinkedList<ILayer> implements ILayer {

	private static final long serialVersionUID = -8651572425902988389L;
	private LayerConfiguration configuration;
	private boolean active = true;
	
	public GroupLayer(GroupLayerConfiguration configuration) {
		this.configuration = configuration;
	}
	@Override
	public boolean isGroup() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		for (ILayer layer : this) {
			if (layer != null)
				layer.init(screenManager, null, null);
		}		
	}
	
	@Override
	public void setName(String name){
		this.configuration.setName(name);
		for (ILayer layer : this) {
			layer.getConfiguration().setGroup(name);
		}
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.configuration.getName();
	}

	@Override
	public String getComplexName() {
		return getName() + " (" + this.size() + ")";
	}

	@Override
	public String getQualifiedName() {
		// TODO Auto-generated method stub
		return getName();
	}

	@Override
	public int getSRID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getSupprtedDatatypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLayerUpdater(LayerUpdater layerUpdater) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Style getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(GC gc) {
		for (ILayer layer : this) {
			if (layer != null) layer.draw(gc);
		}
	}

	@Override
	public int getTupleCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GroupLayerConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return (GroupLayerConfiguration)this.configuration;
	}

	@Override
	public void setConfiguration(LayerConfiguration configuration) {
		if (configuration instanceof GroupLayerConfiguration)
		this.configuration = configuration;
		
	}

	@Override
	public boolean isActive() {
		// TODO Auto-generated method stub
		return active;
	}
	
	@Override
	public void setActive(boolean b){
		this.active  = b;
		for (ILayer layer : this) {
			if (layer != null) layer.setActive(b);
		}
	}
	
	public boolean containsDeep(GroupLayer o) {
		for (ILayer iLayer : this) {
			if (iLayer.isGroup()){
	//			if ((GroupLayer)iLayer).containsDeep(o)){
					return true;
			}
			
		}
		return super.contains(o);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getComplexName();
	}
	@Override
	public Envelope getEnvelope() {
		Envelope env = new Envelope();
		for (ILayer iLayer : this) {
			env.expandToInclude(iLayer.getEnvelope());
		}
		return env;
	}
}
