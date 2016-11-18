package navatrana.projecttakenote;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txtVersion = (TextView) findViewById(R.id.txtVersion);
        String versionName = BuildConfig.VERSION_NAME;
        txtVersion.setText(getResources().getString(R.string.version) + versionName);
        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
        String myData = getResources().getString(R.string.about_text);

        WebView webView = (WebView) findViewById(R.id.webAbout);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        if (Build.VERSION.SDK_INT >= 19)
            webView.setWebContentsDebuggingEnabled(false);
        if (Build.VERSION.SDK_INT >= 19)
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        else
            if (Build.VERSION.SDK_INT >= 11)
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadData(String.format(htmlText, myData), "text/html", "utf-8");
    }
}
