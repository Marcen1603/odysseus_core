extern "C"

__global__ int select (int predict, int *input, int *output) 
{
	if(element < predict)
	{
		output = element;
	}
	
	__syncthreads();
	
}