package com.snicolas.mixedservicelifecycle;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentStarted extends RoboFragment {

    private MainActivity mainActivity;

    @InjectView(R.id.button1)
    private Button buttonStart;

    @InjectView(R.id.button4)
    private Button buttonStop;

    private final static String LOG_TAG = FragmentStarted.class.getSimpleName();

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mainActivity = (MainActivity) getActivity();
        buttonStart.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onStart( v );
            }
        } );
        buttonStop.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onStop( v );
            }
        } );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        setRetainInstance( true );
        View view = inflater.inflate( R.layout.fragment_started, container, false );
        return view;
    }

    public void onStart( View v ) {
        Intent intent = new Intent( mainActivity, MainService.class );
        mainActivity.startService( intent );
        Log.d( LOG_TAG, "Started" );
    }

    public void onStop( View v ) {
        Intent intent = new Intent( mainActivity, MainService.class );
        mainActivity.stopService( intent );
        Log.d( LOG_TAG, "Stopped" );
    }

}
