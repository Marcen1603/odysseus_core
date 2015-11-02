/**
 * 
 */
package de.uniol.inf.is.odysseus.gpu.physicaloperator;

import static jcuda.driver.JCudaDriver.cuCtxDestroy;
import static jcuda.driver.JCudaDriver.cuCtxPushCurrent;
import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleUnload;

import java.io.IOException;
import java.util.List;

import org.w3c.dom.views.AbstractView;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.gpu.Prepare;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import jcuda.CudaException;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

/**
 * @author Alexander
 *
 */
public class GpuProjectionPO<T extends IMetaAttribute> extends AbstractPipe<IStreamObject<T>, IStreamObject<T>> {

	
	
	private int[] restrictList;
	
	private CUdeviceptr inputDevicePtr;
	private CUdeviceptr outputDevicePtr;
	private CUdeviceptr restrictDevicePtr;

	private int SIZE;

	private CUcontext context;

	private CUmodule modul;

	private CUfunction function;

	private int gridDimX = 1;

	private int blockDimX = 1;

	public GpuProjectionPO(int [] restrictList) {
		this.restrictList = restrictList;
	}

	public GpuProjectionPO(GpuProjectionPO<T> gpuProject) {
		super();
		//Quelle RelationalProjectPO - Jonas Jacobi
		int length = gpuProject.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(gpuProject.restrictList, 0, restrictList, 0, length);	
	}

	@Override
	public String toString() {		
		return super.toString();
	}
	
	public int[] getRestrictList() {
		return restrictList;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);		
	}

	@Override
	public OutputMode getOutputMode() {		
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {		
		super.process_open();
		
		try {
			String ptxFileName = Prepare.ptxFile("CUDA/Select.cu");		
		
			CUdeviceptr restrictDevicePtr = new CUdeviceptr();
			CUdeviceptr inputDevicePtr = new CUdeviceptr();			
			CUdeviceptr outputDevicePtr = new CUdeviceptr();
			
			//Vor dem input müssen alle Attr bestimmt werden
			Attr();
			this.inputDevicePtr = inputDevicePtr;
			
			this.outputDevicePtr = outputDevicePtr;
			this.restrictDevicePtr = restrictDevicePtr;
					
			Prepare.cuLoad(ptxFileName, "Project");			
			
			//Bei projection sind viele unterschiedliche Datentypen
			//Lösungsmöglichkeit jeden Datentyp init. und allokieren und falls benötigt nutzen
			int [] restrict = getRestrictList();
			
			//Für ein Puffer muss die Size geändert werden
			SIZE = restrict.length * Sizeof.INT;
			
			cuMemAlloc(restrictDevicePtr, SIZE);
			
			cuMemAlloc(outputDevicePtr, SIZE);
			
			cuMemcpyHtoD(restrictDevicePtr, Pointer.to(restrict), SIZE);
		

			context = Prepare.getContext();			
			modul = Prepare.getModul();
			function = Prepare.getFunction();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(CudaException e){
			
		}
	}
	
	@Override
	protected void process_next(IStreamObject<T> inputElement, int port) {
	
		System.out.println("GPU Stopp");
		
		transfer(CUDA_process((Tuple<T>) inputElement));
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void process_close() {
		
		super.process_close();
		
		// CUDA-Nachbereitung

		cuMemFree(inputDevicePtr);
		cuMemFree(outputDevicePtr);
		cuMemFree(restrictDevicePtr);
		
		cuModuleUnload(modul);
		
		if (context != null) {
				cuCtxDestroy(context);
		}
		// Speicher auf der GPU freigeben
				
	}

	private IStreamObject<T> CUDA_process(Tuple<T> inputElement) {
		
		cuCtxPushCurrent(context);
		
		//Input übertragen
		
		Pointer kernelParameter = Pointer.to(
				Pointer.to(restrictDevicePtr),
				Pointer.to(inputDevicePtr),
				Pointer.to(outputDevicePtr)
				);
		
		//Start Kernel auf der GPU
		cuLaunchKernel(function,
		gridDimX, 1, 1,
		blockDimX, 1, 1,
		0, null,
		kernelParameter, null);
		
		//blockiert bis alle Grids abgeschloßen sind
		cuCtxSynchronize();
		
		Object[] newAttr;
		
		IStreamObject<T> outputElement = inputElement;
		
		return outputElement;
	}	
	
	private void Attr(){
		SDFSchema input = getInputSchema(0);
		
		for(int i = 0; i < input.size(); i++){
			
			SDFDatatype attributeDatatype = input.getAttribute(i).getDatatype() ;
		
			if (attributeDatatype.equals(SDFDatatype.INTEGER)){
				
			}else if (attributeDatatype.equals(SDFDatatype.FLOAT)) {

				
				
			} else if (attributeDatatype.equals(SDFDatatype.DOUBLE)) {								
				
								
				
				
			} else if(attributeDatatype.equals(SDFDatatype.CHAR)){
				
				
			} else {
				throw new OpenFailedException("Nicht unterstützter Datentyp " + attributeDatatype);		
			}
		}
	}
}
