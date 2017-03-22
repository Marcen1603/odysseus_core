/********************************************************************************** 
 * Copyright 2017 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.test.generator.transporthandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.test.generator.datatype.ByteTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.CharTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.DoubleTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.FloatTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.IntegerTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.ListTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.LongTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.ShortTestGenerator;
import de.uniol.inf.is.odysseus.test.generator.datatype.StringTestGenerator;

/**
 * @author Patrick Bruns
 * 
 */
public class TestGeneratorTransportHandler extends AbstractSimplePullTransportHandler<Tuple<? extends IMetaAttribute>>{
	 
    private static final String NAME = "TestGenerator";
    private static final String NUM = "num";
    private static final String OVERLAPPING = "overlapping";
    private static final String WINDOWSIZE = "windowsize";
    private static final String WINDOWSIZE1 = "windowsize1";
    private static final String WINDOWSIZE2 = "windowsize2";
    private static final String ADJACENT = "adjacent";
    private static final String OUTOFORDER = "outoforder";
    private static final String IDENTICALSTART = "identicalstart";
    private static final String IDENTICALEND = "identicalend";
    private static final String INFINITE = "infinite";
    private static final String DELAY = "delay";
    
    
	public static final String COMPLETELY_OVERLAPPING = "completely";
	public static final String NOT_OVERLAPPING = "not";
	public static final String PARTIALLY_OVERLAPPING = "partially";
    
    private int num;
    private int count;
    private boolean adjacent;
    private boolean outoforder;
    
    private long diff;
    
    private long start;
    private long end;
    private long start1;
    private long start2;
    private long end1;
    private long end2;
    private long gap = 1;
    private String overlapping;
    private boolean identicalWindowsize;
    private boolean identicalStart;
    private boolean identicalEnd;
    private long windowsize;
    private long windowsize1;
    private long windowsize2;
    private boolean infinite;
    private long delay;
    
    private boolean lastWindowsize1 = false;
    
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(TestGeneratorTransportHandler.class);
		}
		return _logger;
	}
    
    
    public TestGeneratorTransportHandler(){
    	super();
    }
 
    public TestGeneratorTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
    	super(protocolHandler, options);
    	this.delay = options.getLong(DELAY, 0);
    	this.num = options.getInt(NUM, 0);
    	this.overlapping = options.getString(OVERLAPPING, NOT_OVERLAPPING); 
    	this.windowsize = options.getLong(WINDOWSIZE, 0);
    	this.adjacent = options.getBoolean(ADJACENT, true);
    	this.outoforder = options.getBoolean(OUTOFORDER, false);
    	this.infinite = options.getBoolean(INFINITE, false);
    	this.windowsize1 = options.getLong(WINDOWSIZE, 0);

		this.identicalStart = options.getBoolean(IDENTICALSTART, false);
    	if(this.outoforder){
    		this.end = Long.MAX_VALUE-this.windowsize;
    		this.end1 = Long.MAX_VALUE-this.windowsize;
    		this.end2 = Long.MAX_VALUE-this.windowsize;
    		this.end1 = 1000000;
    		this.end2 = 1000000;
    		this.end = 100000;
    		this.gap=-1;
    		if(this.infinite){
    			this.start2=Long.MAX_VALUE-this.windowsize;
        		this.start2 = 1000000;
    		}
    	}
    	
    	if(overlapping.equals(COMPLETELY_OVERLAPPING)){
    		diff = 0;
    		if(outoforder){
    			this.start = this.end-windowsize;
    		}else{
        		this.end = windowsize;	
    		}
    	}else{
    		diff = windowsize;
    	}
    	if(options.containsKey(WINDOWSIZE)){
    		this.identicalWindowsize = true;
    		if(options.containsKey(WINDOWSIZE1) || options.containsKey(WINDOWSIZE2)){
    			getLogger().warn("windowsize1 and windowsize2 ignored, because windowsize used");
    		}
    		if(options.containsKey(IDENTICALSTART) || options.containsKey(IDENTICALEND)){
    			getLogger().warn("identicalstart and identicalend ignored, because identical windowsize");
    		}
    		
    	}else{
    		this.identicalWindowsize = false;
    		this.identicalEnd = options.getBoolean(IDENTICALEND, false);
    		
    		if(options.getLong(WINDOWSIZE1, 0)>options.getLong(WINDOWSIZE2, 0)){
    			this.windowsize1 = options.getLong(WINDOWSIZE1, 0);
    			this.windowsize2 = options.getLong(WINDOWSIZE2, 0);
    		}else{
    			this.windowsize1 = options.getLong(WINDOWSIZE2, 0);
    			this.windowsize2 = options.getLong(WINDOWSIZE1, 0);
    		}
    		if(this.identicalStart && this.identicalEnd){
    			throw new IllegalArgumentException("With different windowsizes, start and end  can't be identical");
    		}
    		if(options.containsKey(OVERLAPPING)){
    			getLogger().warn("overlapping ignored, because different windowsize used");
    		}
    		
    	}
	}

	@Override
    public String getName() {
        return NAME;
    }
 
    @Override
    public boolean hasNext() {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// interrupting the delay might be correct
				// e.printStackTrace();
			}
		}
    	if(num==-1) return true;
    	if(count<num){
    		return true;	
    	}
        return false;
    }
 
    @Override
    public Tuple<? extends IMetaAttribute> getNext() {
    	count++;
    	if(this.infinite){
    		return this.processInfiniteWindowSize();
    	}
    	if(this.identicalWindowsize){
    		return this.processIdenticalWindowsize();
    	}else{
    		return this.processDifferentWindowsize();
    	}
    }
    
    /**
     * 
     * @return
     */
    private Tuple<? extends IMetaAttribute> processIdenticalWindowsize(){
    	boolean completely_overlapping = this.overlapping.equals(COMPLETELY_OVERLAPPING);
    	if(!completely_overlapping){
	    	if(this.outoforder){
	    		this.start=this.end-diff;
	    	}else{
	        	this.end = this.start+this.diff;
	    	}
    	}
        Tuple<? extends IMetaAttribute> tuple = new Tuple<>(this.getSchema().size(), false);
    	for(SDFAttribute attribute : this.getSchema().getAttributes()){
    		Object value = null;
    		if(attribute.getDatatype().equals(SDFDatatype.START_TIMESTAMP)){
    			if(!this.adjacent && this.outoforder && !completely_overlapping){
    	    		this.start+=this.gap;
    	    	}
    			value = this.start;
    	    	if(!this.adjacent && !this.outoforder && !completely_overlapping){
    	    		value=(long)value+this.gap;
    	    	} 
    		}else if(attribute.getDatatype().equals(SDFDatatype.END_TIMESTAMP)){
    			if(!this.adjacent && !this.outoforder && !completely_overlapping){
    				this.end+=this.gap;
    			}
    			value = this.end;
    			if(!this.adjacent && this.outoforder && !completely_overlapping){
    				value=(long)value+this.gap;
    			}
    		}else{
    			value = this.generateValue(attribute.getDatatype());
    		}
			tuple.setAttribute(this.getSchema().indexOf(attribute), value);
    	}
        if(!completely_overlapping){
	        if(this.outoforder){
	        	if(this.overlapping.equals(PARTIALLY_OVERLAPPING)){
	        		this.end = this.start+diff/2;
	        	}else{
		        	this.end = this.start;	
	        	}
	    	}else{
	        	if(this.overlapping.equals(PARTIALLY_OVERLAPPING)){
	        		this.start = this.end-diff/2;
	        	}else{
		            this.start = this.end;	
	        	}
	    	}
        }
        return tuple;
    }
    
    private Tuple<? extends IMetaAttribute> processDifferentWindowsize(){
    	if(this.lastWindowsize1){
    		this.lastWindowsize1 = false;
    		//Smaller window
    		if(this.identicalStart){
				if(this.outoforder){
					this.start2 = this.end1-this.windowsize1;
					this.end2 = this.end1-this.windowsize1+this.windowsize2;
				}else{
					this.start2 = this.start1-this.windowsize1;
					this.end2 = this.start1+this.windowsize2-this.windowsize1;	
				}
			}else if(this.identicalEnd){
				if(this.outoforder){
					this.start2 = this.end1-this.windowsize2;
					this.end2 = this.end1;
				}else{
					this.start2 = this.start1-this.windowsize1+(this.windowsize1-this.windowsize2);
					this.end2 = this.end1;	
				}
			}else{
				if(this.outoforder){
					this.start2 = this.end1-this.windowsize1+(this.windowsize1-this.windowsize2)/2;
					this.end2 = this.end1-(this.windowsize1-this.windowsize2)/2;
				}else{
					this.start2 = this.start1-this.windowsize1+(this.windowsize1-this.windowsize2)/2;
					this.end2 = this.end1-(this.windowsize1-this.windowsize2)/2;
				}
			}
    		if(!this.adjacent && this.outoforder){
    			this.start2+=this.gap;
    			this.end2+=this.gap;
    		}
    	}else{
    		this.lastWindowsize1 = true;
    		//Bigger Window
    		if(this.outoforder){
    			this.start1 = this.end1-this.windowsize1;
    		}else{
        		this.end1 = this.start1+this.windowsize1;	
    		}
    	}
        Tuple<? extends IMetaAttribute> tuple = new Tuple<>(this.getSchema().size(), false);
    	for(SDFAttribute attribute : this.getSchema().getAttributes()){
    		Object value = null;
    		if(attribute.getDatatype().equals(SDFDatatype.START_TIMESTAMP)){
    			if(!this.lastWindowsize1){
    				value = this.start2;
    			}else{
    				value = this.start1;
    				if(!this.adjacent && !this.outoforder){
    					value=(long)value+this.gap;
    				}
    			}
    		}else if(attribute.getDatatype().equals(SDFDatatype.END_TIMESTAMP)){
    			if(!this.lastWindowsize1){
    				value = this.end2;
    			}else{
    				value = this.end1; 
    				if(!this.adjacent && !this.outoforder){
    					value=(long)value+this.gap;
    				}
    			}
    		}else{
    			value = this.generateValue(attribute.getDatatype());
    		}
			tuple.setAttribute(this.getSchema().indexOf(attribute), value);
    	}
    	if(!this.adjacent){
			this.end1+=this.gap;
			this.start1+=this.gap;
		}
    	if(this.outoforder){
    		this.end1 = this.start1;
    		this.end2 = this.start2;
    	}else{
    		this.start1 = this.end1;
    		this.start2 = this.end2;	
    	}
		
    	return tuple;
    }
    
    private boolean lastElementInfinite = true;
    
    private Tuple<? extends IMetaAttribute> processInfiniteWindowSize(){
		if(this.windowsize>0){

//			if(this.lastElementInfinite){
//				if(this.identicalStart){
//					this.end = this.start+this.windowsize+this.windowsize;
//				}else{
//					this.end = this.start+this.windowsize+this.windowsize+this.windowsize;	
//				}
//				
//				if(this.outoforder){
//					this.start-=this.windowsize;
//				}
//				this.lastElementInfinite = false;
//			}else{
//				if(!this.adjacent){
//					this.end-=this.gap;
//					this.start-=this.gap;
//				}
//				if(this.outoforder){
//					this.end+=this.windowsize;
//					this.start+=this.windowsize+this.windowsize;
//				}
//				this.lastElementInfinite = true;
//			}
//			if(!this.identicalStart || (!this.lastElementInfinite && this.identicalStart)){
//				this.start += this.windowsize;	
//			}
			
			if(this.lastElementInfinite){

				if(this.outoforder){
					this.start1=this.end-this.windowsize;
				}else{
					this.start1+=this.windowsize;
					this.end = this.start1+this.windowsize;
				}
			}else{
				if(this.outoforder){
					if(this.identicalStart){
						this.start2=this.start1;
					}else{
						this.start2=this.start1-this.windowsize;
					}
					this.end = this.start2;
				}else{
					if(this.identicalStart){
						this.start2=this.start1;
					}else{
						this.start2=this.start1+this.windowsize;
					}
					this.start1+=this.windowsize;
				}
				if(!this.adjacent && !this.identicalStart){
					this.start2+=this.gap;
				}
			}
			
		}else{
			this.lastElementInfinite = false;
			//Beide infinite
			if(this.identicalStart){
				this.start = 0;//Weil kein Fenster alle Starten bei 0	
			}else{
				if(this.outoforder){
					this.start2--;
				}else{
					this.start2++;	
				}
			}
			infinite = true;
		}
		

        Tuple<? extends IMetaAttribute> tuple = new Tuple<>(this.getSchema().size(), false);
		for(SDFAttribute attribute : this.getSchema().getAttributes()){
    		Object value = null;
    		if(attribute.getDatatype().equals(SDFDatatype.START_TIMESTAMP)){
    			if(this.lastElementInfinite){
    				value = this.start1;
    			}else{
    				value = this.start2;
    				if(this.outoforder && !this.adjacent && !this.identicalStart){
    					value=(long)value-this.gap;
    				}
    			}
				if(!this.adjacent && !this.outoforder){
					value=(long)value+this.gap;
				}
    		}else if(attribute.getDatatype().equals(SDFDatatype.END_TIMESTAMP)){
    			if(this.lastElementInfinite){
    				value = this.end;
    				if(!this.adjacent && !this.outoforder){
    					value=(long)value+this.gap;
    				}
    			}
    		}else{
    			value = this.generateValue(attribute.getDatatype());
    		}
			tuple.setAttribute(this.getSchema().indexOf(attribute), value);
    	}
		if(!this.adjacent){
			this.end+=this.gap;
			this.start+=this.gap;
		}			
		if(this.outoforder){
			this.start-=this.windowsize+this.windowsize;
		}
		if(this.windowsize>0){
			if(this.lastElementInfinite){
				this.lastElementInfinite = false;
			}else{
				this.lastElementInfinite = true;
			}
		}
		return tuple;
	}

    
    private Object generateValue(SDFDatatype datatype){
    	if(datatype.equals(SDFDatatype.DOUBLE)){
    		return DoubleTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.STRING)){
    		return StringTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.INTEGER)){
    		return IntegerTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.CHAR)){
    		return CharTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.FLOAT)){
    		return FloatTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.SHORT)){
    		return ShortTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.BYTE)){
    		return ByteTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.LONG)){
    		return LongTestGenerator.getInstance().getValue();
    	}else if(datatype.equals(SDFDatatype.LIST)){
    		return ListTestGenerator.getInstance().getValue();
    	}
    	return null;
    }
     
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler other) {
        return false;
    }

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
    	TestGeneratorTransportHandler tHandler = new TestGeneratorTransportHandler(protocolHandler,options);
        return tHandler;
	}
}
