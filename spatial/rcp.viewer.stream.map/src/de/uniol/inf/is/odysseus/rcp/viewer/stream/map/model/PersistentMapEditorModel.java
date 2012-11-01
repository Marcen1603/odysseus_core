package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.PQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.PersestentStyle;

public class PersistentMapEditorModel implements Serializable {

    private static final long serialVersionUID = 5385369189395888964L;
    
    private LinkedList<String> files = null;
	private LinkedList<PQuery> queries = null;
	private LinkedList<LayerConfiguration> layers = null;
	
	public void addLayer(ILayer layer){
		if(layers == null)
			layers = new LinkedList<LayerConfiguration>();
		
		if(layer.getStyle() != null)
			layer.getConfiguration().setStyle(new PersestentStyle(layer.getStyle()));
		layers.add(layer.getConfiguration());
	}
	
	public void addQuery(PQuery query){
		if(queries == null)
			queries = new LinkedList<PQuery>();
		
		queries.add(query);
	}
	
	public void addFile(String file){
		if(files == null)
			files = new LinkedList<String>();
		
		files.add(file);
	}
	
	public LinkedList<LayerConfiguration> getLayers(){
		return layers;
	}
	
	public List<PQuery> getQueries(){
		return queries;
	}
	
	public List<String> getFiles(){
		return files;
	}
	
}
