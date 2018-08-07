package lt.kaunascoding.myapplication.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DBActions extends SQLiteOpenHelper {
    public static final String DB_NAME = "BookcampTodo";
    public static final int DB_VERSION = 1;

    public DBActions(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createTable = "CREATE TABLE " + TaskTable.TABLE + " ( " +
                TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskTable.COL_TASK_DONE_ + " INTEGER DEFAULT 0," +
                TaskTable.COL_TASK_TITLE_ + " TEXT NOT NULL" +
                ");";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskTable.TABLE);
        onCreate(db);
    }

    public ArrayList<ItemVO> getAllItems() {
        ArrayList<ItemVO> result = new ArrayList<ItemVO>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TaskTable.TABLE + ";", null);

        while (cursor.moveToNext()) {

            int idIndex = cursor.getColumnIndex(TaskTable._ID);
            int doneIndex = cursor.getColumnIndex(TaskTable.COL_TASK_DONE_);
            int titleIndex = cursor.getColumnIndex(TaskTable.COL_TASK_TITLE_);

            ItemVO itemVo = new ItemVO();
            itemVo.id = cursor.getInt(idIndex);
            itemVo.done = cursor.getInt(doneIndex);
            itemVo.title = cursor.getString(titleIndex);

            System.out.println(itemVo.id + ":" + itemVo.done + ":" + itemVo.title);
            result.add(itemVo);

        }


        cursor.close();
        db.close();
        return result;
    }

    public void addItem(String task) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskTable.COL_TASK_TITLE_, task);

        db.insertWithOnConflict(TaskTable.TABLE,null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();

    }


    public void deleteItem(ItemVO itemVO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskTable.TABLE,
                TaskTable._ID + " = ?",
                new String[] {Integer.toString(itemVO.id)});
        db.close();
    }

    public void updateItem(ItemVO itemVO) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTable.COL_TASK_DONE_, itemVO.done);
        SQLiteDatabase db = this.getWritableDatabase();
        db.updateWithOnConflict(TaskTable.TABLE,
                contentValues,
                TaskTable._ID + " = ?",
                new String[] {Integer.toString(itemVO.id)},
                SQLiteDatabase.CONFLICT_REPLACE );
        db.close();
    }


}

class TaskTable implements BaseColumns {

    public static final String TABLE = "tasks";
    public static final String COL_TASK_DONE_ = "done";
    public static final String COL_TASK_TITLE_ = "title";
}