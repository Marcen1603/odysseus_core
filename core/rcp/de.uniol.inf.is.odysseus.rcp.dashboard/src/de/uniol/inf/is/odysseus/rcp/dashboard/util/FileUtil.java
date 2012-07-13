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

package de.uniol.inf.is.odysseus.rcp.dashboard.util;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class FileUtil {

	public static List<String> read(IFile file) throws CoreException {
		if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		Scanner lineScanner = new Scanner(file.getContents());

		List<String> lines = Lists.newArrayList();
		while (lineScanner.hasNextLine()) {
			lines.add(lineScanner.nextLine());
		}
		return ImmutableList.copyOf(lines);		
	}
	
	public static void write( List<String> lines, IFile to ) throws CoreException {
		String oneString = concat(lines);
		to.setContents(new ByteArrayInputStream(oneString.getBytes()), IFile.KEEP_HISTORY | IFile.FORCE, null);
	}

	public static String concat(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for( String line : lines ) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	public static List<String> separateLines(String queryText) {
		Scanner lineScanner = new Scanner(queryText);

		List<String> lines = Lists.newArrayList();
		while (lineScanner.hasNextLine()) {
			lines.add(lineScanner.nextLine());
		}

		return lines;
	}
}
