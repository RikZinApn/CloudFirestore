package com.example.provafiresotre


import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        val edtNome: EditText = findViewById(R.id.edtNome)
        val edtEndereco: EditText = findViewById(R.id.edtEndereco)
        val edtBairro: EditText = findViewById(R.id.edtBairro)
        val edtCep: EditText = findViewById(R.id.edtCep)
        val btnCadastrar: Button = findViewById(R.id.btnCadastro)
        val btnLer: Button = findViewById(R.id.btnListar)
        val txtResult: EditText = findViewById(R.id.txtResult)

        btnLer.setOnClickListener{
            val intent = Intent(this, ListaDados::class.java)
            startActivity(intent)
            txtResult.setText(null)
        }

        btnCadastrar.setOnClickListener {
            // Create a new user with a first and last name
            if (edtNome.text.isEmpty() || edtEndereco.text.isEmpty() || edtBairro.text.isEmpty() || edtCep.text.isEmpty()){
                Toast.makeText(this, "Insira os Dados!", Toast.LENGTH_LONG).show()
            }else{
                val user = hashMapOf(
                    "nome" to edtNome.text.toString(),
                    "endereco" to edtEndereco.text.toString(),
                    "bairro" to edtBairro.text.toString(),
                    "cep" to edtCep.text.toString()
                )

                // Add a new document with a generated ID
                db.collection("cadastro")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        txtResult.setText("Guarde seu Id: " + documentReference.id)
                        edtNome.setText(null)
                        edtEndereco.setText(null)
                        edtBairro.setText(null)
                        edtCep.setText(null)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }
        }

    }
}