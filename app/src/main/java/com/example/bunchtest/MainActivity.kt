package com.example.bunchtest

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileWriter
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var button: FloatingActionButton
    private lateinit var button2: FloatingActionButton
    private lateinit var dbh: DBHelper
    private lateinit var newArrayList: ArrayList<DataList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        button = findViewById(R.id.floatingActionButton)
        button2 = findViewById(R.id.floatingActionButton2)

        button2.setOnClickListener { view ->
            checkPermissions(view)
        }

        button.setOnClickListener {
            intent = Intent(this, AddActivity::class.java)
            startActivity(intent)}

        dbh = DBHelper(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayuser()
    }

    private fun displayuser() {
        var newCursor: Cursor? = dbh!!.getData()
        newArrayList = ArrayList<DataList>()
        while (newCursor!!.moveToNext()){
            val uname = newCursor.getString(0)
            val uemail = newCursor.getString(1)
            newArrayList.add(DataList(uname, uemail))
        }
        recyclerView.adapter = Adapter(newArrayList)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isAceptado ->
        if (isAceptado) Toast.makeText(this, "GRANTED PERMITS", Toast.LENGTH_SHORT).show()
        else Toast.makeText(this, "PERMISSIONS DENIED", Toast.LENGTH_SHORT).show()

    }
    private fun checkPermissions(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "GRANTED PERMITS", Toast.LENGTH_SHORT).show()
                    createCsvFile()
                }

                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    Snackbar.make(
                        view,
                        "THIS PERMISSION IS REQUIRED TO CREATE THE CSV FILE",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("OK") {
                        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }.show()
                }

                else -> {
                    requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        } else {
            createCsvFile()
        }
    }

    fun createCsvFile() {
        val files =
            File(Environment.getExternalStorageDirectory().absolutePath + "/bunchTest")
        val principalFile = "$files/Contacts.csv"
        var isCreate = false
        if (!files.exists()) {
            isCreate = files.mkdir()
        }
        try {
            val fileWriter = FileWriter(principalFile)
            val admin = DBHelper(this@MainActivity)
            val db: SQLiteDatabase = admin.getWritableDatabase()
            val fila = db.rawQuery("select * from UserData", null)
            if (fila != null && fila.count != 0) {
                fila.moveToFirst()
                do {
                    fileWriter.append(fila.getString(0))
                    fileWriter.append(",")
                    fileWriter.append(fila.getString(1))
                    fileWriter.append(",")
                    fileWriter.append(fila.getString(2))
                    fileWriter.append("\n")
                } while (fila.moveToNext())
            } else {
                Toast.makeText(this@MainActivity, "NO RECORDS", Toast.LENGTH_LONG).show()
            }
            db.close()
            fileWriter.close()
            Toast.makeText(
                this@MainActivity,
                "CSV FILE CREATED SUCCESSFULLY",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
        }
    }
}