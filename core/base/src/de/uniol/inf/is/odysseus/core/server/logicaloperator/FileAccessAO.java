/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Deprecated
public class FileAccessAO extends AbstractAccessAO {

	private static final long serialVersionUID = 3955519214402695311L;

	private String path;
	private String fileType;
	private String separator = ";";

	// This variable will be used to generate an ID for every new input tuple.
	private static long ID = 1;

	// This variable will be used, if a wildcard is necessary for an id
	private static Long wildcard = Long.valueOf(-1);

	public FileAccessAO() {
		super();
	}

	public FileAccessAO(FileAccessAO po) {
		super(po);
		this.path = po.path;
		this.fileType = po.fileType;
		this.separator = po.separator;
	}

	public FileAccessAO(String sourceName, String type, Map<String, String> optionsMap) {
		super(sourceName, type, optionsMap);
	}

	@Override
	public FileAccessAO clone() {
		return new FileAccessAO(this);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	private static long genID() {
		return ++ID;
	}

	public static List<Long> nextID() {
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(Long.valueOf(genID()));
		return idList;
	}

	public static Long getWildcard() {
		return wildcard;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
