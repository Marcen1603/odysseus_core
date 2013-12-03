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
package de.uniol.inf.is.odysseus.core.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation extends IPunctuation{
	
	public Boolean evaluate(Long tupleTS, List<String> userRoles, Tuple<?> tuple, SDFSchema schema);

	public ISecurityPunctuation processSP(ISecurityPunctuation sp2);
	public ISecurityPunctuation union(ISecurityPunctuation sp2);
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2);
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2, Long newTS);

	public SDFSchema getSchema();
	
	public Object getAttribute(String key);	
	public Long getLongAttribute(String key);
	public Integer getIntegerAttribute(String key);
	public String getStringAttribute(String key);
	public String[] getStringArrayAttribute(String string);
	public ArrayList<String> getStringArrayListAttribute(String string);

	public int getNumberofAttributes();
	public Boolean isEmpty();
}
