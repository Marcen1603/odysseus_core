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
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.Arrays;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndex;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;

/**
 * 
 * @author Benjamin G
 *
 */
public class StreamCarsExpressionVariable implements IStreamCarsExpressionVariable {

	public static final String NO_SOURCE = "NO_SOURCE";

	private final IStreamCarsExpression parent;
	private Variable mepVariable;
	private SchemaIndexPath schemaPath;

	private String name;
	private String nameWithoutMetadata;
	private String sourceName;
	private String metadataInfo;

	private int[] relativePath;
	private int[] absolutePath;

	/**
	 * 
	 * @param parent
	 * @param mepVariable
	 */
	protected StreamCarsExpressionVariable(IStreamCarsExpression parent, Variable mepVariable) {
		this.parent = parent;
		this.mepVariable = mepVariable;
		this.name = this.mepVariable.getIdentifier();
		this.initSourceName(this.name);
		this.initMetadataInfo(this.name);
	}

	/**
	 * 
	 * @param parent
	 * @param name
	 */
	protected StreamCarsExpressionVariable(IStreamCarsExpression parent, String name) {
		this.parent = parent;
		this.name = name;
		this.initSourceName(this.name);
		this.initMetadataInfo(this.name);
	}

	@Override
	public IStreamCarsExpression getParent() {
		return parent;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSourceName() {
		return sourceName;
	}

	@Override
	public String getMetadataInfo() {
		return metadataInfo;
	}

	@Override
	public Object getValue() {
		return mepVariable != null ? mepVariable.getValue() : null;
	}

	@Override
	public double getDoubleValue() {
		return ((Number) getValue()).doubleValue();
	}

	@Override
	public int[] getPath() {
		return absolutePath;
	}

	@Override
	public int[] getPath(boolean copy) {
		if(copy) {
			int[] copyArray = new int[this.absolutePath.length];
			System.arraycopy(this.absolutePath, 0, copyArray, 0, this.absolutePath.length);
			return copyArray;
		}
		return absolutePath;
	}

	@Override
	public boolean isSchemaVariable() {
		return !sourceName.equals(NO_SOURCE);
	}

	@Override
	public boolean isSchemaVariable(SDFSchema schema) {
		if(!isSchemaVariable()) return false;
		return sourceName.equals(getSchemaSourceName(schema));
	}

	@Override
	public boolean isVirtual() {
		return mepVariable == null;
	}

	@Override
	public boolean hasMetadataInfo() {
		return metadataInfo != null;
	}


	@Override
	public void replaceVaryingIndex(int index) {
		if(isSchemaVariable()) {
			if(schemaPath == null) throw new RuntimeException("the variable " + name + "was not properly initialized: schemaIndexPath is null");
			for(int i=0; i<absolutePath.length; i++) {
				if(relativePath[i] == -1) {
					absolutePath[i] = index;
				}
			}
		}
	}

	@Override
	public void replaceVaryingIndex(int index, boolean copy) {
		if(!copy) {
			replaceVaryingIndex(index);
			return;
		}
		if(isSchemaVariable()) {
			if(schemaPath == null) throw new RuntimeException("the variable " + name + "was not properly initialized: schemaIndexPath is null");
			absolutePath = new int[relativePath.length];
			for(int i=0; i<absolutePath.length; i++) {
				if(relativePath[i] == -1) {
					absolutePath[i] = index;
				} else {
					absolutePath[i] = relativePath[i];
				}
			}
		}
	}

	@Override
	public void bind(Object value) {
		if(mepVariable != null) {
			mepVariable.bind(value);
		}
	}

	@Override
	public void bindTupleValue(MVRelationalTuple<?> tuple) {
		Object currentAttr = tuple;
		int depth = absolutePath.length - 1;
		for(int i=0; i<depth; i++) {
			if(currentAttr instanceof MVRelationalTuple) {
				currentAttr = ((MVRelationalTuple<?>)currentAttr).getAttribute(absolutePath[i]);
			} else if(currentAttr instanceof List) {
				currentAttr = ((List<?>)currentAttr).get(absolutePath[i]);
			}
		}
		
		if(currentAttr instanceof MVRelationalTuple) {
			bind(((MVRelationalTuple<?>)currentAttr).getAttribute(absolutePath[depth]));
		} else if(currentAttr instanceof List) {
			bind(((List<?>)currentAttr).get(absolutePath[depth]));
		}
	}

	@Override
	public boolean isInList(int[] pathToList) {
		int listIndex = pathToList.length - 1;
		
		if( schemaPath.getLength() < pathToList.length)
			return false;
		
		for( int i = 0; i < pathToList.length; i++ ) {
			int index = schemaPath.getSchemaIndex(i).toInt();
			if( index != pathToList[i])
				return false;
		}
		
		return schemaPath.getSchemaIndex(listIndex).isList();
//		int listIndex = pathToList[pathToList.length-1];
//		return schemaPath.getSchemaIndex(listIndex).isList();
	}

	@Override
	public boolean isInList(TupleIndexPath pathToList) {
		int listIndex = pathToList.getLength() - 1;
		
		if( schemaPath.getLength() < pathToList.getLength())
			return false;
		
		for( int i = 0; i < pathToList.getLength(); i++ ) {
			int index = schemaPath.getSchemaIndex(i).toInt();
			if( index != pathToList.getTupleIndex(i).toInt())
				return false;
		}
		
		return schemaPath.getSchemaIndex(listIndex).isList();
	}

	@Override
	public SchemaIndexPath getSchemaIndexPath() {
		return schemaPath;
	}

	/**
	 * @param schema
	 */
	@Override
	public void init(SDFSchema schema) {
		if(isSchemaVariable(schema)) {
			SchemaHelper helper = new SchemaHelper(schema);
			schemaPath = helper.getSchemaIndexPath(nameWithoutMetadata);
			initRelativeIndex();
			absolutePath = schemaPath.toArray(true);
		}
	}
	
	/**
	 * @return
	 */
	@Override
	public String getNameWithoutMetadata() {
		return nameWithoutMetadata;
	}

	/**
	 * 
	 * @param variableName
	 */
	protected void initMetadataInfo(String variableName) {
		String[] split = variableName.split("\\#");
		if(split.length > 2) {
			throw new IllegalArgumentException("the variable: " + variableName + " has more than one metadata seperators ('#'");
		} else if(split.length < 2) {
			this.metadataInfo = null;
			nameWithoutMetadata = variableName;
		} else {
			nameWithoutMetadata = split[0];
			metadataInfo = split[1];
		}
	}

	/**
	 * 
	 * @param variableName
	 */
	protected void initSourceName(String variableName) {
		String[] dotSplit = variableName.split("\\.");
		if(dotSplit.length > 2) {
			throw new IllegalArgumentException("the variable: " + variableName + " has more than one source seperators ('.'");
		} else if(dotSplit.length < 2) {
			sourceName = NO_SOURCE;
		} else {
			sourceName =  dotSplit[0];
		}
	}

	/**
	 * 
	 */
	protected void initRelativeIndex() {
		if(schemaPath == null) throw new RuntimeException("the variable " + name + "was not properly initialized: schemaIndexPath is null");
		relativePath = new int[schemaPath.getLength()];
		boolean isUnderList = false;
		for(int i=0; i<relativePath.length; i++) {
			SchemaIndex index = schemaPath.getSchemaIndex(i);
			if(isUnderList) {
				relativePath[i] = -1;
				isUnderList = false;
//				relativePathIndexIndex = i;
			} else {
				relativePath[i] = index.getIndex();
			}

			if(index.isList()) {
				isUnderList = true;
			}

		}
	}

	/**
	 * 
	 * @param schema
	 * @return
	 */
	protected String getSchemaSourceName(SDFSchema schema) {
		if(schema == null) return NO_SOURCE;
		return schema.getAttribute(0).getSourceName();
	}

	@Override
	public String toString() {
		return "\n\t ScarsExpressionVariable ["
				+ "\n\t\t name: " + name
				+ "\n\t\t value: " + getValue()
				+ "\n\t\t nameWithoutMetadata: " + nameWithoutMetadata
				+ "\n\t\t sourceName: " + sourceName
				+ "\n\t\t metadataInfo: " + metadataInfo
				+ "\n\t\t isSchemaVariable: " + isSchemaVariable()
				+ "\n\t\t isVirtual: " + isVirtual()
				+ "\n\t\t schemaPath: " + schemaPath
				+ "\n\t\t relativePath: " + (relativePath != null ? Arrays.toString(relativePath) : null)
				+ "\n\t\t absolutePath: " + (absolutePath != null ? Arrays.toString(absolutePath) : null)
				+ "\n\t   ]";
	}

	@Override
	public void reset() {
		mepVariable.bind(null);
//		absolutePath[relativePathIndexIndex] = -1;
	}


}
