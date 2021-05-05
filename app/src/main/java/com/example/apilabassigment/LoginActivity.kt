package com.example.apilabassigment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var loginBtn :Button
    lateinit var signupText:TextView
    var volleyRequestQueue: RequestQueue? = null
    val TAG = "nourr"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        email = findViewById(R.id.emailLogin)
        password = findViewById(R.id.passwordLogin)
        loginBtn = findViewById(R.id.loginbtn)
        signupText = findViewById(R.id.signupText)
        signupText.setOnClickListener{

            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))

        }

        loginBtn.setOnClickListener {
            userLogin()


        }
    }

 private fun userLogin() {

val emailLogIn = email.text.toString().trim()
     val passwordLogin = password.text.toString().trim()

     if (TextUtils.isEmpty(emailLogIn)) {
         email.error = "Please enter Email";
         email.requestFocus();
         return;
     }

     if (TextUtils.isEmpty(passwordLogin)) {
         password.error = "Please enter Password";
         password.requestFocus();
         return;
     }


     volleyRequestQueue = Volley.newRequestQueue(this)
     val parameters: MutableMap<String, String> = HashMap()
     parameters["email"] = emailLogIn
     parameters["password"] = passwordLogin

     val strReq: StringRequest = object : StringRequest(
         Method.POST, URLs.URL_LOGIN,
         Response.Listener { response ->
             Log.e(TAG, "response: $response")
             val user= User(

                 emailLogIn,
                 passwordLogin

             )
             Log.e(TAG, "$user")

             SharedPrefManager.getInstance(applicationContext).userLogin(user)
             finish()
             startActivity(Intent(applicationContext, HomeActivity::class.java))

             try {
                 val responseObj = JSONObject(response)

                 if(!responseObj.getBoolean("error")){
                     Toast.makeText(
                         applicationContext,
                         responseObj.getString("message"),
                         Toast.LENGTH_SHORT
                     ).show();


                      val message = responseObj.getString("message")

                         val data = responseObj.getJSONObject("data")


                     Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                 }else{
                     Toast.makeText(applicationContext, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                     Log.e(TAG, responseObj.getString("message"))


                 }




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
     //volleyRequestQueue?.add(strReq)
     VolleySingleton.getInstance(this).addToRequestQueue(strReq)


 }


}