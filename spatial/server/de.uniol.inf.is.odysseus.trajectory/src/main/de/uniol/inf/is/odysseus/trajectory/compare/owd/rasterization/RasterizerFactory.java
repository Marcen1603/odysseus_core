/*
 *Copyright 2015 Marcus Behrendt
 * 
 \* Licensed under the Apache License, Version 2.0 (the "License");
 \* you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractSimpleFactory;

/**
 * An implementation of <tt>AbstractSimpleFactory</tt> that returns
 * an <tt>IRasterizer</tt>.
 * 
 * @author marcus
 *
 */
public class RasterizerFactory extends AbstractSimpleFactory<IRasterizer> {

	/** the singleton instance */
	private final static RasterizerFactory INSTANCE = new RasterizerFactory();
	
	/**
	 * Returns the <tt>RasterizerFactory</tt> as an eager singleton.
	 * 
	 * @return the <tt>RasterizerFactory</tt> as an eager singleton
	 */
	public static RasterizerFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Beware this class from being instantiated because it is a <i>singleton</i>.
	 */
	private RasterizerFactory() { }
	
	@Override
	protected IRasterizer create() {
		return new AdvancedBresenhamRasterizer();
	}

}
