/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea7Raquel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author raquel
 */
public class GestorFichero {

    public static void main(String[] args) {

        /* Lectura del fichero */
        String idFichero = "RelPerCen.csv";

        //para guardar los datos que se van leyendo
//        String[] tokens;
//        String linea;
//        ArrayList<Empleado> empleados = new ArrayList<>();
        leeFichero(idFichero);
    }

    /* Metodo que lee el fichero */
    public static void leeFichero(String fichero) {

        //para guardar los datos que se van leyendo
        String[] tokens;
        String linea;
        ArrayList<Empleado> empleados = new ArrayList<>();

        // Instanciación de BufferedReader a partir de un objeto InputStreamReader
        // InputStreamReader permite indicar el tipo de codificación del archivo
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichero), "ISO-8859-1"))) {

            //elimino la primera linea (cabecera)
            br.readLine();

            //mientras el metodo readline no devuelva null es que existen datos por leer
            while ((linea = br.readLine()) != null) {

                //se guarda en el array cada elemento de la linea en funcion
                //del separador
                tokens = linea.split(",");

                Empleado tmp = new Empleado();

                tmp.setNombre(tokens[0] + tokens[1]);
                tmp.setDni(tokens[2].substring(1, tokens[2].length() - 1));
                tmp.setPuesto(tokens[3].substring(1, tokens[3].length() - 1));
                //para la fecha hay que tener en cuenta el formato en el que aparece
                //en en fichero --> dd/MM/yyyy
                //ademas hay que quitarle las comillas "" 
                tmp.setFecTomaPosesion(conversionFecha(tokens[4].substring(1, tokens[4].length() - 1)));
                tmp.setFecCese(conversionFecha(tokens[5].substring(1, tokens[5].length() - 1)));
                tmp.setTelefono(tokens[6].substring(1, tokens[6].length() - 1));
                //si en el token pone que no es --> set esevaluador a false
                tmp.setEsEvaluador(!tokens[7].equalsIgnoreCase("No"));
                //si en el token pone que no es --> set escoordinador a false
                tmp.setEsCoordinador(!tokens[8].equalsIgnoreCase("No"));

                empleados.add(tmp);

            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        for (Empleado em : empleados) {
            System.out.println(em);
        }

    }

    public static LocalDate conversionFecha(String fecha) {

        //hay empleados sin fecha de cese y salta una excepcion
        if (fecha.equals("")) {
            return null;
        } else {
            return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

    }

}
