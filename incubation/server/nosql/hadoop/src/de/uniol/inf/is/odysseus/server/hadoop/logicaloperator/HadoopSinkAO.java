package de.uniol.inf.is.odysseus.server.hadoop.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

@LogicalOperator(name = "HADOOPSINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a Hadoop-Filesystem.", category = {
        LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE })
public class HadoopSinkAO extends AbstractNoSQLSinkAO {

	private static final long serialVersionUID = -533758138172292374L;
	private String path;

    public HadoopSinkAO() {
        super();
    }

    public HadoopSinkAO(HadoopSinkAO old) {
        super(old);
        this.path = old.path;
    }

    @Parameter(name = "PATH", type = StringParameter.class, optional = false, doc = "Path of the file in which all tuples will be appended")
    public void setCollectionName(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public HadoopSinkAO clone() {
        return new HadoopSinkAO(this);
    }
}
