package com.snicolas.mixedservicelifecycle;

import javax.annotation.Nullable;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectFragment;
import android.content.res.Configuration;
import android.os.Bundle;

@ContentView(R.layout.activity_explanation)
public class ExplanationActivity extends RoboFragmentActivity {

    @InjectFragment(R.id.explanation_fragment)
    private FragmentExplanation fragmentExplanation;

    @InjectExtra(FragmentExplanation.BUNDLE_KEY_URL)
    @Nullable
    private String url;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // Need to check if Activity has been switched to landscape mode
        // If yes, finished and go back to the start Activity
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            finish();
            return;
        }
        if ( url != null ) {
            fragmentExplanation.loadExplanation( url );
        }
    }
}
