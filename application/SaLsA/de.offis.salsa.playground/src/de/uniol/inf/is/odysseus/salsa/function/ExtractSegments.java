package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.mep.IExpression;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 * @param <IMetadata>
 */
public class ExtractSegments extends AbstractFunction<List<RelationalTuple<TimeInterval>>> {
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private static Logger LOG = LoggerFactory.getLogger(ExtractSegments.class);
    
    public static final SDFDatatype[] accTypes0 = new SDFDatatype[] {
        SDFDatatype.SPATIAL_MULTI_POINT
    };
    public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
        SDFDatatype.DOUBLE
    };

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A matrix and a threashold.");
        }
        else {
            switch (argPos) {
                case 0:
                    return ExtractSegments.accTypes0; // Coordinate[].class;
                case 1:
                default:
                    return ExtractSegments.accTypes1;
            }
        }
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype getReturnType() {
        return new SDFDatatype(null, SDFDatatype.KindOfDatatype.TUPLE, SDFDatatype.SPATIAL);
    }

    @Override
    public String getSymbol() {
        return "ExtractSegments";
    }

    //FIXME need metadata to set timestamp
    @Override
    public List<RelationalTuple<TimeInterval>> getValue() {

    	PointInTime startTimestamp = new PointInTime();
    	
    	for (IExpression<?> expr : getArguments()) {
    		
    		if(expr.getReturnType().isStartTimestamp()){
    			//LOG.debug("Foundtime");
    			startTimestamp = (PointInTime)expr.getValue();
    		}
    		else{
    			//LOG.debug("Found no time");
    			startTimestamp = new PointInTime(System.currentTimeMillis());
    		}
    	}
    	
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double threshold = (Double) this.getInputValue(1);
        final Coordinate[] coordinates = geometry.getCoordinates();
        final List<RelationalTuple<TimeInterval>> segments = new LinkedList<RelationalTuple<TimeInterval>>();
        Coordinate tmp = new Coordinate(Double.MAX_VALUE, Double.MAX_VALUE);
        boolean isSegment = false;
        int start = 0;
        
        TimeInterval time = new TimeInterval(startTimestamp);
        
        for (int i = 0; i < coordinates.length; i++) {
            final Coordinate coordinate = coordinates[i];

            final Double distance = tmp.distance(coordinate);
            tmp = coordinate;
            if (distance >= threshold) {
                if (isSegment) {
                	
                	final List<Coordinate> segment = new ArrayList<Coordinate>();
                    for (int j = start; j < i; j++) {
                        if ((coordinates[j].x < Float.MAX_VALUE)
                                && (coordinates[j].y < Float.MAX_VALUE)) {
                            segment.add(coordinates[j]);
                        }
                    }
                    RelationalTuple<TimeInterval> tuple = new RelationalTuple<TimeInterval>(1);
                    tuple.setMetadata(time);
                    if (segment.size() > 1) {
                        tuple.setAttribute(0, this.geometryFactory.createLineString(segment.toArray(new Coordinate[] {})));
                        segments.add(tuple);
                    }
                    else if (segment.size() == 1) {
                        tuple.setAttribute(0, this.geometryFactory.createPoint(segment.get(0)));
                        segments.add(tuple);

                    }
                    isSegment = (tmp.x < Double.MAX_VALUE) && (tmp.y < Double.MAX_VALUE);
                }
                else {
                    isSegment = true;
                }
                start = i;
            }
        }
        if (isSegment) {
            final List<Coordinate> segment;
            if (coordinates[coordinates.length - 1].distance(coordinates[0]) > threshold) {
                segment = new ArrayList<Coordinate>();
            }
            else {
                segment = Arrays.asList(((Geometry) (segments.get(0).getAttribute(0)))
                        .getCoordinates());
                segments.remove(0);
            }
            for (int j = start; j < coordinates.length; j++) {
                if ((coordinates[j].x < Float.MAX_VALUE) && (coordinates[j].y < Float.MAX_VALUE)) {
                    segment.add(coordinates[j]);
                }
            }
            
            RelationalTuple<TimeInterval> tuple = new RelationalTuple<TimeInterval>(1);
            if (segment.size() > 1) {
                tuple.setMetadata(time);
                tuple.setAttribute(0, this.geometryFactory.createLineString(segment.toArray(new Coordinate[] {})));
                segments.add(tuple);
            }
            else if (segment.size() == 1) {
                tuple.setMetadata(time);
                tuple.setAttribute(0, this.geometryFactory.createPoint(segment.get(0)));
                segments.add(tuple);

            }
        }
        return segments;
    }
}
