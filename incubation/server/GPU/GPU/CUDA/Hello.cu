extern "C"
__global__ void Hello(void)
{
	printf("Hello World from GPU thread x = %d and y = %d and z = %d\n",threadIdx.x, threadIdx.y, threadIdx.z);
	
	/*__syncthreads();*/
}