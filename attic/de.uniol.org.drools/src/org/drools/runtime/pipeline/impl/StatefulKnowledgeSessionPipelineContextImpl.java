package org.drools.runtime.pipeline.impl;

import org.drools.runtime.CommandExecutor;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.pipeline.ResultHandler;
import org.drools.runtime.pipeline.StatefulKnowledgeSessionPipelineContext;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

public class StatefulKnowledgeSessionPipelineContextImpl extends BasePipelineContext
    implements
    StatefulKnowledgeSessionPipelineContext {
    private StatefulKnowledgeSession ksession;
    private WorkingMemoryEntryPoint  entryPoint;

    public StatefulKnowledgeSessionPipelineContextImpl(StatefulKnowledgeSession ksession,
                                                       WorkingMemoryEntryPoint entryPoint,
                                                       ResultHandler resultHandler,
                                                       ClassLoader classLoader) {
        super( classLoader,
               resultHandler );
        this.ksession = ksession;
        this.entryPoint = entryPoint;
    }

    public StatefulKnowledgeSession getStatefulKnowledgeSession() {
        return this.ksession;
    }

    public WorkingMemoryEntryPoint getEntryPoint() {
        return entryPoint;
    }

    @Override
	public CommandExecutor getCommandExecutor() {
        return this.ksession;
    }
    
    
        

}
