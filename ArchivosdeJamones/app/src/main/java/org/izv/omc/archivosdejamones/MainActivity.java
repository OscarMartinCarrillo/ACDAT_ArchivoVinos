package org.izv.omc.archivosdejamones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.izv.omc.archivosdejamones.data.*;
import org.izv.omc.archivosdejamones.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG= MainActivity.class.getName() + "xyzyx";
    public static final String fileName = "archivo.txt";
    protected Button btAddPrincipal, btEditPrincipal;
    protected TextView tvContenido;
    protected Spinner spinner;
    static final List<Long> idVinos = new ArrayList<Long>() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeExternalFile2("");
        initialize();
    }

    //para que los cargue bien
    @Override
    protected void onRestart() {
        super.onRestart();
        cargarContenido();
    }

    private void cargarContenido() {
        String raw = readExternalFile();

        String lines[] = raw.split("\n");
        String tvMensaje="";
        idVinos.clear();

        for (String strTemp: lines) {
            if (!strTemp.equals("") && !strTemp.equals("\n")){
                idVinos.add(Csv.getVino(strTemp).getId());
                tvMensaje += Csv.getVino(strTemp) + "\n";
            }
        }

        //Cargamos el Spinner con el array
        ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(this,
                android.R.layout.simple_spinner_item, idVinos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tvContenido.setText(tvMensaje);
    }

    private void initialize(){


        //Si no fuese estatico los metodos de CSV se crearian objetos solo para eso
        //String csv;
        //Csv csv = new Csv();
        //String csvs = csv.getCsv(v);

        /*
        String csv = Csv.getCsv(v);
        Log.v(TAG, csv);
        Vino v2 = Csv.getVino(csv);
        Log.v(TAG, v2.toString());
         */


        btAddPrincipal = findViewById(R.id.btAddPrincipal);
        btEditPrincipal = findViewById(R.id.btEditPrincipal);
        tvContenido = findViewById(R.id.tvContenido);
        spinner = findViewById(R.id.spinner);

        cargarContenido();

        btAddPrincipal.setOnClickListener((View view) ->{
            openAdd();
        });

        btEditPrincipal.setOnClickListener((View view) -> {
            openEdit();
        });
    }

    private void openAdd() {
        Intent intent = new Intent(this, CreateActivity.class);

        startActivity(intent);
    }

    private void openEdit() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", spinner.getSelectedItem().toString());
        startActivity(intent);
    }

    private String readFile(File file, String fileName) {
        File f = new File(file, fileName);
        String texto = "";

        try (BufferedReader br = new BufferedReader(new FileReader((f)))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                texto += linea + "\n";
            }
        } catch (IOException e) {
            texto = null;
            Log.v(TAG, e.toString());
        }
        return texto;
    }

    private String readExternalFile() {
        return readFile(getExternalFilesDir(null), fileName);
    }

    private boolean writeFile2(File file, String fileName, String string){
        File f = new File(file, fileName);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        boolean ok=true;
        try {
            fw = new FileWriter(f, true);
            fw.write(string);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok=false;
            Log.v(TAG, e.toString());
        }
        return ok;
    }

    private void writeExternalFile2(String csv) {
        writeFile2(getExternalFilesDir(null), fileName, csv);
    }
}