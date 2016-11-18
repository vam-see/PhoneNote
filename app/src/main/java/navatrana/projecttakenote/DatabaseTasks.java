package navatrana.projecttakenote;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Vamsee on 8/30/2016.
 */
public class DatabaseTasks extends AsyncTask<Object, Void, Void> {
    Context context;
    DatabaseHandler db;

    DatabaseTasks(Context context){
        this.context = context;
        db = new DatabaseHandler(context);
    }

    @Override
    protected Void doInBackground(Object... objects) {
        String op = (String) objects[0];
        switch  (op){
            case "ADD":
                Contact contact = (Contact) objects[1];
                db.addContact(contact, context);
                break;
            case "UPDATE":
                String number = (String)objects[1];
                String message = (String)objects[2];
                db.updateMessage(number, message);
                break;
            default: return null;
        }
        return null;
    }
}
