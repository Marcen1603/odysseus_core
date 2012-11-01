package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

public class LayerUpdater extends ArrayList<ILayer> implements IStreamElementListener<Object>, Serializable {

    private static final long serialVersionUID = 1092858542289960843L;
    
    private static final Logger LOG = LoggerFactory.getLogger(LayerUpdater.class);
    
	private final IStreamMapEditor streamMapEditor;
	private final IStreamConnection<Object> connection;
	private final IPhysicalQuery query;
	
	public LayerUpdater(IStreamMapEditor streamMapEditor,IPhysicalQuery query, IStreamConnection<Object> connection) {
		super();
		this.streamMapEditor = streamMapEditor;
		this.connection = connection;
		this.query = query;
		connection.addStreamElementListener(this);
		if(!connection.isConnected()){
			connection.connect();
		}
	}

	public IStreamConnection<Object> getConnection(){
		return connection;
	}

	public IPhysicalQuery getQuery() {
	    return query;
    }

	
	@Override
	public void streamElementRecieved(Object element, int port) {
		if (!(element instanceof Tuple<?>)) {
			LOG.error("Warning: StreamMap is only for spatial relational tuple!");
			return;
		}
		
		// SweepArea definiert Element Fenster bzw. die zu visualisierenden Elemente. 
		
		
		for (ILayer layer : this) {
			layer.addTuple((Tuple<?>) element);
			if (layer.getTupleCount() > this.streamMapEditor.getMaxTuplesCount()) {
				layer.removeLast();
			}
		}
		
		streamMapEditor.getScreenManager().getDisplay().asyncExec(new Runnable() {	
			@Override
			public void run() {
				streamMapEditor.getScreenManager().getCanvas().redraw();
				
			}
		});
		
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		
	}
	
}