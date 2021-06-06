package mx.tecnm.tepic.ladm_u4_prctica_1_auto_contestadora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.CallLog
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class llamada : BroadcastReceiver() {
    private var mTelephonyManager: TelephonyManager? = null
    var baseremota = FirebaseFirestore.getInstance()

    override fun onReceive(context: Context, intent: Intent) {
        mTelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val mPhoneStateListener: PhoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(
                state: Int,
                incomingNumber: String
            ) {

                super.onCallStateChanged(state, incomingNumber)

                when (state) {

                    TelephonyManager.CALL_STATE_RINGING -> {
                        var baseDatos=BaseDatos(context,"ENTRANTE",null,1)
                        var insertar=baseDatos.writableDatabase

                        var SQL="INSERT INTO ENTRANTE VALUES('${incomingNumber.substring(3)}')"
                        insertar.execSQL(SQL)
                        baseDatos.close()

                        baseremota.collection("numero").document(incomingNumber.substring(3)).get()
                            .addOnSuccessListener {
                                var cadena= "${it.get("tipo")as String?}"

                                baseremota.collection("mensaje").document(cadena).get()
                                    .addOnSuccessListener {
                                        var cadena1= "${it.get("tipo")as String?}"
                                        SmsManager.getDefault().sendTextMessage(incomingNumber.substring(3),null,cadena1,null,null)
                                        Toast.makeText(context,"se envio el mensaje",Toast.LENGTH_LONG)
                                            .show()
                                    }
                            }


                    }
                }
            }
        }
        if (!isListening) {
            mTelephonyManager!!.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
            isListening = true
        }

    }

    companion object {
        var isListening = false
    }
}