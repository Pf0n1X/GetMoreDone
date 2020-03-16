package com.pf0n1x.getmoredone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class SettingsAboutActivity extends AppCompatActivity {

    // Data Members
    TextView mAboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);

        mAboutTextView = findViewById(R.id.textview_about);

        setLinksClickable();
    }

    public void setLinksClickable() {
        mAboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
