dalvikMainActivityFragment(importList,osgiBindCode,bodyCode,startCode)::=<<

package com.app.odysseus.odysseustest;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


$importList: {importClass| 
import $importClass$;
}$


public class MainActivityFragment extends Fragment {

    public MainActivityFragment()  throws IOException{

        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String path =  externalStoragePublicDirectory.getPath();

        startOdysseus(path);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    private void startOdysseus(String path) throws IOException {
      	
      	$osgiBindCode$
	
		$bodyCode$
	
		$startCode$
    }
}


>>