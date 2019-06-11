package com.saurov.attendancemanager.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.saurov.attendancemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.PreferenceThemeLight_NoActionBar);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");

        Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material, getTheme());
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.pref_content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        SharedPreferences sharedPreferences;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

//            Log.d("Saurov", "onSharedPreferenceChanged: ");
//
//            Map<String, ?> preferencesMap = sharedPreferences.getAll();
//
//            // get the preference that has been changed
//            Object changedPreference = preferencesMap.get(key);
//            // and if it's an instance of EditTextPreference class, update its summary
//            if (preferencesMap.get(key) instanceof EditTextPreference) {
//                updateSummary((EditTextPreference) changedPreference);
//            }

        }

        //
        private void updateSummary(EditTextPreference preference) {
            // set the EditTextPreference's summary value to its current text
            preference.setSummary(preference.getText());
        }

        @Override
        public void onResume() {
            super.onResume();

//            sharedPreferences =  getPreferenceScreen().getSharedPreferences();

            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

//            Map<String, ?> preferencesMap = sharedPreferences.getAll();
//            // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
//            for (Map.Entry<String, ?> preferenceEntry : preferencesMap.entrySet()) {
//                if (preferenceEntry instanceof EditTextPreference) {
//                    updateSummary((EditTextPreference) preferenceEntry);
//                }
//            }
        }


        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
