/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test2.function.estimateupdate;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test2.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
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
@SuppressWarnings("unused")
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
	@Override
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
	
		
		IConnection connected = measurementTuple.getMetadata().getConnectionList().get(0);
		
		//correctStateEstimateFunction.compute(scannedTupleIndexPath, predictedTupleIndexPath, null );
	
		assertEquals(expectedTuple,measurementTuple);
	

	}
	
}