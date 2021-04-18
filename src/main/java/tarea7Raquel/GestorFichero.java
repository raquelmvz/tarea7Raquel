/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea7Raquel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 *
 * @author raquel
 */
public class GestorFichero {

    public static void main(String[] args) {

        String idFichero = "RelPerCen.csv"; //fichero a leer
        ArrayList<Empleado> lista; //lista de donde leer los objetos (para la parte de escritura)
        lista = leeFichero(idFichero); //aqui se ejecuta el metodo de lectura del fichero
        String idFichero2 = "ListaEmpleados.csv"; //fichero donde escribir
        escribeFichero(idFichero2, lista); //aqui se ejecuta el metodo de escritura

    }

    /* Metodo que lee el fichero */
    public static ArrayList<Empleado> leeFichero(String fichero) {

        /* para guardar los datos que se van leyendo */
        String[] tokens;
        String linea;
        ArrayList<Empleado> empleados = new ArrayList<>();

        /*Instanciación de BufferedReader a partir de un objeto InputStreamReader
        InputStreamReader permite indicar el tipo de codificación del archivo */
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichero), "ISO-8859-1"))) {

            br.readLine(); //elimino la primera linea (cabecera)

            //mientras el metodo readline no devuelva null es que existen datos por leer
            while ((linea = br.readLine()) != null) {

                tokens = linea.split(","); //se guarda cada elemento de la linea en funcion del separador

                Empleado tmp = new Empleado();

                tmp.setNombre(formateaTexto(tokens[0] + tokens[1]));
                tmp.setDni(formateaTexto(tokens[2]));
                tmp.setPuesto(formateaTexto(tokens[3]));
                /*para la fecha hay que tener en cuenta el formato en el que aparece
                en el fichero --> dd/MM/yyyy
                ademas hay que quitarle las comillas "" */
                tmp.setFecTomaPosesion(conversionFecha(formateaTexto(tokens[4])));
                tmp.setFecCese(conversionFecha(formateaTexto(tokens[5])));
                tmp.setTelefono(formateaTexto(tokens[6]));
                tmp.setEsEvaluador(!tokens[7].equalsIgnoreCase("No")); //"no" --> set esevaluador a false
                tmp.setEsCoordinador(!tokens[8].equalsIgnoreCase("No")); //"no" --> set escoordinador a false

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

        return empleados;

    }

    /* Devuelve una fecha a partir de un string que tiene el formato
    del archivo csv
     */
    private static LocalDate conversionFecha(String fecha) {

        //hay empleados sin fecha de cese y salta una excepcion
        if (fecha.equals("")) {
            return null;
        } else {
            return LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

    }

    /* Metodo para quitar las comillas a todos los datos que
    se recogen en el fichero */
    private static String formateaTexto(String texto) {
        return texto.substring(1, texto.length() - 1);
    }

    /* Metodo que escribe un fichero */
    public static void escribeFichero(String fichero, ArrayList<Empleado> lista) {

        /* Estructura try-with-resources. Instancia el objeto con el fichero a escribir
        y se encarga de cerrar el recurso "flujo" una vez finalizadas las operaciones */
        try (BufferedWriter flujo = new BufferedWriter(new FileWriter(fichero))) {

            flujo.write("NOMBRE EMPLEADO, DNI/PASAPORTE, PUESTO, FECHA DE TOMA DE POSESION, FECHA DE CESE, TELEFONO, EVALUADOR, COORDINADOR");
            flujo.newLine(); //salto de linea

            for (Empleado emple : lista) {

                /* si llevan mas de 20 años trabajando
                hay que tener en cuenta que la fecha de cese debe ser posterior a la actual */
                if (((ChronoUnit.YEARS.between(emple.getFecTomaPosesion(), LocalDate.now())) > 20)
                        && (fechaCeseValida(emple.getFecCese()))) {

                    flujo.write(emple.toString()); //usamos el metodo write para escribir en el buffer
                    flujo.newLine();  //salto de linea

                }

            }
            flujo.flush(); //flush para guardar cambios
            System.out.println("----ESCRITURA DE FICHERO----");
            System.out.println("El fichero se ha creado");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /* para controlar que los empleados lleven mas de 20 años trabajando
    si la fecha de cese no existe es que siguen trabajando
    si la fecha de cese es anterior a la de hoy no sirve */
    private static boolean fechaCeseValida(LocalDate fecCese) {
        if (fecCese == null) {
            return true;
        } else {
            return fecCese.isAfter(LocalDate.now());
        }
    }

}
