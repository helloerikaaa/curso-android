package com.tec.aplicacionapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tec.aplicacionapi.helper.CheckNetworkStatus;
import com.tec.aplicacionapi.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificarActivity extends AppCompatActivity {

    private EditText nombreET;
    private EditText descET;
    private EditText generoET;
    private EditText ratingET;
    private String peliculaId;
    private Button btnEliminar;
    private Button btnModificar;
    private ProgressDialog progressDialog;
    private String nombre;
    private String desc;
    private String genero;
    private String rating;
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        Intent intent = getIntent();
        nombreET = (EditText) findViewById(R.id.nombreET);
        descET = (EditText) findViewById(R.id.descET);
        generoET = (EditText) findViewById(R.id.generoET);
        ratingET = (EditText) findViewById(R.id.ratingET);
        peliculaId = intent.getStringExtra("idPelicula");
        new FetchDetallePelicula().execute();
        btnEliminar = (Button) findViewById(R.id.btn_eliminar);
        btnModificar = (Button) findViewById(R.id.btn_modificar);

        btnEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                eliminarPelicula();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    actualizarPelicula();
                }else{
                    Toast.makeText(ModificarActivity.this,
                            "No hay conexión a Internet",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class FetchDetallePelicula extends
            AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =
                    new ProgressDialog(ModificarActivity.this);
            progressDialog.
                    setMessage("Cargando información de la película...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    "172.20.10.2/peliculas/detalle_pelicula.php?id=" + peliculaId, "GET", httpParams);
            try {
                int success = jsonObject.getInt("estado");
                JSONObject pelicula;
                if (success == 1) {
                    pelicula = jsonObject.getJSONObject("mensaje");
                    nombre = pelicula.getString("nombre");
                    desc = pelicula.getString("descripcion");
                    genero = pelicula.getString("genero");
                    rating = pelicula.getString("rating");
                }
            } catch (JSONException e) {
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
                    nombreET.setText(nombre);
                    descET.setText(desc);
                    generoET.setText(genero);
                    ratingET.setText(rating);
                }
            });
        }
    }
    private void eliminarPelicula(){
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ModificarActivity.this);
        builder.setMessage("Estás seguro de eliminar la película?");
        builder.setPositiveButton("Eliminar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                            new EliminarPelicula().execute();
                        }else{
                            Toast.makeText(ModificarActivity.this,
                                    "No hay Internet",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private class EliminarPelicula
            extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =
                    new ProgressDialog(ModificarActivity.this);
            progressDialog.
                    setMessage("Eliminando Película...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put("id", peliculaId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    "165.2.3.5/peliculas/eliminar_pelicula.php",
                    "POST",
                    httpParams);
            try{
                success = jsonObject.getInt("estado");
            }catch(JSONException e){
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
                    if(success == 1){
                        Toast.makeText(ModificarActivity.this,
                                "La película se eliminó",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ModificarActivity.this,
                                "Hubo un error al eliminar la película",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void actualizarPelicula(){
        if(!nombreET.getText().toString().equals("") &&
                !descET.getText().equals("") &&
                !generoET.getText().equals("") &&
                !ratingET.getText().equals("")){
            nombre = nombreET.getText().toString();
            desc = descET.getText().toString();
            genero = generoET.getText().toString();
            rating = ratingET.getText().toString();
            new ActualizarPelicula().execute();
        }else{
            Toast.makeText(ModificarActivity.this,
                    "Algún campo está vacío",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class ActualizarPelicula extends
            AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =
                    new ProgressDialog(ModificarActivity.this);
            progressDialog.
                    setMessage("Actualizando película...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put("id", peliculaId);
            httpParams.put("nombre", nombre);
            httpParams.put("descripcion", desc);
            httpParams.put("genero", genero);
            httpParams.put("rating", rating);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    "http://172.20.10.2/peliculas/actualiza_pelicula.php",
                    "POST",
                    httpParams);
            try{
                success = jsonObject.getInt("estado");
            }catch(JSONException e){
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
                    if(success == 1){
                        Toast.makeText(ModificarActivity.this,
                                "La película se actualizó",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ModificarActivity.this,
                                "Hubo un error la actualizar",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


