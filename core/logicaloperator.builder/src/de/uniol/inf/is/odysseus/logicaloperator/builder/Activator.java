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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static final String SPLIT = "SPLIT";
	private static final String ACCESS = "ACCESS";
	private static final String FILEACCESS = "FILEACCESS";
	private static final String SELECT = "SELECT";
	private static final String JOIN = "JOIN";
	private static final String MAP = "MAP";
	private static final String PROJECT = "PROJECT";
	private static final String UNION = "UNION";
	private static final String RENAME = "RENAME";
	private static final String WINDOW = "WINDOW";
	private static final String DIFFERENCE = "DIFFERENCE";
	private static final String EXISTENCE = "EXISTENCE";
	private static final String AGGREGATION = "AGGREGATION";
	private static final String FILESINK = "FILESINK";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		OperatorBuilderFactory.putOperatorBuilderType(ACCESS, AccessAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(FILEACCESS, FileAccessAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SELECT, SelectAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(JOIN, JoinAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(PROJECT, ProjectAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(MAP, MapAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(UNION, UnionAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(RENAME, RenameAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(WINDOW, WindowAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(SPLIT, SplitAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(DIFFERENCE, DifferenceAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(EXISTENCE, ExistenceAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(AGGREGATION, AggregateAOBuilder.class);
		OperatorBuilderFactory.putOperatorBuilderType(FILESINK, FileSinkAOBuilder.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		OperatorBuilderFactory.removeOperatorBuilderType(ACCESS);
		OperatorBuilderFactory.removeOperatorBuilderType(FILEACCESS);
		OperatorBuilderFactory.removeOperatorBuilderType(SELECT);
		OperatorBuilderFactory.removeOperatorBuilderType(JOIN);
		OperatorBuilderFactory.removeOperatorBuilderType(MAP);
		OperatorBuilderFactory.removeOperatorBuilderType(PROJECT);
		OperatorBuilderFactory.removeOperatorBuilderType(UNION);
		OperatorBuilderFactory.removeOperatorBuilderType(RENAME);
		OperatorBuilderFactory.removeOperatorBuilderType(WINDOW);
		OperatorBuilderFactory.removeOperatorBuilderType(SPLIT);
		OperatorBuilderFactory.removeOperatorBuilderType(DIFFERENCE);
		OperatorBuilderFactory.removeOperatorBuilderType(EXISTENCE);
		OperatorBuilderFactory.removeOperatorBuilderType(AGGREGATION);
		OperatorBuilderFactory.removeOperatorBuilderType(FILESINK);		
	}

}
