package com.snicolas.mixedservicelifecycle;

import java.lang.ref.WeakReference;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MainServiceConnection implements ServiceConnection {
    private final WeakReference< MainActivity > mainActivity;

    /**
     * @param mainActivity
     */
    public MainServiceConnection( MainActivity mainActivity ) {
        this.mainActivity = new WeakReference< MainActivity >( mainActivity );
    }

    @Override
    public void onServiceDisconnected( ComponentName name ) {
        if ( mainActivity.get() != null ) {
            mainActivity.get().setBinderStatus( "UnBound" );
        }
    }

    @Override
    public void onServiceConnected( ComponentName name, IBinder service ) {
        if ( mainActivity.get() != null ) {
            mainActivity.get().setBinderStatus( "Bound" );
        }
    }
}