package com.example.provafiresotre

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.common.base.Enums.getField
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ListaDados : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_dados)

        val db = Firebase.firestore

        val edtPesquisa: EditText = findViewById(R.id.edtPesquisa)
        val btnBusca: Button = findViewById(R.id.btnBusca)
        val txtResultNome: TextView = findViewById(R.id.txtResultNome)
        val txtResultId: TextView = findViewById(R.id.txtResultId)
        val txtResultEnd: TextView = findViewById(R.id.txtResultEnd)
        val txtResultBairro: TextView = findViewById(R.id.txtResultBairro)
        val txtResultCep: TextView = findViewById(R.id.txtResultCep)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)
        val btnEditar: Button = findViewById(R.id.btnEditar)
        val btnInicio: Button = findViewById(R.id.btnInicio)

        db.collection("Cadastro")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

        btnBusca.setOnClickListener {
            if (edtPesquisa.text.isNotEmpty()) {
                val docRef = db.collection("cadastro").document(edtPesquisa.text.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                            txtResultId.setText("${document.id}")
                            txtResultNome.setText(" ${document.get("nome")}")
                            txtResultEnd.setText(" ${document.get("endereco")}")
                            txtResultBairro.setText(" ${document.get("bairro")}")
                            txtResultCep.setText("${document.get("cep")}")
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            } else {
                Toast.makeText(this, "Insira um Id para completar a busca!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        btnExcluir.setOnClickListener {

            db.collection("cadastro").document(edtPesquisa.text.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    txtResultId.setText(null)
                    txtResultNome.setText(null)
                    txtResultEnd.setText(null)
                    txtResultBairro.setText(null)
                    txtResultCep.setText(null)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
            Toast.makeText(this, "Deletado com Sucesso!", Toast.LENGTH_SHORT).show()
        }

        btnEditar.setOnClickListener {
            val id = db.collection("cadastro").document(edtPesquisa.text.toString())

            // Set the "isCapital" field of the city 'DC'
            id
                .update(
                    mapOf(
                        "nome" to txtResultNome.text.toString(),
                        "endereco" to txtResultEnd.text.toString(),
                        "bairro" to txtResultBairro.text.toString(),
                        "cep" to txtResultCep.text.toString()
                    )
                )
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            Toast.makeText(this, "Atualizado com Sucesso!", Toast.LENGTH_SHORT).show()
        }
        btnInicio.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
