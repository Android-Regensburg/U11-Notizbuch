package de.ur.mi.android.base.db;

import android.provider.BaseColumns;

/**
 * Class storing the table layout for a history entry.
 */
public class NoteDBContract {

   // To prevent someone from accidentally instantiating the contract class,
   // give it an empty constructor.
   public NoteDBContract() {
   }

   /* Inner class that defines the table contents */
   public abstract class NoteEntry implements BaseColumns {
      public static final String TABLE_NAME = "noteTable";
      public static final String COLUMN_NAME_DATE = "date";
      public static final String COLUMN_NAME_TEXT = "text";
      public static final String COLUMN_NAME_TITLE = "title";
   }

}
