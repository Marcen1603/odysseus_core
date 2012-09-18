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

package de.uniol.inf.is.odysseus.rcp.dashboard.test;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.script.parser.PreParserStatement;

public class DashboardPlugInTest {

	private class SampleScriptParser implements IOdysseusScriptParser {

		@Override
		public List<?> parseAndExecute(String completeText, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {
			return null;
		}

		@Override
		public List<?> execute(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {
			return null;
		}

		@Override
		public List<PreParserStatement> parseScript(String completeText, ISession caller) throws OdysseusScriptException {
			return null;
		}

		@Override
		public List<PreParserStatement> parseScript(String[] textToParse, ISession caller) throws OdysseusScriptException {
			return null;
		}

		@Override
		public Map<String, String> getReplacements(String text) throws OdysseusScriptException {
			return null;
		}

		@Override
		public Map<String, String> getReplacements(String[] text) throws OdysseusScriptException {
			return null;
		}

		@Override
		public String getParameterKey() {
			return null;
		}

		@Override
		public String getReplacementStartKey() {
			return null;
		}

		@Override
		public String getReplacementEndKey() {
			return null;
		}

		@Override
		public String getSingleLineCommentKey() {
			return null;
		}

		@Override
		public Set<String> getKeywordNames() {
			return null;
		}

		@Override
		public Set<String> getStaticWords() {
			return null;
		}
	}
	
	@Test
	public void testConstructor() throws Throwable {
		new DashboardPlugIn();
	}
	
	@Test
	public void testBindScriptParser() throws Throwable {
		DashboardPlugIn plugin = new DashboardPlugIn();
		
		IOdysseusScriptParser parser = new SampleScriptParser();
		plugin.bindScriptParser(parser);
	
		assertEquals(DashboardPlugIn.getScriptParser(), parser, "Parser is not equal");
		
		plugin.unbindScriptParser(parser);
		assertNull(DashboardPlugIn.getScriptParser(), "Parser is not null after unbind");
	}
	
	@Test
	public void testUnbindOtherScriptParser() throws Throwable {
		DashboardPlugIn plugin = new DashboardPlugIn();

		IOdysseusScriptParser parser = new SampleScriptParser();
		IOdysseusScriptParser parser2 = new SampleScriptParser();
		
		plugin.bindScriptParser(parser);
		plugin.unbindScriptParser(parser2);
		
		assertEquals(DashboardPlugIn.getScriptParser(), parser, "Parser not equal after unbound by other parser");
	}
}
