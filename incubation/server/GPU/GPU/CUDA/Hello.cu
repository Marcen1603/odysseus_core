extern "C"
__global__ void Hello_World(void)
{
	printf("Hello World \n");
	
	__syncthreads();
}