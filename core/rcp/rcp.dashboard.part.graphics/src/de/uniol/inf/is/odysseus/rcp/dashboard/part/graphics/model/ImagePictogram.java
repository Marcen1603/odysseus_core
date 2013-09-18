/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author DGeesen
 *
 */
public class ImagePictogram extends Pictogram{


	private String filename;	
	private RelationalPredicate predicate;	
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#init(java.util.Map)
	 */
	@Override
	protected void load(Map<String, String> values) {		
		setFilename(values.get("filename"));
		setPredicate(values.get("predicate"));
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#save(java.util.Map)
	 */
	@Override
	protected void save(Map<String, String> values) {
		values.put("filename", filename);		
		values.put("predicate", predicate.toString());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#process(de.uniol.inf.is.odysseus.core.collection.Tuple)
	 */
	@Override
	protected void process(Tuple<?> tuple) {
		if(this.predicate.evaluate(tuple)){
			setVisibile(true);
		}else{
			setVisibile(false);
		}
		
	}

	/**
	 * @param predicate
	 */
	public void setPredicate(String predicate) {
		if(predicate==null){
			predicate = "true";
		}
		RelationalPredicate pred = new RelationalPredicate(new SDFExpression(predicate, MEP.getInstance()));		
		this.predicate = pred;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model.Pictogram#init(java.util.Collection)
	 */
	@Override
	protected void init(Collection<IPhysicalOperator> roots) {
		//TODO: this is only working with one root!!
		this.predicate.init(roots.iterator().next().getOutputSchema(), null);
	}
	
	

}
