package com.example.parentsconnect

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.database.getBlobOrNull
import androidx.core.database.getStringOrNull
import java.io.ByteArrayOutputStream
import java.sql.Blob

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

//    private var dataChangeListener: DataChangeListener? = null
//
//    fun setDataChangeListener(listener: DataChangeListener) {
//        this.dataChangeListener = listener
//    }

    companion object{
        // below is variable for database name
        private const val DATABASE_NAME = "PARENTS_CONNECT_TWO"

        // below is the variable for database version
        private const val DATABASE_VERSION = 1

        // below is the variable for table name
        const val TABLE_USERS = "USERS"
        const val TABLE_PARENTS = "PARENTS"
        const val TABLE_TEACHERS = "TEACHERS"
        const val TABLE_STUDENTS = "STUDENTS"
        const val TABLE_TEACHING = "TEACHING_DETAILS"
        const val TABLE_PROGRESS = "PROGRESS_POSTS"
        const val TABLE_CHAT = "CHAT_MESSAGES"
        const val TABLE_NOTICES = "NOTICES"
        const val TABLE_FEE = "FEE"
        const val TABLE_FEE_DETAILS = "FEE_DETAILS"
    }

    override fun onCreate(db: SQLiteDatabase) {

        // TABLE `USERS` CREATION

        val query = ("CREATE TABLE " + TABLE_USERS + " (" +
                "ID         INTEGER PRIMARY KEY," +
                "EMAIL      VARCHAR(40)," +
                "PASSWORD   VARCHAR(20), " +
                "FNAME      VARCHAR(30)," +
                "LNAME      VARCHAR(30)," +
                "IMAGE      BLOB" +
//                "IMAGE_URI  VARCHAR(200)" +
                ")")
        db.execSQL(query)

        val values = ContentValues()
        values.put("EMAIL", "admin@gmail.com")
        values.put("PASSWORD", "admin123")
        values.put("FNAME", "Admin")
        values.put("LNAME", "Admin")
        db.insert(TABLE_USERS, null, values)

        // TABLE `TEACHERS`

        val createTeachers = ("CREATE TABLE " + TABLE_TEACHERS + " (" +
                "ID      INTEGER PRIMARY KEY," +
                "FOREIGN KEY (ID) REFERENCES USERS (ID) ON DELETE CASCADE" +
                ")")
        db.execSQL(createTeachers)

        // TABLE `STUDENTS`

        val createStudents = ("CREATE TABLE " + TABLE_STUDENTS + " (" +
                "ID     INTEGER PRIMARY KEY," +
                "EMAIL  VARCHAR(40)," +
                "FNAME  VARCHAR(20)," +
                "LNAME  VARCHAR(20)," +
                "CLASS  VARCHAR(10)" +
                ")")
        db.execSQL(createStudents)

        // TABLE `PARENTS`

        val createParents = ("CREATE TABLE " + TABLE_PARENTS + " (" +
                "ID             INTEGER PRIMARY KEY," +
                "USER_ID        INTEGER," +
                "STUDENT_ID     INTEGER," +
                "FOREIGN KEY (STUDENT_ID) REFERENCES STUDENTS (ID), " +
                "FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE" +
                ")")
        db.execSQL(createParents)

        // TABLE `TEACHING_DETAILS`

        val createTeaching = ("CREATE TABLE " + TABLE_TEACHING + " (" +
                "TEACHER_ID     INTEGER," +
                "CLASS          VARCHAR(10)," +
                "FOREIGN KEY (TEACHER_ID) REFERENCES TEACHERS (ID) ON DELETE CASCADE," +
                "FOREIGN KEY (CLASS) REFERENCES STUDENTS (CLASS)" +
                ")")
        db.execSQL(createTeaching)

        // TABLE `PROGRESS_POSTS`

        val createProgress = ("CREATE TABLE " + TABLE_PROGRESS + " (" +
                "ID             INTEGER PRIMARY KEY," +
                "TEACHER_ID     INTEGER," +
                "CLASS          VARCHAR(10)," +
                "MESSAGE        VARCHAR(500)" +
                ")")
        db.execSQL(createProgress)

        // TABLE `CHAT_MESSAGES`

//        val createChat = ("CREATE TABLE " + TABLE_CHAT + " (" +
//                "ID             INTEGER PRIMARY KEY," +
//                "SENDER_ID      INTEGER," +
//                "RECEIVER_ID    INTEGER," +
//                "MESSAGE        VARCHAR(500)," +
//                "IS_READ        BOOLEAN," +
//                "SEND_DATE      DATE," +
//                "SEND_HOUR      INTEGER," +
//                "SEND_MINUTE    INTEGER," +
//                "FOREIGN KEY (SENDER_ID) REFERENCES USERS (ID)," +
//                "FOREIGN KEY (RECEIVER_ID) REFERENCES USERS (ID)" +
//                ")")
        val createChat = ("CREATE TABLE " + TABLE_CHAT + " (" +
                "ID             INTEGER PRIMARY KEY," +
                "SENDER_ID      INTEGER," +
                "RECEIVER_ID    INTEGER," +
                "MESSAGE        VARCHAR(500)," +
                "IS_READ        BOOLEAN," +
                "FOREIGN KEY (SENDER_ID) REFERENCES USERS (ID)," +
                "FOREIGN KEY (RECEIVER_ID) REFERENCES USERS (ID)" +
                ")")
        db.execSQL(createChat)

        // TABLE `NOTICES`

        val createNotice = ("CREATE TABLE " + TABLE_NOTICES + " (" +
                "ID                     INTEGER PRIMARY KEY," +
                "TITLE                  VARCHAR(40)," +
                "MESSAGE                VARCHAR(500)," +
                "IS_IMPORTANT           BOOLEAN," +
                "DOCUMENT_URI           VARCHAR(200)" +
                ")")
        db.execSQL(createNotice)

        // TABLE `FEE`

        val createFee = ("CREATE TABLE " + TABLE_FEE + " (" +
                "ID             INTEGER PRIMARY KEY," +
                "CLASS          INTEGER," +
                "TUITION        REAL," +
                "DEVELOPMENT    REAL," +
                "MISCELLANEOUS  REAL," +
                "ANNUAL         REAL," +
                "INSURANCE      REAL," +
                "TOTAL          REAL" +
                ")")
        db.execSQL(createFee)

        // TABLE `FEE_DETAILS`

        val createFeeDetails = ("CREATE TABLE " + TABLE_FEE_DETAILS + " (" +
                "ID             INTEGER PRIMARY KEY," +
                "PARENT_ID      INTEGER," +
                "APRIL          BOOLEAN," +
                "MAY            BOOLEAN," +
                "JUNE           BOOLEAN," +
                "JULY           BOOLEAN," +
                "AUGUST         BOOLEAN," +
                "SEPTEMBER      BOOLEAN," +
                "OCTOBER        BOOLEAN," +
                "NOVEMBER       BOOLEAN," +
                "DECEMBER       BOOLEAN," +
                "JANUARY        BOOLEAN," +
                "FEBRUARY       BOOLEAN," +
                "MARCH          BOOLEAN" +
                ")")
        db.execSQL(createFeeDetails)
    }


    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db!!.execSQL("DROP TABLE IF EXISTS " + DBHelper.TABLE_USERS)
        onCreate(db)
    }

    fun addUser(email: String, password: String, fname: String, lname: String, user_type: String) : Boolean {
        val db = this.writableDatabase
        val res: Cursor = db.rawQuery("SELECT * FROM USERS", null)
        val id = res.count + 1

        val values = ContentValues()
        values.put("EMAIL", email)
        values.put("PASSWORD", password)
        values.put("FNAME", fname)
        values.put("LNAME", lname)
        db.insert(TABLE_USERS, null, values)
        if(user_type == "faculty") {
            val values2 = ContentValues()
            values2.put("ID", id)
            db.insert(TABLE_TEACHERS, null, values2)
            return true
        }
        return  false
    }

    fun addFaculty(email: String, password: String, fname: String, lname: String, classes: String) : Boolean {
        val db = this.writableDatabase
        val classesArr = classes.split(" ").toTypedArray()
        val res: Cursor = db.rawQuery("SELECT * FROM USERS", null)
        val id = res.count + 1

        // inserting into users tables
        val values = ContentValues()
        values.put("EMAIL", email)
        values.put("PASSWORD", password)
        values.put("FNAME", fname)
        values.put("LNAME", lname)
        var success = db.insert(TABLE_USERS, null, values)
        if(success.equals(-1)) {
            return false
        }

        // inserting into teachers
        val values2 = ContentValues()
        values2.put("ID", id)
        success = db.insert(TABLE_TEACHERS, null, values2)
        if(success.equals(-1)) {
            return false
        }

        // inserting into teaching_details

        for (x in classesArr) {
            val values3 = ContentValues()
            values3.put("TEACHER_ID", id)
            values3.put("CLASS", x)
            success = db.insert(TABLE_TEACHING,  null, values3)
            if(success.equals(-1)) {
                return false
            }
        }
        return true
    }

    fun addParent(email: String, password: String, fname: String, lname: String, studentEmail: String) : Boolean {
        val db = this.writableDatabase
        val db2 = this.readableDatabase
        val res: Cursor = db.rawQuery("SELECT * FROM USERS", null)
        val id = res.count + 1

        // inserting into users tables
        val values = ContentValues()
        values.put("EMAIL", email)
        values.put("PASSWORD", password)
        values.put("FNAME", fname)
        values.put("LNAME", lname)
        var success = db.insert(TABLE_USERS, null, values)
        if(success.equals(-1)) {
            return false
        }

        // fetching student id

        val query = "SELECT * FROM $TABLE_STUDENTS WHERE EMAIL=?"
        val args = ArrayList<String>()
        args.add(studentEmail)
        val cursor = db2.rawQuery(query, args.toTypedArray())
        cursor.moveToNext()
        val stdId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))

        // inserting into parents

        val values2 = ContentValues()
        values2.put("USER_ID", id)
        values2.put("STUDENT_ID", stdId)
        success = db.insert(TABLE_PARENTS, null, values2)
        if(success.equals(-1)) {
            return false
        }

        // inserting into fee_details
        val values3 = ContentValues()
        values3.put("PARENT_ID", id)
        values3.put("APRIL", false)
        values3.put("MAY", false)
        values3.put("JUNE", false)
        values3.put("JULY", false)
        values3.put("AUGUST", false)
        values3.put("SEPTEMBER", false)
        values3.put("OCTOBER", false)
        values3.put("NOVEMBER", false)
        values3.put("DECEMBER", false)
        values3.put("JANUARY", false)
        values3.put("FEBRUARY", false)
        values3.put("MARCH", false)
        success = db.insert(TABLE_FEE_DETAILS, null, values3)
        if(success.equals(-1)) {
            return false
        }

        return true
    }

    fun addStudent(email: String, fname: String, lname: String, studentClass: String) : Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("EMAIL", email)
        values.put("FNAME", fname)
        values.put("LNAME", lname)
        values.put("CLASS", studentClass)

        val result = db.insert(TABLE_STUDENTS, null, values)
        return !result.equals(-1)
    }

    fun addProgressPost(teacher_id: Int, targetClass: String, message: String) : Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("TEACHER_ID", teacher_id)
        values.put("CLASS", targetClass)
        values.put("MESSAGE", message)

        val result = db.insert(TABLE_PROGRESS, null, values)
        return !result.equals(-1)
    }

    fun addChatMessage(senderId: Int, receiverId: Int, message: String) : Boolean {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("SENDER_ID", senderId)
        values.put("RECEIVER_ID", receiverId)
        values.put("MESSAGE", message)
        values.put("IS_READ", false)

        val result = db.insert(TABLE_CHAT, null, values)
        return !result.equals(-1)
    }

    fun addNotice(title: String, message: String, isImportant: Boolean, documentUri: Uri?) : Boolean  {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("TITLE", title)
        values.put("MESSAGE", message)
        values.put("IS_IMPORTANT", isImportant)
        if(documentUri == null) {
            values.putNull("DOCUMENT_URI")
        } else {
            values.put("DOCUMENT_URI", documentUri.toString())
        }

        val result = db.insert(TABLE_NOTICES, null, values)
        return !result.equals(-1)
    }

    fun addFeeDetails(targetClass: Int, tuition: Float, development: Float, misc: Float, annual: Float, insurance: Float) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("CLASS", targetClass)
        values.put("TUITION", tuition)
        values.put("DEVELOPMENT", development)
        values.put("MISCELLANEOUS", misc)
        values.put("ANNUAL", annual)
        values.put("INSURANCE", insurance)
        val res = db.insert(TABLE_FEE, null, values)
        return !res.equals(-1)
    }

    fun checkUser(email: String, password: String) : Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE EMAIL=? and PASSWORD=?"
        val db = this.readableDatabase
        val arguments = ArrayList<String>()
        arguments.add(email)
        arguments.add(password)
        val cursor = db.rawQuery(query, arguments.toTypedArray())
        if(cursor.count == 0) {
            return false
        }
        return true
    }

    fun checkEmail(email: String) : Boolean {
        val query = "SELECT * FROM $TABLE_USERS WHERE EMAIL=?"
        val db = this.readableDatabase

        val cursor = db.rawQuery(query, arrayOf(email))
        if(cursor.count == 0) {
            return false
        }
        return true
    }

    fun checkClassInFee(targetClass: Int) : Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_FEE WHERE CLASS=?"
        val cursor = db.rawQuery(query, arrayOf(targetClass.toString()))
        if(cursor.count > 0) {
            return true
        }
        return false
    }

    fun getUserType(email: String, password: String) : String {

        // fetching id of user
        var query = "SELECT * FROM $TABLE_USERS WHERE EMAIL=? AND PASSWORD=?"
        val db = this.readableDatabase
        val args = ArrayList<String>()
        args.add(email)
        args.add(password)
        val cursor = db.rawQuery(query, args.toTypedArray())
        cursor.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))

        // checking if user is faculty
        query = "SELECT * FROM $TABLE_TEACHERS WHERE ID=?"
        val cursor2 = db.rawQuery(query, arrayOf(id.toString()))
        if(cursor2.count != 0) {
            return "teacher"
        }

        // checking if user is parent
        query = "SELECT * FROM $TABLE_PARENTS WHERE USER_ID=?"
        val cursor3 = db.rawQuery(query, arrayOf(id.toString()))
        if(cursor3.count != 0) {
            return "parent"
        }
        return "admin"
    }

    fun getUserId(email: String) : Int {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE EMAIL=?"
        val cursor = db.rawQuery(query, arrayOf(email))
        cursor.moveToNext()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
        return id
    }

    fun getUserName(id: Int) : String {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE ID=?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToNext()
        val fname = cursor.getString(cursor.getColumnIndexOrThrow("FNAME"))
        val lname = cursor.getString(cursor.getColumnIndexOrThrow("LNAME"))
        return "$fname $lname"
    }

    fun getEmail(id: Int) : String {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE ID=?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToNext()
        return cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"))
    }

    fun getPassword(id: Int) : String {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE ID=?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToNext()
        return cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"))
    }

    fun getStudentClass(id: Int) : String {
        val db = this.readableDatabase
        val query = "SELECT STUDENTS.* FROM $TABLE_PARENTS " +
                "JOIN $TABLE_STUDENTS ON PARENTS.STUDENT_ID = STUDENTS.ID " +
                "WHERE PARENTS.USER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToNext()
        val student_class = cursor.getString(cursor.getColumnIndexOrThrow("CLASS"))
        return student_class
    }

    fun getStudentName(id: Int) : String {
        val db = this.readableDatabase
        val query = "SELECT STUDENTS.* FROM $TABLE_PARENTS " +
                "JOIN $TABLE_STUDENTS ON PARENTS.STUDENT_ID = STUDENTS.ID " +
                "WHERE PARENTS.USER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        cursor.moveToNext()
        val studentFname = cursor.getString(cursor.getColumnIndexOrThrow("FNAME"))
        val studentLname = cursor.getString(cursor.getColumnIndexOrThrow("LNAME"))
        return "$studentFname $studentLname"
    }

    fun getAllNotices() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NOTICES"
        val cursor = db.rawQuery(query, null)
        return cursor
    }

    fun getTeacherClasses(teacher_id: Int) : ArrayList<String> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TEACHING WHERE TEACHER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(teacher_id.toString()))
        val classes = ArrayList<String>()
        while(cursor.moveToNext()) {
            classes.add(cursor.getString(cursor.getColumnIndexOrThrow("CLASS")))
        }
        return classes
    }

    fun getAllParents() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT USERS.* FROM $TABLE_PARENTS " +
                "JOIN $TABLE_USERS ON PARENTS.USER_ID = USERS.ID"
        return db.rawQuery(query, null)
    }

    fun getAllTeachers() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT USERS.* FROM $TABLE_TEACHERS " +
                "JOIN $TABLE_USERS ON TEACHERS.ID = USERS.ID"
        return db.rawQuery(query, null)
    }

    fun getParentList(teacher_id: Int) : Cursor {
        val db = this.readableDatabase
        var query = "SELECT STUDENTS.ID FROM $TABLE_TEACHING " +
                "JOIN $TABLE_STUDENTS ON TEACHING_DETAILS.CLASS = STUDENTS.CLASS " +
                "WHERE TEACHING_DETAILS.TEACHER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(teacher_id.toString()))
        val studentIdsList = mutableListOf<Int>()
        while(cursor.moveToNext()) {
            studentIdsList.add(cursor.getInt(cursor.getColumnIndexOrThrow("ID")))
        }
        val studentList = studentIdsList.toIntArray()
        val studentIdStr = studentList.joinToString(",")
        query = "SELECT USERS.* " +
                "FROM $TABLE_PARENTS " +
                "JOIN $TABLE_STUDENTS ON PARENTS.STUDENT_ID = STUDENTS.ID " +
                "JOIN $TABLE_USERS ON PARENTS.USER_ID = USERS.ID " +
                "WHERE STUDENTS.ID IN ($studentIdStr)"

        return db.rawQuery(query, null)
    }

    fun getUnreadParents(teacher_id: Int) : ArrayList<Int> {
        val parentListCursor = getParentList(teacher_id)
        val result = ArrayList<Int>()
        val db = this.readableDatabase
        if(parentListCursor.moveToNext()) {
            val parentId = parentListCursor.getInt(parentListCursor.getColumnIndexOrThrow("ID"))
            val query = "SELECT * FROM $TABLE_CHAT " +
                    "WHERE RECEIVER_ID=? AND SENDER_ID=? AND IS_READ=?"
            val cursor = db.rawQuery(query, arrayOf(teacher_id.toString(), parentId.toString(), "0"))
            if(cursor.count > 0) {
                result.add(parentId)
            }
        }
        return result
    }

    fun getTeacherList(parent_id: Int) : Cursor {
        val db = this.readableDatabase
        val joinQuery = "SELECT STUDENTS.CLASS FROM STUDENTS " +
                "WHERE PARENTS.USER_ID=? " +
                "JOIN PARENTS ON STUDENTS.ID = PARENTS.STUDENT_ID"
        var query = "SELECT * FROM $TABLE_PARENTS WHERE USER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(parent_id.toString()))
        cursor.moveToNext()
        val student_id = cursor.getInt(cursor.getColumnIndexOrThrow("STUDENT_ID"))
        query = "SELECT * FROM $TABLE_STUDENTS WHERE ID=?"
        val cursor2 = db.rawQuery(query, arrayOf(student_id.toString()))
        cursor2.moveToNext()
        val studentClass = cursor2.getString(cursor2.getColumnIndexOrThrow("CLASS"))
        query = "SELECT USERS.* FROM $TABLE_TEACHING " +
                "JOIN $TABLE_USERS ON USERS.ID = TEACHING_DETAILS.TEACHER_ID " +
                "WHERE TEACHING_DETAILS.CLASS=?"
        val cursor3 = db.rawQuery(query, arrayOf(studentClass))
        return cursor3
    }

    fun getUnreadTeachers(parent_id: Int) : ArrayList<Int> {
        val teacherListCursor = getTeacherList(parent_id)
        val result = ArrayList<Int>()
        val db = this.readableDatabase
        if(teacherListCursor.moveToNext()) {
            val teacherId = teacherListCursor.getInt(teacherListCursor.getColumnIndexOrThrow("ID"))
            val query = "SELECT * FROM $TABLE_CHAT " +
                    "WHERE RECEIVER_ID=? AND SENDER_ID=? AND IS_READ=?"
            val cursor = db.rawQuery(query, arrayOf(parent_id.toString(), teacherId.toString(), "0"))
            if(cursor.count > 0) {
                result.add(teacherId)
            }
        }
        return result
    }

    fun getProgressPosts(parent_id: Int) : Cursor {
        val db = this.readableDatabase
        var query = "SELECT * FROM $TABLE_PARENTS WHERE USER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(parent_id.toString()))
        cursor.moveToNext()
        val studentId = cursor.getInt(cursor.getColumnIndexOrThrow("STUDENT_ID"))
        query = "SELECT * FROM $TABLE_STUDENTS WHERE ID=?"
        val cursor2 = db.rawQuery(query, arrayOf(studentId.toString()))
        cursor2.moveToNext()
        val studentClass = cursor2.getString(cursor2.getColumnIndexOrThrow("CLASS"))
        query = "SELECT * FROM $TABLE_PROGRESS WHERE CLASS=?"
        val cursor3 = db.rawQuery(query, arrayOf(studentClass))
        return cursor3
    }

//    val values = ContentValues()
//    values.put("PASSWORD", password)
//
//    val whereClause = "id=?"
//    db.update(TABLE_USERS, values, whereClause, arrayOf(userId.toString()))

    fun markAllChatsRead(senderId: Int, receiverId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("IS_READ", true)
        val whereClaus = "SENDER_ID=? AND RECEIVER_ID=? AND IS_READ=?"
        db.update(TABLE_CHAT, values, whereClaus, arrayOf(senderId.toString(), receiverId.toString(), "0"))
    }

    fun getProgressPostsFaculty(teacher_id: Int) : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PROGRESS WHERE TEACHER_ID=?"
        val cursor = db.rawQuery(query, arrayOf(teacher_id.toString()))
        return cursor
    }

    fun getMessageList(senderId: Int, receiverId: Int): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_CHAT WHERE (SENDER_ID=? AND RECEIVER_ID=?) OR (SENDER_ID=? AND RECEIVER_ID=?)"
        val cursor = db.rawQuery(query, arrayOf(senderId.toString(), receiverId.toString(), receiverId.toString(), senderId.toString()))
        return cursor
    }

    fun getImage(userId: Int) : ByteArray? {
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_USERS WHERE ID=?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        cursor.moveToNext()
        return cursor.getBlobOrNull(cursor.getColumnIndexOrThrow("IMAGE"))
    }

    fun getFeeDetailsForClass(targetClass: Int) : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_FEE WHERE CLASS=?"
        return db.rawQuery(query, arrayOf(targetClass.toString()))
    }

    fun getMonthsTrue(userId: Int) : ArrayList<String> {
        val db = this.readableDatabase
        val result = ArrayList<String>()
        val query = "SELECT * FROM $TABLE_FEE_DETAILS WHERE PARENT_ID=?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        if(cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndexOrThrow("APRIL")) == 1) {
                result.add("APRIL")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("MAY")) == 1) {
                result.add("MAY")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("JUNE")) == 1) {
                result.add("JUNE")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("JULY")) == 1) {
                result.add("JULY")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("AUGUST")) == 1) {
                result.add("AUGUST")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("SEPTEMBER")) == 1) {
                result.add("SEPTEMBER")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("OCTOBER")) == 1) {
                result.add("OCTOBER")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("NOVEMBER")) == 1) {
                result.add("NOVEMBER")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("DECEMBER")) == 1) {
                result.add("DECEMBER")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("JANUARY")) == 1) {
                result.add("JANUARY")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("FEBRUARY")) == 1) {
                result.add("FEBRUARY")
            }
            if(cursor.getInt(cursor.getColumnIndexOrThrow("MARCH")) == 1) {
                result.add("MARCH")
            }
        }
        return result
    }

    fun updateUserName(userId: Int, firstName: String, lastName: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("FNAME", firstName)
        values.put("LNAME", lastName)

        val whereClause = "id=?"

        db.update(TABLE_USERS, values, whereClause, arrayOf(userId.toString()))
    }

    fun updatePassword(userId: Int, password: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("PASSWORD", password)

        val whereClause = "id=?"
        db.update(TABLE_USERS, values, whereClause, arrayOf(userId.toString()))
    }

    fun paymentMade(months: ArrayList<String>, parentId: Int) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        for(month in months) {
            values.put("${month.toUpperCase()}", true)
        }
        val whereClause = "PARENT_ID=?"
        val res = db.update(TABLE_FEE_DETAILS, values, whereClause, arrayOf(parentId.toString()))
        return !res.equals(-1)
    }

    fun insertImage(userId: Int, image: Bitmap) {
        val db = this.writableDatabase
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val values = ContentValues()
        values.put("IMAGE", byteArray)

        val whereClause = "ID=?"
        db.update(TABLE_USERS, values, whereClause, arrayOf(userId.toString()))
    }

    fun removeImage(userId: Int) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.putNull("IMAGE")
        val whereClause = "ID=?"
        return db.update(TABLE_USERS, values, whereClause, arrayOf(userId.toString())) > 0
    }

    fun deleteUser(email: String) : Boolean {
        val db = this.writableDatabase

        val userId = getUserId(email)

        var whereClaus = "ID=?"
        val whereArgs = arrayOf(userId.toString())
        val res = db.delete(TABLE_USERS, whereClaus, whereArgs)
        val res2 = db.delete(TABLE_TEACHERS, whereClaus, whereArgs)

        whereClaus = "USER_ID=?"
        val res3 = db.delete(TABLE_PARENTS, whereClaus, whereArgs)

        whereClaus = "TEACHER_ID=?"
        val res4 = db.delete(TABLE_TEACHING, whereClaus, whereArgs)

        if(res > 0 && res2 > 0 && res3 > 0 && res4 > 0)
            return true
        return false
    }

}