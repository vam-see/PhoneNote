package navatrana.projecttakenote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;

/**
 * Created by Vamsee on 8/28/2016.
 */
public class ContactNameTask extends AsyncTask<String, Void, String> {

    Context context;
    Intent intent;
    String name;
    String number;

    ContactNameTask(Context context, Intent intent){
        this.context = context;
        this.intent = intent;
    }
    @Override
    protected String doInBackground(String... strings) {
        number = strings[0];
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,
        ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.HAS_PHONE_NUMBER};

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        cursor.close();
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        String message = databaseHandler.getContactMessage(number);
        intent.putExtra("MESSAGE", message);
        return name == null ? number : name;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        intent.putExtra("NAME", s);
        intent.putExtra("NUMBER", number);
        context.startService(intent);
    }
}
