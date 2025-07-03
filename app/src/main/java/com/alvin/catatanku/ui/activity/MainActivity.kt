package com.alvin.catatanku.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alvin.catatanku.R
import com.alvin.catatanku.data.NoteModel
import com.alvin.catatanku.data.SubmitModel
import com.alvin.catatanku.service.ApiRetrofit
import com.alvin.catatanku.ui.activity.CreateActivity
import com.alvin.catatanku.ui.adapter.NoteAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }

    private lateinit var rvNotes: RecyclerView
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupListener()
        setupAdapter()

    }

    override fun onStart() {
        super.onStart()
        getNote()
    }

    private fun setupView() {
        rvNotes = findViewById(R.id.rv_notes)
        fabCreate = findViewById(R.id.fab_create)
    }

    private fun setupListener() {
        fabCreate.setOnClickListener {
            Intent(this, CreateActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun setupAdapter() {
        noteAdapter = NoteAdapter(mutableListOf(), object : NoteAdapter.OnAdapterListener {
            override fun onUpdate(note: NoteModel.Data) {
                startActivity(
                    Intent(this@MainActivity, UpdateActivity::class.java)
                        .putExtra("note", note)
                )
            }
            override fun onDelete(note: NoteModel.Data) {
                deleteNote(id = note.id!!)
            }

        })
        rvNotes.adapter = noteAdapter
    }

    private fun getNote() {
        api.data().enqueue(object : Callback<NoteModel> {
            override fun onResponse(
                call: Call<NoteModel?>,
                response: Response<NoteModel?>
            ) {
                if (response.isSuccessful) {
                    Log.e(this@MainActivity.localClassName, response.toString())
                    val listData = response.body()!!.notes
                    noteAdapter.setData(listData)
                }
            }

            override fun onFailure(
                call: Call<NoteModel?>,
                t: Throwable
            ) {
                Log.e(this@MainActivity.localClassName, t.toString())
            }
        })
    }

    private fun deleteNote(id: String) {
        api.delete(id = id)
            .enqueue(object : Callback<SubmitModel> {
                override fun onResponse(
                    call: Call<SubmitModel?>,
                    response: Response<SubmitModel?>
                ) {
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
                            val submit = response.body()
                            Toast.makeText(applicationContext, submit!!.message, Toast.LENGTH_SHORT).show()
                            getNote()
                        }
                    }
                }
                override fun onFailure(
                    call: Call<SubmitModel?>,
                    t: Throwable
                ) {
                    Log.e(this@MainActivity.localClassName, t.toString())
                }
            })
    }

}