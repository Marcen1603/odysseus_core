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

package de.uniol.inf.is.odysseus.rcp.dashboard.test.queryprovider;

import java.util.List;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.SimpleQueryTextProvider;

public class SimpleQueryTextProviderTest {

	@Test
	public void testConstructor() {
		new SimpleQueryTextProvider(Lists.<String>newArrayList());
	}
	
	@Test
	public void testGetText() {
		List<String> text = Lists.newArrayList();
		text.add("Moin");
		text.add("Moin2");
		
		IDashboardPartQueryTextProvider provider = new SimpleQueryTextProvider(text);
		assertEquals(provider.getQueryText().size(), text.size());
		for( int i = 0; i < text.size(); i++ ) {
			assertEquals(provider.getQueryText().get(i), text.get(i));
		}
	}
	
	@Test( expectedExceptions = NullPointerException.class )
	public void testConstructorNullArgs() {
		new SimpleQueryTextProvider((List<String>)null);
	}
}
