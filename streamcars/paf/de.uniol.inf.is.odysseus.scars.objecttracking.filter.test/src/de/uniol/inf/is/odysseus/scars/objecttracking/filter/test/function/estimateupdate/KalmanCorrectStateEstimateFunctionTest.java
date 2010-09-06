package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.function.estimateupdate;

import java.util.ArrayList;


import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;



import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateEstimateFunction;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * @author dtwumasi
 * @param <T>
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanCorrectStateEstimateFunctionTest<K> extends TestCase {

	private AbstractDataUpdateFunction correctStateEstimateFunction;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> resultTuple;
	
	private SDFAttributeList outputSchema;
	
	private SchemaHelper schemaHelper;
	private SchemaIndexPath oldObjectListPath;
	private SchemaIndexPath newObjectListPath;
	private SchemaIndexPath newObjPath;
	private SchemaIndexPath oldObjPath;
	
	TupleIndexPath scannedTupleIndexPath;
	TupleIndexPath predictedTupleIndexPath;
	 
	private String newObjListPath ="scan.newlist";
	
	private String oldObjListPath = "scan.oldlist";
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterFunctionTestData<K> testData = new FilterFunctionTestData<K>();
		
		// Measurement Data
		
		double speedOld = 0.9;
		
		double posOld = 1.7;
		
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		double speedNew = 1.0;
		
		double posNew = 2.0;
		
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		
		double[][] gain = { {0.2,0.2}, {0.1,0.4}};
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,1);
		
		this.outputSchema = testData.getSchema();
		
		this.schemaHelper = new SchemaHelper(this.outputSchema);
		
		
	    this.newObjectListPath = this.schemaHelper.getSchemaIndexPath(this.newObjListPath);
	    this.newObjPath = this.schemaHelper.getSchemaIndexPath(this.newObjListPath + SchemaHelper.ATTRIBUTE_SEPARATOR
	        + this.newObjectListPath.getAttribute().getSubattribute(0).getAttributeName());

	    this.oldObjectListPath = this.schemaHelper.getSchemaIndexPath(this.oldObjListPath);
	    this.oldObjPath = this.schemaHelper.getSchemaIndexPath(this.oldObjListPath + SchemaHelper.ATTRIBUTE_SEPARATOR
	        + this.oldObjectListPath.getAttribute().getSubattribute(0).getAttributeName());
	  
	    scannedTupleIndexPath = this.newObjPath.toTupleIndexPath(measurementTuple);
	    predictedTupleIndexPath = this.oldObjPath.toTupleIndexPath(measurementTuple);
		
		
		
		// the expected tuple
		
		double speedOldExp = 0.98;
		
		double posOldExp = 1.83;
		
		
		
		expectedTuple = testData.generateTestTuple(speedOldExp, posOldExp, covarianceOld, speedNew, posNew, covarianceNew,gain,1);
		
		correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction();
		
		
	
	
	}
	
	@Test
	public  void test() {
	
		
		Connection connected = measurementTuple.getMetadata().getConnectionList().get(0);
		
		correctStateEstimateFunction.compute(scannedTupleIndexPath, predictedTupleIndexPath, null );
	
		assertEquals(expectedTuple,measurementTuple);
	

	}
	
}