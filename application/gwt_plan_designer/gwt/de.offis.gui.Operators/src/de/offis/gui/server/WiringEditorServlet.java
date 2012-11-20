package de.offis.gui.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.xmlbeans.impl.util.Base64;

import de.offis.client.DataStreamType;
import de.offis.client.Operator;
import de.offis.client.OperatorType;
import de.offis.client.ScaiException;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorType;
import de.offis.gui.client.module.AbstractModuleModel;
import de.offis.gui.client.rpc.WiringEditorService;
import de.offis.gui.client.util.BackgroundImageUtil.BackgroundImages;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorMetaTypes;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;
import de.offis.gui.shared.ScaiLoadedOperatorGroup;
import de.offis.gui.shared.ScaiOperatorsData;
import de.offis.scaiconnector.factory.ScaiCommand;
import de.offis.scaiconnector.factory.ScaiFactory;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;
import de.offis.scampi.stack.scai.builder.types.SCAISensorReference;
import de.offis.server.ScaiServlet;
import de.offis.xml.schema.scai20.SCAIDocument;
import de.uniol.inf.is.odysseus.planmanagement.executor.gwtclient.GwtClient;
import de.uniol.inf.is.odysseus.webservice.client.LogicalQueryInfo;
import de.uniol.inf.is.odysseus.webservice.client.OperatorBuilderInformation;
import de.uniol.inf.is.odysseus.webservice.client.ParameterInfo;

/**
 * Server-side implementation of RPC-Methods.
 *
 * @author Alexander Funk
 * 
 */
@SuppressWarnings("serial")
public class WiringEditorServlet extends ScaiServlet implements WiringEditorService {

	private Logger log = Logger.getLogger("Server.WiringEditorServlet");
	
    private ScaiFactory scaiFactory;
    
    GwtClient client;
    
    Map<Integer, GWTPlan> plans;
    
    private static int operatorID = 0;
    
    
    /**
     * this switch changes everything!
     */
    private static boolean useOdysseus = true;
    
    private static final String FALLBACK_WSDL = "http://localhost:9669/odysseus?wsdl";
    private static final String FALLBACK_SENS = "http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/";
    private static final String FALLBACK_SERV = "WebserviceServerService";
    private static final String FALLBACK_USERNAME = "System";
    private static final String FALLBACK_PASSWORD = "manager";
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	scaiFactory = scai;
    	client = new GwtClient();
    	String connect = "";
    	if(odysseus_wsdllocation.isEmpty() || odysseus_service.isEmpty() || odysseus_servicenamespace.isEmpty()) {
    		connect = FALLBACK_WSDL + ";" + FALLBACK_SENS + ";" + FALLBACK_SERV;
    	} else {
    		connect = odysseus_wsdllocation + ";" + odysseus_servicenamespace + ";" + odysseus_service;
    	}
    	if(!client.connect(connect)) {
    		System.out.println("[GWT] Connection failed.");
    		System.out.println("[GWT] " + connect);
    	}
    	client.login(FALLBACK_USERNAME, FALLBACK_PASSWORD.getBytes());
    	
    	setupStandardSources();
    	plans = new HashMap<Integer, GWTPlan>();
    }
    
    private void setupStandardSources() {
    	String parserID = "OdysseusScript";
    	String query = "#PARSER CQL\n" + 
    			"#TRANSCFG Standard\n" + 
    			"#QUERY\n" +
    			"ATTACH STREAM nexmark:person2 (timestamp STARTTIMESTAMP,id INTEGER,name STRING,email STRING,creditcard STRING,city STRING,state STRING) CHANNEL localhost : 65440 marco test\n" +
    			"ATTACH STREAM nexmark:bid2 (timestamp STARTTIMESTAMP,	auction INTEGER, bidder INTEGER, datetime LONG,	price DOUBLE) CHANNEL localhost : 65442 marco test\n" +
    			"ATTACH STREAM nexmark:auction2 (timestamp STARTTIMESTAMP,	id INTEGER,	itemname STRING,	description STRING,	initialbid INTEGER,	reserve INTEGER,	expires LONG,	seller INTEGER ,category INTEGER) CHANNEL localhost : 65441\n" + 
    			"ATTACH STREAM nexmark:category2 (id INTEGER, name STRING, description STRING, parentid INTEGER) CHANNEL localhost : 65443\n" +

    			"GRANT READ ON nexmark:person2 TO Public;\n" + 
    			"GRANT READ ON nexmark:bid2 TO Public;\n" + 
    			"GRANT READ ON nexmark:auction2 TO Public;\n" +
    			"GRANT READ ON nexmark:category2 TO Public;";
    	client.addQuery(query, parserID, client.getUser(), "Standard");
    }
    
    public void deployOperatorGroup(	boolean simulate, 
    									String operatorGroup,
    									List<InputModuleModel> sensors,
    									List<OutputModuleModel> outputs,
    									List<OperatorModuleModel> operators,
    									List<ScaiLinkModel> links) throws Exception{

    	if(useOdysseus) {
        	// Optional addQuery with GwtClient
        	String parserID = "PQL";
        	String queryBuildConfigurationName = "Standard";
        	GWTPlan plan = new GWTPlan(operatorGroup, sensors, outputs, operators, links);
        	plans.put(Integer.valueOf(operatorGroup), plan);
        	client.addQuery(PQLTranslator.translateToPQL(plan), parserID, client.getUser(), queryBuildConfigurationName);
        	return;
    	}
    	
    	
        BuilderSCAI20 builder = new BuilderSCAI20();

        // first create corrosponding operatorgroup
        scaiFactory.Create.OperatorGroup(builder, operatorGroup);
        SCAIReference group = new SCAIReference(null, operatorGroup);

        // then create all inputs
        for (InputModuleModel m : sensors) {
            String name = m.getId();
            String operationId = "deployOperatorGroup-[Input]-" + name;
            SCAISensorReference sensorRef = new SCAISensorReference(m.getSensor(), m.getDomain());
            HashMap<String, String> properties = m.getProperties();
            
          List<String> nullKeys = new ArrayList<String>();
          
          // do not allow null or empty values
          for(String key : properties.keySet()){
          	if(properties.get(key) == null || properties.get(key).equals("")){
          		nullKeys.add(key);
          	}
          }
          
          for(String key : nullKeys){
      		properties.remove(key);
          }

            builder.addCreateOperatorInputOperator(group, name, properties, sensorRef, operationId);
        }

        // then create all outputs
        for (OutputModuleModel m : outputs) {
            // first create virtual sensor as output
            String sensorName = m.getVsSensorName();
            SCAIReference sensorDomain = new SCAIReference(null, m.getVsSensorDomain());
            SCAIReference sensorType = new SCAIReference(null, m.getVsSensorType());
            String sensorOperationId = "deployOperatorGroup-[Output]-[VirtualSensor]-"+sensorName;
            builder.addCreateSensor(sensorName, sensorDomain, sensorType, true, sensorOperationId);


            String name = m.getId();
            String operationId = "deployOperatorGroup-[Output]-" + name;
            HashMap<String, String> properties = m.getProperties();

            SCAISensorReference sensorRef = new SCAISensorReference(sensorName, m.getVsSensorDomain());
            
          List<String> nullKeys = new ArrayList<String>();
          
          // do not allow null or empty values
          for(String key : properties.keySet()){
          	if(properties.get(key) == null || properties.get(key).equals("")){
          		nullKeys.add(key);
          	}
          }
          
          for(String key : nullKeys){
      		properties.remove(key);
          }
            
            builder.addCreateOperatorOutputOperator(group, name, properties, sensorRef, operationId);
        }

        // then create all operators
        for (OperatorModuleModel m : operators) {
            String name = m.getId();
            String operationId = "deployOperatorGroup-[Service]-" + name;
            String operatorType = m.getOperatorType();
            HashMap<String, String> properties = m.getProperties();
            
            List<String> nullKeys = new ArrayList<String>();
            
            // do not allow null or empty values
            for(String key : properties.keySet()){
            	if(properties.get(key) == null || properties.get(key).equals("")){
            		nullKeys.add(key);
            	}
            }
            
            for(String key : nullKeys){
        		properties.remove(key);
            }
            
            builder.addCreateOperatorServiceOperator(group, name, properties, new SCAIReference(null, operatorType), operationId);
        }


        // and finally create all links
        for (ScaiLinkModel w : links) {

            SCAIReference source = new SCAIReference(null, w.getSource());

            SCAIReference destination = new SCAIReference(null, w.getDestination());

            String operationId = "saveLink-[" + w.getSource() + " --- " + w.getDestination() + "]";

            switch (w.getType()) {
                case ScaiLinkModel.INPUT_TO_OUTPUT:
                    builder.addLinkOperatorsInputToOutput(group, source, destination, operationId);
                    break;
                case ScaiLinkModel.INPUT_TO_SERVICE:
                    builder.addLinkOperatorsInputToService(group, source, destination, operationId);
                    break;
                case ScaiLinkModel.SERVICE_TO_OUTPUT:
                    builder.addLinkOperatorsServiceToOutput(group, source, destination, operationId);
                    break;
                case ScaiLinkModel.SERVICE_TO_SERVICE:
                    builder.addLinkOperatorsServiceToService(group, source, destination, operationId);
                    break;
            }
        }

        builder.addIdentification(null, "Superuser",  Base64.encode("QWERT1q!".getBytes()));
        
        log.info("SAVED: " + sensors.size() + " Sensors, " + operators.size() + " Ops, " + outputs.size() + " Outputs and " + links.size() + " Links." );

        if(simulate){
        	SCAIDocument xbean = (SCAIDocument) builder.getDocument();        	
            throw new ScaiException(xbean.toString());
        } else {
        	ScaiCommand cmd = scaiFactory.createScaiCommand(builder);
        	cmd.commit();        	
        }

    }
	
	@Override
	public void removeOperatorGroup(String name) throws Exception {
		if(useOdysseus) {
			client.removeQuery(Integer.valueOf(name), client.getUser());
			plans.remove(Integer.valueOf(name));
			return;
		}
		
		ScaiCommand cmd = scai.createScaiCommand();		
		
		for(Operator o : listAllOperators(name)){			
			if(o.getOperatorType() == Operator.OUTPUT){
				cmd.Builder.addRemoveSensor(o.getSensorName(), o.getSensorDomain(), "Remove Sensor: " + o.getSensorName() + o.getSensorDomain());
			}
		}
		
		super.removeOperatorGroup(name);

		try {			
			cmd.commit();
			log.info(cmd.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ScaiOperatorsData loadOperatorsData() {
		// falls odysseus genutzt wird
		if(useOdysseus) {
			return loadOperatorsDataOdysseus();
		}
		ArrayList<InputModuleModel> inputs = new ArrayList<InputModuleModel>();
		ArrayList<OutputModuleModel> outputs = new ArrayList<OutputModuleModel>();
		ArrayList<OperatorModuleModel> operators = new ArrayList<OperatorModuleModel>();
        
		final String LIST_OP_TYPES_OP_ID = "listAllOperationTypes";
		final String LIST_SENSORS_OP_ID = "listAllSensors";
		final String LIST_SENSOR_TYPES_OP_ID = "listAllSensorTypes";
		final String LIST_DST_TYPES_OP_ID = "listAllDataStreamTypes";
		final String LIST_DE_TYPES_OP_ID = "listAllDataElements";
		
		ScaiCommand cmd = scaiFactory.createScaiCommand();
		cmd.Builder.addListAllOperatorTypes(LIST_OP_TYPES_OP_ID);
		cmd.Builder.addListAllDataStreamTypes(LIST_DST_TYPES_OP_ID);
		cmd.Builder.addListAllSensors(LIST_SENSORS_OP_ID);
		cmd.Builder.addListAllSensorTypes(LIST_SENSOR_TYPES_OP_ID);
		cmd.Builder.addListAllDataElements(LIST_DE_TYPES_OP_ID);
		
		List<OperatorType> operatorTypes = new ArrayList<OperatorType>();
		List<SensorType> sensorTypes = new ArrayList<SensorType>();
		List<Sensor> sensors = new ArrayList<Sensor>();
		List<DataStreamType> dataStreamTypes = new ArrayList<DataStreamType>();
		
		try {
			cmd.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		operatorTypes = cmd.Util.extractOperatorType(cmd.getReplyResponse(LIST_OP_TYPES_OP_ID));
		sensorTypes = cmd.Util.extractSensorType(cmd.getReplyResponse(LIST_SENSOR_TYPES_OP_ID));
		sensors = cmd.Util.extractSensor(cmd.getReplyResponse(LIST_SENSORS_OP_ID));
		dataStreamTypes = cmd.Util.extractDataStreamType(cmd.getReplyResponse(LIST_DST_TYPES_OP_ID), cmd.getReplyResponse(LIST_DE_TYPES_OP_ID));
				
        for (OperatorType e : operatorTypes) {
          String metaType = e.getMetaType().toUpperCase();
          Map<String, String> properties = e.getProps();
          
          // wenn metatype nicht existiert ignore ... 
          OperatorMetaTypes type = null;
          try {
        	  type = OperatorMetaTypes.valueOf(metaType);
        	  if(type == null)
        		  throw new Exception();
          } catch (Exception ex){
        	  continue;
          }
          
          // keine inputs oder outputs als operator anzeigen
          if(metaType.equalsIgnoreCase(OperatorMetaTypes.INPUT.toString())){
        	  continue;
          }
          
          // outputs anzeigen
          if(type.equals(OperatorMetaTypes.OUTPUT)){
          	outputs.add(new OutputModuleModel(e.getName(), properties));
          	continue;
          }
                    
//          Logger.getLogger("Server").log(Level.INFO, properties.toString());
          operators.add(new OperatorModuleModel(e.getName(), OperatorMetaTypes.valueOf(metaType), e.getName(), properties));
        }
		
				
        Map<String, Sensor> sensorsMap = new HashMap<String, Sensor>();
        Map<String, SensorType> sensorTypesMap = new HashMap<String, SensorType>();
        Map<String, List<String>> dataElementsString = new HashMap<String, List<String>>();
        
        for (Sensor sensor : sensors) {
            String name = sensor.getName();
            sensorsMap.put(name, sensor);
        }
        
        for(SensorType sensorType : sensorTypes){
            String name = sensorType.getName();
            sensorTypesMap.put(name, sensorType);
        }

        for(DataStreamType dst : dataStreamTypes){
            String name = dst.getName();            
            dataElementsString.put(name, dst.getDataElementNames());
        }
        
        for (Sensor s : sensorsMap.values()) {
            String name = s.getName();
            String domain = s.getSensorDomainName();
            String sensorType = s.getSensorTypeName();            
            List<String> dataElems = dataElementsString.get(sensorTypesMap.get(sensorType).getDataStreamTypeName());

            inputs.add(new InputModuleModel(name, name, domain, sensorType, dataElems.toArray(new String[0])));
        }        
        
//        outputs.add(new OutputModuleModel("Twitter", null));
//        outputs.add(new OutputModuleModel("HTTP", null));
        HashMap<String, String> props = new HashMap<String, String>();
        props.put("target", "http://srvverkehr01.offis.uni-oldenburg.de:10080/scai");
        outputs.add(new OutputModuleModel("Visualizer", props));

		return new ScaiOperatorsData(operators, inputs, outputs);
	}

	private ScaiOperatorsData loadOperatorsDataOdysseus() {
		ArrayList<InputModuleModel> inputs = new ArrayList<InputModuleModel>();
		ArrayList<OutputModuleModel> outputs = new ArrayList<OutputModuleModel>();
		ArrayList<OperatorModuleModel> operators = new ArrayList<OperatorModuleModel>();
		
		// daten über alle operatoren die es gibt und so weiter
		List<OperatorBuilderInformation> ops = client.getOperatorList();
		
		for(OperatorBuilderInformation op : ops) {
			AbstractModuleModel model = convertToModel(op);
			if(model instanceof InputModuleModel) {
				inputs.add((InputModuleModel) model);
			} else if(model instanceof OutputModuleModel) {
				outputs.add((OutputModuleModel) model);
			} else if(model instanceof OperatorModuleModel) {
				operators.add((OperatorModuleModel) model);
			}
		}
		
		return new ScaiOperatorsData(operators, inputs, outputs);
	}
	
	private AbstractModuleModel convertToModel(OperatorBuilderInformation op) {
		// TODO: Wann muss ein Operator zu einem bestimmten operator?
		// InputModuleModel -> Datenquelle
		// OutputModuleMode -> Datensenke
		// OperatorModuleModel -> Feldwaldundwiesenoperator
		Map<String, String> properties = new HashMap<String, String>();
		for(ParameterInfo parameter : op.getParameters()) {
			properties.put(parameter.getName(), parameter.getValue());
		}
		OperatorModuleModel model = new OperatorModuleModel("" + operatorID, operatorID + op.getName(), OperatorMetaTypes.valueOf(op.getName().toUpperCase()), op.getName().toUpperCase(), properties);
		operatorID++;
		return model;
	}

	@Override
	public ScaiLoadedOperatorGroup loadOperatorGroup(String name) {
		if(useOdysseus) {
			return loadOperatorGroupOdysseus(name);
		}
		ArrayList<AbstractModuleModel> models = new ArrayList<AbstractModuleModel>();
		
		for(Operator o : listAllOperators(name)){
			switch(o.getOperatorType()){
			case Operator.INPUT:
				models.add(new InputModuleModel(o.getName(), o.getName(), o.getSensorName(), o.getSensorDomain(), "SENSORTYPE", new String[]{"DATALEMS"}));
				break;
			case Operator.SERVICE:
				models.add(new OperatorModuleModel(o.getName(), o.getServiceType(), OperatorMetaTypes.COMPLEX, o.getServiceType(), o.getProperties()));
				break;
			case Operator.OUTPUT:
				OutputModuleModel m = new OutputModuleModel(o.getName(), o.getName(), o.getSensorName(), o.getSensorDomain(), o.getProperties());
				models.add(m);
				break;
			}
		}
		
		ArrayList<ScaiLink> links = new ArrayList<ScaiLink>(listAllLinks(name));
		
		return new ScaiLoadedOperatorGroup(models, links);
	}

	private ScaiLoadedOperatorGroup loadOperatorGroupOdysseus(String name) {
		// FIXME: Methode muss noch implementiert werden. Das was unten steht ist ok, aber listAllLinks muss anders gemacht werden.
//		ArrayList<AbstractModuleModel> models = new ArrayList<AbstractModuleModel>();
//		ArrayList<ScaiLink> links = new ArrayList<ScaiLink>(listAllLinks(name));
//		int queryId = Integer.valueOf(name);
//		
//		LogicalQueryInfo queryInfo = client.getLogicalQuery(queryId, client.getUser());
//		// TODO: aus LogicalQueryInfo -> GWTPlan
//		GWTPlan plan = plans.get(queryId);
//		// TODO: eventuell alle queries, die nicht nur von diesem client geladen wurden
//		// hole alle queries
//		models.addAll(plan.getSensors());
//		models.addAll(plan.getOutputs());
//		models.addAll(plan.getOperators());
//		
//		for(ScaiLinkModel model : plan.getLinks()) {
//			links.add(new ScaiLink(model.getSource(), model.getDestination()));
//		}
//		
//		return new ScaiLoadedOperatorGroup(models, links);
		return null;
	}

	@Override
	public BackgroundImages getBackgroundImages() {
		File file = new File(getServletContext().getRealPath("/") + "/images/inputs/");
        ArrayList<String> sensorTypeImages = new ArrayList<String>();        

        for (File f : file.listFiles()) {
        	if(!(f.getName().endsWith("png") || f.getName().endsWith("svg"))){
        		continue;
        	}
        	
            // name 
            int index = f.getName().lastIndexOf('.');
            String name = f.getName().substring(0, index);

            sensorTypeImages.add(name);
        }

        File file2 = new File(getServletContext().getRealPath("/") + "/images/operators/");
        ArrayList<String> metaTypeImages = new ArrayList<String>();        

        for (File f : file2.listFiles()) {
        	if(!(f.getName().endsWith("png") || f.getName().endsWith("svg"))){
        		continue;
        	}
        	
            // name 
            int index = f.getName().lastIndexOf('.');
            String name = f.getName().substring(0, index);

            metaTypeImages.add(name);
        }
        
		File file3 = new File(getServletContext().getRealPath("/") + "/images/outputs/");
        ArrayList<String> outputImages = new ArrayList<String>();        

        for (File f : file3.listFiles()) {
        	if(!(f.getName().endsWith("png") || f.getName().endsWith("svg"))){
        		continue;
        	}
        	
            // name 
            int index = f.getName().lastIndexOf('.');
            String name = f.getName().substring(0, index);

            outputImages.add(name);
        }
		
		return new BackgroundImages(metaTypeImages, outputImages, sensorTypeImages);
	}
	
	public static boolean isOdysseus() {
		return useOdysseus;
	}
	
	public static void setOdysseus(boolean ody) {
		useOdysseus = ody;
	}
}
