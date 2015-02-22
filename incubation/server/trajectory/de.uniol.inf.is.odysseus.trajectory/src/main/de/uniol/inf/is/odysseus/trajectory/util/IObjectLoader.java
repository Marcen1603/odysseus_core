/*
 * Copyright 2015 Marcus Behrendt
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
 *
**/

package de.uniol.inf.is.odysseus.trajectory.util;

/**
 * An interface for loading objects of type <tt>E</tt> from parameter <tt>P</tt>
 * and additional information <tt>A</tt>.
 * 
 * @author marcus
 *
 * @param <E> the type of the object that will be loaded
 * @param <P> the type of parameter that is required to load an object of type <tt>E</tt>
 * @param <A> the type of the additional information to load an object of type <tt>E</tt>
 */
public interface IObjectLoader<E, P, A> {

	/**
	 * Loads and returns an object of type <tt>E</tt> from the the passed <i>parameter</i> and the
	 * <i>additional info</i>.
	 * 
	 * @param param the <i>parameter</i> that is required to load an object of type <tt>E</tt>
	 * @param additional the </i>additional</i> information to load an object of type <tt>E</tt>
	 * @return an object of type <tt>E</tt> loaded from the passed parameter and additional
	 *         info
	 */
	public E load(P param, A additional);
}
