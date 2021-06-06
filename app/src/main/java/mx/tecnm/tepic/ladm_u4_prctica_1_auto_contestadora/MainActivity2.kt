package mx.tecnm.tepic.ladm_u4_prctica_1_auto_contestadora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var baseremota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var datalista2 = ArrayList<String>()
    var listaID2 = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        baseremota.collection("numero")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }

                datalista.clear()
                listaID.clear()

                for (document in querySnapshot!!) {
                    var cadena = " ${document.get("tipo")}--${document.get("telefono")}"
                    datalista.add(cadena)

                    listaID.add(document.id.toString())
                }

                listapersonas.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_expandable_list_item_1,
                    datalista
                )
                listapersonas.setOnItemClickListener { parent, view, posicion, i ->
                    dialogoEliminar(posicion)
                }
            }

        save.setOnClickListener {
            insertar()
        }
            }
    private fun dialogoEliminar(posicion: Int){
        var idElegido = listaID.get(posicion)

        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("¿QUE DESEAS HACER CON\n${datalista.get(posicion)}?n${idElegido}?")
            .setPositiveButton("ELIMINAR"){d, i->
                eliminar(idElegido)
            }

            .setNegativeButton("CANCELAR"){d,i->}
            .show()
    }

    private fun eliminar(idElegido: String) {
        baseremota.collection("deseados")
            .document(idElegido)
            .delete()
            .addOnSuccessListener {
                alerta("SE ELIMINO EL DOCUMENTO")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message!!}")
            }
    }

    private fun insertar() {
        //para insertar el metodo a usar es ADD
        //ADD espera todos los campos del documento
        //Con formato CLAVE VALOR

        var datosInsertar = hashMapOf(
            "tipo" to "1",
            "telefono" to celular.text.toString()

        )
        var datosInsertar2 = hashMapOf(
            "tipo" to "2",
            "telefono" to celular.text.toString()

        )
        if(si.isChecked) {
            baseremota.collection("numero").document(celular.text.toString())
                .set(datosInsertar as Any)
                .addOnSuccessListener {
                    alerta("SE INSERTO CORRECTAMENTE EN LA NUBE")
                }
                .addOnFailureListener {
                    mensaje("ERROR: ${it.message!!}")
                }
        }else{
            baseremota.collection("numero").document(celular.text.toString())
                .set(datosInsertar2 as Any)
                .addOnSuccessListener {
                    alerta("SE INSERTO CORRECTAMENTE EN LA NUBE")
                }
                .addOnFailureListener {
                    mensaje("ERROR: ${it.message!!}")
                }
        }

        celular.setText("")

    }



    private fun alerta(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show()
    }
    private fun mensaje(s: String){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
}