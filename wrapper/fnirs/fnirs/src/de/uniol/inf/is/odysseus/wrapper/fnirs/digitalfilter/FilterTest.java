package de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter.FilterType;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter.PassType;

public class FilterTest 
{

	public static void main(String[] args) 
	{
		DigitalFilter filter = new DigitalFilter(FilterType.BUTTERWORTH, PassType.LOWPASS, 4, 0.2, 0.0, 0.0, false);
		FilterLoop loop = new FilterLoop(filter);
		
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for (;;)
			{
				System.out.println(loop.filterStep(Double.parseDouble(in.readLine())));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
