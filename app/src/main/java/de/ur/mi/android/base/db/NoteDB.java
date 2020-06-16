package de.ur.mi.android.base.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;

import de.ur.mi.android.base.Note;
import de.ur.mi.android.base.db.NoteDBContract.NoteEntry;

/**
 * Database handler class for storing the history entries.
 */
public class NoteDB extends SQLiteOpenHelper {

   public static final int DATABASE_VERSION = 1;
   public static final String DATABASE_NAME = "NoteDB.sqlite";

   private Context context;

   public NoteDB(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      this.context = context;
   }

   private static final String SQL_CREATE_ENTRIES =
           "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" + NoteEntry._ID +
                   " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NoteEntry.COLUMN_NAME_DATE +
                   " DATETIME DEFAULT " + "CURRENT_TIMESTAMP, " + NoteEntry.COLUMN_NAME_TEXT +
                   " TEXT, " + NoteEntry.COLUMN_NAME_TITLE + ");";

   private static final String SQL_DELETE_ENTRIES =
           "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME + ";";

   // This trigger updates the last modified date column when a row is updated
   private static final String SQL_DATE_TRIGGER = "CREATE TRIGGER update_time_trigger" +
           " AFTER UPDATE ON " + NoteEntry.TABLE_NAME + " FOR EACH ROW BEGIN UPDATE " +
           NoteEntry.TABLE_NAME + " SET " + NoteEntry.COLUMN_NAME_DATE + " = CURRENT_TIMESTAMP" +
           " WHERE " + NoteEntry._ID + " = old." + NoteEntry._ID + "; END";

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_ENTRIES);
      db.execSQL(SQL_DATE_TRIGGER);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // Code performing database upgrades, here just recreation
      db.execSQL(SQL_DELETE_ENTRIES);
      onCreate(db);
   }

   /**
    * Inserts an history entry in the database.
    *
    * @param text  the text of the entry to insert
    * @param title the title to insert
    * @return the rowid of the inserted entry (or -1 if an error occurred)
    */
   public long insertEntry(String text, String title) {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getWritableDatabase();
      ContentValues values = new ContentValues();
      // Id and date are automatically generated (see SQL_CREATE_ENTRIES)
      values.put(NoteEntry.COLUMN_NAME_TEXT, text);
      values.put(NoteEntry.COLUMN_NAME_TITLE, title);
      return db.insert(NoteEntry.TABLE_NAME, NoteEntry.COLUMN_NAME_TEXT, values);
   }

   /**
    * Reads the note entries from the database.
    *
    * @return the array with the entries
    */
   public Note[] readEntriesFull() {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getReadableDatabase();
      // Define a projection that specifies which columns from the database
      // you will actually use after this query.
      String[] projection = {NoteEntry._ID, NoteEntry.COLUMN_NAME_DATE, NoteEntry.COLUMN_NAME_TEXT,
              NoteEntry.COLUMN_NAME_TITLE};
      // How you want the results sorted in the resulting Cursor
      String sortOrder = NoteEntry.COLUMN_NAME_DATE + " DESC";
      Cursor cursor = db.query(NoteEntry.TABLE_NAME, // The table to query
              projection, // The columns to return
              null, // The columns for the WHERE clause
              null, // The values for the WHERE clause
              null, // don't group the rows
              null, // don't filter by row groups
              sortOrder // The sort order
      );
      cursor.moveToFirst();
      int colCount = cursor.getCount();
      Note[] noteEntries = new Note[colCount];
      for (int i = 0; i < colCount; ++i) {
         int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(NoteEntry._ID)));
         String date = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_DATE));
         String text = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));
         String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE));
         noteEntries[i] = new Note(title, text, date);
         noteEntries[i].setId(id);
         cursor.moveToNext();
      }
      return noteEntries;
   }

   /**
    * Reads the titles and ids of the entries from the database.
    *
    * @return the array with the entries
    */
   public Note[] readEntriesTitle() {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getReadableDatabase();
      // Define a projection that specifies which columns from the database
      // you will actually use after this query.
      String[] projection =
              {NoteEntry._ID, NoteEntry.COLUMN_NAME_DATE, NoteEntry.COLUMN_NAME_TITLE};
      // How you want the results sorted in the resulting Cursor
      String sortOrder = NoteEntry.COLUMN_NAME_DATE + " DESC";
      Cursor cursor = db.query(NoteEntry.TABLE_NAME, // The table to query
              projection, // The columns to return
              null, // The columns for the WHERE clause
              null, // The values for the WHERE clause
              null, // don't group the rows
              null, // don't filter by row groups
              sortOrder // The sort order
      );
      cursor.moveToFirst();
      int colCount = cursor.getCount();
      Note[] noteEntries = new Note[colCount];
      for (int i = 0; i < colCount; ++i) {
         int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(NoteEntry._ID)));
         String date = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_DATE));
         String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE));
         noteEntries[i] = new Note(title, null, date);
         noteEntries[i].setId(id);
         cursor.moveToNext();
      }
      return noteEntries;
   }

   /**
    * Reads an entry based on its id from the database.
    *
    * @param id the id of the entry to read
    * @return the note entry or <code>null</code>, if an non-existing id was provided
    */
   public Note readEntryById(int id) {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getReadableDatabase();
      // Define a projection that specifies which columns from the database
      // you will actually use after this query.
      String[] projection = {NoteEntry._ID, NoteEntry.COLUMN_NAME_DATE, NoteEntry.COLUMN_NAME_TITLE,
              NoteEntry.COLUMN_NAME_TEXT};
      String[] selectionArgs = {String.valueOf(id)};
      Cursor cursor = db.query(NoteEntry.TABLE_NAME, // The table to query
              projection, // The columns to return
              NoteEntry._ID + " = ?", // The columns for the WHERE clause
              selectionArgs, // The values for the WHERE clause
              null, // don't group the rows
              null, // don't filter by row groups
              null // No sorting required for only one entry
      );
      boolean success = false;
      if (cursor != null) {
         success = cursor.moveToFirst();
      }
      if (!success) {
         return null;
      }
      String date = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_DATE));
      String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE));
      String text = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TEXT));
      cursor.close();
      Note note = new Note(title, text, date);
      note.setId(id);
      return note;
   }

   /**
    * Updates a note entry
    *
    * @param id    the id of the entry to update
    * @param text  the new text of the entry
    * @param title the new title of the entry
    * @return the number of affected rows
    */
   public long updateEntry(int id, String text, String title) {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getWritableDatabase();
      ContentValues values = new ContentValues();
      // Id and date are automatically generated (see SQL_CREATE_ENTRIES)
      values.put(NoteEntry.COLUMN_NAME_TEXT, text);
      values.put(NoteEntry.COLUMN_NAME_TITLE, title);
      // Define 'where' part of query
      String selection = NoteEntry._ID + " = ?";
      Log.d("NoteDB", "Update entry with ID: " + id);
      // Specify arguments in placeholder order.
      String[] selectionArgs = {String.valueOf(id)};
      return db.update(NoteEntry.TABLE_NAME, values, selection, selectionArgs);
   }

   /**
    * Performs a search query in the database.
    *
    * @param selection     the WHERE clause without the "WHERE" at the beginnig
    * @param selectionArgs the arguments for the possible placeholders ("?") in the selection
    * @param columns       the list of columns to return
    * @return a cursor holding the query results
    */
   private Cursor query(String selection, String[] selectionArgs, String[] columns) {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getReadableDatabase();
      SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
      builder.setTables(NoteEntry.TABLE_NAME);
      Cursor cursor = builder.query(db, columns, selection, selectionArgs, null, null, null);
      if (cursor == null) {
         return null;
      }
      return cursor;
   }

   /**
    * Remove an entry from the database.
    *
    * @param id the id of the entry to delete
    * @return the amount of deleted rows
    */
   public int removeEntry(int id) {
      NoteDB nDB = new NoteDB(context);
      SQLiteDatabase db = nDB.getWritableDatabase();
      // Define 'where' part of query
      String selection = NoteEntry._ID + " = ?";
      Log.d("NoteDB", "Delete entry with ID: " + id);
      // Specify arguments in placeholder order.
      String[] selectionArgs = {String.valueOf(id)};
      // Issue SQL statement.
      return db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
   }

   /**
    * Returns the matching entries from the note database.
    *
    * @param query The user-given query string
    * @return the <code>Cursor</code> containing the results
    */
   public Cursor getNoteMatches(String query) {
      String selection =
              NoteEntry.COLUMN_NAME_TEXT + " LIKE ? OR " + NoteEntry.COLUMN_NAME_TITLE + " LIKE ?";
      String[] selectionArgs = new String[]{"%" + query + "%"};
      return query(selection, selectionArgs, null);
   }

   /**
    * Helper method for the <code>AndroidDatabaseManager</code> class. Source:
    * <code>https://github.com/sanathp/DatabaseManager_For_Android</code>
    *
    * @param Query The query to perform
    * @return The result set
    */
   public ArrayList<Cursor> getData(String Query) {
      // get writable database
      SQLiteDatabase sqlDB = this.getWritableDatabase();
      String[] columns = new String[]{"mesage"};
      // an array list of cursor to save two cursors one has results from the
      // query
      // other cursor stores error message if any errors are triggered
      ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
      MatrixCursor Cursor2 = new MatrixCursor(columns);
      alc.add(null);
      alc.add(null);

      try {
         String maxQuery = Query;
         // execute the query results will be save in Cursor c
         Cursor c = sqlDB.rawQuery(maxQuery, null);

         // add value to cursor2
         Cursor2.addRow(new Object[]{"Success"});

         alc.set(1, Cursor2);
         if (null != c && c.getCount() > 0) {

            alc.set(0, c);
            c.moveToFirst();

            return alc;
         }
         return alc;
      } catch (SQLException sqlEx) {
         Log.d("printing exception", sqlEx.getMessage());
         // if any exceptions are triggered save the error message to cursor an
         // return the arraylist
         Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
         alc.set(1, Cursor2);
         return alc;
      } catch (Exception ex) {

         Log.d("printing exception", ex.getMessage());

         // if any exceptions are triggered save the error message to cursor an
         // return the arraylist
         Cursor2.addRow(new Object[]{"" + ex.getMessage()});
         alc.set(1, Cursor2);
         return alc;
      }

   }

}
