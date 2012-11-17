package com.snicolas.mixedservicelifecycle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.snicolas.mixedservicelifecycle.MainApplication.BoundState;
import com.snicolas.mixedservicelifecycle.MainApplication.StartedState;

public class MainService extends Service {

    private static final int ID = 76543;

    private static int serviceCount = 0;

    private int serviceNumber = serviceCount++;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText( this, "Service created :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        if ( getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).getBoolean( MainApplication.PREFERENCE_FOREGROUND, false ) ) {

            Intent intentReturn = new Intent( this, MainActivity.class );
            PendingIntent pendingIntent = PendingIntent.getActivity( getApplicationContext(), 43, intentReturn, 0 );
            // This constructor is deprecated. Use Notification.Builder instead
            Notification notification = new NotificationCompat.Builder( this )//
                    .setWhen( System.currentTimeMillis() )//
                    .setSmallIcon( R.drawable.ic_action_info )//
                    .setContentTitle( "Service Running" )//
                    .setContentText( "Service life cycle demo" )//
                    .setOngoing( true )//
                    .setContentIntent( pendingIntent )//
                    .build();
            startForeground( ID, notification );
        }
    }

    @Override
    @Deprecated
    public void onStart( Intent intent, int startId ) {
        super.onStart( intent, startId );
        Toast.makeText( this, "Service started :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setStartedServiceState( StartedState.STARTED );

    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        Toast.makeText( this, "Service started by command :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setStartedServiceState( StartedState.STARTED );
        return getSharedPreferences( "default", Activity.MODE_WORLD_WRITEABLE ).getInt( MainApplication.PREFERENCE_STICKY, Service.START_STICKY );
    }

    @Override
    public IBinder onBind( Intent intent ) {
        Toast.makeText( this, "Service bound :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setBoundState( BoundState.BOUND );
        return new MainBinder();
    }

    @Override
    public void onRebind( Intent intent ) {
        Toast.makeText( this, "Service rebound :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setBoundState( BoundState.BOUND );
        super.onRebind( intent );
    }

    @Override
    public boolean onUnbind( Intent intent ) {
        Toast.makeText( this, "Service unbound :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setBoundState( BoundState.UNBOUND );
        return super.onUnbind( intent );
    }

    @Override
    public void onDestroy() {
        Toast.makeText( this, "Service destroyed :#" + this.serviceNumber, Toast.LENGTH_SHORT ).show();
        ( (MainApplication) getApplication() ).setStartedServiceState( StartedState.STOPPED );
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.cancel( ID );
        super.onDestroy();
    }

    private class MainBinder extends Binder {

    }

}
