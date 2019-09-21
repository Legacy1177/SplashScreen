package com.cuna.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import modelos.Usuario;

public class RegistroActivity extends AppCompatActivity {
    EditText txtUsuario,txtDireccion,txtContrasena,txtCorreo,txtNombre;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuario=findViewById(R.id.txtUsuario);
        txtContrasena=findViewById(R.id.txtPassword);
        txtDireccion=findViewById(R.id.txtDireccion);
        txtCorreo=findViewById(R.id.txtCorreo);
        txtNombre=findViewById(R.id.txtNombre);

        btnAdd=findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarDatos(txtUsuario.getText().toString().trim(),
                        txtNombre.getText().toString().trim(),
                        txtCorreo.getText().toString().trim(),
                        txtDireccion.getText().toString().trim(),
                        txtContrasena.getText().toString())){

                    //crea objeto de tipo usuario
                    Usuario usuario= new Usuario();
                    usuario.setNickname(txtUsuario.getText().toString().trim());
                    usuario.setNombre(txtNombre.getText().toString().trim());
                    usuario.setCorreo(txtCorreo.getText().toString().trim());
                    usuario.setDireccion(txtDireccion.getText().toString().trim());
                    usuario.setPassword(txtContrasena.getText().toString());

                    //crear la instancia de la subclase asynctask para realizar la conexion
                    new AddUser().execute(usuario);

                }

            }
        });
    }

    public boolean validarDatos(String nickname, String nombre, String correo,
                                     String direccion,
                                String password){

        //validar datos
        if(nombre.isEmpty()){

            txtNombre.setError("Campo nombre vacio");
            txtNombre.setFocusable(true);
            return false;

        }

        if(nickname.isEmpty()){

            txtUsuario.setError("Campo usuario vacio");
            txtUsuario.setFocusable(true);
            return false;

        }if(correo.isEmpty()){

            txtCorreo.setError("Campo correo vacio");
            txtCorreo.setFocusable(true);
            return false;

        }if(direccion.isEmpty()){

            txtDireccion.setError("Campo direccion vacio");
            txtDireccion.setFocusable(true);
            return false;

        }if(password.isEmpty()){

            txtContrasena.setError("Campo contraseña vacio");
            txtContrasena.setFocusable(true);
            return false;

        }

        return true;

    }



    //creacion hilo async
    class AddUser extends AsyncTask<Usuario, Integer, Boolean>{


        @Override
        protected Boolean doInBackground(Usuario... usuarios) {

            //prepara los datos de insercion

            String params="";
            params= "user="+usuarios[0].getNickname()+"&" +
                    "nombre="+usuarios[0].getNombre()+"&" +
                    "correo="+usuarios[0].getCorreo()+"&" +
                    "direccion="+usuarios[0].getDireccion()+"&" +
                    "password="+usuarios[0].getPassword();

            //preparar la conexion

            try {

                URL url = new URL("http://172.18.26.67/cursoAndroid/vista/Usuario/crearUsuario.php");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();

                //cambiar metodo a POST y preparar para enviar y recibir datos
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //se crea el outputstream y el buffered writer para escribir los datos
                OutputStream outputStream= connection.getOutputStream();
                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream,
                        "UTF-8"));

                //se escriben los datos en params, se limpia el writer y se cierra el writerstream
                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();

                //se conecta
                connection.connect();

                //se obtiene el response code de la conexion
                int responseCode= connection.getResponseCode();

                //verifica si la conexion se realiza con exito
                if(responseCode== HttpURLConnection.HTTP_OK){

                    //mensaje para el usuario de connection_ok
                    Log.i("AddUser", "usuario agregado con exito");
                    return true;

                }

            }catch (MalformedURLException e){

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            };


            return false;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            super.onPostExecute(aBoolean);

            if(aBoolean){

                //true
                Toast.makeText(RegistroActivity.this, "Usuario Agregado con Exito",
                        Toast.LENGTH_SHORT).show();

                    //termina el activity
                    finish();

            }else{

                Toast.makeText(RegistroActivity.this, "Usuario no Agregado", Toast.LENGTH_SHORT).show();

            }

        }
    }

}



/* validar que no estan vacion los txt
    obtener los datos
    agregar los datos a un objeto
    instancia de clase async
    mandar los datos obtenidos del ususario
 */
