package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import static jcuda.driver.JCudaDriver.cuCtxDestroy;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleUnload;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.gpu.Prepare;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import de.uniol.inf.is.odysseus.gpu.Prepare;

public abstract class GpuJoinPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	
	
	private Object attributeDatatype;
	
	private int SIZE;
	
	private String ptxFileName;
	private CUcontext context;
	private CUmodule modul;
	private CUfunction function;
	private CUdeviceptr rightInputDevicePtr;
	private CUdeviceptr leftInputDevicePtr;
	private CUdeviceptr outputDevicePtr;
	private CUdeviceptr predDevicePtr;
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		super.process_open();
		
		setName("GPU Join");

		System.out.println("Beginn CUDA");

		try {
					
			
//CUDA-Vorbereitung
			
			//CUDA Try- catch-Block
			
			// CUDA .ptx laden
			
			ptxFileName = Prepare.ptxFile("CUDA/Join.cu");
			
			
			// Initialisieren des Treibers und schaffen eines Kontext für das
			// Gerät 0					

//			predikatObj = new Object[]{150,"Hello"};
			
			
			
			//CUDA Predikat vorbereiten und Speicher allekolieren 
			
			CUdeviceptr predDevicePtr = new CUdeviceptr();
			CUdeviceptr rightInputDevicePtr = new CUdeviceptr();	
			CUdeviceptr leftInputDevicePtr = new CUdeviceptr();		
			CUdeviceptr outputDevicePtr = new CUdeviceptr();
			
			
			this.rightInputDevicePtr = rightInputDevicePtr;
			this.leftInputDevicePtr = leftInputDevicePtr;
			this.outputDevicePtr = outputDevicePtr;
			this.predDevicePtr = predDevicePtr;
			
			if (attributeDatatype.equals(SDFDatatype.INTEGER)) {
				
				Prepare.cuLoad(ptxFileName, "Integer");								
				
				SIZE = 1 * Sizeof.INT;
				
				double predikat[] = new double[]{150};
							
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemAlloc(rightInputDevicePtr, SIZE);
				
				cuMemAlloc(leftInputDevicePtr, SIZE);
				
				cuMemAlloc(outputDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
				
			} else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {

				Prepare.cuLoad(ptxFileName, "Float");	
				
				SIZE = 1 * Sizeof.FLOAT;
				
				double predikat[] = new double[]{150};
							
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemAlloc(rightInputDevicePtr, SIZE);
				
				cuMemAlloc(leftInputDevicePtr, SIZE);
				
				cuMemAlloc(outputDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
				
			} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
				
				Prepare.cuLoad(ptxFileName, "Double");			
				
				
				SIZE = 1 * Sizeof.DOUBLE;
				
				double predikat[] = new double[]{150};
							
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemAlloc(rightInputDevicePtr, SIZE);
				
				cuMemAlloc(leftInputDevicePtr, SIZE);
				
				cuMemAlloc(outputDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);	
				
				
			} else if(attributeDatatype.equals(SDFDatatype.CHAR)){
				
				Prepare.cuLoad(ptxFileName, "Char");
				
				//int zeichen = predicate.length;
				
				//SIZE = zeichen * Sizeof.CHAR;
				
				double predikat[] = new double[]{150};
							
				cuMemAlloc(predDevicePtr, SIZE);
				
				cuMemAlloc(rightInputDevicePtr, SIZE);
				
				cuMemAlloc(leftInputDevicePtr, SIZE);
				
				cuMemAlloc(outputDevicePtr, SIZE);
				
				cuMemcpyHtoD(predDevicePtr, Pointer.to(predikat), SIZE);
				
			} else {
				throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);		
			}
			
			context = Prepare.getContext();			
			modul = Prepare.getModul();
			function = Prepare.getFunction();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//allekolierten Speicher freigeben
			
		}
	}
	
	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		
		
		
	}
	
	@Override
	protected void process_close() {
		// TODO Auto-generated method stub
		super.process_close();		

		// CUDA-Nachbereitung
		
		cuMemFree(rightInputDevicePtr);
		cuMemFree(outputDevicePtr);
		cuMemFree(predDevicePtr);
		
		cuModuleUnload(modul);
		
		if (context != null) {
				cuCtxDestroy(context);
		}
			// Speicher auf der GPU freigeben
	}

}
