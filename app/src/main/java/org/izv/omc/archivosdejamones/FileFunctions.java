package org.izv.omc.archivosdejamones;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileFunctions {
    public static final String TAG= FileFunctions.class.getName() + "xyzyx";

    public static String readFile(File file, String fileName) {
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

    public static void writeFile(File file, String fileName, String string, Boolean append, Boolean fromMain){
        File f = new File(file, fileName);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        try {
            fw = new FileWriter(f, append);
            if(fromMain) {
                fw.write(string);
            }else{
                fw.write(string+"\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            Log.v(TAG, e.toString());
        }
    }
}
