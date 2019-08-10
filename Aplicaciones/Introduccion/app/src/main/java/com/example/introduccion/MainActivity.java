package com.example.introduccion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // Key para mandar el mensaje por el intent
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Método para el onClick() del boton
    public void sendMessage(View view) {
        // Se inicializa el intent para iniciar la siguiente actividad
        Intent intent = new Intent(this, ResultadoActivity.class);
        // Obtiene el mensaje del EditText
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        // Agrega el mensaje al intent
        intent.putExtra(EXTRA_MESSAGE, message);
        // Inicia la siguiente actividad
        startActivity(intent);
    }
}
