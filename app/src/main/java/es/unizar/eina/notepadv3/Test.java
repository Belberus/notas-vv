package es.unizar.eina.notepadv3;

/*
 * Alberto Martínez Menéndez (681061)
 * Pablo Piedrafita Castañeda (691812)
 */

import android.util.Log;
import java.util.Random;

public class Test {

    /**
     * Execute black box tests related to add a note
     * @param dbAdapter Object of type NotesDbAdapter
     */
    static void pruebaCrearNota(NotesDbAdapter dbAdapter){
        try {
            long caso1 = dbAdapter.createNote("titulo", "cuerpo", 0);
            if(caso1>=0)
                Log.d("TEST CREAR NOTA caso1"," CREADA con id= "+Long.toString(caso1));
            else
                Log.d("TEST NOTA CREAR caso1"," NO creada");
        }catch (Throwable ex){
            Log.d("TEST CREAR NOTA caso1", "EXCEPTION");
        }
        try{
            long caso2 = dbAdapter.createNote(null, "cuerpo", 0);
            if(caso2>=0)
                Log.d("TEST CREAR NOTA caso2"," CREADA con id= "+Long.toString(caso2));
            else
                Log.d("TEST CREAR NOTA caso2"," NO creada");
        }catch (Throwable ex){
            Log.d("TEST CREAR NOTA caso2", "EXCEPTION");
        }
        try {
            long caso3 = dbAdapter.createNote("", "cuerpo", 0);
            if(caso3>=0)
                Log.d("TEST CREAR NOTA caso3"," CREADA con id= "+Long.toString(caso3));
            else
                Log.d("TEST CREAR NOTA caso3"," NO creada");
        }catch (Throwable ex){
        Log.d("TEST CREAR NOTA caso3", "EXCEPTION");
        }
        try{
            long caso4 = dbAdapter.createNote("titulo", null, 0);
            if(caso4>=0)
                Log.d("TEST CREAR NOTA caso4"," CREADA con id= "+Long.toString(caso4));
            else
                Log.d("TEST CREAR NOTA caso4"," NO creada");
        }catch (Throwable ex){
            Log.d("TEST CREAR NOTA caso4", "EXCEPTION");
        }
    }

    /**
     * Execute black box tests related to delete a note
     * @param dbAdapter Object of type NotesDbAdapter
     */
   static void pruebaBorrarNota(NotesDbAdapter dbAdapter){
       try{
           boolean caso1 = dbAdapter.deleteNote(1);
           if(caso1)
               Log.d("TEST BORRAR NOTA caso1"," ELIMINADA");
           else
               Log.d("TEST BORRAR NOTA caso1"," NO eliminada");
       }catch (Throwable ex){
           Log.d("TEST BORRAR NOTA caso1", "EXCEPTION");
       }
       try{
           boolean caso2 = dbAdapter.deleteNote(-1);
           if(caso2)
               Log.d("TEST BORRAR NOTA caso2"," ELIMINADA");
           else
               Log.d("TEST BORRAR NOTA caso2"," NO eliminada");
       }catch (Throwable ex){
           Log.d("TEST BORRAR NOTA caso2", "EXCEPTION");
       }
       try{
           boolean caso3 = dbAdapter.deleteNote(0);
           if(caso3)
               Log.d("TEST BORRAR NOTA caso3"," ELIMINADA");
           else
               Log.d("TEST BORRAR NOTA caso3"," NO eliminada");
       }catch (Throwable ex){
           Log.d("TEST BORRAR NOTA caso3", "EXCEPTION");
       }
   }

    /**
     * Execute black box tests related to edit a note
     * @param dbAdapter Object of type NotesDbAdapter
     */
    static void pruebaModificarNota(NotesDbAdapter dbAdapter){
        try {
            boolean caso1 = dbAdapter.updateNote(1, "titulo", "cuerpo",0);
            if(caso1)
                Log.d("TEST MODIF NOTA caso1"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso1"," NO modificada");
        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso1", "EXCEPTION");
        }
        try{
            boolean caso2 = dbAdapter.updateNote(1, null, "cuerpo",0);
            if(caso2)
                Log.d("TEST MODIF NOTA caso2"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso2"," NO modificada");
        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso2", "EXCEPTION");
        }
        try{
            boolean caso3 = dbAdapter.updateNote(1, "", "cuerpo",0);
            if(caso3)
                Log.d("TEST MODIF NOTA caso3"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso3"," NO modificada");
        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso3", "EXCEPTION");
        }
        try{
            boolean caso4 = dbAdapter.updateNote(1, "titulo", null, 0);
            if(caso4)
                Log.d("TEST MODIF NOTA caso4"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso4"," NO modificada");
        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso4", "EXCEPTION");
        }
        try{
            boolean caso5 = dbAdapter.updateNote(-1, "titulo", "cuerpo", 0);
            if(caso5)
                Log.d("TEST MODIF NOTA caso5"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso5"," NO modificada");

        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso5", "EXCEPTION");
        }
        try{
            boolean caso6 = dbAdapter.updateNote(0, "titulo", "cuerpo", 0);
            if(caso6)
                Log.d("TEST MODIF NOTA caso6"," MODIFICADA");
            else
                Log.d("TEST MODIF NOTA caso6"," NO modificada");
        }catch (Throwable ex){
            Log.d("TEST MODIF NOTA caso6", "EXCEPTION");
        }
    }

    /**
     * Execute volume test trying to create 1000 notes with different title and id
     * and same body "cuerpo"
     * @param dbAdapter Object of type NotesDbAdapter
     */
    static void pruebaVolumen(NotesDbAdapter dbAdapter){
        for (int i=1;i<=1050;i++){
            try {
                long caso1 = dbAdapter.createNote("Nota_"+i, "cuerpo", 0);
                if(caso1>=0)
                    Log.d("TEST VOLUMEN"," CREADA Nota_"+i+" con id= "+Long.toString(caso1));
                else
                    Log.d("TEST VOLUMEN"," NO creada");
            }catch (Throwable ex){
                Log.d("TEST VOLUMEN", "EXCEPTION");
            }
        }
        Log.d("TEST DE VOLUMEN", " ÉXITO");
    }

    /**
     * Execute overload test trying to create as many notes as it is possible
     * with different body in each of them, incrementing the length of body in
     * each new note
     * @param dbAdapter Object of type NotesDbAdapter
     */
    static void pruebaSobrecarga(NotesDbAdapter dbAdapter){
        long salida = 0;
        int i=1;
        while (salida==0){
            try {
                int length = i*100;
                long caso1 = dbAdapter.createNote("Nota_sobreCarga_"+i,random(length), 0);
                if(caso1>=0) {
                    Log.d("TEST SOBRECARGA length ", Integer.toString(length));
                }else {
                    Log.d("TEST SOBRECARGA", " NO creada");
                    salida = -1;
                }
                i++;
            }catch (Throwable ex){
                Log.d("TEST SOBRECARGA", "EXCEPTION");
                salida = -1;
            }
        }
    }

    /**
     * Create a random string of length indicated by the parameter
     * @param length int that represent the max length of the string return
     * @return string of length indicated
     */
    private static String random(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(length);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
