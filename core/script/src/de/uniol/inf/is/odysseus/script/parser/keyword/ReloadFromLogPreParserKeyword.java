/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.script.parser.keyword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * 
 * @author Dennis Geesen Created at: 18.08.2011
 */
public class ReloadFromLogPreParserKeyword extends AbstractPreParserKeyword {

	private static Logger logger = LoggerFactory.getLogger(ReloadFromLogPreParserKeyword.class);
	public static final String LOG_FILENAME = System.getProperty("user.home") + "/odysseus/reloadlog.store";

	@Override
	public void validate(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, ISession caller) throws OdysseusScriptException {
		logger.debug("Start reloading queries from reload log file...");
		loadData(caller);
		logger.debug("Reloading queries from reload log file done.");
		return null;
	}

	private void loadData(ISession caller) {

		String toParse = "";
		String newline = System.getProperty("line.separator");
		try {
			File file = new File(LOG_FILENAME);
			BufferedReader in = new BufferedReader(new FileReader(file));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				zeile = zeile.trim();
				if (!zeile.contains("#USER")) {
					toParse = toParse + newline + zeile;
				}
			}
			getParser().parseAndExecute(toParse, caller, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OdysseusScriptException e) {
			e.printStackTrace();
		}

	}

}
