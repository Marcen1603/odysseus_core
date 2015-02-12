package de.uniol.inf.is.odysseus.sports.sportsql.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * An operator for a heatmap.
 * @author Tobias Brandt
 *
 */
@LogicalOperator(name = "HEATMAP", minInputPorts = 1, maxInputPorts = 1, doc = "This operator creates a value array for heatmaps", category = { LogicalOperatorCategory.ADVANCED })
public class SportsHeatMapAO extends AbstractLogicalOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5048701632775540705L;

	public final static int REDUCE_LOAD_STANDARD_FACTOR = 1000;
	public final static int NUM_TIMES_HORIZONTAL_STANDARD = 15;
	public final static int NUM_TIMES_VERTICAL_STANDARD = 15;
	
	int reduceLoadFacor = REDUCE_LOAD_STANDARD_FACTOR;
	int numTilesX = NUM_TIMES_HORIZONTAL_STANDARD;
	int numTilesY = NUM_TIMES_VERTICAL_STANDARD;
	
	/**
	 * Creates a heatmap with standard settings
	 */
	public SportsHeatMapAO() {

	}
	
	/**
	 * 
	 * @param reduceLoadFactor The factor by which the load is reduced. If "1000", the map is only send every 1000 tuples of input
	 * @param numTilesHorizontal The number of tiles used horizontal
	 * @param numTilesVertical The number of tiles used vertical
	 */
	public SportsHeatMapAO(int reduceLoadFactor, int numTilesHorizontal, int numTilesVertical) {
		this.reduceLoadFacor = reduceLoadFactor;
		this.numTilesX = numTilesHorizontal;
		this.numTilesY = numTilesVertical;
	}

	public SportsHeatMapAO(SportsHeatMapAO heatmap) {
		this.reduceLoadFacor = heatmap.reduceLoadFacor;
		this.numTilesX = heatmap.numTilesX;
		this.numTilesY = heatmap.numTilesY;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SportsHeatMapAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add detected sentiment to schema
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute map = new SDFAttribute(null, "map", SDFDatatype.STRING, null, null, null);
		SDFAttribute map2 = new SDFAttribute(null, "map2", SDFDatatype.STRING, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(map);
		outputAttributes.add(map2);
		SDFSchema outSchema = new SDFSchema(inSchema, outputAttributes);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

	public int getReduceLoadFacor() {
		return reduceLoadFacor;
	}

	public void setReduceLoadFacor(int reduceLoadFacor) {
		this.reduceLoadFacor = reduceLoadFacor;
	}

	public int getNumTilesX() {
		return numTilesX;
	}

	public void setNumTilesX(int numTilesX) {
		this.numTilesX = numTilesX;
	}

	public int getNumTilesY() {
		return numTilesY;
	}

	public void setNumTilesY(int numTilesY) {
		this.numTilesY = numTilesY;
	}
	
	

}
