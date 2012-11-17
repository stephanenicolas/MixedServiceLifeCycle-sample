package com.snicolas.mixedservicelifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;

public class MainApplication extends Application {

    public enum StartedState {
        STARTED, STOPPED;
    }

    public enum BoundState {
        BOUND, UNBOUND;
    }

    public interface ServiceStateListener {

        void onServiceStateChange( StartedState startedState, BoundState boundState );

    }

    public static final String PREFERENCE_STICKY = "PREFERENCE_STICKY";
    public static final String PREFERENCE_FOREGROUND = "PREFERENCE_FOREGROUND";

    private List< ServiceStateListener > listServiceListener = Collections.synchronizedList( new ArrayList< ServiceStateListener >() );

    private StartedState startedState = StartedState.STOPPED;
    private BoundState boundState = BoundState.UNBOUND;

    public void setStartedServiceState( StartedState startedState ) {
        this.startedState = startedState;
        fireServiceStateChanged();
    }

    public void setBoundState( BoundState boundState ) {
        this.boundState = boundState;
        fireServiceStateChanged();
    }

    public void add( ServiceStateListener serviceStateListener ) {
        this.listServiceListener.add( serviceStateListener );
        serviceStateListener.onServiceStateChange( startedState, boundState );
    }

    public void remove( ServiceStateListener serviceStateListener ) {
        this.listServiceListener.remove( serviceStateListener );
    }

    private void fireServiceStateChanged() {
        synchronized ( listServiceListener ) {
            for ( ServiceStateListener serviceStateListener : listServiceListener ) {
                serviceStateListener.onServiceStateChange( startedState, boundState );
            }
        }
    }

}
