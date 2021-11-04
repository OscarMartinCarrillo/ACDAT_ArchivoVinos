package org.izv.omc.archivosdejamones;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.izv.omc.archivosdejamones.data.*;
import org.izv.omc.archivosdejamones.util.*;

public class CreateActivity extends AppCompatActivity {
    private final static String TAG = CreateActivity.class.getName() + "xyzyz";
    private String fileName;
    protected Button btCreate;
    protected EditText etIDCreate, etNombreCreate, etColorCreate, etBodegaCreate, etOrigenCreate, etGraduacionCreate, etFechaCreate;
    static final List<Long> idVinos = new ArrayList<Long>() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initialize();
    }

    private void initialize() {
        btCreate = findViewById(R.id.btDeleteEdit);

        etIDCreate = findViewById(R.id.etIDCreate);
        etNombreCreate = findViewById(R.id.etNombreEdit);
        etColorCreate = findViewById(R.id.etColorEdit);
        etBodegaCreate = findViewById(R.id.etBodegaEdit);
        etOrigenCreate = findViewById(R.id.etOrigenEdit);
        etGraduacionCreate = findViewById(R.id.etGraduacionEdit);
        etFechaCreate = findViewById(R.id.etFechaEdit);

        fileName = "archivo.txt";

        getIds();

        btCreate.setOnClickListener((View view) -> {
            crearVino();
        });


    }

    private void getIds() {
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
    }

    private String readExternalFile() {
        return readFile(getExternalFilesDir(null), fileName);
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

    private void crearVino() {
        if(comprobarId()){

            Toast.makeText(getApplicationContext(),"ID REPETIDO",Toast.LENGTH_LONG).show();

            etIDCreate.setText("");
        }else {
            Vino v = new Vino();
            try {
                v.setId(Long.parseLong(etIDCreate.getText().toString().trim()));
            } catch (NumberFormatException e) {
                Log.v(TAG, e.toString());
            }
            //Les he puesto trim() para quitar los posibles espacios al inicio o al final
            v.setNombre(etNombreCreate.getText().toString().trim());
            v.setBodega(etBodegaCreate.getText().toString().trim());
            v.setColor(etColorCreate.getText().toString().trim());
            v.setOrigen(etOrigenCreate.getText().toString().trim());

            try {
                v.setGraduacion(Double.parseDouble(etGraduacionCreate.getText().toString().trim()));
            } catch (NumberFormatException e) {
                Log.v(TAG, e.toString());
            }

            try {
                v.setFecha(Integer.parseInt(etFechaCreate.getText().toString().trim()));
            } catch (NumberFormatException e) {
                Log.v(TAG, e.toString());
            }

            writeExternalFile(Csv.getCsv(v));

            finish();
        }
    }

    private boolean comprobarId() {
        if(idVinos.contains(Long.parseLong(String.valueOf(etIDCreate.getText())))){
            return true;
        }else {
            return false;
        }
    }

    private boolean writeFile(File file, String fileName, String string){
        File f = new File(file, fileName);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        boolean ok=true;
        try {
            fw = new FileWriter(f, true);
            fw.write(string+"\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok=false;
            Log.v(TAG, e.toString());
        }
        return ok;
    }

    private void writeExternalFile(String csv) {
        writeFile(getExternalFilesDir(null), fileName, csv);
    }
}