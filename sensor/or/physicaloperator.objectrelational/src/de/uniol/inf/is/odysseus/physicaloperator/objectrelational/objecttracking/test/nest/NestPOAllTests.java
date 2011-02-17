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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.CreateOutputTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.GroupId;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.Nest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.NestOfNesting;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.TwoGroupingAttributeNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.suites.UpdateAllTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class NestPOAllTests extends TestSuite {
    public NestPOAllTests() {
        this.setName("Asserting all NestPO tests");
    }
    
    public static Test suite() {
    	TestSuite suite = new TestSuite("ObjectTracking NestPO tests");
    	
    	suite.addTest(new GroupId());
    	//suite.addTest(new Merge());
    	suite.addTest(new UpdateAllTests());
    	suite.addTest(new CreateOutputTuple());
    	suite.addTest(new Nest());
    	suite.addTest(new TwoGroupingAttributeNest());
    	suite.addTest(new NestOfNesting());
    	
		return suite;
    }    
}

