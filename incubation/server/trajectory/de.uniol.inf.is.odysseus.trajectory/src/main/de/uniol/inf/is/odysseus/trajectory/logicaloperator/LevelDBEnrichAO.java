package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import java.io.File;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractEnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LEVELDBENRICH", doc="Enrich stream objects with information from a LevelDB.", category={LogicalOperatorCategory.ENRICH, LogicalOperatorCategory.DATABASE})
public class LevelDBEnrichAO extends AbstractEnrichAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3545605159481674813L;
	
	private File dbPath;
	
	private SDFAttribute key;
	
	public LevelDBEnrichAO() {}
	
	
	public LevelDBEnrichAO(LevelDBEnrichAO levelDBEnrichAO) {
		super(levelDBEnrichAO);
		
		this.dbPath = levelDBEnrichAO.dbPath;
		this.key = levelDBEnrichAO.key;
	}
	
	@Parameter(name = "DBPATH", type = FileParameter.class)
	public void setDBPath(final File dbPath) {
		this.dbPath = dbPath;
	}

	@Parameter(name = "KEY", type = ResolvedSDFAttributeParameter.class)
	public void setKey(final SDFAttribute key) {
		this.key = key;
	}
	
	
	public File getDBPath() {
		return this.dbPath;
	}
	
	public SDFAttribute getKey() {
		return this.key;
	}
	

	@Override
	public AbstractLogicalOperator clone() {
		return new LevelDBEnrichAO(this);
	}

}
