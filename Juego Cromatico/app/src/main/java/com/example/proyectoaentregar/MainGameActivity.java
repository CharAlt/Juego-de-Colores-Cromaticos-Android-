package com.example.proyectoaentregar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainGameActivity extends AppCompatActivity {
    // vecAyudas usado para poder hacer referencia a que leyout de ayuda debemos ir dependiendo del nivel seleccionado
    int[] vecLayout = new int[]{R.layout.activity_main, R.layout.activity_main2, R.layout.activity_main3};
    //usados para hacer escalable el las iteraciones
    int rangoI = 4;
    int rangoJ = 4;
    //usado para hacer escalable la dimensiones del vector y arraylist
    int cantColores = 25;
    //identificador usado para saber de que actividad venimos

    // varaibles usadas en metodo cambiar
    int id1, cambios = 0;
    Button btn;
    boolean cam = true, swap = false;
    Drawable color1, color2;
    TextView t;
    // vector para poner los colores ordenados y poder chequear ya que el cuadro esta en la proxima actividad y el otro arrayList para guarda estado
    int[] vec;
    // arraylist usado para salvar el estado de los botones de la actividad ante un cambio de actividad o rotacion del dispositivo
    ArrayList<Integer> coloresSalvar;
    //arraylist que solo durara para asignar los colores random
    List<Integer> col;

    // nivel selecionado
    int lvl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            lvl = bundle.getInt("nivel");
            setContentView(vecLayout[lvl]);
            Handler hundle = new Handler();
            Toast.makeText(this, R.string.cincoSegs, Toast.LENGTH_SHORT).show();
            hundle.postDelayed(runnable, 5000);
        }
        t = (TextView) findViewById(R.id.visorCambios);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            determinarVec();
            asignarArrayList();
            asignarColoresRandom();
            swap = true;
        }
    };

    public void asignarArrayList(){
        int i,j = cantColores - 1;
        for (i=0; i<=j; i++)
            col.add(i,vec[i]);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void cambiar(View v) {
        if(swap) {
            if (cam) {
                id1 = v.getId();
                btn = (Button) findViewById(id1);
                btn.setText("o");
                color1 = v.getBackground();
            } else {
                if (v.getId() != id1) {
                    color2 = v.getBackground();
                    btn = (Button) findViewById(id1);
                    btn.setBackground(color2);
                    btn = (Button) findViewById(v.getId());
                    btn.setBackground(color1);
                    btn = (Button) findViewById(id1);
                    cambios++;

                    if (compararResultado()) {
                        Intent i = new Intent(this, ranking.class);
                        i.putExtra("id", lvl);
                        i.putExtra("score", cambios);
                        startActivity(i);
                        this.finish();
                    }
                    t.setText(String.valueOf(cambios));
                }
                btn.setText("");
            }
            cam = !cam;
        }
    }


    // funcion de asignar los colors
    public void asignarColoresRandom() {
        Random rand = new Random();   // variable de tipo de dato objeto random para poder generar numeros aleatorios
        int i, j, indice, id;         // i y j para las repeticiones de los for, num para el numero random generado y id para poder guardar el id retornado por getResorces
        String bld;                   // string usado para identificar el id con getResorces;
        for (i = 0; i <= rangoI; i++)
            for (j = 0; j <= rangoJ; j++) {
                bld = ("b" + j) + i;
                indice = rand.nextInt(col.size());            // genera numero random entro 0 y la dimension del arrayList
                id = getResources().getIdentifier(bld, "id", getPackageName()); // identificamos el id con string, retorna entero y lo guardas en id
                btn = (Button) findViewById(id);                                   // referenciamos al boton con ese id
                btn.setBackgroundColor(getResources().getColor(col.get(indice)));  //establecemos el color
                col.remove(indice);
            }
    }
    // asinar colores random
    public boolean compararResultado() {
        String bld1;        //usado para identificar a cada recurso com GetResorces
        int id1, i, j, color1ld,colorId;   /*id1 usado para identificar una vez que getResorces retorna el id en entero
                                            , i y j usados en el for y color1ld y color2ld usados para almacenar el color puro en haxadecimal, pero en entero */
       Button btn1;  // usado para referenciar al recurso boton
        ColorDrawable color1;
        int indice = 0;
        boolean continuar = true;
        i = 0;
        while ((i <=rangoI) && (continuar)) {
            j = 0;
            while ((j <= rangoJ) && (continuar)) {
                bld1 = ("b" + j) + i;    // crear otra variable para eficientar el sistema
                id1 = getResources().getIdentifier(bld1, "id", getPackageName());
                btn1 = (Button) findViewById(id1);
                color1 = (ColorDrawable) btn1.getBackground();
                color1ld = color1.getColor();
                colorId=getResources().getColor(vec[indice]);
                if (color1ld == colorId) {
                    j++;
                    indice++;
                } else
                    continuar = false;
            }
            i++;
        }
        return continuar;
    }

    private void Tableroayuda() {
        Intent intent = new Intent(this, AyudaActivities.class);
        intent.putExtra("nivel",lvl);
        //guardarEstado();
        intent.putIntegerArrayListExtra("colores", (ArrayList<Integer>) coloresSalvar);
        intent.putExtra("vec",vec);
        intent.putExtra("cambios",cambios);
        intent.putExtra("nivel",lvl);
        startActivity(intent);
    }


    //ponemos nuevamente el estado del tablero como estaba
   public void ResetTablero() {
        int i, j, id, indice;
        String bld;
        indice = 0;
        for (i = 0; i <= rangoI; i++)
            for (j = 0; j <= rangoJ; j++) {
                bld = ("b" + j) + i;
                id = getResources().getIdentifier(bld, "id", getPackageName());
                btn = (Button) findViewById(id);
                btn.setBackgroundColor(coloresSalvar.get(indice));
                indice++;
            }
    }
    //guardamos el estado de los colores
    public void guardarEstado() {
        int i, j, idB, indice;
        Button btn;
        String bld;
        ColorDrawable pasar;
        indice = 0;
        for (i = 0; i <= rangoI; i++)
            for (j = 0; j<=rangoJ; j++) {
                bld = ("b" + j) + i;
                idB = getResources().getIdentifier(bld, "id", getPackageName());
                btn = (Button) findViewById(idB);
                pasar = (ColorDrawable) btn.getBackground();
                coloresSalvar.add(indice, pasar.getColor());
                indice++;
            }
    }

    //en caso de rotar la pantalla
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        guardarEstado();
        outState.putIntegerArrayList("colores", (ArrayList<Integer>) coloresSalvar);
        outState.putIntArray("vecColor", vec);
        outState.putInt("cambios", cambios);
        outState.putInt("var",-1);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        coloresSalvar = savedInstanceState.getIntegerArrayList("colores");
        cambios = savedInstanceState.getInt("cambios");
        vec = savedInstanceState.getIntArray("vecColor");
        ResetTablero();
    }

    public void determinarVec(){
      switch (lvl){
          case 0:
              rangoI = 4;
              rangoJ = 4;
              cantColores =25;
              vec = new int[cantColores];
              col = new ArrayList<>(cantColores);
              coloresSalvar = new ArrayList<>(cantColores);
              cargarColores1();
              break;
          case 1:
              rangoI = 5;
              rangoJ = 5;
              cantColores =36;
              vec = new int[cantColores];
              col = new ArrayList<>(cantColores);
              coloresSalvar = new ArrayList<>(cantColores);
              cargarColores2();
              break;
          case 2:
              rangoI = 6;
              rangoJ = 6;
              cantColores =49;
              vec = new int[cantColores];
              col = new ArrayList<>(cantColores);
              coloresSalvar = new ArrayList<>(cantColores);
              cargarColores3();
              break;
      }

    }

    // metodos para cargar los colores
    public void cargarColores1() { // debido a que no se pueden cambiar los id
        // carga de colores rojos
        vec[0] = (R.color.md_red_50);
        vec[1] = (R.color.md_red_100);
        vec[2] = (R.color.md_red_200);
        vec[3] = (R.color.md_red_300);
        vec[4] = (R.color.md_red_400);
        //carga de colores rosados
        vec[5] = (R.color.md_pink_50);
        vec[6] = (R.color.md_pink_100);
        vec[7] = (R.color.md_pink_200);
        vec[8] = (R.color.md_pink_300);
        vec[9] = (R.color.md_pink_400);
        //carga de colores deep purpuras
        vec[10] = (R.color.md_deep_purple_50);
        vec[11] = (R.color.md_deep_purple_100);
        vec[12] = (R.color.md_deep_purple_200);
        vec[13] = (R.color.md_deep_purple_300);
        vec[14] = (R.color.md_deep_purple_400);
        //carga de colores indigos
        vec[15] = (R.color.md_indigo_50);
        vec[16] = (R.color.md_indigo_100);
        vec[17] = (R.color.md_indigo_200);
        vec[18] = (R.color.md_indigo_300);
        vec[19] = (R.color.md_indigo_400);
        //carga de colores blue
        vec[20] = (R.color.md_blue_50);
        vec[21] = (R.color.md_blue_100);
        vec[22] = (R.color.md_blue_200);
        vec[23] = (R.color.md_blue_300);
        vec[24] = (R.color.md_blue_400);
    }

    public void cargarColores2() { // debido a que no se pueden cambiar los id
        // carga de colores rojos
        vec[0] = (R.color.md_red_50);
        vec[1] = (R.color.md_red_100);
        vec[2] = (R.color.md_red_200);
        vec[3] = (R.color.md_red_300);
        vec[4] = (R.color.md_red_400);
        vec[5] = (R.color.md_red_500);
        //carga de colores rosados
        vec[6] = (R.color.md_pink_50);
        vec[7] = (R.color.md_pink_100);
        vec[8] = (R.color.md_pink_200);
        vec[9] = (R.color.md_pink_300);
        vec[10] = (R.color.md_pink_400);
        vec[11] = (R.color.md_pink_500);
        //carga de colores deep purpuras
        vec[12] = (R.color.md_deep_purple_50);
        vec[13] = (R.color.md_deep_purple_100);
        vec[14] = (R.color.md_deep_purple_200);
        vec[15] = (R.color.md_deep_purple_300);
        vec[16] = (R.color.md_deep_purple_400);
        vec[17] = (R.color.md_deep_purple_500);
        //carga de colores indigos
        vec[18] = (R.color.md_indigo_50);
        vec[19] = (R.color.md_indigo_100);
        vec[20] = (R.color.md_indigo_200);
        vec[21] = (R.color.md_indigo_300);
        vec[22] = (R.color.md_indigo_400);
        vec[23] = (R.color.md_indigo_500);
        //carga de colores blue
        vec[24] = (R.color.md_blue_50);
        vec[25] = (R.color.md_blue_100);
        vec[26] = (R.color.md_blue_200);
        vec[27] = (R.color.md_blue_300);
        vec[28] = (R.color.md_blue_400);
        vec[29] = (R.color.md_blue_500);
        //carga de los colores ligth blue
        vec[30] = (R.color.md_light_blue_50);
        vec[31] = (R.color.md_light_blue_100);
        vec[32] = (R.color.md_light_blue_200);
        vec[33] = (R.color.md_light_blue_300);
        vec[34] = (R.color.md_light_blue_400);
        vec[35] = (R.color.md_light_blue_500);
    }
    public void cargarColores3() { // debido a que no se pueden cambiar los id
        // carga de colores rojos
        vec[0] = (R.color.md_red_50);
        vec[1] = (R.color.md_red_100);
        vec[2] = (R.color.md_red_200);
        vec[3] = (R.color.md_red_300);
        vec[4] = (R.color.md_red_400);
        vec[5] = (R.color.md_red_500);
        vec[6] = (R.color.md_red_600);
        //carga de colores rosados
        vec[7] = (R.color.md_pink_50);
        vec[8] = (R.color.md_pink_100);
        vec[9] = (R.color.md_pink_200);
        vec[10] = (R.color.md_pink_300);
        vec[11] = (R.color.md_pink_400);
        vec[12] = (R.color.md_pink_500);
        vec[13] = (R.color.md_pink_600);
        //carga de colores deep purpuras
        vec[14] = (R.color.md_deep_purple_50);
        vec[15] = (R.color.md_deep_purple_100);
        vec[16] = (R.color.md_deep_purple_200);
        vec[17] = (R.color.md_deep_purple_300);
        vec[18] = (R.color.md_deep_purple_400);
        vec[19] = (R.color.md_deep_purple_500);
        vec[20] = (R.color.md_deep_purple_600);
        //carga de colores indigos
        vec[21] = (R.color.md_indigo_50);
        vec[22] = (R.color.md_indigo_100);
        vec[23] = (R.color.md_indigo_200);
        vec[24] = (R.color.md_indigo_300);
        vec[25] = (R.color.md_indigo_400);
        vec[26] = (R.color.md_indigo_500);
        vec[27] = (R.color.md_indigo_600);
        //carga de colores blue
        vec[28] = (R.color.md_blue_50);
        vec[29] = (R.color.md_blue_100);
        vec[30] = (R.color.md_blue_200);
        vec[31] = (R.color.md_blue_300);
        vec[32] = (R.color.md_blue_400);
        vec[33] = (R.color.md_blue_500);
        vec[34] = (R.color.md_blue_600);
        //carga de los colores ligth blue
        vec[35] = (R.color.md_light_blue_50);
        vec[36] = (R.color.md_light_blue_100);
        vec[37] = (R.color.md_light_blue_200);
        vec[38] = (R.color.md_light_blue_300);
        vec[39] = (R.color.md_light_blue_400);
        vec[40] = (R.color.md_light_blue_500);
        vec[41] = (R.color.md_light_blue_600);
        //carga de los colores cyan
        vec[42] = (R.color.md_cyan_50);
        vec[43] = (R.color.md_cyan_100);
        vec[44] = (R.color.md_cyan_200);
        vec[45] = (R.color.md_cyan_300);
        vec[46] = (R.color.md_cyan_400);
        vec[47] = (R.color.md_cyan_500);
        vec[48] = (R.color.md_cyan_600);
    }
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater mi= getMenuInflater();
        mi.inflate(R.menu.menu_in_game,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if ((item.getItemId()==R.id.menuAyuda)&&(swap)){
            Tableroayuda();
        }
        if (item.getItemId()==R.id.menuSalir){
            ConfirmationDialogFragment dialog=new ConfirmationDialogFragment();
            dialog.show(getSupportFragmentManager(),"tagConfirmacion");
        }
        return true;
    }

    }

