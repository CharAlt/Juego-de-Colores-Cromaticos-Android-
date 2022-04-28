package com.example.proyectoaentregar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ranking extends AppCompatActivity {
    final static int cantNiveles = 3;
    String niveles[] = new String[]{"facil", "medio", "dificil"};
    JSONArray vecoresJson[] = new JSONArray[3];
    String atributos[] = new String[]{"jugador", "puntuacion"};
    String nombreJugador = "jugador desconocido";
    int cantActual;
    String imp = "";
    int score;
    int idActivity;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            idActivity = bundle.getInt("id");
        }
        try {
            recuperar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bundle != null) {
            score = bundle.getInt("score");
            try {
                chequear(score,idActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            try {
                imprimirRankigTotal();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // modulo star que contiene todos los demas modulos
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void start() throws JSONException {
        if (imp.equals("")) {
            for (int i = 0; i < vecoresJson.length; i++)
                vecoresJson[i] = new JSONArray();
            try {
                crearJugadores();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                guardarCraedos();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //modulo para chaquear y pedir el nombre al usuario en caso de entrar al ranking
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void chequear(final int score, final int id) throws JSONException {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String titulo = getResources().getString(R.string.felicidades);
            titulo = titulo + String.valueOf(score);
            builder.setTitle(titulo);
           builder.setPositiveButton(R.string.reiniciar, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   Intent intent = new Intent(ranking.this, MainGameActivity.class);
                   intent.putExtra("nivel",idActivity);
                   startActivity(intent);
                   ranking.this.finish();
               }
           }).setNegativeButton(R.string.nuevo, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                 ranking.this.finish();
               }
           });
            final int posicion = entra(score, id);
            if(posicion != 5) {
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(editText);
                builder.setNeutralButton(R.string.ingresar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!editText.getText().toString().equals("")){
                            nombreJugador = editText.getText().toString();
                        }
                        JSONObject jugador = new JSONObject();
                        try {
                            jugador.put("jugador",nombreJugador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jugador.put("puntuacion",score);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            vecoresJson[id].put(posicion,jugador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            guardarSoloUno(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            imprimirRankigTotal();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else{
                try {
                    imprimirRankigTotal();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        imprimirRankigTotal();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
           builder.show();
          }

    public int determinarPos(int score, int id) throws JSONException {
        int i = 0;
            int cant;
            String puntuacion;
            int pun;
            JSONObject jugadorAct;
            boolean entro = false;
            cant = cantActual - 1;
            while ((!entro) && (i <= cant)) {
                jugadorAct = new JSONObject();
                jugadorAct = vecoresJson[id].getJSONObject(i);
                puntuacion = jugadorAct.getString("puntuacion");
                pun = Integer.parseInt(puntuacion);
                if (score < pun) {
                    entro = true;
                } else
                    i++;
            }
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int entra(int score, int id) throws JSONException {
        int pos = 5;
        if(score < vecoresJson[id].getJSONObject(4).getInt("puntuacion")) {
            pos = determinarPos(score, id);
            if (pos != 5) {
                if ((pos <= cantActual - 1)) {
                    int pos2 = cantActual - 1;
                    if (cantActual == 5) {
                        pos2--;
                    } else {
                        cantActual++;
                    }
                    for (int i = pos2; i >= pos; i = i - 1)
                        vecoresJson[id].put(i + 1, vecoresJson[id].getJSONObject(i));
                } else
                    cantActual++;
            }
        }
        return pos;
    }

    public void imprimirRankigTotal() throws JSONException {
        int id,
                i,
                j,
                p,
                x = 0;
        String txt,
         nom = getResources().getString(R.string.tipoJugador);
        JSONObject json;
        for (i = 0; i <= 2; i++) {
            for (p = 0; p <= 4; p++) {
                json = new JSONObject();
                for (j = 1; j <= 2; j++) {
                    txt = ("txt" + x) + j;
                    id = getResources().getIdentifier(txt, "id", getPackageName());
                    try {
                        json = vecoresJson[i].getJSONObject(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        txt = json.getString(atributos[j - 1]);
                        if(j-1 == 0){
                            if(txt.equals("-1"))
                              txt = nom + " " + (x + 1);
                        }
                        else{
                          if(txt.equals("9999"))
                            txt = "-1";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ((TextView) findViewById(id)).setText(txt);
                }
                x++;
            }
        }
    }

  // crea por primira vez los jugadores
    private void crearJugadores() throws JSONException {
        int cant = 1;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 4; j++) {
                JSONObject jugador = new JSONObject();
                jugador.put("jugador", "-1");
                jugador.put("puntuacion", "9999");
                vecoresJson[i].put(j, jugador);
            }
            cant = cant + 5;
        }
    }

    //guarda los jugadores creados por primira vez
    private void guardarCraedos() throws JSONException {
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        String nivel;
        JSONObject jsonObject;
        for (int i = 0; i <= 2; i++) {
            jsonObject = new JSONObject();
            jsonObject.put("jugadores", vecoresJson[i]);
            jsonObject.put("cantActual", 0);
            nivel = jsonObject.toString();
            editor.putString(niveles[i], nivel);
        }
        editor.apply();
    }

    private void recuperar() throws JSONException {
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        JSONObject jsonObject = null;


            imp = preferencias.getString(niveles[idActivity], "");
            if (!imp.equals("")) {
                jsonObject = new JSONObject(imp);
                cantActual = jsonObject.getInt("cantActual");
            }

        int i = 0;
        imp = preferencias.getString(niveles[i], "");
        while (!imp.equals("") && (i < cantNiveles)) {
            imp = preferencias.getString(niveles[i], "");
            try {
                jsonObject = new JSONObject(imp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                vecoresJson[i] = jsonObject.getJSONArray("jugadores");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
    // fin de ambos modulos

    //guarda los jugadores del nivel jugado
    private void guardarSoloUno(int id) throws JSONException {
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor  editor = preferencias.edit();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jugadores",vecoresJson[id]);
        jsonObject.put("cantActual",cantActual);
        String str = jsonObject.toString();
        editor.putString(niveles[id],str);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String stringParaEnviar() throws JSONException {
        String mandar = "";
        JSONObject man;
        int j;
        String dif = getResources().getString(R.string.dificultad),
                nom = getResources().getString(R.string.nombreJugador),
                sco = getResources().getString(R.string.pun),
                sinNombre = getResources().getString(R.string.tipoJugador),
                act;
        int x,
        n = 1;
        for(int i=0; i<=2; i++){
            x = getResources().getIdentifier(niveles[i],"string",getPackageName());
            mandar = mandar + dif + ": " + getResources().getString(x);
            mandar = mandar + System.lineSeparator();
          for(j =0; j<=4; j++){
              man = new JSONObject();
              man = vecoresJson[i].getJSONObject(j);
              act = (String) man.get("jugador");
              if(act.equals("-1"))
                mandar = mandar + sinNombre + " " + n;
              else
                mandar = mandar + act;   	
              mandar = mandar + System.lineSeparator();
              if(man.get("puntuacion").equals("9999"))
                  mandar = mandar + sco + ": " + "-1";
              else
                mandar = mandar + sco + ": " + man.get("puntuacion");
              mandar = mandar + System.lineSeparator();
              n++;
          }
       }
        return mandar;
    }

    //metodo que crea los menues
   public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        ((MenuInflater) mi).inflate(R.menu.ranking_menu, menu);
        return true;
    }

    // metodo para las acciones del menu
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.irAlMenu) {
            this.finish();
        } else if (item.getItemId() == R.id.shareRanking) {
            String dato = "";
            try {
                dato = stringParaEnviar();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, dato);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            }
        }
        return true;
    }
}
