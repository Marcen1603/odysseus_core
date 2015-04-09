package de.uniol.inf.is.odysseus.server.nosql.elasticsearch.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.nosql.base.logicaloperator.AbstractNoSQLSinkAO;

@LogicalOperator(name = "ELASTICSEARCHSINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to elasticsearch.", category = {
        LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE })
public class ElasticSearchSinkAO extends AbstractNoSQLSinkAO {

	private static final long serialVersionUID = -3438050552621086690L;
	private String indexName;   //like database
    private String typeName;    //like table

    public ElasticSearchSinkAO(){
        super();
    }

    public ElasticSearchSinkAO(ElasticSearchSinkAO old){
        super(old);
        this.indexName = old.indexName;
        this.typeName = old.typeName;
    }

    @Parameter(name = "INDEXNAME", type = StringParameter.class, optional = false, doc = "Name of elasticsearch index")
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    @Parameter(name = "TYPENAME", type = StringParameter.class, optional = false, doc = "Name of elasticsearch type")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getIndexName() {
        return indexName;
    }

    @Override
    public ElasticSearchSinkAO clone() {
        return new ElasticSearchSinkAO(this);
    }
}
