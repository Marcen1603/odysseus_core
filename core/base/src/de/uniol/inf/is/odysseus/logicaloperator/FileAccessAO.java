package de.uniol.inf.is.odysseus.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FileAccessAO extends AbstractLogicalOperator implements OutputSchemaSettable{
	
	private static final long serialVersionUID = 3955519214402695311L;
	
	protected SDFSource source = null;
	private SDFAttributeList outputSchema;
	
	private String path;
	private String fileType;
	private long delay;
	
	//This variable will be used to generate an ID for every new input tuple.
	private static long ID = 1;
	
	//This variable will be used, if a wildcard is necessary for an id
	private static Long wildcard = new Long(-1);
	
	public FileAccessAO() {
		super();
	}
	
	public FileAccessAO(AbstractLogicalOperator po) {
		super(po);
	}
	
	public FileAccessAO(SDFSource source) {
		this.source = source;
	}
	
	public FileAccessAO(FileAccessAO po){
		super(po);
		this.source = po.source;
		this.path = po.path;
		this.fileType = po.fileType;
		this.delay = po.delay;
		this.outputSchema = po.outputSchema.clone();
	}
	
	public synchronized SDFSource getSource() {
		return source;
	}

	public synchronized void setSource(SDFSource source) {
		this.source = source;
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}
	
	@Override
	public FileAccessAO clone() {
		return new FileAccessAO(this);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	public String getSourceType() {
		return this.source.getSourceType();
	}
	
	private static long genID(){
		return ++ID;
	}
	
	public static List<Long> nextID(){
		ArrayList<Long> idList = new ArrayList<Long>();
		idList.add(new Long(genID()));
		return idList;
	}
	
	public static Long getWildcard(){
		return wildcard;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileAccessAO other = (FileAccessAO) obj;
		
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;

		if (fileType == null) {
			if (other.fileType != null)
				return false;	
		} else if (!fileType.equals(other.fileType))
			return false;
		
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName() + " (" + this.getSource().getURI() + " | "+this.getSourceType()+")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
}
