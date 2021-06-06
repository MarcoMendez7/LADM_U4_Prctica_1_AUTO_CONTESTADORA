package mx.tecnm.tepic.ladm_u4_prctica_1_auto_contestadora

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var contadorHilo=0
    var contador=0
    var contadorV=0


    var mantener=true
    val siPermiso=1
    var baseremota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()


    private var mTelephonyManager: TelephonyManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), siPermiso)
        }
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),siPermiso)
        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siPermiso)
        }


        var ultimo=""
        try{
            val cursor = BaseDatos(this,"ENTRANTE",null,1)
                .readableDatabase
                .rawQuery("SELECT * FROM ENTRANTE",null)

            if(cursor.moveToFirst()){
                do{
                    ultimo=cursor.getString(0).toString()
                }while(cursor.moveToNext())
            }else{
                ultimo="sin llamada"
            }

        }catch (err:SQLiteException){
            Toast.makeText(this,err.message,Toast.LENGTH_LONG).show()

        }
     save.setOnClickListener {
          /*
            */
            var intent = Intent(this, MainActivity2::class.java)

            startActivity(intent)

        }
        mensaje.setOnClickListener {
            var intent = Intent(this, MainActivity3::class.java)

            startActivity(intent)


        }

    }

    // ...


    // ...
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if(requestCode==siPermiso){

    }

    }




    private fun envioSMS(ultimo: String){









    }
    private fun mensaje(s: String){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->}
            .show()
    }
}