package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.mep.Variable;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndex;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	
	protected StreamCarsExpressionVariable(IStreamCarsExpression parent, Variable mepVariable) {
		this.parent = parent;
		this.mepVariable = mepVariable;
		this.name = this.mepVariable.getIdentifier();
		this.initSourceName(this.name);
		this.initMetadataInfo(this.name);
	}
	
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
	public boolean isSchemaVariable(SDFAttributeList schema) {
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
		MVRelationalTuple<?> current = tuple;
		int depth = absolutePath.length - 1;
		for(int i=0; i<depth; i++) {
			current = (MVRelationalTuple<?>)current.getAttribute(absolutePath[i]);
		}
		bind(current.getAttribute(absolutePath[depth]));
	}
	
	
	
	public void init(SDFAttributeList schema) {
		if(isSchemaVariable(schema)) {
			SchemaHelper helper = new SchemaHelper(schema);
			schemaPath = helper.getSchemaIndexPath(nameWithoutMetadata);
			initRelativeIndex();
			absolutePath = schemaPath.toArray(true);
		}
	}
	
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
	
	protected void initSourceName(String variableName) {
		String[] dotSplit = variableName.split("\\.");
		if(dotSplit.length > 2) {
			throw new IllegalArgumentException("the variable: " + variableName + " has more than one source seperators ('.'");
		} else if(dotSplit.length < 2) {
			sourceName = NO_SOURCE;
		}
		sourceName =  dotSplit[0];
	}
	
	protected void initRelativeIndex() {
		if(schemaPath == null) throw new RuntimeException("the variable " + name + "was not properly initialized: schemaIndexPath is null");
		relativePath = new int[schemaPath.getLength()];
		for(int i=0; i<relativePath.length; i++) {
			SchemaIndex index = schemaPath.getSchemaIndex(i);
			if(index.isList()) {
				relativePath[i] = -1;
			} else {
				relativePath[i] = index.getIndex();
			}
		}
	}
	
	protected String getSchemaSourceName(SDFAttributeList schema) {
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


}
