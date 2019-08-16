package com.tec.aplicacionapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tec.aplicacionapi.helper.CheckNetworkStatus;
import com.tec.aplicacionapi.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListadoActivity extends AppCompatActivity {

    private ListView listView;
    private ProgressDialog progressDialog;
    ArrayList listaPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        listView = (ListView) findViewById(R.id.listadoPeliculas);
        new FetchPeliculas().execute();
    }

    private class FetchPeliculas extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListadoActivity.this);
            progressDialog.setMessage("Cargando películas...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    "http://172.20.10.2/peliculas/obtener_peliculas.php",
                    "GET",
                    null);
            try{
                int success = jsonObject.getInt("estado");
                JSONArray peliculas;
                if(success == 1){
                    listaPeliculas = new ArrayList<>();
                    peliculas = jsonObject.getJSONArray("data");
                    for(int i = 0; i < peliculas.length(); i++){
                        JSONObject pelicula = peliculas.getJSONObject(i);
                        Integer peliculaId = pelicula.getInt("id");
                        String peliculaNombre = pelicula.getString("nombre");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", peliculaId.toString());
                        map.put("nombre", peliculaNombre);
                        listaPeliculas.add(map);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    llenarListaPeliculas();
                }
            });
        }
    }

    private void llenarListaPeliculas(){
        ListAdapter listAdapter = new SimpleAdapter(
                ListadoActivity.this,
                listaPeliculas, R.layout.list_item, new String[]{"id", "nombre"},
                new int[]{R.id.movieId, R.id.movieName});
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {
                if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    String peliculaId =
                            ((TextView) view.findViewById(R.id.movieId)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            ModificarActivity.class);
                    intent.putExtra("idPelicula", peliculaId);
                    startActivityForResult(intent, 20);
                }else{
                    Toast.makeText(ListadoActivity.this,
                            "No se puede conectar a Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 20){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
