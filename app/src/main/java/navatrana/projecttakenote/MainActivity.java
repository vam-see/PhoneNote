package navatrana.projecttakenote;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactDialog, ContactUpdateDialog {

    final private static int PERMISSIONS_REQUEST_CODE = 1;
    final private static int OVERLAY_PERMISSION_REQ_CODE = 2;
    final private static int NOTE_EXISTS = 1;
    final private static int NO_NUMBER = 2;
    private static Context context;
    private static int PICK_CONTACT = 1;
    Uri contactData;
    String cName = null;
    String cNumber = null;
    DatabaseHandler db;
    String[] permissions;
    public ArrayList<String> reqPerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = MainActivity.this;
        db = new DatabaseHandler(context);
        reqPerms = new ArrayList<>();
        permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.CALL_PHONE};
        if (hasPermissions(context, permissions).size() > 0)
            ActivityCompat.requestPermissions(this, reqPerms.toArray(new String[reqPerms.size()]), PERMISSIONS_REQUEST_CODE);
        overlayPerm();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new AsyncTask<Void, Void, Integer>(
        ) {
            @Override
            protected Integer doInBackground(Void... voids) {
                int status = pickContact(requestCode, resultCode, data);
                return status;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (integer == NOTE_EXISTS){
                    Toast.makeText(context, "Contact has already note", Toast.LENGTH_LONG).show();
                }
                else if (integer == NO_NUMBER){
                    Toast.makeText(context, "Select Contact with number", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle(getString(R.string.alert_title))
                            .setMessage(getString(R.string.alert_message))
                            .setPositiveButton(R.string.alert_setting, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent permIntent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                                    permIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(permIntent);
                                }
                            });
                    builder.create();
                    builder.show();
                    Toast.makeText(MainActivity.this, "Permission(s) Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case OVERLAY_PERMISSION_REQ_CODE:
                if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(MainActivity.this, "Overlay Permission Denied", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch (itemId) {
            case R.id.hf_setting:
            case R.id.bug_setting:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.mailto_id).toString()});

                if (itemId == R.id.hf_setting)
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.extra_mail_subject));
                else
                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.extra_mail_bug_subject));
                try{
                    startActivity(Intent.createChooser(intent, "Email developer"));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(MainActivity.this, "No email clients found", Toast.LENGTH_LONG)
                            .show();
                }

                return true;
            case R.id.share_setting:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.extra_share_subject));
                String message = getResources().getString(R.string.extra_text);
                message = message + "https://play.google.com/store/apps/details?id=navatrana.projecttakenote";
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                intent = Intent.createChooser(shareIntent, "Share with");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try{
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException ex){

                }
                return true;
            case R.id.about_setting:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.app_setting:
                intent = new Intent(this, AppSettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public int pickContact(int requestCode, int resultCode, Intent data){
        try {
            if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
                contactData = data.getData();

                Cursor cursor = context.getContentResolver().query(contactData, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (hasPhone.equalsIgnoreCase("1")) {
                        final Cursor contacts = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null);
                        if (contacts.moveToFirst()) {
                            cName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            cursor.close();
                            cNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            cNumber = cNumber.replaceAll("[^0-9+]", "");
                            cNumber = cNumber.replaceAll("\\D+", "");
                            contacts.close();
                        }
                        if (db.getContact(cName) > 0) {
                            return NOTE_EXISTS;
                        }
                    } else {
                        return NO_NUMBER;
                    }
                }

                MessageDialog messageDialog = MessageDialog.newInstance("ADD", cName, cNumber, null, null);
                messageDialog.show(getSupportFragmentManager(), "Message Dialog");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onDialogPositiveClickUpdate(DialogFragment dialog, String... params) {
        try {
            String name = params[0];
            String number = params[1];
            String message = params[2];
            int position = Integer.parseInt(params[3]);
            DatabaseTasks dbTask = new DatabaseTasks(context);
            dbTask.execute("UPDATE", number, message);
            MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.updateItem(position, new Contact(name, number, message));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        return;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String... params) {
        try {
            String name = params[0];
            String number = params[1];
            String message = params[2];
            DatabaseTasks dbTask = new DatabaseTasks(context);
            dbTask.execute("ADD", new Contact(name, number, message));
            MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.addItem(new Contact(name, number, message));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> hasPermissions(Context context, String... permissions){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    reqPerms.add(permission);
                }
            }
        }
        return reqPerms;
    }

    public void overlayPerm(){
        try{
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
