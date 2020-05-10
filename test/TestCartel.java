
import carteles.Cartel;

/**
 * Clase de prueba que ejecuta métodos de Cartel
 * 
 * @author David Bernabé
 * @version 1.0
 */
public class TestCartel {
    
    /**
     * Método que lanza la aplicaciónd de Test
     * @param args 
     */
    public static void main(String[] args) {
        
        crearFichero();
        //crearFicheroVacio();
        leerFichero();
        
    }
    
    public static void crearFichero() {
        Cartel.crearFicheroSerializado();
    }
    
    public static void crearFicheroVacio() {
        Cartel.crearFicheroSerializadoVacio();
    }
    
    public static void leerFichero() {
        Cartel.leerFicheroSerializado();
    }
    
}
