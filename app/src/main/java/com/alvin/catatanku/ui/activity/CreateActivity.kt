package com.alvin.catatanku.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvin.catatanku.R
import com.alvin.catatanku.data.SubmitModel
import com.alvin.catatanku.service.ApiRetrofit
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateActivity : AppCompatActivity() {

    private val api by lazy { ApiRetrofit().endpoint }

    private lateinit var etNote: EditText
    private lateinit var bSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        supportActionBar!!.title = "Catatan Baru"

        setupView()
        setupListener()

    }

    private fun setupView() {
        etNote = findViewById(R.id.et_note)
        bSave = findViewById(R.id.b_save)
    }

    private fun setupListener() {
        bSave.setOnClickListener {
            val etValue = etNote.text.toString()
            if (etValue.isNotEmpty()) {
                createNote(
                    note = etValue
                )
            } else {
                Toast.makeText(this, "Mohon isi kolom yang tersedia!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNote(note: String) {
        api.create(note = note)
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
                    Log.e(this@CreateActivity.localClassName, t.toString())
                }

            })
    }

}
