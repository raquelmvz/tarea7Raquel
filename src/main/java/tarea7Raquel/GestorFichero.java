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

        //aqui se ejecuta el metodo de lectura del fichero
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

                tmp.setNombre(formateaTexto(tokens[0] + tokens[1]));
                tmp.setDni(formateaTexto(tokens[2]));
                tmp.setPuesto(formateaTexto(tokens[3]));
                //para la fecha hay que tener en cuenta el formato en el que aparece
                //en el fichero --> dd/MM/yyyy
                //ademas hay que quitarle las comillas "" 
                tmp.setFecTomaPosesion(conversionFecha(formateaTexto(tokens[4])));
                tmp.setFecCese(conversionFecha(formateaTexto(tokens[5])));
                tmp.setTelefono(formateaTexto(tokens[6]));
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

        System.out.println("----LECTURA DEL FICHERO----");
        for (Empleado em : empleados) {
            System.out.println(em);
        }

    }

    private static LocalDate conversionFecha(String fecha) {

        //hay empleados sin fecha de cese y salta una excepcion
        if (fecha.equals("")) {
            return null;
        } else {
            return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

    }

    //metodo para quitar las comillas a todos los datos que
    //se recogen en el fichero
    private static String formateaTexto(String texto) {
        return texto.substring(1, texto.length() - 1);
    }

}
