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
package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test.nest;


import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases.NestOfNesting;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases.SimpleNesting;

import junit.framework.TestSuite;

public class NestAOAllTests extends TestSuite {
    public NestAOAllTests() {
        this.addTest(new SimpleNesting());
        this.addTest(new NestOfNesting());
    }
}
