package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamConnection;
import de.uniol.inf.is.odysseus.core.streamconnection.IStreamElementListener;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.DataSet;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

public class LayerUpdater extends ArrayList<ILayer> implements IStreamElementListener<Object>, Serializable, PropertyChangeListener {

    private static final long serialVersionUID = 1092858542289960843L;
    
    private static final Logger LOG = LoggerFactory.getLogger(LayerUpdater.class);
    
	private final IStreamMapEditor streamMapEditor;
	private final IStreamConnection<Object> connection;
	private final IPhysicalQuery query;
	
	private DefaultTISweepArea<Tuple<? extends ITimeInterval>> puffer;
	private HashMap<Integer, Quadtree> index = new HashMap<Integer, Quadtree>();
	private HashMap<Integer, Envelope> env = new HashMap<Integer, Envelope>();
	private int srid;

	private List<Tuple<? extends ITimeInterval>> elementList;
	
	public LayerUpdater(IStreamMapEditor streamMapEditor, IPhysicalQuery query, IStreamConnection<Object> connection) {
		super();
		this.streamMapEditor = streamMapEditor;
		this.connection = connection;
		this.query = query;
		this.puffer = new DefaultTISweepArea<Tuple<? extends ITimeInterval>>();
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
		@SuppressWarnings("unchecked")
		Tuple<? extends ITimeInterval> tuple = (Tuple<? extends ITimeInterval>) element;
		PointInTime timestamp = tuple.getMetadata().getStart().clone();
		this.streamMapEditor.getScreenManager().setMaxIntervalEnd(timestamp);
		puffer.insert(tuple);
    	if (this.streamMapEditor.getScreenManager().getInterval().getEnd().isInfinite()){
    		addTuple(tuple);
    	}
		
		streamMapEditor.getScreenManager().redraw();
		
	}

	@Override
	public void punctuationElementRecieved(IPunctuation point, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
		// TODO Auto-generated method stub
	}

    /**
     * @param tuple
     */
    public void addTuple(Tuple<? extends ITimeInterval> tuple) {
    	this.getElementList();
    	synchronized(elementList){
    		this.elementList.add(tuple);
    	}
		ScreenTransformation transformation = this.streamMapEditor.getScreenManager().getTransformation();
		int destSrid = this.streamMapEditor.getScreenManager().getSRID();
			for (Entry<Integer, Quadtree> tree : this.index.entrySet()) {
				synchronized (tree) {
					DataSet newDataSet = new DataSet(tuple, tree.getKey(), destSrid, transformation);
					tree.getValue().insert(newDataSet.getEnvelope(), newDataSet);
					this.env.get(tree.getKey()).expandToInclude(newDataSet.getEnvelope());

			}
		}
//		if (this.configuration.getMaxTupleCount() < this.dataSets.size()){
//				LOG.debug("(REMOVE)Current Geometries:" + dataSets.size());
//			synchronized (dataSets) {
//				DataSet toRemove = this.dataSets.poll();
//				this.tree.remove(toRemove.getEnvelope(), toRemove);
//			}
//		}
    }


	public List<?> query(Envelope searchEnv, int idx){
		return getTree(idx).query(searchEnv);
	}
	
	private Quadtree getTree(int idx){
		Quadtree tree = null;
		synchronized (this.index){
			tree = this.index.get(idx);
			ScreenTransformation transformation = this.streamMapEditor.getScreenManager().getTransformation();
			int destSrid = this.streamMapEditor.getScreenManager().getSRID();
			if (this.srid != destSrid){
					tree = null;
					this.srid = destSrid;
			}
			if (tree == null) { 
				tree = new Quadtree();
				this.env.put(idx, new Envelope());
				getElementList();
				synchronized(elementList){
					for (Tuple<? extends ITimeInterval> tuple : this.getElementList()) {
						DataSet newDataSet = new DataSet(tuple, idx, destSrid, transformation);
						tree.insert(newDataSet.getEnvelope(), newDataSet);
						this.env.get(idx).expandToInclude(newDataSet.getEnvelope());
					}
				}
				this.index.put(idx, tree);
			}
		}
		return tree;
	}
	
	@Override
	public boolean add(ILayer e) {
		e.setLayerUpdater(this);
		return super.add(e);
	}
	
	public List<Tuple<? extends ITimeInterval>> getElementList(){

		if (this.elementList == null){
			this.elementList = this.puffer.queryOverlapsAsList(this.streamMapEditor.getScreenManager().getInterval());
		}
		return this.elementList;
	}

	public Envelope getEnvelope(int idx) {
		// TODO Auto-generated method stub
		this.index.get(idx);
		return this.env.get(idx);
	}

	public int size(int idx) {
		// TODO Auto-generated method stub
		return this.elementList.size();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("intervalStart".equals(evt.getPropertyName())){
			this.elementList = this.puffer.queryOverlapsAsList(this.streamMapEditor.getScreenManager().getInterval());
			//System.out.println(this.puffer.getMinTs() + " " + this.puffer.getMaxTs() + " " + this.elementList.size());
			this.index = new HashMap<Integer, Quadtree>(this.index.size());
		}
		if ("intervalEnd".equals(evt.getPropertyName())){
			this.elementList = this.puffer.queryOverlapsAsList(this.streamMapEditor.getScreenManager().getInterval());
			this.index = new HashMap<Integer, Quadtree>(this.index.size());
		}
	}
}