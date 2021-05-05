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


class MainActivity : AppCompatActivity() {
    lateinit var firstName: EditText
    lateinit var scrondName: EditText
    lateinit var email: EditText
    lateinit var passwrod: EditText
    lateinit var loginText : TextView
    lateinit var signupBtn: Button
    var volleyRequestQueue: RequestQueue? = null
    val TAG = "nour"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))


        }
        firstName = findViewById(R.id.firstName)
        scrondName = findViewById(R.id.secondName)
        email = findViewById(R.id.email)
        passwrod = findViewById(R.id.password)
        signupBtn = findViewById(R.id.signUp)
        loginText = findViewById(R.id.loginText)

        signupBtn.setOnClickListener {

            registerUser()


        }

        loginText.setOnClickListener {

            finish()
            startActivity(Intent(applicationContext, LoginActivity::class.java))

        }


    }

    private fun registerUser() {

        val fName = firstName.text.toString().trim()
        val sName = scrondName.text.toString().trim()
        val eemail = email.text.toString().trim()
        val pass = passwrod.text.toString().trim()

        if (TextUtils.isEmpty(fName)) {
            firstName.error = "Please enter First Name";
            firstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(sName)) {
            scrondName.error = "Please enter Second Name";
            scrondName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(eemail)) {
            email.error = "Please enter Email";
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            passwrod.error = "Please enter Password";
            passwrod.requestFocus();
            return;
        }
        volleyRequestQueue = Volley.newRequestQueue(this)
        val parameters: MutableMap<String, String> = HashMap()
        parameters["firstName"] = fName;
        parameters["secondName"] = sName;
        parameters["email"] = eemail;
        parameters["password"] = pass;


        val strReq: StringRequest = object : StringRequest(
            Method.POST, URLs.URL_REGISTER,
            Response.Listener { response ->
                Log.e(TAG, "response: $response")
                val user = User(
                    eemail,
                    pass
                )
                SharedPrefManager.getInstance(applicationContext).userLogin(user)
                finish()
                startActivity(Intent(applicationContext, HomeActivity::class.java))

                try {
                    val responseObj = JSONObject(response)
                    val isSuccess = responseObj.getBoolean("isSuccess")
                    val code = responseObj.getInt("code")
                    val message = responseObj.getString("message")
                    if (responseObj.has("data")) {
                        val data = responseObj.getJSONObject("data")

                    }
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


    }


}