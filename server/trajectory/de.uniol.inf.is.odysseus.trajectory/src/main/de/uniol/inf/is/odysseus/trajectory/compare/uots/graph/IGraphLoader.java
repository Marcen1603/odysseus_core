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

package de.uniol.inf.is.odysseus.trajectory.compare.uots.graph;

import de.uniol.inf.is.odysseus.trajectory.util.IObjectLoader;

/**
 * An extension to <tt>IObjectLoader</tt> in order load a <tt>NetGraph</tt>.
 *  
 * @param <P> type of the parameter which is required to load a <tt>NetGraph</tt>
 * @param <A> type of the additional Information which is required to load a <tt>NetGraph</tt>
 * 
 * @author marcus
 */
public interface IGraphLoader<P, A> extends IObjectLoader<NetGraph, P, A> {

}
