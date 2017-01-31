package com.foodie.app.ui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initializeDaniel();

        initializeDavid();

        setDanielClicks();

        setDavidClicks();



    }

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

    private void initializeDavid() {
        david= (CardView) findViewById(R.id.cardViewDavid);
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
     * Opens eamil activity implicitly.
     * @param email the email address to sent to the activity.
     */
    private void openEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

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
}
