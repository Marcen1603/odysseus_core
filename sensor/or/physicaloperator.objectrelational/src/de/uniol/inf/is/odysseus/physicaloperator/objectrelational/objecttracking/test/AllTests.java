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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.nest.NestPOAllTests;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.UnnestPOAllTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {
    public static Test suite() {
    	TestSuite suite = new TestSuite("AllTests");    	
        
    	suite.addTest(NestPOAllTests.suite());
        suite.addTest(UnnestPOAllTests.suite());
        
    	return suite;
    } 
}