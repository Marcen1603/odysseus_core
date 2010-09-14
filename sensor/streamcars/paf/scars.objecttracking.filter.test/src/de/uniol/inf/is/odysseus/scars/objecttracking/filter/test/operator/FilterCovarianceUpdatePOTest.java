/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.operator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterCovarianceUpdatePO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterGainPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.testdata.provider.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author dtwumasi
 *
 */
public class FilterCovarianceUpdatePOTest<K> {
	
	private FilterCovarianceUpdatePO covarianceUpdatePO;
	
	private AbstractMetaDataUpdateFunction metaDataUpdateFunction;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;

	private MVRelationalTuple<StreamCarsMetaData<K>> resultTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;
	
	private SDFAttributeList outputSchema;
	
	private SchemaHelper schemaHelper;
	private SchemaIndexPath oldObjectListPath;
	private SchemaIndexPath newObjectListPath;
	private SchemaIndexPath newObjPath;
	private SchemaIndexPath oldObjPath;
	
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
		
		double[][] gain = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688} };
			
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,5);
		
		
		// the expected tuple
		
		double[][] covarianceOldExp = { {-10.320000000000002,26.12}, {26.12,0.48} };
		
		
		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOldExp, speedNew, posNew, covarianceNew, gain,5);
	
		outputSchema = testData.getSchema();
		
		metaDataUpdateFunction = new KalmanCorrectStateCovarianceFunction();
		
		covarianceUpdatePO = new FilterCovarianceUpdatePO();
		
		covarianceUpdatePO.setMetaDataUpdateFunction(metaDataUpdateFunction);		
		
		
	
	
	
	}

	/**
	 * Test method for {@link de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterCovarianceUpdatePO#computeAll(de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple)}.
	 */
	@Test
	public void testComputeAll() {
		
		resultTuple = covarianceUpdatePO.computeAll(measurementTuple);
		
		assertEquals(expectedTuple,resultTuple);
	}

}
