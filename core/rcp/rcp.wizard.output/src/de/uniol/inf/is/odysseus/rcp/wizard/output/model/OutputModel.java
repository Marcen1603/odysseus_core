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

package de.uniol.inf.is.odysseus.rcp.wizard.output.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.ui.IViewPart;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;

/**
 * 
 * @author Dennis Geesen Created at: 01.12.2011
 */
public class OutputModel {

	private String queryFile;
	private ISink<?> sink;
	private IViewPart viewPart;

	public String getQueryFile() {
		return queryFile;
	}

	public void setQueryFile(String queryFile) {
		this.queryFile = queryFile;
	}

	public String getQueryText() throws FileNotFoundException {
		String text = "";
		Scanner scanner = new Scanner(new File(queryFile));
		while (scanner.hasNextLine()) {
			text = text + scanner.nextLine() + "\n";
		}
		return text;
	}

	public ISink<?> getSink() {
		return sink;
	}

	public void setSink(ISink<?> sink) {
		this.sink = sink;
	}

	public IViewPart getViewPart() {
		return viewPart;
	}

	public void setViewPart(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

}
