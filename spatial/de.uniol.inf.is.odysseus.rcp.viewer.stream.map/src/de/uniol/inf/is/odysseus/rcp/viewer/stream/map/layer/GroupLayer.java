package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.GC;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditorPart;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.GroupLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

public class GroupLayer extends LinkedList<ILayer> implements ILayer {

	private LayerConfiguration configuration;
	
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
	public void addTuple(Tuple<?> tuple) {
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
		return true;
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
}
