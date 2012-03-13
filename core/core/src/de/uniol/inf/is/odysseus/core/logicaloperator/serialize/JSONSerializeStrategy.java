/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.core.logicaloperator.serialize;

import java.util.Map;

/**
 * 
 * @author Dennis Geesen Created at: 16.01.2012
 */
public class JSONSerializeStrategy extends AbstractSerializerStrategy<String> {

	@Override
	public String performSerialize(SerializeNode node) {
		String s = "{";
//		s = s + serializeProperties(node.getProperties());
//		String sep = "";
//		for (SerializeNode child : node.getChilds()) {
//			s = s + sep + "\"" + child.getRepresentingClass().getCanonicalName() + "\" : " + this.performSerialize(child);
//			sep = ", " + System.getProperty("line.separator");
//		}
//		s = s + "}";
		return s;
	}

	@SuppressWarnings("unused")
	private static String serializeProperties(Map<String, ISerializeProperty<?>> values) {
		String s = "";
//		String sep = "";
//		for (Entry<String, SerializeProperty> e : values.entrySet()) {
//			s = s + sep + "\"" + e.getKey() + "\": ";
//			if (e.getValue().getValue() instanceof String) {
//				s = s + "\"" + e.getValue() + "\"";
//			} else {
//				s = s + e.getValue();
//			}
//			sep = "," + System.getProperty("line.separator");
//		}
		return s;
	}

	@Override
	public SerializeNode performDeserialize(String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
