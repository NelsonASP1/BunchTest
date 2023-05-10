package com.example.bunchtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddActivity : AppCompatActivity() {

    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var save: Button
    private lateinit var dbSQL: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        name = findViewById(R.id.textEdit)
        email = findViewById(R.id.textEdit2)
        save = findViewById(R.id.save_data)

        dbSQL = DBHelper(this)

        save.setOnClickListener{
            val name = name.text.toString()
            val email = email.text.toString()
            val savedata = dbSQL.saveuserdata(name,email)
            if (TextUtils.isEmpty(name) ||TextUtils.isEmpty(email)){
                Toast.makeText(this, "Add Name & Email", Toast.LENGTH_SHORT).show()
            }
            else{
                if (savedata == true){
                    Toast.makeText(this, "Save Contact", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Exist Contact", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}