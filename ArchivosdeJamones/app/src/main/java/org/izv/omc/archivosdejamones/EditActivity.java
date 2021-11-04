package org.izv.omc.archivosdejamones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.izv.omc.archivosdejamones.util.*;
import org.izv.omc.archivosdejamones.util.*;
import org.izv.omc.archivosdejamones.data.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {
    public static final String TAG= MainActivity.class.getName() + "xyzyx";
    public static final String fileName = "archivo.txt";
    protected TextView tvIDEdit;
    protected EditText etNombreEdit, etBodegaEdit, etOrigenEdit, etGraduacionEdit, etFechaEdit, etColorEdit;
    protected int id;
    protected String match=null;
    protected Button btEditEdit, btDeleteEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle bundle = getIntent().getExtras();
        id = Integer.parseInt(bundle.getString("id"));
        initialize();
    }

    private void initialize() {

        Vino v = cargarContenido(id);

        tvIDEdit = findViewById(R.id.tvIDEdit);
        etNombreEdit = findViewById(R.id.etNombreEdit);
        etBodegaEdit = findViewById(R.id.etBodegaEdit);
        etOrigenEdit = findViewById(R.id.etOrigenEdit);
        etGraduacionEdit = findViewById(R.id.etGraduacionEdit);
        etFechaEdit = findViewById(R.id.etFechaEdit);
        etColorEdit = findViewById(R.id.etColorEdit);
        btEditEdit = findViewById(R.id.btEditEdit);
        btDeleteEdit = findViewById(R.id.btDeleteEdit);

        tvIDEdit.setText(String.valueOf(id));
        etNombreEdit.setText(v.getNombre());
        etBodegaEdit.setText(v.getBodega());
        etOrigenEdit.setText(v.getOrigen());
        etColorEdit.setText(v.getColor());
        etGraduacionEdit.setText(String.valueOf(v.getGraduacion()));
        etFechaEdit.setText(String.valueOf(v.getFecha()));

        btEditEdit.setOnClickListener((View view) -> {
            actionEdit();
            finish();
        });

        btDeleteEdit.setOnClickListener((View view) -> {
            actionDelete();
            finish();
        });

    }

    private void actionEdit() {
        actionDelete();
        formarVino();
    }

    private void formarVino() {
        Vino v = new Vino();
        try{
            v.setId(id);
        }catch (NumberFormatException e){
            Log.v(TAG, e.toString());
        }
        //Les he puesto trim() para quitar los posibles espacios al inicio o al final
        v.setNombre(etNombreEdit.getText().toString().trim());
        v.setBodega(etBodegaEdit.getText().toString().trim());
        v.setColor(etColorEdit.getText().toString().trim());
        v.setOrigen(etOrigenEdit.getText().toString().trim());

        try {
            v.setGraduacion(Double.parseDouble(etGraduacionEdit.getText().toString().trim()));
        } catch (NumberFormatException e) {
            Log.v(TAG, e.toString());
        }

        try {
            v.setFecha(Integer.parseInt(etFechaEdit.getText().toString().trim()));
        } catch (NumberFormatException e) {
            Log.v(TAG, e.toString());
        }

        writeExternalFile(Csv.getCsv(v));
    }

    private void writeExternalFile(String csv) {
        writeFile(getExternalFilesDir(null), fileName, csv, true);
    }

    private void actionDelete() {
        String raw = readExternalFile();

        String lines[] = raw.split("\n");
        String tvMensaje="";
        Vino vAux = new Vino();

        for (String strTemp: lines) {
            if (!strTemp.equals("") && !strTemp.equals("\n")){
                vAux = Csv.getVino(strTemp);

                if (vAux.getId() != id){
                    tvMensaje += strTemp + "\n";
                }
            }
        }
        writeFile(getExternalFilesDir(null), fileName, tvMensaje, false);
    }

    private boolean writeFile(File file, String fileName, String string, Boolean append){
        File f = new File(file, fileName);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        boolean ok=true;
        try {
            fw = new FileWriter(f, append);
            fw.write(string+"\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok=false;
            Log.v(TAG, e.toString());
        }
        return ok;
    }

    private Vino cargarContenido(int id) {
        String raw = readExternalFile();

        String lines[] = raw.split("\n");

        Vino v2 = null;
        for (String strTemp: lines) {
            if (!strTemp.equals("") && !strTemp.equals("\n")){
                Vino v = Csv.getVino(strTemp);

                if (v.getId() == id){
                    match = strTemp;
                    v2 = v;
                }
            }
        }
        return v2;
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
}