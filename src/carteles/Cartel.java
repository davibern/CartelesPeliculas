package carteles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que contiene la definición de la película
 * 
 * @author David Bernabé
 * @version 1.0
 */
public class Cartel implements Serializable{
    
    // Atributos
    private String nombre;
    private int anyo;
    private String ruta;
    private static final long serialVersionUID = 54L;
    public static final String RUTA_FICHERO = "src//data//peliculas.dat";
    private static List<Cartel> listaPeliculas = new LinkedList<>();

    /**
     * Contructor de la clase Cartel
     * 
     * @param nombre Nombre de la película
     * @param anyo Año de estreno de la película
     * @param ruta Ruta donde se guarda el cartel de cine
     */
    public Cartel(String nombre, int anyo, String ruta) {
        this.nombre = nombre;
        this.anyo = anyo;
        this.ruta = ruta;
    }
    
    public Cartel() {
        
    }
    
    /**
     * Método que devuelve el nombre de la película
     * @return Nombre de la película
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Método que devuelve el año de estreno
     * @return Año de estreno de la película
     */
    public int getAnyo() {
        return anyo;
    }
    
    /**
     * Método que devuelve la ruta del cartel de la película
     * @return Ruta de la película
     */
    public String getRuta() {
        return ruta;
    }
    
    public String getRutaFichero() {
        return this.RUTA_FICHERO;
    }
    
    /**
     * Método que cambia el nombre de la película
     * @param nombre Nuevo nombre de la película
     */
    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método que cambia el año del estreno de la película
     * @param anyo Nuevo año del estreno de la película
     */
    private void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    /**
     * Método que cambia la ruta del cartel de la película
     * @param ruta Nueva ruta del cartel de la película
     */
    private void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    /**
     * Método sobrecargado que devuelve los datos de la película
     * @return Devuelve los datos de la película
     */
    @Override
    public String toString() {
        return this.nombre + " # (" + this.anyo + ") # " + this.ruta;
    }
    
    /**
     * Método estático que crea un fichero serializado con tres películas de pruebas
     */
    public static void crearFicheroSerializado() {
        
        Cartel pelicula1 = new Cartel("El Señor de los Anillos, La Comunidad del Anillo", 2001, "src//media//la-comunidad-del-anillo.jpg");
        Cartel pelicula2 = new Cartel("El Señor de los Anillos, Las Dos Torres", 2002, "src//media//las-dos-torres.jpg");
        Cartel pelicula3 = new Cartel("El Señor de los Anillos, El Retorno del Rey", 2003, "src//media//el-retorno-del-rey.jpg");
        listaPeliculas.add(pelicula1);
        listaPeliculas.add(pelicula2);
        listaPeliculas.add(pelicula3);
        
        try(FileOutputStream fichero = new FileOutputStream(RUTA_FICHERO);
            ObjectOutputStream oos = new ObjectOutputStream(fichero)){
            oos.writeObject(listaPeliculas);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
       
    }
    
    /**
     * Método que genera un fichero serializado vacío
     */
    public static void crearFicheroSerializadoVacio() {
                
        try(FileOutputStream fichero = new FileOutputStream(RUTA_FICHERO);
            ObjectOutputStream oos = new ObjectOutputStream(fichero)) {
            oos.writeObject(listaPeliculas);
            oos.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    /**
     * Método estático que imprime por pantalla lo que contiene el fichero serializado
     */
    public static void leerFicheroSerializado () {
        
        try(FileInputStream fichero = new FileInputStream(RUTA_FICHERO);
            ObjectInputStream objFichero = new ObjectInputStream(fichero)){
            
            List <Cartel> listaCarteles = new LinkedList<>();
            listaCarteles = (List<Cartel>) objFichero.readObject();
            
            System.out.println("El fichero tiene: " + listaCarteles.size() + " peliculas.");
            for(Cartel c : listaCarteles)
                System.out.println(c.toString());
        
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
}
