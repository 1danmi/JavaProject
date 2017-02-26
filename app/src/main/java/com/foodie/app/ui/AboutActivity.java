package com.foodie.app.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodie.app.R;

public class AboutActivity extends AppCompatActivity {

    private CardView daniel;
    private CardView david;
    private RelativeLayout danielEmailLayout, danielGitLayout, danielInLayout, danielTelegramLayout;
    private RelativeLayout davidEmailLayout, davidGitLayout, davidInLayout, davidTelegramLayout;
    private TextView danielEmail, danielGit, danielIn, danielTelegram;
    private TextView davidEmail, davidGit, davidIn, davidTelegram;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    /**
     * Creates the activity that shows the developers info details.
     * @param savedInstanceState A saves instance of the activity from previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar_about);

        initializeDaniel();

        initializeDavid();

        setDanielClicks();

        setDavidClicks();

        setAppBar();

        setTitle("About");


    }

    /**
     * Handles the click's actions in Daniel's tab.
     */
    private void setDanielClicks() {

        danielEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmail(danielEmail.getText().toString().toLowerCase());
            }
        });
        danielGitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(danielGit.getText().toString().trim());
            }
        });
        danielInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(danielIn.getText().toString().trim());
            }
        });
        danielTelegramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(danielTelegram.getText().toString().trim());

            }
        });
    }

    /**
     * Handles the click's actions in David's tab.
     */
    private void setDavidClicks() {
        davidEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmail(davidEmail.getText().toString().toLowerCase());
            }
        });
        davidGitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(davidGit.getText().toString().trim());
            }
        });
        davidInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(davidIn.getText().toString().trim());
            }
        });
        davidTelegramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTabs(davidTelegram.getText().toString().trim());
            }
        });
    }

    /**
     * Initializes the views in Daniel's tab.
     */
    private void initializeDaniel() {
        daniel = (CardView) findViewById(R.id.cardViewDaniel);
        danielEmail = (TextView) daniel.findViewById(R.id.email_text);
        danielGit = (TextView) daniel.findViewById(R.id.github_link);
        danielIn = (TextView) daniel.findViewById(R.id.linkedin_link);
        danielTelegram = (TextView) daniel.findViewById(R.id.telegram_link);
        danielEmailLayout = (RelativeLayout) daniel.findViewById(R.id.email_layout);
        danielGitLayout = (RelativeLayout) daniel.findViewById(R.id.github_layout);
        danielInLayout = (RelativeLayout) daniel.findViewById(R.id.linkedin_layout);
        danielTelegramLayout = (RelativeLayout) daniel.findViewById(R.id.telegram_layout);
    }

    /**
     * Initializes the views in David's tab.
     */
    private void initializeDavid() {
        david = (CardView) findViewById(R.id.cardViewDavid);
        davidEmail = (TextView) david.findViewById(R.id.email_text);
        davidGit = (TextView) david.findViewById(R.id.github_link);
        davidIn = (TextView) david.findViewById(R.id.linkedin_link);
        davidTelegram = (TextView) david.findViewById(R.id.telegram_link);
        davidEmailLayout = (RelativeLayout) david.findViewById(R.id.email_layout);
        davidGitLayout = (RelativeLayout) david.findViewById(R.id.github_layout);
        davidInLayout = (RelativeLayout) david.findViewById(R.id.linkedin_layout);
        davidTelegramLayout = (RelativeLayout) david.findViewById(R.id.telegram_layout);
    }

    /**
     * Opens email activity implicitly.
     * @param email the email address to sent to the activity.
     */
    private void openEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    /**
     * Open links in Chrome Custom Tabs mode.
     * @param url The URI to open.
     */
    private void openCustomTabs(String url) {
        if (url.length() > 0) {
            if (!url.startsWith("https://") && !url.startsWith("http://")) {
                url = "https://" + url;
            }
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.primary)).setShowTitle(true);
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        }
    }

    /**
     * Sets the appbar.
     */
    private void setAppBar() {
        setSupportActionBar(toolbar);
        setActionBar();
    }

    /**
     * Sets the actionbar visibility.
     */
    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    /**
     * Handles the menu items clicks.
     * @param item The menu item's ID.
     * @return A boolean that represents whether the operation was successful.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
