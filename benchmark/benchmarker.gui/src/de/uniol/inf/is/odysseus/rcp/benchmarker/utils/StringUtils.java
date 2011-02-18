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
package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

/**
 * Diese Klasse überprüft, ob Strings empty oder blank sind
 * 
 * @author Stefanie Witzke
 * 
 */
public final class StringUtils {

	private StringUtils() {
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isNotBlank(String... strings) {
		if (strings == null) {
			return false;
		}
		for (String str : strings) {
			if (isNotBlank(str)) {
				return true;
			}
		}
		return false;
	}

	public static String nameToFoldername(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must be given!");
		}

		StringBuilder sb = new StringBuilder(name.length());
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if (Character.isLetterOrDigit(c)) {
				sb.append(c);
			} else if (c == ' ') {
				sb.append('_');
			}
		}

		return sb.toString();
	}

	public static String foldernameToName(String folderName) {
		if (folderName == null) {
			throw new IllegalArgumentException("FolderName must be given!");
		}

		StringBuilder sb = new StringBuilder(folderName.length());
		char[] chars = folderName.toCharArray();
		for (char c : chars) {
			if (c == '_') {
				sb.append(' ');
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String splitString(String string) {
		String[] stringArray = string.split("\\.");
		int i = stringArray.length;
		return stringArray[i - 1];
	}
}