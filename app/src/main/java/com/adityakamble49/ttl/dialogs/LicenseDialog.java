package com.adityakamble49.ttl.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.adityakamble49.ttl.R;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LicenseDialog extends DialogFragment {

    public static LicenseDialog create(boolean darkTheme, int accentColor) {
        LicenseDialog dialog = new LicenseDialog();
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        args.putInt("accent_color", accentColor);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View customView;
        try {
            customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_webview, null);
        } catch (InflateException e) {
            throw new IllegalStateException("This device does not support Web Views.");
        }
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.license)
                .customView(customView, false)
                .positiveText(android.R.string.ok)
                .build();

        final WebView webView = (WebView) customView.findViewById(R.id.v_wv_webview);
        try {
            // Load from changelog.html in the assets folder
            StringBuilder buf = new StringBuilder();
            InputStream json = getActivity().getAssets().open("license.html");
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null)
                buf.append(str);
            in.close();

            // Inject color values for WebView body background and links
            final int accentColor = getArguments().getInt("accent_color");
            webView.loadData(buf.toString()
                            .replace("{style-placeholder}", getArguments().getBoolean
                                    ("dark_theme") ?
                                    "body { background-color: #444444; color: #fff; }" :
                                    "body { background-color: #fff; color: #000; }")
                            .replace("{link-color}", colorToHex(shiftColor(accentColor, true)))
                            .replace("{link-color-active}", colorToHex(accentColor))
                    , "text/html", "UTF-8");
        } catch (Throwable e) {
            webView.loadData("<h1>Unable to load</h1><p>" + e.getLocalizedMessage() + "</p>",
                    "text/html", "UTF-8");
        }
        return dialog;
    }

    private String colorToHex(int color) {
        return Integer.toHexString(color).substring(2);
    }

    private int shiftColor(int color, boolean up) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (up ? 1.1f : 0.9f); // value component
        return Color.HSVToColor(hsv);
    }
}
