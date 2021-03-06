/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider;

import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;

public class ResourceFileQueryTextProvider implements IDashboardPartQueryTextProvider {

	private static final Logger LOG = LoggerFactory.getLogger(ResourceFileQueryTextProvider.class);

	private final IFile queryFile;

	public ResourceFileQueryTextProvider(IFile queryFile) {
		this.queryFile = queryFile; Objects.requireNonNull(queryFile, "QueryFile must not be null!");
	}

	public IFile getFile() {
		return queryFile;
	}

	@Override
	public ImmutableList<String> getQueryText() {
		try {
			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
			}
			final Scanner lineScanner = new Scanner(queryFile.getContents());

			final List<String> lines = Lists.newArrayList();

			while (lineScanner.hasNextLine()) {
				lines.add(lineScanner.nextLine());
			}
			lineScanner.close();
			return ImmutableList.copyOf(lines);
		} catch (final Exception ex) {
			LOG.error("Could not get query text from file {}.", queryFile.getName(), ex);
			return ImmutableList.of();
		}
	}

}
