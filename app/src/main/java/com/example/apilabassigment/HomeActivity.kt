package com.example.apilabassigment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import org.json.JSONObject


class HomeActivity : AppCompatActivity() {
    val TAG = "nn"
    var volleyRequestQueue: RequestQueue? = null
    lateinit var emailText : TextView
    lateinit var passwrodText :TextView
    lateinit var tokenText :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        emailText = this.findViewById(R.id.emailText)
        passwrodText = findViewById(R.id.passwrodText)
        tokenText = findViewById(R.id.tokenText)
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {

            val user = SharedPrefManager.getInstance(this).getUser()

            val email = user.email
            val password = user.password

            Log.e(TAG, " $password $email ")
            emailText.text = email
            passwrodText.text = password





            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this,
                OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                    val token = instanceIdResult.token
                    tokenText.text = token
                    volleyRequestQueue = Volley.newRequestQueue(this)
                    val parameters: MutableMap<String, String> = HashMap()
                    parameters["email"] = email
                    parameters["password"] = password
                    parameters["reg_token"] = token

                    val strReq: StringRequest = object : StringRequest(
                        Method.PUT, URLs.URL_TOKEN,
                        Response.Listener { response ->
                            Log.e(TAG, "response: $response")

                            try {
                                val responseObj = JSONObject(response)
                                Toast.makeText(
                                    applicationContext,
                                    responseObj.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show();


                                val message = responseObj.getString("message")

                                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                                Log.e(TAG, "problem occurred")
                                e.printStackTrace()
                            }
                        },
                        Response.ErrorListener { volleyError ->
                            Log.e(TAG, "problem occurred, volley error: " + volleyError.message)
                        }) {

                        override fun getParams(): MutableMap<String, String> {
                            return parameters;
                        }

                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {

                            val headers: MutableMap<String, String> = HashMap()
                            return headers
                        }
                    }
                    volleyRequestQueue?.add(strReq)

                })


        }

    }
}