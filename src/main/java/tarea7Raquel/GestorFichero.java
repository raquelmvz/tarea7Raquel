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
        System.out.println("--------------------");
        profesoresBiologiaAPI(lista);
        System.out.println("--------------------");
        apellidosEmpleadosAPI(lista);
        System.out.println("--------------------");
        verificarJohnAPI(lista);

    }

    /* Metodo que lee el fichero */
    public static ArrayList<Empleado> leeFichero(String fichero) {

        /* para guardar los datos que se van leyendo */
        String[] tokens;
        String linea;
        ArrayList<Empleado> empleados = new ArrayList<>();

        /*Instanciaci??n de BufferedReader a partir de un objeto InputStreamReader
        InputStreamReader permite indicar el tipo de codificaci??n del archivo */
        try ( BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fichero), "ISO-8859-1"))) {

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
        try ( BufferedWriter flujo = new BufferedWriter(new FileWriter(fichero))) {

            flujo.write("NOMBRE EMPLEADO, APELLIDOS EMPLEADO, DNI/PASAPORTE, PUESTO, FECHA DE TOMA DE POSESION, FECHA DE CESE, TELEFONO, EVALUADOR, COORDINADOR");
            flujo.newLine(); //salto de linea

            for (Empleado emple : lista) {

                /* si llevan mas de 20 a??os trabajando
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
            if (emple.getPuesto().contains("Inform??tica")) {
                profesInformatica.add(emple);
            }
        }

        return profesInformatica.size();

    }

    /* Para saber si algun profesor de biologia es coordinador */
    public static void profesorBioCoordinador(ArrayList<Empleado> lista) {

        ArrayList<String> profesBioCoord = new ArrayList<>();

        for (Empleado emple : lista) {
            if (emple.getPuesto().contains("Biolog??a")
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
        //en lugar de una lista directamente almaceno el resultado aqui
        long cantidadProfesores = lista.stream() //esta lista stream contiene lo 
                //que haya en la lista completa de profesores
                //al aplicar filter: el stream queda solo con los prof que cumplan la 
                //condicion del filter
                //ese stream solo tendra x resultados -- los que sean
                //de informatica
                .filter(p -> p.getPuesto().contains("Inform??tica"))
                //en lugar de convertir a una lista directamente cuento aqui
                .count();

        System.out.println("NUMERO DE PROFESORES DE INFORMATICA (CON API STREAM): " + cantidadProfesores);

    }

    /* Para saber si algun profesor de biologia es coordinador */
    private static void profesoresBiologiaAPI(ArrayList<Empleado> lista) {
        /*long cantidadProfesBioCoord = lista.stream().filter(p -> (p.getPuesto().contains("Biolog??a")
                && p.isEsCoordinador()))
                .count(); //cuantos casos hay*/

        //esto devuelve un boolean con la respuesta
        //de si hay o no
        boolean hayProfesores = lista.stream().anyMatch(p -> (p.getPuesto().contains("Biolog??a")
                && p.isEsCoordinador()));

        System.out.println("??Existen profesores de biologia"
                + " que sean coordinadores? --> " + hayProfesores);

    }

    /* Lista ordenada alfabeticamente con los apellidos de los empleados cuyo 
    NIF contenga la letra N */
    private static void apellidosEmpleadosAPI(ArrayList<Empleado> lista) {
        List<String> apellidos = lista.stream()
                .filter(p -> p.getDni().contains("N"))
                .map(p -> p.getApellidos())//cojo el objeto pojo
                //y obtengo un string con el apellido
                //tenemos un stream de string con los apellidos
                .sorted()//se esta ordenando segun el orden natural
                //podemos en otro caso poner un criterio personalizado 
                //en los parentesis
                .collect(Collectors.toList());//nos guarda el string en una lista

        System.out.println("LISTA ORDENADA DE APELLIDOS (CON API STREAM)");
        apellidos.forEach(System.out::println);
    }

    /* Verificar que ningun profesor se llama John */
    private static void verificarJohnAPI(ArrayList<Empleado> lista) {
        boolean esJohn = lista.stream()
                .noneMatch(p -> p.getNombre().contains("John"));

        //nonematch verifica que NO se cumple la condicion
        System.out.println("Ningun profesor se llama John -- " + esJohn);
        //System.out.println("Hay " + empleados.size() + " empleados que se llaman John");
    }

    /* Obtener una lista de todas las fechas de cese
    que hay (usando API stream)*/
    public static List<LocalDate> listaFechasCese(ArrayList<Empleado> lista) {

        List<LocalDate> fechas = lista.stream()
                .map(p -> p.getFecCese())
                .filter(p -> p != null)//las fechas que no sean nulas
                .collect(Collectors.toList());

        return fechas;
    }

    /* quien se jubila ma??ana -- apellidos */
    public static List<String> empleadosQueSeJubilan(ArrayList<Empleado> lista, LocalDate fechaJubilacion) {

        List<String> apellidos = lista.stream()
                .filter(p -> p.getFecCese().equals(fechaJubilacion))
                .map(p -> p.getApellidos())
                .collect(Collectors.toList());

        return apellidos;
    }

    //fecha de cese es null !!!
    /* para controlar que los empleados lleven mas de 20 a??os trabajando
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
