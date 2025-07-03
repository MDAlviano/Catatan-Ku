package com.alvin.catatanku.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvin.catatanku.R
import com.alvin.catatanku.data.NoteModel
import com.alvin.catatanku.data.SubmitModel
import com.alvin.catatanku.service.ApiRetrofit
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }
    private val note by lazy { intent.getSerializableExtra("note") as NoteModel.Data }

    private lateinit var etNote: EditText
    private lateinit var bUpdate: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        supportActionBar!!.title = "Edit Catatan"

        setupView()
        setupListener()

    }

    private fun setupView() {
        etNote = findViewById(R.id.et_edit_note)
        bUpdate = findViewById(R.id.b_update)
        etNote.setText( note.note )
    }

    private fun setupListener() {
        bUpdate.setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        api.update(id = note.id!!, note = etNote.text.toString())
            .enqueue(object : Callback<SubmitModel> {
                override fun onResponse(
                    call: Call<SubmitModel?>,
                    response: Response<SubmitModel?>
                ) {
                    if (response.isSuccessful) {
                        val submit = response.body()
                        Toast.makeText(applicationContext, submit!!.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(
                    call: Call<SubmitModel?>,
                    t: Throwable
                ) {
                    Log.e(this@UpdateActivity.localClassName, t.toString())
                }

            })
    }

}