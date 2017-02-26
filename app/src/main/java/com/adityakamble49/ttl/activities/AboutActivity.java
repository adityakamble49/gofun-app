package com.adityakamble49.ttl.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.adityakamble49.ttl.R;
import com.adityakamble49.ttl.utils.Constants;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class AboutActivity extends MaterialAboutActivity {


    @Override
    protected MaterialAboutList getMaterialAboutList(final Context context) {
        MaterialAboutList.Builder listBuilder = new MaterialAboutList.Builder();

        // App Card
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(getString(R.string.app_name))
                .icon(R.mipmap.ic_launcher)
                .build());
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.version))
                .subText(getString(R.string.app_version))
                .icon(R.drawable.ic_info)
                .build());
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.changelog))
                .icon(R.drawable.ic_changelog)
                .build());
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.license))
                .icon(R.drawable.ic_license)
                .build());

        // Developer Card
        MaterialAboutCard.Builder developerCardBuilder = new MaterialAboutCard.Builder();
        developerCardBuilder.title(getString(R.string.developer));
        developerCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.developer_name))
                .subText(getString(R.string.developer_country))
                .icon(R.drawable.ic_developer)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        openUrl(Constants.ReferenceUrls.DEVELOPER_GOOGLE_PLUS);
                    }
                })
                .build());
        developerCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.view_portfolio))
                .icon(R.drawable.ic_portfolio)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        openUrl(Constants.ReferenceUrls.DEVELOPER_PORTFOLIO);
                    }
                })
                .build());

        // Developer Card
        MaterialAboutCard.Builder inspirationsCardBuilder = new MaterialAboutCard.Builder();
        inspirationsCardBuilder.title(getString(R.string.inspirations));
        inspirationsCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.inspiration_gohome_title))
                .subText(getString(R.string.inspiration_gohome_fork))
                .icon(R.drawable.ic_star)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        openUrl(Constants.ReferenceUrls.INSPIRATION_GOHOME);
                    }
                })
                .build());
        inspirationsCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.inspiration_gohome_developer))
                .subText(getString(R.string.inspiration_gohome))
                .icon(R.drawable.ic_developer)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        openUrl(Constants.ReferenceUrls.INSPIRATION_GOHOME_DEVELOPER);
                    }
                })
                .build());
        inspirationsCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.inspiration_gohome_mobile_thinker))
                .subText(getString(R.string.inspiration_gohome_mobile_idea))
                .icon(R.drawable.ic_developer)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        openUrl(Constants.ReferenceUrls.INSPIRATION_GOHOME_MOBILE);
                    }
                })
                .build());

        listBuilder.addCard(appCardBuilder.build());
        listBuilder.addCard(developerCardBuilder.build());
        listBuilder.addCard(inspirationsCardBuilder.build());
        return listBuilder.build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about);
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
