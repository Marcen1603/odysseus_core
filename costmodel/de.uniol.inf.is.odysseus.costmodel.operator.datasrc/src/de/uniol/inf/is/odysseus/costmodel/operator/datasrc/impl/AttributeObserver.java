package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.CombinedSampling;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.EqualWidthHistogramFactory;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.FreedmanDiaconisRule;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram.IHistogramFactory;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@SuppressWarnings("rawtypes")
public class AttributeObserver implements IDataSourceObserverListener {

	private static Logger _logger = null;
	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AttributeObserver.class);
		}
		return _logger;
	}

	private SDFAttribute attribute;
	private DataSourceObserverSink<?> source;
	private int schemaPosition;
	
	private IHistogramFactory factory;
	
	public AttributeObserver( SDFAttribute attribute ) {
		this.attribute = attribute;

		
		this.factory = new EqualWidthHistogramFactory(attribute, new CombinedSampling(2000), new FreedmanDiaconisRule());
		
		getLogger().debug("AttributeObserver for " + attribute + " created");
	}
	
	public boolean hasSink() {
		return this.source != null;
	}
	
	@SuppressWarnings("unchecked")
	public void setSink( DataSourceObserverSink<?> sink ) {
		if( this.source != null ) {
			this.source.removeListener(this);
		}
		this.source = sink;
		if( this.source != null ) {
			this.schemaPosition = this.source.getOutputSchema().indexOf(attribute);
			this.source.addListener(this);
			
			getLogger().debug("Source for " + attribute + " set");
			getLogger().debug("Schema-Position is " + this.schemaPosition);
		}
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
	
	@Override
	public void streamElementRecieved(DataSourceObserverSink sender, Object element, int port) {
		
		// direct input... e.g. cached values
		if( element instanceof Double ) {
			factory.addValue((Double)element);
			getLogger().debug("Direct value for " + attribute + ": " + ((Double)element) );
			return;
		}
		
		RelationalTuple<?> tuple = (RelationalTuple<?>)element;
		
		Object value = tuple.getAttribute(schemaPosition);
		getLogger().debug("Recieved value for " + attribute + ": " + value);
		
		if( value instanceof Double )
			factory.addValue((Double)value);
		else if (value instanceof Integer ) 
			factory.addValue( ((Integer)value).doubleValue() );
		else if (value instanceof Long ) 
			factory.addValue( ((Long)value).doubleValue() );
	}

	@Override
	public void punctuationElementRecieved(DataSourceObserverSink sender, PointInTime punctuation, int port) {

	}

	public IHistogram getHistogram() {
		return factory.create();
	}
	
	public Collection<Double> getValues() {
		return factory.getValues();
	}
}
