package navatrana.projecttakenote;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.dropbox.core.android.Auth;

/**
 * Created by Vamsee on 8/29/2016.
 */
public abstract class DActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("takenote-dropbox", MODE_PRIVATE);
        String accesstoken = prefs.getString("access-token", null);
        if (accesstoken == null){
            accesstoken = Auth.getOAuth2Token();
            if (accesstoken != null){
                prefs.edit().putString("access-token", accesstoken).apply();
                initLoadData(accesstoken);
            }
        }
        else{
            initLoadData(accesstoken);
        }
    }

    private void initLoadData(String accesstoken){
        DropboxClientFactory.init(accesstoken);
        loadData();
    }

    protected abstract void loadData();

    protected boolean hasToken(){
        SharedPreferences prefs = getSharedPreferences("takenote-dropbox", MODE_PRIVATE);
        String accesstoken = prefs.getString("access-token", null);
        return accesstoken != null;
    }
}
