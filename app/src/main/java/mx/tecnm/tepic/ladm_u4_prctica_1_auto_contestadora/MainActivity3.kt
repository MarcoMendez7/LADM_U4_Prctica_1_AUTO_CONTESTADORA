package mx.tecnm.tepic.ladm_u4_prctica_1_auto_contestadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main3.*

class MainActivity3 : AppCompatActivity() {
    var baseremota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()
    var listaID = ArrayList<String>()
    var datalista2 = ArrayList<String>()
    var listaID2 = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        consultar()
        save.setOnClickListener {
            insertar()
            consultar()

        }
    }
private fun consultar (){
    baseremota.collection("mensaje").document("1").get()
        .addOnSuccessListener {

            datalista.clear()
            listaID.clear()
            var cadena = " ${it.get("1") as String?} "
            datalista.add(cadena)

            listaID.add(it.id.toString())


            mensajesi.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                datalista
            )


        }

    baseremota.collection("mensaje").document("2").get()
        .addOnSuccessListener {

            datalista2.clear()
            listaID2.clear()
            var cadena = " ${it.get("2") as String?} "
            datalista2.add(cadena)

            listaID2.add(it.id.toString())


            mensajeno.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                datalista2
            )


        }

}

    private fun insertar() {
        var ubicacion="";
        if(si.isChecked){
            ubicacion="1"
        }else{
            ubicacion="2"
        }
        baseremota.collection("mensaje")
            .document(ubicacion)
            .update(ubicacion, mensaje.text.toString())
    }


    private fun alerta(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK") { d, i -> }
            .show()
    }
}
