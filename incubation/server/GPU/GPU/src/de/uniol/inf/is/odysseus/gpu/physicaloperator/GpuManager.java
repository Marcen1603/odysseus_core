package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import static jcuda.driver.JCudaDriver.cuMemFree;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

//JCUDA
import jcuda.*;
import jcuda.driver.*;
import jcuda.driver.CUdeviceptr;

public class GpuManager <T extends IMetaAttribute> {

	private int Count = 0;
	
	private double [][][] TupleList;	

	private int TuplesNum;
	private int AttributesNum;
	private int AttributeLength;
	
	private CUdeviceptr []tuplePtr = new CUdeviceptr[Count];
	private CUdeviceptr []attributePtr = new CUdeviceptr[AttributesNum];
	private CUdeviceptr valuePtr = new CUdeviceptr();

	private SDFSchema inputSchema;
	
	public GpuManager(int tuples, int attributes,SDFSchema inputSchema) {
		super();
		
		this.TuplesNum = tuples;
		this.AttributesNum = attributes;
		this.inputSchema = inputSchema;
		
		TupleList = new double[tuples][attributes][1];
	}
	
	public Tuple<T> getTuple(){
		
		
		
		Tuple<T> newTuple = new Tuple<T>(AttributesNum, false);
		
		SDFDatatype attributeDatatype; 
		
		double [][] attributes = TupleList[TuplesNum - Count];
		
		//Felder Daten zuweisen
		for(int pos = 0; pos < AttributesNum; pos++){			
			
			attributeDatatype = inputSchema.getAttribute(pos).getDatatype();									
			
			if(attributeDatatype.equals(SDFDatatype.STRING)){	
				
				String string = "";
				
				for(double letter :  attributes[pos]){					
					string += (char) letter;					
				}
				
				newTuple.addAttributeValue(pos, string);
			}else if(attributeDatatype.equals(SDFDatatype.LONG) ){
				newTuple.addAttributeValue(pos, (long) attributes[pos][0]); 
			}else if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
				newTuple.addAttributeValue(pos, (int) attributes[pos][0]); 
			}else if(attributeDatatype.equals(SDFDatatype.START_TIMESTAMP) | attributeDatatype.equals(SDFDatatype.END_TIMESTAMP)){
				newTuple.addAttributeValue(pos,(long) attributes[pos][0]); 
			}else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {	
				newTuple.addAttributeValue(pos, attributes[pos][0]); 
			}else if(attributeDatatype.equals(SDFDatatype.FLOAT)){
				newTuple.addAttributeValue(pos, (float) attributes[pos][0]); 
			}
		}
		Count--;
		return (Tuple<T>) newTuple;
		
	}
	
	

	public CUdeviceptr getInputPtr(){		
		
		//Pointer auf Attribute	
		CUdeviceptr inputPtr = new CUdeviceptr();
		CUdeviceptr []tuplePtr = new CUdeviceptr[Count];
		CUdeviceptr [] attributePtr = new CUdeviceptr[AttributesNum];
		CUdeviceptr valuePtr = new CUdeviceptr();
		
		for(int tnr = 0; tnr < Count; tnr++){
			for(int pos = 0; pos < AttributesNum; pos++){
				for(int letter = 0; letter < AttributeLength; letter++){					
					JCudaDriver.cuMemAlloc(valuePtr, AttributeLength * Sizeof.DOUBLE);
					JCudaDriver.cuMemcpyHtoD(valuePtr, Pointer.to(TupleList[tnr][pos]), AttributeLength * Sizeof.DOUBLE);
					
					this.valuePtr = valuePtr;
				}
				attributePtr[pos] = new CUdeviceptr();
				
				JCudaDriver.cuMemAlloc(attributePtr[pos], AttributesNum * Sizeof.POINTER);
				JCudaDriver.cuMemcpyHtoD(attributePtr[pos], Pointer.to(valuePtr), AttributesNum * Sizeof.POINTER);
				
//				this.attributePtr[pos] = new CUdeviceptr();
//				this.attributePtr[pos] = attributePtr[pos];
			}
			tuplePtr[tnr] = new CUdeviceptr();
			JCudaDriver.cuMemAlloc(tuplePtr[tnr], Count * Sizeof.POINTER);
			JCudaDriver.cuMemcpyHtoD(tuplePtr[tnr], Pointer.to(attributePtr), Count * Sizeof.POINTER);
			
//			this.tuplePtr[tnr] = new CUdeviceptr();
//			this.tuplePtr[tnr] = tuplePtr[tnr];
		}
		
		JCudaDriver.cuMemAlloc(inputPtr, Count * Sizeof.POINTER);
		JCudaDriver.cuMemcpyHtoD(inputPtr, Pointer.to(tuplePtr), Count * Sizeof.POINTER);	
		
		//inputPtr zurückgeben
		
		TupleList = new double[TuplesNum][AttributesNum][AttributeLength];
		return inputPtr;		
	}
	


	public void setOutputPtr(CUdeviceptr outputPtr) {
		
		TupleList = new double[TuplesNum][AttributesNum][AttributeLength];
	
		double []out = new double[1];
		JCudaDriver.cuMemcpyDtoH(Pointer.to(out), outputPtr, Sizeof.DOUBLE);	
		
		System.out.println(out);
		
//		JCudaDriver.cuMemcpyDtoH(Pointer.to(tuplePtr),outputPtr, Count * Sizeof.POINTER);
//		for(int tnr = 0; tnr < Count; tnr++){
//			JCudaDriver.cuMemcpyDtoH(Pointer.to(attributePtr),tuplePtr[tnr], AttributesNum * Sizeof.POINTER);
//			for(int pos = 0; pos < AttributesNum; pos++){
//				JCudaDriver.cuMemcpyDtoH(Pointer.to(valuePtr), attributePtr[pos], AttributeLength * Sizeof.POINTER);
//				for(int letter = 0; letter < AttributeLength; letter++){
//					JCudaDriver.cuMemcpyDtoH(Pointer.to(TupleList[tnr][pos]), valuePtr[letter], 1 * Sizeof.DOUBLE);
//			
//				}
//			}
//		}
	}

	/**
	 * 
	 * 
	 */
	public void AddTuple(Tuple<T> tuple){
		
		double [][] attribute = GpuTuple(tuple);		
		
		for(int i= 0; i < AttributesNum; i++){
			if(attribute[i].length > AttributeLength){
				
				double [][][] puffer = new double[TuplesNum][AttributesNum][AttributeLength];
				
				System.arraycopy(TupleList, 0, puffer, 0, TuplesNum);
										
				TupleList = new double[TuplesNum][AttributesNum][attribute[0].length];
				
				int j = 0;
				for(double[][] iterator : TupleList){
					System.arraycopy(puffer[j], 0, iterator, 0, AttributesNum);
					j++;
				}
				
				AttributeLength = attribute[i].length;
							
			}
		}
		
		TupleList[Count] = attribute;
		
		Count++;
	}
	
	public int getCount() {
		return Count;
	}

	/**
	 * 
	 * @param SDFSchema inputSchema
	 * @param Tuple tuple
	 * @return
	 */
	public double[][] GpuTuple(Tuple<T> tuple){
		
		SDFDatatype attributeDatatype; 
		
		double[][]attributes = new double[inputSchema.getAttributes().size()][1];
		
		//Felder Daten zuweisen
		for(int pos = 0; pos < tuple.size(); pos++){			
			
			attributeDatatype = inputSchema.getAttribute(pos).getDatatype();									
			
			if(attributeDatatype.equals(SDFDatatype.STRING)){	
				
				String string = tuple.getAttribute(pos);
				
				
				
				for(double []attribute : attributes){
					
					if(attribute.length < string.length()){
						
						double [][] puffer = new double[attributes.length][attributes[0].length];
						
						System.arraycopy(attributes, 0, puffer, 0, attributes.length);
												
						attributes = new double[tuple.size()][string.length()];
						int i = 0;
						for(double[] iterator : puffer){							
							int j = 0;
							for(double value : iterator){
								attributes[i][j] = value;
								j++;
							}
							i++;
						}
						
					}
				}
								
				int i = 0;						
				for(char letter : string.toCharArray()){
					attributes[pos][i] = letter;
					i++;
				}
				
				
			
			}else if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
				attributes[pos][0] = (int) tuple.getAttribute(pos);
				
			}else if(attributeDatatype.equals(SDFDatatype.LONG) | attributeDatatype.equals(SDFDatatype.START_TIMESTAMP) |attributeDatatype.equals(SDFDatatype.END_TIMESTAMP)){
				
				Long l = new Long((long)tuple.getAttribute(pos));
				
				attributes[pos][0] = l.doubleValue();
				
			}else if (attributeDatatype.equals(SDFDatatype.DOUBLE) | attributeDatatype.equals(SDFDatatype.FLOAT)) {	
					
				attributes[pos][0] = tuple.getAttribute(pos);
			} 
		}
		
		return attributes;
		
	}

	public int gettuple() {
		
		return Count;
	}

	public int getpos() {
		// TODO Auto-generated method stub
		return AttributesNum;
	}

	public void destroy() {
		
		for(int tnr = 0; tnr < TuplesNum; tnr++){
			cuMemFree(tuplePtr[tnr]);
			for(int pos = 0;pos < AttributesNum ;pos++){
				cuMemFree(attributePtr[pos]);
				for(int letter = 0;letter< AttributeLength; letter++){
					cuMemFree(valuePtr);
				}
			}
		}
	}
}
