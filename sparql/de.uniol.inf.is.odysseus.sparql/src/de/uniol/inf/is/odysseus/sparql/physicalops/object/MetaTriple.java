//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.object;
//
//import com.hp.hpl.jena.graph.Node;
//import com.hp.hpl.jena.graph.Triple;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.container.IMetaAttribute;
//
//public class MetaTriple <T extends IClone> extends Triple implements IMetaAttribute<T>{
//	
//	T metadata;
//	
//	public MetaTriple(Node s, Node p, Node o){
//		super(s, p, o);
//	}
//	
//	public MetaTriple(MetaTriple<T> old){
//		super(old.getSubject(), old.getPredicate(), old.getObject());
//		this.metadata = (T)old.getMetadata().clone();
//	}
//	
//	public IClone clone(){
//		return new MetaTriple<T>(this);
//	}
//
//	public void setMetadata(T data){
//		this.metadata = data;
//	}
//	
//	public T getMetadata(){
//		return this.metadata;
//	}
//}
