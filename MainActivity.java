package com.cuna.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    TextView lblUser,lblPassword;
    EditText txtUser, txtPassword;

    String user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        txtPassword=findViewById(R.id.txtPassword);
        txtUser=findViewById(R.id.txtUser1);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);

            }
        });

    }

    public void Login(View view ){
        user= txtUser.getText().toString().trim();
        pass=txtPassword.getText().toString();
        //validar datos
        if(TextUtils.isEmpty(user)){
            txtUser.setError("Usuario requerido");
            txtUser.setFocusable(true);
            return;
        }


        if(TextUtils.isEmpty(pass)){
            txtPassword.setError("Contrasena requerida");
            txtPassword.setFocusable(true);
            return;
        }
        new LoginRest().execute(user,pass);
    }








    //clase AsyncTask
    class LoginRest extends AsyncTask<String, Integer, String>{

        //variable de peticion de conexion
        URLConnection connection=null;
        //variable para el resultado
        String result="0";



        @Override
        protected String doInBackground(String... strings) {
            try {
                connection=new URL("http://172.18.26.66/cursoAndroid/vista/usuario/iniciarSesion.php?usuario="+strings[0]+"&password="+strings[1]).openConnection();

                InputStream inputStream= (InputStream) connection.getContent();
                byte[] buffer=new byte[10000];

                int size=inputStream.read(buffer);

                result= new String(buffer,0,size);
                Log.i("result",result);



            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("1")){
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this, "Usuario o contrasena incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }








}
