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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.Configuration;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;

public class AbstractDashboardPartTest {

	private static class TestDashboardPart extends AbstractDashboardPart {
		@Override
		public void createPartControl(Composite parent, ToolBar toolbar) {
		}

		@Override
		public void punctuationElementRecieved(IPunctuation punctuation, int port) {
		}

		@Override
		public void securityPunctuationElementRecieved(ISecurityPunctuation sp, int port) {
		}

		@Override
		public void settingChanged(String settingName, Object oldValue, Object newValue) {
		}

		@Override
		public void streamElementRecieved(IStreamObject<?> element, int port) {
		}
	}

	private static class TestQueryTextProvider implements IDashboardPartQueryTextProvider {
		@Override
		public ImmutableList<String> getQueryText() {
			return null;
		}
	}

	@Test
	public void testDefaultImplementations() {
		final TestDashboardPart part = new TestDashboardPart();

		part.onLoad(Maps.<String, String> newHashMap());
		part.onPause();
		part.onUnpause();
		part.onStop();

		assertNotNull(part.onSave());
	}

	@Test
	public void testInit() throws Exception {
		final Configuration config = newConfiguration();

		final TestDashboardPart part = new TestDashboardPart();

		assertTrue(part.init(config));
		assertEquals(part.getConfiguration(), config);
	}

	@Test
	public void testQueryTextProviderGetAndSet() {
		final TestDashboardPart part = new TestDashboardPart();

		final IDashboardPartQueryTextProvider provider = new TestQueryTextProvider();
		part.setQueryTextProvider(provider);
		assertEquals(part.getQueryTextProvider(), provider);
	}

	private Configuration newConfiguration() {
		final Map<String, Setting<?>> settingMap = Maps.newHashMap();
		return new Configuration(settingMap);
	}
}
