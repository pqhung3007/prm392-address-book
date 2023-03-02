package vn.edu.fpt.addressbook.data;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


public class AddressBookDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AddressBook.db";
    private static final int DATABASE_VERSION = 1;

    public AddressBookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // query to create a new table named contacts
        String createQuery = "CREATE TABLE " + DatabaseDescription.Contact.TABLE_NAME + "(" +
                DatabaseDescription.Contact._ID + " integer primary key, " +
                DatabaseDescription.Contact.COLUMN_NAME + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_PHONE + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_EMAIL + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_STREET + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_CITY + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_STATE + " TEXT, " +
                DatabaseDescription.Contact.COLUMN_ZIP + " TEXT)";

        db.execSQL(createQuery); // execute the query
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

