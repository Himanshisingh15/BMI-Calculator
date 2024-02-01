import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.service.autofill.UserData
import com.example.bmicalculator.MainActivity

private val DB_NAME = "BMI"
private val TABLE_NAME = "bmi_Value"
private val DB_VERSION = 1

private val ID = "id"
private val HEIGHT = "fname"
private val WEIGHT = "mname"
private val BMIVALUE = "lname"
private val BMIINFO = "mobile"

class DbAdapter(context: Context) {

    var CREATE_TABLE = "CREATE TABLE $TABLE_NAME($ID INTEGER PRIMARY KEY AUTOINCREMENT, $HEIGHT TEXT, $WEIGHT TEXT, $BMIVALUE TEXT, $BMIINFO TEXT)"

    var myOpenHelper = MyOpenHelper(context)
    var sqLiteDatabase = myOpenHelper.writableDatabase

    fun createData(height: String, weight: String, bmiValue: String, bmiInfo: String){
        var values = ContentValues()
        values.put(HEIGHT, height)
        values.put(WEIGHT, weight)
        values.put(BMIVALUE, bmiValue)
        values.put(BMIINFO, bmiInfo)

        var rowId = sqLiteDatabase.insert(TABLE_NAME, null, values)
        if (rowId > 0){
//            Toast.makeText(this, "Inserted data", Toast.LENGTH_SHORT).show()
        }else{
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchData(): ArrayList<UserData>{
        var dataList = ArrayList<UserData>()
        var cursor = sqLiteDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null, null)
        if (cursor.count > 0) {
            do {
                var id = cursor.getInt(0)
                var height = cursor.getString(1)
                var weight = cursor.getString(2)
                var bmiValue = cursor.getString(3)
                var bmiInfo = cursor.getString(4)
            } while (cursor.moveToNext())
        }else{
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
        }

        return dataList
    }

    inner class MyOpenHelper(context: Context): SQLiteOpenHelper(context,DB_NAME, null, DB_VERSION){
        override fun onCreate(sqliteDB: SQLiteDatabase?) {
            sqliteDB?.execSQL(CREATE_TABLE)
        }

        override fun onUpgrade(sqliteDB: SQLiteDatabase?, p1: Int, p2: Int) {
            TODO("Not yet implemented")
        }
    }

}