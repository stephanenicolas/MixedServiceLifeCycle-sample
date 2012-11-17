package com.snicolas.mixedservicelifecycle;

import javax.annotation.Nullable;

import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.snicolas.mixedservicelifecycle.MainApplication.BoundState;
import com.snicolas.mixedservicelifecycle.MainApplication.ServiceStateListener;
import com.snicolas.mixedservicelifecycle.MainApplication.StartedState;
import com.viewpagerindicator.TabPageIndicator;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboSherlockFragmentActivity implements ServiceStateListener {

    public static final String[] CONTENT = { "Started", "Bound", "Mixed", "Sticky" };
    public static final String[] URLS = { "started.html", "bound.html", "mixed.html", "sticky.html" };
    public static int position;

    @InjectView(R.id.status_service)
    private TextView statusBarService;

    @InjectView(R.id.status_binder)
    private TextView statusBarBinder;

    @InjectView(R.id.pager)
    private ViewPager pager;

    @InjectView(R.id.indicator)
    private TabPageIndicator indicator;

    @InjectFragment(R.id.explanation_fragment)
    @Nullable
    private FragmentExplanation fragmentExplanation;

    @InjectView(R.id.button_explanation)
    @Nullable
    private Button buttonExplanation;

    private MyAdapter mAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        mAdapter = new MyAdapter( getSupportFragmentManager() );
        pager.setAdapter( mAdapter );
        indicator.setViewPager( pager );
        indicator.setOnPageChangeListener( new OnLifeCycleTabChangeListener() );

        Button buttonExplanation = (Button) findViewById( R.id.button_explanation );
        if ( buttonExplanation != null ) {
            buttonExplanation.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick( View v ) {
                    loadExplanation( position );
                }
            } );
        } else {
            loadExplanation( 0 );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ( (MainApplication) getApplication() ).add( this );
    }

    @Override
    protected void onStop() {
        super.onStop();
        ( (MainApplication) getApplication() ).remove( this );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getSupportMenuInflater().inflate( R.menu.activity_main, menu );
        return true;
    }

    @Override
    public boolean onMenuItemSelected( int featureId, MenuItem item ) {
        Intent intent = new Intent( this, ExplanationActivity.class );
        intent.putExtra( FragmentExplanation.BUNDLE_KEY_URL, "info.html" );
        startActivity( intent );
        return true;
    }

    private void loadExplanation( int position ) {
        String url = URLS[ position % URLS.length ];
        if ( fragmentExplanation != null ) {
            fragmentExplanation.loadExplanation( url );
        } else {
            Intent intent = new Intent( this, ExplanationActivity.class );
            intent.putExtra( FragmentExplanation.BUNDLE_KEY_URL, url );
            startActivity( intent );
        }
    }

    public void setServiceStatus( String text ) {
        this.statusBarService.setText( text );
    }

    public void setBinderStatus( String text ) {
        // this.statusBarBinder.setText( text );
    }

    private final class OnLifeCycleTabChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected( int position ) {
            MainActivity.position = position;
            if ( fragmentExplanation != null ) {
                loadExplanation( position );
            }
        }

        @Override
        public void onPageScrolled( int arg0, float arg1, int arg2 ) {

        }

        @Override
        public void onPageScrollStateChanged( int arg0 ) {

        }
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter( FragmentManager fm ) {
            super( fm );
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle( int position ) {
            return CONTENT[ position % CONTENT.length ].toUpperCase();
        }

        @Override
        public Fragment getItem( int position ) {
            switch ( position ) {
                case 0:
                    return new FragmentStarted();

                case 1:
                    return new FragmentBound();

                case 2:
                    return new FragmentMixed();

                case 3:
                    return new FragmentSticky();

                default:
                    return null;
            }
        }
    }

    @Override
    public void onServiceStateChange( StartedState startedState, BoundState boundState ) {
        final StringBuilder builder = new StringBuilder();
        builder.append( startedState.toString().toLowerCase() + " & " + boundState.toString().toLowerCase() );
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                setServiceStatus( builder.toString() );
            }
        } );
    }

}
