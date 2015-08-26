dalvikMainActivityFragment(importList,osgiBindCode,bodyCode,startCode)::=<<

package com.app.odysseus.odysseustest;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


$importList: {importClass| 
import $importClass$;
}$


public class MainActivityFragment extends Fragment {

	private Button startButton;
	private Handler handler;
	private ProgressDialog progress;
	private Context context;

    public MainActivityFragment()  throws IOException{

    }
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		context = view.getContext();
		progress = new ProgressDialog(context);
		progress.setTitle("Query run!");
		progress.setMessage("Wait!!");
		progress.setCancelable(false);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		startButton = (Button) view.findViewById(R.id.start_button);
		startButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {


					handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							progress.dismiss();

							//refresh media
							new Intent(Intent.ACTION_MEDIA_MOUNTED);
							super.handleMessage(msg);
						}

					};
					progress.show();

					new Thread() {
						public void run() {
							try {
							startQuery();
							} catch (IOException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(0);
						}

					}.start();
			}
		});

	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


      private void startQuery() throws IOException {
      	
      	$osgiBindCode$
	
		$bodyCode$
	
		$startCode$
    }
}


>>