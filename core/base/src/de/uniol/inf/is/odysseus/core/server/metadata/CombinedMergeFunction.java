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
package de.uniol.inf.is.odysseus.core.server.metadata;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.Query;

public class CombinedMergeFunction<T extends IClone> implements
		IMetadataMergeFunction<T>, IClone {

	protected static Logger _logger = null;

	protected static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(CombinedMergeFunction.class);
		}
		return _logger;
	}
	
	private ArrayList<IInlineMetadataMergeFunction<? super T>> mergeFunctions = new ArrayList<IInlineMetadataMergeFunction<? super T>>();

	public CombinedMergeFunction() {
	}

	public CombinedMergeFunction(CombinedMergeFunction<T> cmf)  {
		for (IInlineMetadataMergeFunction<? super T> mf : cmf.mergeFunctions) {
			this.mergeFunctions.add(mf.clone());
		}
	}

	@Override
	public void init() {
	}

	public void add(IInlineMetadataMergeFunction<? super T> func) {
		this.mergeFunctions.add(func);
	}
	
	public boolean containsMergeFunction(Class<? extends IInlineMetadataMergeFunction<? super T>> type){
		for(IInlineMetadataMergeFunction<? super T> curFunc : mergeFunctions){
			if(curFunc.getClass() == type){
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T mergeMetadata(T left, T right) {
		T mergedData;
		try {
			mergedData = (T) left.getClass().newInstance();

			for (IInlineMetadataMergeFunction<? super T> func : mergeFunctions) {
				func.mergeInto(mergedData, left, right);
			}
			return mergedData;
		} catch (Exception e) {
			getLogger().error("could not merge metadata", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public CombinedMergeFunction<T> clone() {
		return new CombinedMergeFunction<T>(this);
	}

}
