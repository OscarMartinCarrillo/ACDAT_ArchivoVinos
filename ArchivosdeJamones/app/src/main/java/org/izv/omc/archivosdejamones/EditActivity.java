package org.izv.omc.archivosdejamones;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.izv.omc.archivosdejamones.util.*;
import org.izv.omc.archivosdejamones.data.*;

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
        });

        btDeleteEdit.setOnClickListener((View view) -> {
            actionDelete();
            finish();
        });

    }

    private void actionEdit() {
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

        //Si los campos estan rellenos se elimina el actual y se crea el nuevo
        if (v.isValid()){
            actionDelete();
            writeExternalFile(Csv.getCsv(v));
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Rellene los campos", Toast.LENGTH_LONG).show();
        }
    }

    private void writeExternalFile(String csv) {
        FileFunctions.writeFile(getExternalFilesDir(null), fileName, csv, true, false);
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
        FileFunctions.writeFile(getExternalFilesDir(null), fileName, tvMensaje, false, false);
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

    private String readExternalFile() {
        return FileFunctions.readFile(getExternalFilesDir(null), fileName);
    }



}