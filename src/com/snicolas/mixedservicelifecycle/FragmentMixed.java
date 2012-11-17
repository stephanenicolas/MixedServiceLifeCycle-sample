package com.snicolas.mixedservicelifecycle;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentMixed extends RoboFragment {

    private ServiceConnection serviceConnection;
    private MainActivity mainActivity;

    @InjectView(R.id.button5)
    private Button buttonStartBind;

    @InjectView(R.id.button3)
    private Button buttonUnbind;

    @InjectView(R.id.button4)
    private Button buttonStop;

    private final static String LOG_TAG = FragmentMixed.class.getSimpleName();

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mainActivity = (MainActivity) getActivity();
        buttonUnbind.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onUnbind( v );
            }
        } );
        buttonStop.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onStop( v );
            }
        } );
        buttonStartBind.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onStartBind( v );
            }
        } );

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_mixed, container, false );
    }

    @Override
    public void onStop() {
        if ( serviceConnection != null ) {
            onUnbind( null );
        }
        super.onStop();
    }

    public void onStartBind( View v ) {
        Intent intent = new Intent( mainActivity, MainService.class );
        mainActivity.startService( intent );
        Log.d( LOG_TAG, "Started" );
        this.serviceConnection = new MainServiceConnection( mainActivity );
        mainActivity.bindService( intent, serviceConnection, Activity.BIND_AUTO_CREATE );
        buttonUnbind.setEnabled( true );
        Log.d( LOG_TAG, "Bind" );
    }

    public void onUnbind( View v ) {
        mainActivity.unbindService( serviceConnection );
        this.serviceConnection = null;
        buttonUnbind.setEnabled( false );
        Log.d( LOG_TAG, "Unbind" );
    }

    public void onStop( View v ) {
        Intent intent = new Intent( mainActivity, MainService.class );
        mainActivity.stopService( intent );
        Log.d( LOG_TAG, "Stopped" );
    }

}
