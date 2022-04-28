package com.example.proyectoaentregar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

public class IniciarJuegos extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    int vec[] = new int[] {R.id.lvlBajo,R.id.lvlMedio,R.id.lvlAlto};
    int[] niveles = new int[] {R.string.lvlbajo, R.string.lvlmedio, R.string.lvlalto};
    int lvl;
    Toast tst;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_juegos);
    }

    public void mostrarPopup(View v){
        PopupMenu popup=new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater=popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu,popup.getMenu());
        popup.show();
    }
    @SuppressLint("ShowToast")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        lvl = 0;
        i =0 ;
        boolean var = false;
        while(!(var)&&(i < vec.length)){
            if(vec[i] == item.getItemId()){
                lvl = i+1;
                Toast.makeText(this,getResources().getString(niveles[i]),Toast.LENGTH_SHORT).show();
                var = true;
            }
            else
               i++;
        }
        return var;
    }

    public void iniciar (View v){
        if (lvl!=0){
            Intent i = new Intent(this,MainGameActivity.class);
            i.putExtra("nivel",lvl-1);
            startActivity(i);
        }
        else{
            tst=Toast.makeText(this,R.string.nolvl,Toast.LENGTH_SHORT);
            tst.show();
        }
    }
    public void salir(View v){
       Confirmacion com = new Confirmacion();
       com.show(getFragmentManager(),"tagDeConfirmacion");
    }
    public void ranking(View v){
        Intent i = new Intent(this,ranking.class);
        startActivity(i);
    }
}

