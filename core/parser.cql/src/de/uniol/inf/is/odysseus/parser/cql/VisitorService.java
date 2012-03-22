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

package de.uniol.inf.is.odysseus.parser.cql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.02.2012
 */
public class VisitorService {
	
	private Logger logger = LoggerFactory.getLogger(VisitorService.class);
	
	public void addVisitor(IVisitor visitor){
		String name = generateName(visitor);
		logger.debug("Adding visitor for CQL: "+name);
		VisitorFactory.getInstance().setVisitor(visitor, name);
	}
	
	public void removeVisitor(IVisitor visitor){
		String name = generateName(visitor);
		logger.debug("Removing visitor for CQL: "+name);
		VisitorFactory.getInstance().removeVisitor(name);
	}
	
	
	private static String generateName(IVisitor visitor){
		return visitor.getClass().getCanonicalName();
	}
}
