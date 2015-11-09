extern "C"

__global__ void Project(int* beschr, double** tuple)
{
	
	printf("Hello World %d \n %d", beschr[1], tuple[1][1]);

	__syncthreads();
}