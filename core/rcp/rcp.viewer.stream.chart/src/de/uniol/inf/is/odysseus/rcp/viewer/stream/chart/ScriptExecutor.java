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

package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;


/**
 * 
 * @author Dennis Geesen Created at: 02.12.2011
 */
public class ScriptExecutor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<ISource<?>> loadAndExecuteQueryScript(String queryFile) {
		try {
			ISession caller = OdysseusRCPPlugIn.getActiveSession();
			DummySink<IStreamObject<?>> dummy = new DummySink<IStreamObject<?>>();
			String text = "";
			Scanner scanner = new Scanner(new File(queryFile));
			while (scanner.hasNextLine()) {
				text = text + scanner.nextLine() + "\n";
			}
			scanner.close();
			OdysseusRCPEditorTextPlugIn.getScriptParser().parseAndExecute(text, caller, dummy, Context.emptyContext());
			List<ISource<?>> sources = new ArrayList<ISource<?>>();
			List<PhysicalSubscription<?>> toRemove = new ArrayList<PhysicalSubscription<?>>();

			for (PhysicalSubscription<?> sub : dummy.getSubscribedToSource()) {
				ISource<?> source = (ISource<?>) sub.getTarget();
				sources.add(source);
				toRemove.add(sub);
			}

			for (ISource source : sources) {
				source.unsubscribeSink(new PhysicalSubscription<ISink>(dummy, 0, 0, source.getOutputSchema()));
			}
			return sources;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ISource<?>>();
	}
}
