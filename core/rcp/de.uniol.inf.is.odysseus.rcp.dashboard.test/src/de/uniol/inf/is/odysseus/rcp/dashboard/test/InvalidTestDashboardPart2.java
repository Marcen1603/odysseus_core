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

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;

// init fails here on purpose for testing
public class InvalidTestDashboardPart2 implements IDashboardPart {

	@Override
	public void streamElementRecieved(IStreamObject<?> element, int port) {
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
	}

	@Override
	public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
	}

	@Override
	public boolean init(Configuration configuration) {
		return false;
	}

	@Override
	public Configuration getConfiguration() {
		return null;
	}

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onUnpause() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public Map<String, String> onSave() {
		return null;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider file) {
	}

	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return null;
	}

}
