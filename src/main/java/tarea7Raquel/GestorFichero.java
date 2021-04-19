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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        /* AMPLIACION */
        System.out.println("\n\n--AMPLIACION----\n"
                + "Numero de profesores de informatica");
        System.out.println(contadorProfesoresInfo(lista));
        System.out.println("--------------------");
        profesorBioCoordinador(lista);
        System.out.println("--------------------");
        listaEmpleadosSeleccionadosOrdenada(lista, "N");
        System.out.println("--------------------");
        verificarNombreProfesor(lista, "John");
        System.out.println("--------------------");
        System.out.println("---------API STREAM---------");
        profesoresInformaticaAPI(lista);

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

                tmp.setApellidos(tokens[0].substring(1, tokens[0].length()));
                tmp.setNombre(tokens[1].substring(1, tokens[1].length() - 1));
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

            flujo.write("NOMBRE EMPLEADO, APELLIDOS EMPLEADO, DNI/PASAPORTE, PUESTO, FECHA DE TOMA DE POSESION, FECHA DE CESE, TELEFONO, EVALUADOR, COORDINADOR");
            flujo.newLine(); //salto de linea

            for (Empleado emple : lista) {

                /* si llevan mas de 20 años trabajando
                hay que tener en cuenta que la fecha de cese debe ser posterior a la actual */
                if (((ChronoUnit.YEARS.between(emple.getFecTomaPosesion(), LocalDate.now())) > 20)
                        && (emple.getFecCese() == null)) {

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

    /* AMPLIACION */
 /* Contar el num de profesores de informatica */
    public static int contadorProfesoresInfo(ArrayList<Empleado> lista) {

        ArrayList<Empleado> profesInformatica = new ArrayList<>();

        for (Empleado emple : lista) {
            if (emple.getPuesto().contains("Informática")) {
                profesInformatica.add(emple);
            }
        }

        return profesInformatica.size();

    }

    /* Para saber si algun profesor de biologia es coordinador */
    public static void profesorBioCoordinador(ArrayList<Empleado> lista) {

        ArrayList<String> profesBioCoord = new ArrayList<>();

        for (Empleado emple : lista) {
            if (emple.getPuesto().contains("Biología")
                    && (emple.isEsCoordinador())) {
                profesBioCoord.add(emple.getNombre() + " " + emple.getApellidos() + " | " + emple.getDni());
            }
        }

        System.out.println("LISTA DE PROFESORES DE BIOLOGIA COORDINADORES:");
        profesBioCoord.forEach(System.out::println);

    }

    /* Lista ordenada alfabeticamente con los apellidos de los empleados cuyo 
    NIF contenga la letra N */
    public static void listaEmpleadosSeleccionadosOrdenada(ArrayList<Empleado> lista, String letraDNI) {

        ArrayList<String> apellidos = new ArrayList<>();

        for (Empleado emple : lista) {
            if (emple.getDni().contains(letraDNI)) {
                apellidos.add(emple.getApellidos() + " | " + emple.getDni());
            }
        }

        Collections.sort(apellidos);

        System.out.println("LISTA APELLIDOS ORDENADOS ALFABETICAMENTE CUYO DNI CONTIENE"
                + " LA LETRA " + letraDNI);
        apellidos.forEach(System.out::println);

    }

    /* Verificar que ningun profesor se llama John */
    public static void verificarNombreProfesor(ArrayList<Empleado> lista, String nombre) {

        ArrayList<String> nombres = new ArrayList<>();

        for (Empleado emple : lista) {
            if (emple.getNombre().contains(nombre)) {
                nombres.add(nombre);
            }
        }

        System.out.println("Hay " + nombres.size() + " empleados que se llamen " + nombre);

    }

    /* AMPLIACION CON API */
 /* Contar el num de profesores de informatica */
    private static void profesoresInformaticaAPI(ArrayList<Empleado> lista) {
        List<Empleado> profesores = lista.stream().filter(p -> p.getPuesto().contains("Informática"))
                .collect(Collectors.toList());

        System.out.println("PROFESORES DE INFORMATICA (CON API STREAM)");
        profesores.forEach(System.out::println);
    }

    //fecha de cese es null !!!
    /* para controlar que los empleados lleven mas de 20 años trabajando
    si la fecha de cese no existe es que siguen trabajando
    si la fecha de cese es anterior a la de hoy no sirve */
//    private static boolean fechaCeseValida(LocalDate fecCese) {
//        if (fecCese == null) {
//            return true;
//        } else {
//            return fecCese.isAfter(LocalDate.now());
//        }
//    }
}
