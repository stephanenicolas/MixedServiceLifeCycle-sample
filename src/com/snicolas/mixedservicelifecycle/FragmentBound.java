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

public class FragmentBound extends RoboFragment {
    private final static String LOG_TAG = FragmentBound.class.getSimpleName();

    @InjectView(R.id.button2)
    private Button buttonBind;

    @InjectView(R.id.button3)
    private Button buttonUnbind;

    private ServiceConnection serviceConnection;
    private MainActivity mainActivity;

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mainActivity = (MainActivity) getActivity();
        buttonBind.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onBind( v );
            }
        } );
        buttonUnbind.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onUnbind( v );
            }
        } );
    }

    @Override
    public void onStop() {
        if ( serviceConnection != null ) {
            onUnbind( null );
        }
        super.onStop();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_bound, container, false );
    }

    public void onBind( View v ) {
        Intent intent = new Intent( mainActivity, MainService.class );
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

}
