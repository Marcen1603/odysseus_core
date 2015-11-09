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
	
	private CUdeviceptr []inputDevicePtr;
	
	private CUdeviceptr restrictDevicePtr;

	private int SIZE;
	
	private int tupleZahl = 3;

	private CUcontext context;

	private CUmodule modul;

	private CUfunction function;

	private GpuManager<T> gpuManager;

	private String ptxFileName;

	public GpuProjectionPO(int [] restrictList) {
		this.restrictList = restrictList;			
	}

	public GpuProjectionPO(GpuProjectionPO<T> gpuProject) {
		super();
		//Quelle RelationalProjectPO - Jonas Jacobi
		int length = gpuProject.restrictList.length;
		restrictList = new int[length];
		System.arraycopy(gpuProject.restrictList, 0, restrictList, 0, length);	
		
		//open auslagern
		try {
			ptxFileName = Prepare.ptxFile("CUDA/Project.cu");				
		
			//Für ein Puffer muss die Size geändert werden
			SIZE = getInputSchema(0).getAttributes().size() * Sizeof.DOUBLE;
			
			GpuManager<T> gpuManager = new GpuManager<>(tupleZahl, getInputSchema(0).getAttributes().size() -1, getInputSchema(0));
			this.gpuManager = gpuManager;
			
			CUdeviceptr restrictDevicePtr = new CUdeviceptr();
			this.restrictDevicePtr = restrictDevicePtr;		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
		
// Enum erstellen mit allen zulässigen Datentypen
		//open auslagern
				try {
					ptxFileName = Prepare.ptxFile("CUDA/Project.cu");				
				
					//Für ein Puffer muss die Size geändert werden
					SIZE = getInputSchema(0).getAttributes().size() * Sizeof.DOUBLE;
					
					GpuManager<T> gpuManager = new GpuManager<>(tupleZahl, getInputSchema(0).getAttributes().size(), getInputSchema(0));
					this.gpuManager = gpuManager;
					
					CUdeviceptr restrictDevicePtr = new CUdeviceptr();
					this.restrictDevicePtr = restrictDevicePtr;		
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
					
		//Vor dem input müssen alle Attr bestimmt werden
					
		Prepare.cuLoad(ptxFileName, "Project");			
		
		//Bei projection sind viele unterschiedliche Datentypen
		//Lösungsmöglichkeit jeden Datentyp init. und allokieren und falls benötigt nutzen
		@SuppressWarnings("unused")
		int [] restrict = getRestrictList();			
		
		cuMemAlloc(restrictDevicePtr, restrict.length * Sizeof.INT);
		
		cuMemcpyHtoD(restrictDevicePtr, Pointer.to(restrict), restrict.length * Sizeof.INT);	

		context = Prepare.getContext();			
		modul = Prepare.getModul();
		function = Prepare.getFunction();
		
	}
	
	@Override
	protected void process_next(IStreamObject<T> inputElement, int port) {	
		
		gpuManager.AddTuple((Tuple<T>) inputElement);	
		
		if(gpuManager.getCount() == tupleZahl){
			try{
				CUDA_process();
			}catch(CudaException e){
				
			}
			while(0 < gpuManager.getCount()){
				transfer((IStreamObject<T>) gpuManager.getTuple());
			}
		}
	}

	@Override
	protected void process_close() {
		
		super.process_close();
		
//		// CUDA-Nachbereitung
		
		gpuManager.destroy();
		
		cuMemFree(restrictDevicePtr);
		cuMemFree(restrictDevicePtr);
		cuModuleUnload(modul);
		
		if (context != null) {
				cuCtxDestroy(context);
		}
//		// Speicher auf der GPU freigeben
				
	}

	private void CUDA_process() { 
		
		//JCUDA
		cuCtxPushCurrent(context);
		
		CUdeviceptr outputPtr = new CUdeviceptr();
		JCudaDriver.cuMemAlloc(outputPtr, Sizeof.DOUBLE);
		
		Pointer kernelParameter = Pointer.to(
				Pointer.to(restrictDevicePtr),			//
//				Pointer.to(new int []{restrictList.length}),	// Size ist die Größe der Restrict
				Pointer.to(gpuManager.getInputPtr())					//
//				Pointer.to(outputPtr)					//
				);
		
		
		
		//Start Kernel auf der GPU
		cuLaunchKernel(function,
		1, 1, 1,							//blöcke
		1, 1,1,							//threads soviele wie Attribute 
		0, null,
		kernelParameter, null);
		
		//blockiert bis alle Grids abgeschloßen sind
		cuCtxSynchronize();
		
//		gpuManager.setOutputPtr(outputPtr);
			
		
	}	
}
