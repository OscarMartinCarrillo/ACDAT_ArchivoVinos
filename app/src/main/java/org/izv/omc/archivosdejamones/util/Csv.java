package org.izv.omc.archivosdejamones.util;

import android.util.Log;

import org.izv.omc.archivosdejamones.MainActivity;
import org.izv.omc.archivosdejamones.data.*;


public class Csv {
    private static final String TAG= MainActivity.class.getName() + "xyzyx";

    public static Vino getVino(String str) {
        String[] atributos = str.split(";");
        Vino v = null;
        if (atributos.length == 7) {
            v = new Vino();
            try{
                v.setId(Long.parseLong(atributos[0].trim()));
            }catch (NumberFormatException e){
                Log.v(TAG, e.toString());
            }
            //Les he puesto trim() para quitar los posibles espacios al inicio o al final
            v.setNombre(atributos[1].trim());
            v.setBodega(atributos[2].trim());
            v.setColor(atributos[3].trim());
            v.setOrigen(atributos[4].trim());

            try {
                v.setGraduacion(Double.parseDouble(atributos[5].trim()));
            } catch (NumberFormatException e) {
                Log.v(TAG, e.toString());
            }

            try {
                v.setFecha(Integer.parseInt(atributos[6].trim()));
            } catch (NumberFormatException e) {
                Log.v(TAG, e.toString());
            }
        }
        return v;
    }

    public static String getCsv(Vino v) {
        return  v.getId() + "; " +
                v.getNombre() + "; " +
                v.getBodega() + "; " +
                v.getColor() + "; " +
                v.getOrigen() + "; " +
                v.getGraduacion() + "; " +
                v.getFecha() + ";";
    }

}
