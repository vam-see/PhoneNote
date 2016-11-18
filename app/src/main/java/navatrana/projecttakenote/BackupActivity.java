package navatrana.projecttakenote;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackupActivity extends DActivity {

    private static final String TAG = BackupActivity.class.getName();
    private String mPath = "";
    private static final String API_KEY = BuildConfig.DB_APP_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btnDrop = (Button) findViewById(R.id.btnDrop);
        Button btnNow = (Button) findViewById(R.id.btnNow);

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.startOAuth2Authentication(BackupActivity.this, API_KEY);
            }
        });

        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFiles();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, AppSettings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String dbPath = new String();
        try{
            dbPath = getApplicationContext().getDatabasePath("projectTakeNote").getPath();
        }catch (Exception e){

        }
        if (hasToken()){
            findViewById(R.id.btnDrop).setVisibility(View.GONE);
            findViewById(R.id.txtEmail).setVisibility(View.VISIBLE);
            findViewById(R.id.btnNow).setVisibility(View.VISIBLE);
            if (dbPath == null){
                Toast.makeText(BackupActivity.this, "No files found to backup", Toast.LENGTH_SHORT).show();
                findViewById(R.id.btnNow).setEnabled(false);
            }
            else{
                findViewById(R.id.btnNow).setEnabled(true);
            }
        }
        else{
            findViewById(R.id.btnDrop).setVisibility(View.VISIBLE);
            findViewById(R.id.txtEmail).setVisibility(View.GONE);
            findViewById(R.id.btnNow).setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                ((TextView) findViewById(R.id.txtEmail)).setText(result.getEmail());
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

    private void uploadFiles(){

        String dbUri = new String();
        try{
            final String dbPath = getApplicationContext().getDatabasePath("projectTakeNote").getPath();
            File dbFile = new File(dbPath);
            dbUri = Uri.fromFile(dbFile).toString();
        }catch (Exception e){

        }
        new UploadFileTask(this, DropboxClientFactory.getClient(), new UploadFileTask.Callback() {
            @Override
            public void onUploadComplete(FileMetadata result) {
                Toast.makeText(BackupActivity.this, "Uploads successful", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed to upload file.", e);
                Toast.makeText(BackupActivity.this,
                        "An error has occurred",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }).execute(dbUri, mPath);
    }
}
