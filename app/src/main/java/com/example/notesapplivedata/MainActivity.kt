package com.example.notesapplivedata

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class MainActivity : AppCompatActivity() {

    private val myViewModel by lazy { ViewModelProvider(this).get(ViewModel::class.java) }
    lateinit var rvNotes: RecyclerView
    lateinit var edtNote: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.getnotes().observe(this){
            updtRC(it)
        }

        edtNote=findViewById(R.id.edtNote)
        rvNotes=findViewById(R.id.rvNotes)
        var subBtn=findViewById(R.id.submBtn) as Button

        subBtn.setOnClickListener {
            val nota = edtNote.text.toString()
            if (nota.isNotEmpty()){
                myViewModel.addNote(Notes(null,nota))
                Toast.makeText(this, "note added successfully", Toast.LENGTH_LONG).show()
                edtNote.text.clear()
                edtNote.clearFocus()
            }
            else{
                Toast.makeText(this,"please add note first", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updtRC(lsNote : List<Notes> ){
        rvNotes.adapter = RVAdapter(this,lsNote)
        rvNotes.layoutManager = LinearLayoutManager(this)
    }


    fun DialogEdit(note : Notes){
        val dialogBuilder = AlertDialog.Builder(this)
        val newNote = EditText(this)
        newNote.hint = "Enter new note"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    dialog, id->
                myViewModel.updtNote(Notes(note.id,newNote.text.toString()))
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(newNote)
        alert.show()
    }

    fun DialogDel(note : Notes ){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure?")
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id-> myViewModel.delNote(note)

            })
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Delete Note")
        alert.show()
    }
}