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
/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class RegExPredicate extends AbstractPredicate<Tuple<?>>
		implements IRelationalPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8478004831165060442L;

	RelationalPredicate predicate;
	private final SDFAttribute attribute;
	private int attributePosition;
	private final Pattern pattern;
	private Matcher matcher;

	public RegExPredicate(RegExPredicate predicate) {
		this.predicate = predicate.predicate;
		this.pattern = predicate.getPattern();
		this.attribute = predicate.getAttribute().clone();
	}

	public RegExPredicate(SDFAttribute attribute, String pattern) {
		this.pattern = Pattern.compile(pattern);
		this.attribute = attribute;
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		Iterator<SDFAttribute> it = leftSchema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			SDFAttribute attr = it.next();
			if (attr.equalsCQL(this.attribute)) {
				this.attributePosition = i;
				break;
			}
		}
	}

	/**
	 * @return the attribute
	 */
	public SDFAttribute getAttribute() {
		return attribute;
	}

	/**
	 * @return the pattern
	 */
	public Pattern getPattern() {
		return pattern;
	}

	public RelationalPredicate getChild() {
		return this.predicate;
	}

	@Override
	public RegExPredicate clone() {
		return new RegExPredicate(this);
	}

	@Override
	public boolean evaluate(Tuple<?> input) {
		String inputString = input.getAttribute(this.attributePosition)
				.toString();
		if (this.matcher == null) {
			this.matcher = this.pattern.matcher(inputString);
			return this.matcher.find();
		}
        return this.matcher.reset(inputString).find();
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RegExPredicate other = (RegExPredicate) obj;
		if (pattern == null) {
			if (other.pattern != null) {
				return false;
			}
		} else if (!pattern.equals(other.pattern)) {
			return false;
		}
		if (predicate == null) {
			if (other.predicate != null) {
				return false;
			}
		} else if (!predicate.equals(other.predicate)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "REGEX (" + getChild() + ") (" + pattern.toString() + ")";
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		List<SDFAttribute> attrList = new ArrayList<SDFAttribute>();				
		attrList.add(this.attribute);
		return Collections.unmodifiableList(attrList);
	}

	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean equals(IPredicate<Tuple<?>> pred) {
		return this.equals((Object)pred);
	}
	
	@Override
	public boolean isContainedIn(IPredicate<?> o) {
		if(!(o instanceof RegExPredicate)) {
			return false;
		}
		return false;
	}

}
