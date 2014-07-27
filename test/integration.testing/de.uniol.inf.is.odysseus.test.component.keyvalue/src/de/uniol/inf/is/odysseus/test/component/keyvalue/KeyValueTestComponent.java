/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.test.component.keyvalue;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

/**
 * 
 * @author Jan Soeren Schwarz
 *
 */
public class KeyValueTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	public KeyValueTestComponent() {
		super();
	}
	
	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		return TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(context.getDataRootPath(), "KEYVALUEOBJECT");	
	}
	
	@Override
	public String getName() {
		return "KeValue Test Component";
	}
	
	@Override
	public boolean isActivated() {
		return true;
	}

}