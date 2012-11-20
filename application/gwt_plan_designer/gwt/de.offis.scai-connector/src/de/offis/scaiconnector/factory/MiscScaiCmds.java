package de.offis.scaiconnector.factory;

import de.offis.scampi.stack.scai.builder.BuilderSCAI20;
import de.offis.scampi.stack.scai.builder.types.SCAIReference;

/**
 * Wrapper for some SCAI Operations.
 *
 * @author Alexander Funk
 * 
 */
public class MiscScaiCmds {
	private ScaiFactory factory;
	
	public MiscScaiCmds(ScaiFactory factory) {
		this.factory = factory;
	}
	
	public void deployOperatorGroup(String operatorGroup) throws Exception {
		BuilderSCAI20 cmd = new BuilderSCAI20();
		cmd.addDeployOperatorGroup(new SCAIReference(operatorGroup, false), null, "OpId");		
		
		ScaiCommand scai = factory.createScaiCommand(cmd);		
		scai.commit();
	}
	
	public void undeployOperatorGroup(String operatorGroup) throws Exception {
		BuilderSCAI20 cmd = new BuilderSCAI20();
		cmd.addUndeployOperatorGroup(new SCAIReference(operatorGroup, false), "OpId");

		ScaiCommand scai = factory.createScaiCommand(cmd);		
		scai.commit();
	}
}
