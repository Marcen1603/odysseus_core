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

package de.uniol.inf.is.odysseus.rcp.editor.text.pql.test.windows;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.rcp.editor.text.pql.windows.PQLAccessStatementGenWindow;
import de.uniol.inf.is.odysseus.testng.TestUtil;

public class PQLAccessStatementGenWindowTest {

	@Test(expectedExceptions = NullPointerException.class)
	public void testConstructorNullArgs() throws Throwable {
		new PQLAccessStatementGenWindow(null);
	}
	
	@Test
	public void testCreateWindow() throws Throwable {
		Shell parent = new Shell();
		Shell newWindow = TestUtil.invoke("createWindow", PQLAccessStatementGenWindow.class, parent);
		
		assertNotNull(newWindow, "Created window must not be null!");
		assertEquals(newWindow.getParent(), parent, "Parent is not equal!");
	}

}
