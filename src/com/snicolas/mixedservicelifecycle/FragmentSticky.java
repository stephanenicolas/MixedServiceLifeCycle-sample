package com.snicolas.mixedservicelifecycle;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.Service;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

public class FragmentSticky extends RoboFragment {
    private final static String LOG_TAG = FragmentSticky.class.getSimpleName();

    @InjectView(R.id.button2)
    private Button buttonSticky;

    @InjectView(R.id.button3)
    private Button buttonNonSticky;

    @InjectView(R.id.button6)
    private CheckBox buttonForeground;

    private ServiceConnection serviceConnection;
    private MainActivity mainActivity;

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        mainActivity = (MainActivity) getActivity();
        buttonSticky.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onUseStickyServices( v );
            }
        } );
        buttonNonSticky.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onUseNonStickyServices( v );
            }
        } );
        buttonSticky
                .setSelected( mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).getInt( MainApplication.PREFERENCE_STICKY, -1 ) == Service.START_STICKY );
        buttonNonSticky.setSelected( mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).getInt( MainApplication.PREFERENCE_STICKY,
                -1 ) == Service.START_NOT_STICKY );
        buttonForeground.setChecked( mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).getBoolean(
                MainApplication.PREFERENCE_FOREGROUND, false ) );
        buttonForeground.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick( View v ) {
                onUseForegroundServices( v );
            }
        } );
    }

    @Override
    public void onStop() {
        if ( serviceConnection != null ) {
            onUseNonStickyServices( null );
        }
        super.onStop();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_sticky, container, false );
    }

    public void onUseStickyServices( View v ) {
        mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).edit().putInt( MainApplication.PREFERENCE_STICKY, Service.START_STICKY )
                .commit();
        Log.d( LOG_TAG, "Sticky" );
    }

    public void onUseNonStickyServices( View v ) {
        mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).edit()
                .putInt( MainApplication.PREFERENCE_STICKY, Service.START_NOT_STICKY ).commit();
        Log.d( LOG_TAG, "Non Sticky" );
    }

    public void onUseForegroundServices( View v ) {
        mainActivity.getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).edit()
                .putBoolean( MainApplication.PREFERENCE_FOREGROUND, buttonForeground.isChecked() ).commit();
        Log.d( LOG_TAG, "Foreground" );
    }

}
