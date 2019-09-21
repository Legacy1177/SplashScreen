package com.cuna.splashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    ArrayList<Category> category= new ArrayList<Category>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lv = (ListView) findViewById(R.id.lstTiendas);

       //fillArray();
        //lv.setAdapter(adapter);

        registerForContextMenu(lv);
        new ConsultarTiendas().execute();

    }

    /*private void fillArray() {

        category.add(new Category("Doña Pelos","Tienda Express"));
        category.add(new Category("Oxxo","Recargas"));

    }*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.menu_context,menu);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){

            case R.id.itemContextUpdate:

                //update code
                Toast.makeText(getApplicationContext(),
                        "UPDATE TOUCH"+category.get(info.position).getId(),
                        Toast.LENGTH_SHORT).show();

                Category category2= new Category();

                category2.setId(category.get(info.position).getId());
                category2.setNombre(category.get(info.position).getNombre());
                category2.setDireccion(category.get(info.position).getDireccion());
                category2.setLatitud(category.get(info.position).getLatitud());
                category2.setLongitud(category.get(info.position).getLongitud());
                category2.setDescripcion(category.get(info.position).getDescripcion());

                Intent intent2= new Intent(getApplicationContext(),FormStore.class);
                intent2.putExtra("myObj", (Serializable) category2);
                startActivity(intent2);

                return true;


            case R.id.itemContextDelete:

                //delete code
                Toast.makeText(getApplicationContext(),
                        "DELETE TOUCH"+category.get(info.position).getId(),
                        Toast.LENGTH_SHORT).show();

                Category category1= new Category();
                category1.setId(category.get(info.position).getId());

                new DeleteStore().execute(category1);

                return true;

            case R.id.itemContextNotify:

                //notifications

                int notificationId=1;
                String channelId="my_channel_01";

                NotificationCompat.Builder noti=
                        new NotificationCompat.Builder(getApplicationContext(),channelId);

                NotificationManager nm=
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent= new Intent(getApplicationContext(),MenuActivity.class);

                PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getActivity(MenuActivity.this,1,intent,0);

                CharSequence name= "myName";
                    String description="myDescrip";

                    int importance= NotificationManager.IMPORTANCE_HIGH;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                        NotificationChannel nChannel = new NotificationChannel(channelId, name,
                                importance);

                        nChannel.setDescription(description);
                        nChannel.enableLights(true);
                        nChannel.setLightColor(Color.BLUE);
                        nChannel.enableVibration(true);
                        nm.createNotificationChannel(nChannel);
                        noti= new NotificationCompat.Builder(getApplicationContext(),channelId);

                    }

                    noti.setSmallIcon(R.drawable.ic_launcher_background).setContentText("myText");
                    noti.setChannelId(channelId);
                    nm.notify(notificationId, noti.build());

                return true;

            default:

                return super.onContextItemSelected(item);

        }

    }

    class ConsultarTiendas extends AsyncTask<Void, Integer, JSONArray>{



        @Override
        protected JSONArray doInBackground(Void... voids) {

            URLConnection connection= null;
            JSONArray jsonArray=null;


            try {


                connection =
                        new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/obtenerTiendas" +
                                ".php").openConnection();

                InputStream inputStream= (InputStream) connection.getContent();

                byte[] buffer= new byte[10000];
                int size= inputStream.read(buffer);

                jsonArray= new JSONArray(new String(buffer, 0, size));


            }catch(Exception e){

                e.printStackTrace();

            }

            return jsonArray;

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            super.onPostExecute(jsonArray);
            Category mycategory= null;

            for (int i=0;i<jsonArray.length();i++){

                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    mycategory= new Category(jsonObject.getInt("idtienda"),jsonObject.getString(
                            "nombre"),jsonObject.getString("descripcion"),jsonObject.getString(
                                    "direccion"),jsonObject.getDouble("latitud"),
                            jsonObject.getDouble("longitud"));

                    Log.i("cadena",mycategory.getId()+"");
                    Log.i("cadena",jsonObject.getString(
                            "nombre")+"");
                    Log.i("cadena",jsonObject.getString("direccion")+"");
                    Log.i("cadena",jsonObject.getDouble("latitud")+"");
                    Log.i("cadena",jsonObject.getDouble("longitud")+"");
                    Log.i("cadena",jsonObject.getString("descripcion")+"");

                    category.add(mycategory);

                } catch (JSONException e) {

                    e.printStackTrace();

                }

            }
            AdapterCategory adapter;
            adapter= new AdapterCategory(MenuActivity.this,category);
            lv.setAdapter(adapter);

        }
    }


    class DeleteStore extends AsyncTask<Category,Integer,Boolean>{


        @Override
        protected Boolean doInBackground(Category... categories) {


            String params="idtienda="+categories[0].getId();





            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda" +
                        "/eliminarTienda.php");
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

                Toast.makeText(MenuActivity.this, "Tienda eliminada con exito",
                        Toast.LENGTH_SHORT).show();



            }else{

                Toast.makeText(MenuActivity.this, "No fue posible eliminar la tienda",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }

}
