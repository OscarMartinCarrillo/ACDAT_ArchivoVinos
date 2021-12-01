package org.izv.omc.archivosdejamones;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import org.izv.omc.archivosdejamones.util.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
        writeExternalFile("");
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

        //Si esta vacio no hay ninguno que editar, asi que lo ocultamos
        if (adapter.isEmpty()) {
            spinner.setVisibility(View.INVISIBLE);
            btEditPrincipal.setVisibility(View.INVISIBLE);
        } else {
            spinner.setVisibility(View.VISIBLE);
            btEditPrincipal.setVisibility(View.VISIBLE);
        }

        tvContenido.setText(tvMensaje);
    }

    private void initialize(){
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

    private String readExternalFile() {
        return FileFunctions.readFile(getExternalFilesDir(null), fileName);
    }

    private void writeExternalFile(String csv) {
        FileFunctions.writeFile(getExternalFilesDir(null), fileName, csv, true, true);
    }
}