/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.constants;

/**
 * Constants for parallelization benchmarker project
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelizationBenchmarkerConstants {
	public static final int DEFAULT_NUMBER_OF_EXECUTIONS = 5;
	public static final long DEFAULT_MAX_EXECUTION_TIME = 80000;
	public static final int DEFAULT_NUMBER_OF_ELEMENTS = 20000;

	public static final String DEFAULT_INTER_OPERATOR_DEGREES = "1,2,4,8";

	public static final String DEFAULT_INTRA_OPERATOR_DEGREES = "1,2,4,8";

	public static final String PRE_TRANSFORM_TOKEN = "#PRETRANSFORM BenchmarkPreTransformation";
}
