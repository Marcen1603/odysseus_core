package de.uniol.inf.is.odysseus.nlp.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.nlp.physicaloperator.TrainTransportHandler;


@LogicalOperator(name="TRAINNLP", minInputPorts=0, maxInputPorts=0, doc = "Trains specific NLP-model and outputs the model as serialzed object.", url = "http://example.com/MyOperator.html", category = { LogicalOperatorCategory.ADVANCED })
public class TrainAO extends AbstractAccessAO {
	private static final long serialVersionUID = 6100531335886603325L;
	private SDFAttribute output = new SDFAttribute(null, "output",
            SDFDatatype.OBJECT, null, null, null);
	//private ITrainableModel model;
	//private String modelName;
		
	public TrainAO(){
		super();
		setTransportHandler(TrainTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(output);
		setAttributes(schema);
	}
	

	public TrainAO(TrainAO trainAO){
		super(trainAO);
	}
	
	@Parameter(name=TrainTransportHandler.MODELKEY, type = StringParameter.class, optional = false, doc = "Train model identifier")
	public void setModel(String model) {
		addOption(TrainTransportHandler.MODELKEY, model);
    	/*
    	String[] data = model.split("\\.");
		if(data.length < 2)
			throw new ParameterException(model + " was not found.");
		try {
			AnnotationModel<?> m = ToolkitFactory.get(data[0], data[1]);
			if(m instanceof ITrainableModel){
				this.model = (ITrainableModel) m;
			}else{
				throw new ParameterException("Model " + data[1] + " in Toolkit "+ data[0] + " is not trainable.");
			}
		}catch (NLPModelNotFoundException e) {
			throw new ParameterException("Model "+ data[1]+" in existing Toolkit "+ data[0] + " was not found.");
		} catch (NLPToolkitNotFoundException e) {
			throw new ParameterException("Toolkit "+data[0]+" was not found.");
		}
		*/
	}

	@Parameter(name=TrainTransportHandler.LANGUAGECODEKEY, type = StringParameter.class, optional=false, doc = "Language code of training data set (eg. en)")
	public void setLanguage(String language){
		addOption(TrainTransportHandler.LANGUAGECODEKEY, language);
	}
	
	@Parameter(name=TrainTransportHandler.URIKEY, type = StringParameter.class, optional = false, doc="Path (URI) to training data")
	public void setUri(String uri) {
		addOption(TrainTransportHandler.URIKEY, uri);
	}

	@Parameter(name=TrainTransportHandler.CHARSETKEY, type = StringParameter.class, optional = false, doc="Charset of specified file")
	public void setCharset(String charset) {
		addOption(TrainTransportHandler.CHARSETKEY, charset);
	}
	
	
    @Parameter(name=TrainTransportHandler.OPTIONSKEY, type = OptionParameter.class, isList = true, optional = true)
    public void setOptions(List<Option> options){
    	for(Option op:options){
    		addOption(op.getName(), op.getValue());
    	}
    }


	@Override
	public AbstractLogicalOperator clone() {
		return new TrainAO(this);
	}
	
	
	
	
}
