package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.FileAccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class FileAccessAOBuilder extends AbstractOperatorBuilder{

	private final DirectParameter<String> sourceName = new DirectParameter<String>(
			"SOURCE", REQUIREMENT.MANDATORY);
	
	private final DirectParameter<String> path = new DirectParameter<String>(
			"PATH", REQUIREMENT.MANDATORY);
	
	private final DirectParameter<String> fileType = new DirectParameter<String>(
			"FILETYPE", REQUIREMENT.MANDATORY);	
	
	private final DirectParameter<String> type = new DirectParameter<String>(
			"TYPE", REQUIREMENT.MANDATORY);
	
	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.MANDATORY, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY));	
	
	private final DirectParameter<Long> delay = new DirectParameter<Long>(
			"DELAY", REQUIREMENT.OPTIONAL);
	
	Logger logger = LoggerFactory.getLogger(FileAccessAOBuilder.class);
	
	public FileAccessAOBuilder() {
		super(0,0);
		setParameters(sourceName,path,fileType,type,attributes,delay);
	}
	
	@Override
	protected ILogicalOperator createOperatorInternal() {
		String sourceName = this.sourceName.getValue();
		if (DataDictionary.getInstance().containsViewOrStream(sourceName, getCaller())) {
			return DataDictionary.getInstance().getViewOrStream(sourceName, getCaller());
		}
		
		FileAccessAO ao = createNewFileAccessAO(sourceName);
		
		DataDictionary.getInstance().setView(sourceName,ao, getCaller());
		return ao;
	}
	
	private FileAccessAO createNewFileAccessAO(String sourceName) {
		SDFSource sdfSource = new SDFSource(sourceName, type.getValue());
		SDFEntity sdfEntity = new SDFEntity(sourceName);
		List<SDFAttribute> attributeList = attributes.getValue();
		SDFAttributeList schema = new SDFAttributeList(attributeList);
		sdfEntity.setAttributes(schema);
		
		DataDictionary.getInstance().addSourceType(sourceName, "RelationalStreaming");
		DataDictionary.getInstance().addEntity(sourceName, sdfEntity, getCaller());
		
		
		FileAccessAO ao = new FileAccessAO(sdfSource);
		ao.setPath(path.getValue());
		ao.setFileType(fileType.getValue());
		ao.setDelay(delay.getValue());
		
		
		ao.setOutputSchema(schema);
		return ao;
	}
	
	@Override
	protected boolean internalValidation() {
		String sourceName = this.sourceName.getValue();
		
		if(delay.getValue() == null)
			delay.setInputValue(0l);
			
//		if (DataDictionary.getInstance().containsViewOrStream(sourceName, getCaller())) {
//			if (path.hasValue() || type.hasValue() || fileType.hasValue() || attributes.hasValue()) {
//				addError(new IllegalArgumentException("view " + sourceName
//						+ " already exists"));
//				return false;
//			}
//		}else {
			if (!(path.hasValue() && type.hasValue() && fileType.hasValue() && attributes.hasValue())) {
				addError(new IllegalArgumentException(
						"missing information for the creation of source "
								+ sourceName
								+ ". expecting path, fileType, type and attributes."));
				return false;
			}
//		}
		/*
		File file = new File(path.getValue());
		if(!file.exists()){
			addError(new IllegalArgumentException("File " + path.getValue() + " does not exists."));
			return false;
		}
		*/	
		return true;
	}
}
