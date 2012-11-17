package com.snicolas.mixedservicelifecycle;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentExplanation extends RoboFragment {
    public static final String BUNDLE_KEY_URL = "BUNDLE_KEY_URL";

    private final static String LOG_TAG = FragmentExplanation.class.getSimpleName();

    private MainActivity mainActivity;

    @InjectView(R.id.webview)
    private WebView webView;

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState );
        if ( getArguments() != null && getArguments().containsKey( BUNDLE_KEY_URL ) ) {
            loadExplanation( getArguments().getString( BUNDLE_KEY_URL ) );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        setRetainInstance( true );
        View view = inflater.inflate( R.layout.fragment_explanation, container, false );
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

    public void loadExplanation( String url ) {
        webView.loadUrl( "file:///android_asset/" + url );
    }
}
