package navatrana.projecttakenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vamsee on 8/26/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "projectTakeNote.db";
    private static final String TABLE_NAME = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phNumber";
    private static final String KEY_MSG = "msg";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_MSG + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //CRUD Operations
    public void addContact(final Contact contact, Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhNumber());
        values.put(KEY_MSG, contact.getMsg());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Deleting single contact
    public void deleteContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_NAME + " = ?",
                new String[] { name });
        db.close();
    }

    public List<Contact> getAllContacts(){

        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhNumber(cursor.getString(2));
                contact.setMsg(cursor.getString(3));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Getting contacts Count
    public int getContact(String name) {
        String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_NAME +" = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    //Getting message
    public String getContactMessage(String number){
        String message = "Add note to contact using Phone Note";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_MSG + " FROM " + TABLE_NAME + " WHERE " + KEY_PH_NO +" = " + number;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
                message = cursor.getString(cursor.getColumnIndex(KEY_MSG));
        }
        cursor.close();
        db.close();

        return message == null ? "Add note to contact using Phone Note" : message;
    }

    //Updating message
    public void updateMessage(String number, String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MSG, message);
        db.update(TABLE_NAME, contentValues, KEY_PH_NO + " = " + number, null);
        db.close();
    }
}
