/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.text;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.server.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.text.function.ColognePhoneticFunction;
import de.uniol.inf.is.odysseus.text.function.LevensteinFunction;
import de.uniol.inf.is.odysseus.text.function.MetaphoneFunction;
import de.uniol.inf.is.odysseus.text.function.SoundexFunction;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TextFunctionProvider implements IFunctionProvider {
	private static final Logger LOG = LoggerFactory
			.getLogger(TextFunctionProvider.class);

	public TextFunctionProvider() {

	}

	@Override
	public List<IFunction<?>> getFunctions() {

		final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		functions.add(new SoundexFunction());
		functions.add(new ColognePhoneticFunction());
		functions.add(new MetaphoneFunction());
		functions.add(new LevensteinFunction());
		TextFunctionProvider.LOG.info(String.format("Register functions: %s",
				functions));
		return functions;
	}

}
