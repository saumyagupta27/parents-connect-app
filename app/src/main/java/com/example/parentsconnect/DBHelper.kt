package com.example.parentsconnect

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        // below is variable for database name
        private const val DATABASE_NAME = "PARENTS_CONNECT"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variable for table name
        const val TABLE_USERS = "users"
        const val TABLE_PARENTS = "parents"

        const val ID_COL = "id"
        const val USER_TYPE_COL = "user_type"
        const val EMAIL_COL = "email"
        const val PASSWORD_COL = "password"
        const val FNAME_COl = "fname"
        const val LNAME_COl = "lname"
        const val STUDENT_COL = "student"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_USERS + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                USER_TYPE_COL + " TEXT," +
                EMAIL_COL + " TEXT," +
                PASSWORD_COL + " TEXT, " +
                FNAME_COl + " TEXT," +
                LNAME_COl + " TEXT," +
                STUDENT_COL + " TEXT" + ")")
        db.execSQL(query)

        val values = ContentValues()
        values.put(USER_TYPE_COL, "admin")
        values.put(EMAIL_COL, "admin@gmail.com")
        values.put(PASSWORD_COL, "admin123")
        values.put(FNAME_COl, "Admin")
        values.put(LNAME_COl, "Admin")
        values.put(STUDENT_COL, "NULL")
        db.insert(TABLE_USERS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }

    fun addUser(user: UserModel ): Boolean {

        // below we are creating a content values variable
        val values = ContentValues()

        // we are inserting our values in the form of key-value pair
        values.put(USER_TYPE_COL, user.user_type)
        values.put(EMAIL_COL, user.email)
        values.put(PASSWORD_COL, user.password)
        values.put(FNAME_COl, user.fname)
        values.put(LNAME_COl, user.lname)
        values.put(STUDENT_COL, user.student)

        // here we are creating a writable variable of
        // our database as we want to insert value in our database
        val db = this.writableDatabase
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        Log.d("addUser", result.toString())
        return !result.equals(-1)
    }

    fun getUsersCount(): Int {
        val query = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(query, null)
        }catch (e: SQLiteException) {
            db.execSQL(query)
            return -1
        }
        return cursor.count
    }

    fun getAllUsers(): Cursor {
        val query = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase
        return db.rawQuery(query, null)
    }

    fun getUserType(email: String): String {
        val query = "SELECT * FROM $TABLE_USERS WHERE email=?"
        val db = this.readableDatabase
        val arguments = ArrayList<String>()
        arguments.add(email)
        val cursor = db.rawQuery(query, arguments.toTypedArray())
        cursor.moveToNext()
        return cursor.getString(cursor.getColumnIndexOrThrow("user_type"))
    }

    fun checkUser(email: String, password: String) : Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE email=? and password=?"
        val db = this.readableDatabase
        val arguments = ArrayList<String>()
        arguments.add(email)
        arguments.add(password)
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(query, arguments.toTypedArray())
        }catch (e: SQLiteException) {
            db.execSQL(query)
            return false
        }
        if(cursor.count == 0) {
            return false
        }
        return true
    }

    fun checkAdmin(email: String, password: String) : Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE email=? and password=?"
        val db = this.readableDatabase
        val arguments = ArrayList<String>()
        arguments.add(email)
        arguments.add(password)
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(query, arguments.toTypedArray())
            while(cursor.moveToNext()) {
                val type = cursor.getString(1)
                if(type == "admin")
                    return true
                return false
            }
        }catch (e: SQLiteException) {
            db.execSQL(query)
            return false
        }
        return true
    }

    fun checkEmail(email: String) : Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE email=?"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        val arguments = ArrayList<String>()
        arguments.add(email)

        try{
            cursor = db.rawQuery(query, arguments.toTypedArray())
            if(cursor.getCount() == 0) {
                return false
            }
            return true
        }catch (e: SQLiteException) {
            db.execSQL(query)
            return false
        }
    }

    fun deleteUser(email: String): Boolean {
        val arguments = ArrayList<String>()
        arguments.add(email)

        val db = this.writableDatabase
        val result = db.delete(TABLE_USERS, "email=?", arguments.toTypedArray())
        return !result.equals(-1)
    }
}