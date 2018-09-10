package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.LayerConfiguration;

public class PersistentMapEditorModel implements Serializable {

	private static final long serialVersionUID = 5385369189395888964L;

	private Integer srid = null;

	private LinkedList<PersistentQuery> queries = null;
	private LinkedList<LayerConfiguration> layers = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addLayers(LinkedList<ILayer> layers) {
		for (ILayer layer : layers) {
			if (!(layer instanceof BasicLayer)) {
				if (layer.getName().length() > 1)
					addLayer(layer);
				if (layer.isGroup())
					addLayers((LinkedList) layer);
			}
		}
	}

	public void addLayer(ILayer layer) {
		if (layers == null)
			layers = new LinkedList<LayerConfiguration>();
		layers.add(layer.getConfiguration());
	}

	public void addQuery(PersistentQuery query) {
		if (queries == null)
			queries = new LinkedList<PersistentQuery>();
		queries.add(query);
	}

	public LinkedList<LayerConfiguration> getLayers() {
		return layers;
	}

	public List<PersistentQuery> getQueries() {
		return queries;
	}

	public void setSrid(Integer srid) {
		this.srid = srid;
	}

	public Integer getSrid() {
		return srid;
	}
}
