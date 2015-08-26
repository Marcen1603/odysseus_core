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
package de.uniol.inf.is.odysseus.nexmark;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;

public class NexmarkApplication implements IApplication {
	private static final Logger logger = LoggerFactory
			.getLogger(BundleActivator.class);

	static private String categories;
	private static final String categoriesFile = "/config/categories.csv";

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();

		String[] args = (String[]) context.getArguments().get(
				"application.args");

		logger.debug("NexMark started ");

		// Right now, just a simple set of parameters
		if (args == null || args.length == 0 ) {

			String elementLimit = System.getenv("el");
			args = new String[elementLimit == null ? 5 : 7];
			args[0] = "-pr";
			args[1] = System.getenv("pr");
			if (args[1] == null)
				args[1] = "65440";
			args[2] = "-useNIO";
			String uN = System.getenv("useNIO");
			if (uN != null) {
				boolean useNIO = Boolean.parseBoolean(uN);
				if (!useNIO)
					args[2] = "";
			} else {
				uN = "true";
			}
			// Read from GeneratorConfigfile?
			args[3] = "-gcf";
			args[4] = System.getenv("gcf");
			if (args[4] == null) {
				args[4] = "/config/NEXMarkGeneratorConfiguration.properties";
			}
			if (args[4] == null || args[4] == "") {
				args[3] = "";
			}
			if (elementLimit != null) {
				args[5] = "-el";
				args[6] = elementLimit;
			}
		}
		URL catURL = context.getBrandingBundle().getEntry(categoriesFile);
		logger.debug("NexMark started " + args[0] + " " + args[1] + " "
				+ args[2] + " " + args[3] + " " + args[4]);
		logger.debug("Read Categories from " + categoriesFile + " --> "
				+ catURL);
		categories = readCategoryFile(catURL);
		logger.debug("done ");
		if (args[1] != null) {
			NexmarkServer.main(args);
		}
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	static public String getCategoryFile() {
		return categories;
	}

	static private String readCategoryFile(URL input) {
		BufferedReader br = null;
		String text = "";

		try {

			// Windows
			br = new BufferedReader(new InputStreamReader(input.openStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				text += line.trim()+"\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return text;

	}

}
