package com.cuna.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.URLConnection;

public class FormStore extends AppCompatActivity implements View.OnClickListener{

    EditText  txtId,txtNombre,txtDireccion,txtLatitud,txtLongitud,txtDescripcion;
    Button btnBorrar,btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_store);

        txtId=findViewById(R.id.storeId);
        txtNombre=findViewById(R.id.storeNombre);
        txtDireccion=findViewById(R.id.storeDireccion);
        txtLatitud=findViewById(R.id.storeLatitud);
        txtLongitud=findViewById(R.id.storeLongitud);
        txtDescripcion=findViewById(R.id.storeDescripcion);

        btnBorrar=findViewById(R.id.btnGuardarForm);
        btnCancelar=findViewById(R.id.btnCancelarForm);

        txtId.setEnabled(false);
        Intent categoryIntent= (Category) getIntent().getExtras().getSerializable("myObj");

        txtId.setText(((Category) categoryIntent).getId());
        txtNombre.setText(((Category) categoryIntent).getNombre());
        txtDescripcion.setText(((Category) categoryIntent).getDescripcion());
        txtLatitud.setText(((Category) categoryIntent).getLatitud().toString());
        txtLongitud.setText(((Category) categoryIntent).getLongitud().toString());
        txtDescripcion.setText(((Category) categoryIntent).getDescripcion());




    }


    class UpdateStore extends AsyncTask<Category,Integer,Boolean>{


        @Override
        protected Boolean doInBackground(Category... categories) {


            String params="nombre="+categories[0].getNombre()+"&&" +
                    "direccion="+categories[0].getDireccion()+"&&" +
                    "latitud="+categories[0].getLatitud()+"&&" +
                    "longitud"+categories[0].getLongitud()+"&&" +
                    "descripcion"+categories[0].getDescripcion();




            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda" +
                        "/modificarTienda.php");
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                OutputStream outputStream= connection.getOutputStream();
                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream,
                        "UTF-8"));

                writer.write(params);
                writer.flush();
                writer.close();

                connection.connect();

                int responseCode=connection.getResponseCode();

                if(responseCode==HttpURLConnection.HTTP_OK){

                    return true;

                }else{

                    return false;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean){

                Toast.makeText(FormStore.this, "Tienda actualizada con exito",
                        Toast.LENGTH_SHORT).show();



            }else{

                Toast.makeText(FormStore.this, "No fue posible actualizar la tienda",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btnCancelarForm:

                break;

            case R.id.btnGuardarForm:

                Category myCategory= new Category();
                myCategory.setId(Integer.parseInt(txtId.getText().toString().trim()));
                myCategory.setNombre(txtNombre.getText().toString().trim());
                myCategory.setDireccion(txtDireccion.getText().toString().trim());
                myCategory.setDescripcion(txtDescripcion.getText().toString().trim());
                myCategory.setLatitud(Double.parseDouble(txtLatitud.getText().toString().trim()));
                myCategory.setLongitud(Double.parseDouble(txtLongitud.getText().toString().trim()));

                new UpdateStore().execute(myCategory);

                break;

        }

    }
}
