package de.offis.scaiconnector.factory;

import java.util.logging.Logger;

import org.apache.xmlbeans.impl.util.Base64;

import de.offis.scaiconnector.controller.HttpClientController;
import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.xml.schema.scai20.SCAIDocument;

/**
 * Maintains connection to a Scai-XML-Service. Creates (empty) ScaiCommands.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiFactory {
	
	private final Logger log = Logger.getLogger("Server.scai-connector.ScaiFactory");

	private final String scaiUrl;
	private final String scaiUser;
	private final String scaiPass;
	
    private HttpClientController scaiHttpConn;
    
    public final CreateScaiCmds Create = new CreateScaiCmds();
    public final UpdateScaiCmds Update = new UpdateScaiCmds();
    public final RemoveScaiCmds Remove = new RemoveScaiCmds(this);
    public final GetScaiCmds Get = new GetScaiCmds(this);
    public final ListAllScaiCmds ListAll = new ListAllScaiCmds(this);
    public final MiscScaiCmds Misc = new MiscScaiCmds(this);
    
    public ScaiFactory(String scaiUrl, String scaiUser, String scaiPass) {
    	this.scaiUrl = scaiUrl;
    	this.scaiUser = scaiUser;
    	this.scaiPass = scaiPass;
    	
        scaiHttpConn = new HttpClientController(this.scaiUrl);
    }
    
    public HttpClientController getConnection() {
        return this.scaiHttpConn;
    }
    
    public SCAIDocument sendScaiBuilder(BuilderSCAI20 scaiRequest) throws Exception {
    	// TODO eigentlich muesste ich den builder kopieren und dann erst name+pass
    	// hinzufuegen, ansonsten kann es probleme durch call-by-ref geben ...
    	
    	scaiRequest.addIdentification(null, scaiUser,  Base64.encode(scaiPass.getBytes()));
    	
    	SCAIDocument scaiDoc = null;
    	
    	String put = scaiRequest.getDocument().toString();
    	
    	log.info("[ScaiFactory-SendingPutRequest]: " + put);
    	
    	try {
    		scaiDoc = SCAIDocument.Factory.parse(scaiHttpConn.sendPutRequest(put));
    		log.info("[ScaiFactory-ReceivedResponse]: " + scaiDoc.toString());
        } catch (Exception ex) {
        	log.info("Exception: " + ex.getMessage());
            throw ex;
        }
        
        return scaiDoc;
    }
    
    public ScaiCommand createScaiCommand(){
    	return new ScaiCommand(this);
    }
    
    public ScaiCommand createScaiCommand(BuilderSCAI20 cmd){
    	return new ScaiCommand(this, cmd);
    }
}