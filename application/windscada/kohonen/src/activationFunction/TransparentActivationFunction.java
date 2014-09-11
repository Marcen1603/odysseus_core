/**
* Copyright (c) 2006, Seweryn Habdank-Wojewodzki
* Copyright (c) 2006, Janusz Rybarski
*
* All rights reserved.
* 
* Redistribution and use in source and binary forms,
* with or without modification, are permitted provided
* that the following conditions are met:
*
* Redistributions of source code must retain the above
* copyright notice, this list of conditions and the
* following disclaimer.
*
* Redistributions in binary form must reproduce the
* above copyright notice, this list of conditions
* and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS
* AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
* A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
* THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
* USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
* HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
* WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
* OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package activationFunction;

/**
 * Transparent activation function return input value without any changes.
 * @author Janusz Rybarski
 * e-mail: janusz.rybarski AT ae DOT krakow DOT pl
 * @author Seweryn Habdank-Wojewdzki
 * e-mail: habdank AT megapolis DOT pl
 * @version 1.0 2006/05/02
 */

public class TransparentActivationFunction implements ActivationFunctionModel{
    
    /**
     * Creates a new instance of TranspatentActivationFunction
     */
    public TransparentActivationFunction() {
    }
    
    /**
     * Do nothing
     * @param paramateresList array of the parameters
     */
    @Override
	public void setParameteres(double[] paramateresList){
    }
    
    /**
     * Return null
     * @return null
     */
    @Override
	public double[] getParamateres(){
        return null;
    }

    /**
     * Return the input value without any changes.
     * @param inputValue input value
     * @return input value
     */
    @Override
	public double getValue(double inputValue) {
        return inputValue;
    }
}
