/*
 * Created on 23.05.2006
 *
 */
package mg.dynaquest.queryexecution.po.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.evaluation.testenv.client.DqtClient;
import mg.dynaquest.evaluation.testenv.shared.misc.Tuple;
import mg.dynaquest.evaluation.testenv.shared.misc.Value;
import mg.dynaquest.evaluation.testenv.shared.remote.RemoteDataSource;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;


/**
 * @author  Marco Grawunder
 */
public class RMIDataAccessPO extends PhysicalAccessPO<SchemaTransformationAccessPO> {
	/**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());
	 
    /**
	 * @uml.property  name="remoteServerAdress"
	 */
    private String remoteServerAdress;
    /**
	 * @uml.property  name="remoteServerName"
	 */
    private String remoteServerName;
    // Muss hier sein, da next() mehrfach drauf zugreift
    /**
	 * @uml.property  name="ds"
	 * @uml.associationEnd  
	 */
    private RemoteDataSource ds;

	/**
	 * @uml.property  name="expectedAttributeOrder" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] expectedAttributeOrder;
        
    public RMIDataAccessPO() {
        super();
    }

    public RMIDataAccessPO(RMIDataAccessPO accessPO) {
        super(accessPO);
        initBaseValues(accessPO.remoteServerAdress, accessPO.remoteServerName);
    }

    public RMIDataAccessPO(String remoteServerAdress, String remoteServerName, AccessPO schemaTransformationAccessPO){
        super(schemaTransformationAccessPO);
        initBaseValues(remoteServerAdress, remoteServerName);
    }
 
    private void initBaseValues(String remoteServerAdress, String remoteServerName) {
       setRemoteServerAdress(remoteServerAdress);
       setRemoteServerName(remoteServerName);
     }

    /*
     * (non-Javadoc)
     * 
     * @see mg.dynaquest.queryexecution.po.algebra.AccessPO#getInternalXMLRepresentation(java.lang.String,
     *      java.lang.String, java.lang.StringBuffer)
     */
    @Override
    protected void getInternalXMLRepresentation(String baseIndent,
            String indent, StringBuffer xmlRetValue) {
    	xmlRetValue.append(indent).append("<remoteServerAdress>").append(remoteServerAdress).append("</remoteServerAdress>\n");
    	xmlRetValue.append(indent).append("<remoteServerName>").append(remoteServerName).append("</remoteServerName>\n");
    	xmlRetValue.append(indent).append("<algebraPO>\n");
    	getAccessPO().getInternalXMLRepresentation(baseIndent,indent+indent,xmlRetValue);
    	xmlRetValue.append(indent).append("</algebraPO>\n");
    }

    /*
     * (non-Javadoc)
     * 
     * @see mg.dynaquest.queryexecution.po.algebra.AccessPO#initInternalBaseValues(org.w3c.dom.NodeList)
     */
    @Override
    protected void initInternalBaseValues(NodeList childNodes) {
        // TODO Auto-generated method stub
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see mg.dynaquest.queryexecution.po.algebra.AccessPO#process_close()
     */
    @Override
    protected boolean process_close() throws POException {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see mg.dynaquest.queryexecution.po.algebra.AccessPO#process_next()
     */
    @Override
    protected synchronized Object process_next() throws POException, TimeoutException {
    	logger.info("Starte process_next()");
        Tuple tuple = null;
        try {
            tuple = getNextTupel();
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // Am Ende der Verarbeitung?
        while (tuple == null){
            // Gibt es noch Eingabedaten?
            try {
                createNewRemoteDataSource();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // Abbrechen der Verarbeitung, wenn es keine Eingabedaten mehr gibt
            if (ds == null){
            	logger.info("Alle Daten gelesen");
                return null;
            }
            
            // Ansonsten Datensatz holen
            // Achtung muss eine while-Schleife sein, falls es mehrere
            // Eingabesätze gibt und zwischendurch Anfragen kein
            // Ergebnis liefern
            try {
                tuple = getNextTupel();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }          
        }
        
        // Jetzt ist sicher etwas in t enthalten
        // --> Umwandeln in Format welches von der Anfrageverarbeitung verstanden wird
        logger.info("Tupel gelesen "+tuple.toString());
        // Achtung die Reihenfolge im Tupel stimmt nicht zwingend mit der erwarteten
        // Reihenfolge überein
        RelationalTuple lAttrList = new RelationalTuple(tuple.getValueString('|',expectedAttributeOrder),'|');
        
        // Und jetzt noch die Daten in das globale Schema übersetzen
        // TODO: process_next in PhysicalAccessPO um die Wandlung der Ausgaben erweitern ...
        // Eventuell die Methode hier in getLocalValues umbenennen und dann in processNext diese 
        // Methode aufrufen.
        //RelationalTuple attrList = getAccessPO().transformToGlobal(lAttrList);
        // TODO: WANDELN!!!!!
        RelationalTuple attrList = lAttrList;
        logger.info("Neues Tupel "+attrList);
        return attrList;
    }

	private Tuple getNextTupel() throws RemoteException {
        logger.debug(this.getPOName());
        this.notifyPOEvent(readInitEvent);
		Tuple t =  ds.getNext();
		this.notifyPOEvent(readDoneEvent);
        logger.debug(this.getPOName()+" durch");		
		return t;
	}

    /*
     * (non-Javadoc)
     * 
     * @see mg.dynaquest.queryexecution.po.algebra.AccessPO#process_open()
     */
    @Override
    protected boolean process_open() throws POException {
        try {  	
            createNewRemoteDataSource();                 
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    private synchronized void createNewRemoteDataSource() throws RemoteException{
    	logger.info("Create New Remote Data Source");    
        // Die Eingabedaten an die Quelle anpassen
        SDFConstantList inValues = getAccessPO().getNextLocalInputValues();
        
        SDFCompareOperatorList opsList = getAccessPO().getCurrentLocalCompareOperatorList();
        
        if (inValues != null){
            Value[] va = new Value[inValues.size()];
            
            for (int i=0;i<inValues.size();i++){
                SDFConstant c = inValues.getConstant(i);
                if (c != null){
                	// In Value das Attribut und den Wert packen
                    if (c.isString()){
                        va[i]= new Value(c.getString(), getAccessPO().getLocalInputAttributes().get(i).getURI(false), opsList.get(i).toString());
                    }else if (c.isDouble()){
                        va[i] = new Value(c.getDouble(),getAccessPO().getLocalInputAttributes().get(i).getURI(false), opsList.get(i).toString());
                    }                
                }else{
                	va[i] = null;
                }
            }
            
            // Und ein Objekt erzeugen
            ArrayList<String> retAttributes = new ArrayList<String>();
            ds = DqtClient.createDateSource(remoteServerAdress, remoteServerName, va, retAttributes);
            // Und dann festlegen, wie die Ergebnisse auf das Zieltupel gemappt werden müssen
            expectedAttributeOrder = new int[retAttributes.size()];
            int i=0;
            for (SDFSchemaElement a: getAccessPO().getLocalOutputAttributes()){
            	int pos = retAttributes.indexOf(a.getURI(false));
            	if (pos == -1){
            		logger.error("Attribute "+a.getURI(false)+" nicht gefunden "+getAccessPO().getSource());
            	}
            	expectedAttributeOrder[i++] = pos;
            	
            }
            
            
        }else{
            ds = null;
        }
        logger.info("Create New Remote Data Source done");
    }
    

    /**
	 * @return  Returns the remoteServerAdress.
	 * @uml.property  name="remoteServerAdress"
	 */
    public synchronized String getRemoteServerAdress() {
        return remoteServerAdress;
    }

    /**
	 * @param remoteServerAdress  The remoteServerAdress to set.
	 * @uml.property  name="remoteServerAdress"
	 */
    public synchronized void setRemoteServerAdress(String remoteServerAdress) {
        this.remoteServerAdress = remoteServerAdress;
    }

    /**
	 * @return  Returns the remoteServerName.
	 * @uml.property  name="remoteServerName"
	 */
    public synchronized String getRemoteServerName() {
        return remoteServerName;
    }

    /**
	 * @param remoteServerName  The remoteServerName to set.
	 * @uml.property  name="remoteServerName"
	 */
    public synchronized void setRemoteServerName(String remoteServerName) {
        this.remoteServerName = remoteServerName;
    }

	public SupportsCloneMe cloneMe() {
		return new RMIDataAccessPO(this);
	}

	@Override
	public String getInternalPOName() {
		return "RMIDataAccessPO";
	}



    
    
}
