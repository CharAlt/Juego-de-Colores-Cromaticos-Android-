package com.example.proyectoaentregar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AyudaActivities extends AppCompatActivity {
    // vecAyudas usado para poder hacer referencia a que leyout de ayuda debemos ir dependiendo del nivel seleccionado
    int[] vecAyudas = new int[]{R.layout.activity_actividad_ayuda5x5,R.layout.activity_actividad_ayuda6x6,R.layout.activity_actividad_ayuda7x7};
    int lvl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            lvl = bundle.getInt("nivel");
            setContentView(vecAyudas[lvl]);
        }
    }
    public void volver(View v){
        this.finish();
    }
}
