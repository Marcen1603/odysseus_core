/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.objecttracking.parser;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateProjectionVisitor;

public class CreateMVProjectionVisitor implements IVisitor{
	
	static boolean registerd = VisitorFactory.getInstance().setVisitor(new CreateMVProjectionVisitor(), "MVProjection");
	
	ISession user = null;
	
	@Override
	public void setUser(ISession user) {
		this.user = user;	
	}
	
	@Override
	public void setDataDictionary(IDataDictionary dd) {
		// Not needed
	}
	
	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		CreateProjectionVisitor cpv = (CreateProjectionVisitor)baseObject;
		
		ObjectTrackingProjectAO project = new ObjectTrackingProjectAO();
		project.subscribeTo(cpv.getTop(), cpv.getTop().getOutputSchema());
		project.setOutputSchema(cpv.getOutputSchema());
		return project;
	}

}
