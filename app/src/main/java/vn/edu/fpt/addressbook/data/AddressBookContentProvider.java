package vn.edu.fpt.addressbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import vn.edu.fpt.addressbook.R;

public class AddressBookContentProvider extends ContentProvider {
    private AddressBookDatabaseHelper databaseHelper;

    // UriMatcher helps ContentProvider determine operation to perform
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // constants used with UriMatcher to determine operation to perform
    private static final int ONE_CONTACT = 1; // manipulate one contact
    private static final int CONTACTS = 2; // manipulate contacts table

    // static block to configure this ContentProvider's UriMatcher
    static {
        // Uri for Contact with the specified id (#)
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME + "/#", ONE_CONTACT);

        // Uri for contacts table
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                DatabaseDescription.Contact.TABLE_NAME, CONTACTS);
    }

    @Override
    public boolean onCreate() {
        // create AddressBookDatabaseHelper
        databaseHelper = new AddressBookDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // create SQLiteDatabase object for querying database
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseDescription.Contact.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                queryBuilder.appendWhere(
                        DatabaseDescription.Contact._ID + "=" + uri.getLastPathSegment());
                break;
            case CONTACTS:
                break;
            default:
                throw new UnsupportedOperationException(getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // execute the query to select one or all contacts
        Cursor cursor = queryBuilder.query(databaseHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newContactUri;

        if (uriMatcher.match(uri) == CONTACTS) {// insert the new contact--success yields new contact's row id
            long rowId = databaseHelper.getWritableDatabase().insert(
                    DatabaseDescription.Contact.TABLE_NAME, null, values);

            // if the contact was inserted, create an appropriate Uri;
            if (rowId > 0) { // SQLite row IDs start at 1
                newContactUri = DatabaseDescription.Contact.buildContactUri(rowId);

                // notify observers that the database changed
                getContext().getContentResolver().notifyChange(uri, null);
            } else
                throw new SQLException(
                        getContext().getString(R.string.insert_failed) + uri);
        } else {
            throw new UnsupportedOperationException(
                    getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newContactUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numberOfRowsUpdated;
        if (uriMatcher.match(uri) == ONE_CONTACT) {
            // get the contact's id
            String id = uri.getLastPathSegment();

            // update the contact
            numberOfRowsUpdated = databaseHelper.getWritableDatabase().update(
                    DatabaseDescription.Contact.TABLE_NAME, values,
                    DatabaseDescription.Contact._ID + "=" + id,
                    selectionArgs);
        } else {
            throw new UnsupportedOperationException(
                    getContext().getString(R.string.invalid_update_uri) + uri);
        }

        // if changes were made, notify observers
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  numberOfRowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        if (uriMatcher.match(uri) == ONE_CONTACT) {
            // get the contact's id
            String id = uri.getLastPathSegment();

            // delete the contact
            numberOfRowsDeleted = databaseHelper.getWritableDatabase().delete(
                    DatabaseDescription.Contact.TABLE_NAME,
                    DatabaseDescription.Contact._ID + "=" + id,
                    selectionArgs);
        } else {
            throw new UnsupportedOperationException(
                    getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // if changes were made, notify observers
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowsDeleted;
    }

}
