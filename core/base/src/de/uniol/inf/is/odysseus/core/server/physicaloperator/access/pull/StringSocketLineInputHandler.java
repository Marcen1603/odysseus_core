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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringSocketLineInputHandler extends AbstractSocketInputHandler<String> {
	private static final Logger LOG = LoggerFactory
			.getLogger(StringSocketLineInputHandler.class);
	private BufferedReader reader;

	public StringSocketLineInputHandler() {
		// needed for declarative service
	}
	
	public StringSocketLineInputHandler(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}

	@Override
	public IInputHandler<String> getInstance(Map<String, String> options) {
		return new StringSocketLineInputHandler(options.get("host"), Integer.parseInt(options.get("port")), 
				options.get("user"), options.get("password"));
	}
	
	@Override
	public void init() {
		super.init();
		reader = new BufferedReader(new InputStreamReader(
				getInputStream())); 
	}
	
	@Override
	public boolean hasNext() {
		try {
			return reader.ready();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getNext() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getName() {
		return "StringSocketLine";
	}
	
}
