/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
