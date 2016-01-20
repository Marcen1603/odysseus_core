/********************************************************************************** 
 * Copyright 2016 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.predicate;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$ *
 *
 */
public class NullPredicate<T> extends AbstractPredicate<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 77266992186012639L;
	private static final NullPredicate instance = new NullPredicate();

	static public NullPredicate getInstance() {
		return instance;
	}

	private NullPredicate() {
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(Object input) {
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Boolean evaluate(Object left, Object right) {
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public AbstractPredicate<T> clone() {
		return this;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(IPredicate<T> predicate) {
		return predicate.getClass().equals(this.getClass());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isContainedIn(IPredicate<?> predicate) {
		return predicate.getClass().equals(this.getClass());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "null";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> and(IPredicate<T> predicate) {
		return predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPredicate<T> or(IPredicate<T> predicate) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPredicate<T> not() {
		return NullPredicate.getInstance();
	}
}
