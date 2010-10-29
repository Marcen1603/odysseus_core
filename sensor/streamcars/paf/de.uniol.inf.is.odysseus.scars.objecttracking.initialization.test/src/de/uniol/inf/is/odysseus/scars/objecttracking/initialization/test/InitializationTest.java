package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.test;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.initialization.AbstractInitializationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.initialization.InitializationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;




/**
 * @author dtwumasi
 *
 */
public class InitializationTest<K> {

	private AbstractInitializationFunction initializationFunction;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;

	private MVRelationalTuple<StreamCarsMetaData<K>> resultTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> initializationTuple;
	
	private SchemaHelper schemaHelper;
	private SchemaIndexPath newObjectListPath;
	private SchemaIndexPath oldObjectListPath;
	
	private String newObjListPath ="scan.newlist";
	
	private String oldObjListPath = "scan.oldlist";
	
	SDFAttributeList inputSchema;
	
	private HashMap<Enum , Object> parameters;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	FilterFunctionTestData testData = new FilterFunctionTestData();
	
	// Measurement Data

	double speedOld = 0.9;

	double posOld = 1.7;

	double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };

	double speedNew = 1.0;

	double posNew = 2.0;

	double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
	
	double speedIni = 1;

	double posIni = 0.5;

	double[][] covarianceIni = { {1,0}, {0,1} };
	
	measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, null,5);
	
	ConnectionList con = new ConnectionList();
	
	StreamCarsMetaData scars = new StreamCarsMetaData();
	
	scars.setConnectionList(con);
	
	measurementTuple.setMetadata(scars);
	
	// the expected tuple

	expectedTuple = testData.generateTestTuple(speedIni, posIni, covarianceIni, speedNew, posNew, covarianceNew, null,5);
	
	initializationFunction = new InitializationFunction();
	
	// Measurement Data

	initializationTuple = testData.generateDataTuple(speedIni, posIni, covarianceIni, null);
	
	inputSchema = testData.getSchema();
	
	this.schemaHelper = new SchemaHelper(inputSchema);

	this.newObjectListPath = this.schemaHelper.getSchemaIndexPath(newObjListPath);
	

	this.oldObjectListPath = this.schemaHelper.getSchemaIndexPath(oldObjListPath);
	
	parameters = new HashMap();
	
	parameters.put(Parameters.InitializationTuple, initializationTuple);
	
	initializationFunction.setParameters(parameters);
	
	
	}

	/**
	 * Test method for {@link de.uniol.inf.is.odysseus.scars.objecttracking.initialization.InitializationFunction#compute(de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple, de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath, de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath)}.
	 */
	@Test
	public void testCompute() {
		
		resultTuple = initializationFunction.compute(measurementTuple, newObjectListPath, oldObjectListPath);
		
		assertEquals(expectedTuple,resultTuple);
	
	}

}
