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
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase que genera la interfaz de la aplicación
 * 
 * @author David Bernabé
 * @version 1.0
 */
public class CartelesDeCineJavaFX extends Application implements Serializable {
    
    // Atributos
    private VBox vBox = new VBox(10);
    private GridPane gridPane = new GridPane();
    private MenuBar menuBar = new MenuBar();
    private Menu menuArchivo = new Menu("Archivo");
    private Menu menuEdicion = new Menu("Edición");
    private Menu menuOpciones = new Menu("Opciones");
    private MenuItem menuAgregar = new MenuItem("Agregar");
    private MenuItem menuBorrar = new MenuItem("Borrar");
    private MenuItem menuGuardar = new MenuItem("Guardar");
    private MenuItem menuCerrar = new MenuItem("Cerrar");
    private MenuItem menuDatos = new MenuItem("Generar fichero con datos");
    private MenuItem menuDatosVacios = new MenuItem("Generar fichero vacío");
    private Label lbl = new Label("Película - Año - Ruta");
    private Button btnCartel = new Button("Ver Cartel");
    private ListView<Cartel> listaCarteles;
    private List<Cartel> lista;
    private FileInputStream imagen;
    private Image image;
    private ImageView imageView;
    private static final long serialVersionUID = 54L;
    private int longitudLista;
    private Scene scene;
    
    /**
     * Método que lanza la interfaz del programa
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        this.crearVentana(primaryStage);
    }
    
    /**
     * Método que lanza el programa
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    /**
     * Método que carga una imagen por defecto de una sala de cine
     */
    private void cartelDefecto() {
        
        try {
            this.imagen = new FileInputStream("src//media//cine.jpg");
            this.image = new Image(imagen);
            this.imageView = new ImageView(image);
            this.imageView.setScaleX(0.75);
            this.imageView.setScaleY(0.75);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    /**
     * Método que carga la lista de películas de un fichero serializado
     */
    private void listarPeliculas() {
        
        // Instancio un LinkedList un ListView vacío
        lista = new LinkedList<>();
        listaCarteles = new ListView<>();
        Cartel cartel = new Cartel();
        
        // Leo un fichero binario guardado en src/data/peliculas.dat
        try(FileInputStream fichero = new FileInputStream(cartel.getRutaFichero());
            ObjectInputStream objFichero = new ObjectInputStream(fichero)){
            
            lista = (List<Cartel>) objFichero.readObject();
            
            // Validaaciones previas
            if(lista.isEmpty()) {
                this.mensaje("No se cargaron datos previos en la aplicación.\nEs posible que no exista el fichero o que esté vacío.");
            } else {
                // Se cumplimenta el ListView con los datos del fichero
                lista.forEach((s) -> {
                    this.listaCarteles.getItems().add(s);
                });
                // Guardo en un entero la cantidad de películas. Esto se usará para comprobar si ha habido cambios que no se hayan guardado.
                longitudLista = lista.size();
            }
        // Excepciones mostradas al usuario    
        } catch (FileNotFoundException ex) {
            this.mensaje("No se cargaron datos previos en la aplicación.\nEs posible que no exista el fichero o que esté vacío.");
        } catch (IOException | ClassNotFoundException ex) {
           this.mensaje("No ha sido posible leer el fichero.");
        }
        
    }
    
    /**
     * Método que crea el layout GridPane y lo configura
     */
    private void crearMarcoCentral() {
        
        ColumnConstraints columnaLista = new ColumnConstraints();
        columnaLista.setPrefWidth(400);
        ColumnConstraints columnaCartel = new ColumnConstraints();
        columnaCartel.setPrefWidth(200);
        gridPane.getColumnConstraints().addAll(columnaLista, columnaCartel);
        
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.add(listaCarteles, 0, 0);
        gridPane.add(imageView, 1, 0);
        gridPane.add(btnCartel, 0, 2);
        
    }
    
    /**
     * Método que configura y crea la ventana principal
     * @param primaryStage Stage principal
     */
    public void crearVentana(Stage primaryStage) {
        
        // Menú del programa y submenus
        menuBar.getMenus().addAll(menuArchivo, menuEdicion, menuOpciones);
        menuArchivo.getItems().addAll(menuGuardar, menuCerrar);
        menuEdicion.getItems().addAll(menuAgregar, menuBorrar);
        menuOpciones.getItems().addAll(menuDatos, menuDatosVacios);
        
        // Se ejecutan los métodos que crean y configuran los layouts y nodos de las ventanas
        this.cartelDefecto();
        this.listarPeliculas();
        this.crearMarcoCentral();
        
        // Posición de la etiqueta
        lbl.setPadding(new Insets(10, 0, 0, 10));
        
        // Layout del menú, lista de películas, imagenes de carteles y botón de añadir
        vBox.getChildren().addAll(menuBar, lbl, gridPane);
        
        // Eventos
        this.menuAgregar.setOnAction(e -> this.agregarPelicula(primaryStage));
        this.menuBorrar.setOnAction(e -> this.eliminarPelicula());
        this.btnCartel.setOnAction(e -> this.verCartel());
        this.listaCarteles.getSelectionModel().selectedItemProperty().addListener(e -> this.imageView.setVisible(false));
        this.menuGuardar.setOnAction(e -> this.guardar());
        this.menuCerrar.setOnAction(e -> this.cerrar(primaryStage));
        this.menuDatos.setOnAction(e -> this.generarFicheroDatos());
        this.menuDatosVacios.setOnAction(e -> this.generarFicheroVacio());
        
        // Creación de la escenae
        this.scene = new Scene(vBox, 800, 580);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Cartel de Películas");
        primaryStage.show();
        
    }
    
    /**
     * Método que actualiza el ListView de películas
     */
    public void actualizarListaPeliculas() {
        
        // Limpio la lista actual de películas
        this.listaCarteles.getItems().clear();
        
        // Recorro la lista de carteles actual y la vuelvo a añadir a la ListView
        this.lista.forEach((cartel) -> {
            this.listaCarteles.getItems().add(cartel);
        });
        
    }
    
    /**
     * Método que añade una pelicula a la lista de películas y actualiza el ListView
     * 
     * @param primaryStage Stage principal
     */
    public void agregarPelicula(Stage primaryStage) {
        
        DialogAltaCartel nuevaPelicula = new DialogAltaCartel(primaryStage);
        nuevaPelicula.showAndWait();

        if(!nuevaPelicula.esCancelado())
            this.lista.add(nuevaPelicula.getCartel());

        this.actualizarListaPeliculas();
        
    }
    
    /**
     * Método que elimina una película de la lista de películas y actualiza el ListView
     */
    public void eliminarPelicula() {
        
        // Guardo la posición de la película de la ListView para poder luego eliminarla del LinkedList
        int posicion = this.listaCarteles.getSelectionModel().getSelectedIndex();
        // Creación y modificación de la ventana de confirmación
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setContentText("¿Estás seguro de querer eliminar la película de la lista?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        
        // Si el usuario indica que sí se elimina se procede, de lo contrario se cierra la ventana
        if(resultado.get() == ButtonType.OK) {
            
            // Se comprueba que se ha seleccionado previamente una película de lista, sino salta mensaje de aviso para el usuario
            try {
                System.out.println(this.listaCarteles.getSelectionModel().getSelectedIndex());
                this.lista.remove(posicion);
                this.actualizarListaPeliculas();
            } catch (IndexOutOfBoundsException e) {
                this.mensaje("No has seleccionado ninguna película para eliminar.");
            }
        
        } else {
            alerta.close();
        }
        
    }
    
    /**
     * Método para ver el cartel de una película seleccionada
     */
    public void verCartel() {
        
        String pelicula = this.listaCarteles.getSelectionModel().getSelectedItem().toString();
        String[] cartel = pelicula.split("#");
        
        // Se comprueba que la imagen existe, si es así, se cambia la imagen de la imageView
        // Si no sale un mensaje indicando que no existe
        try {

            this.imagen = new FileInputStream(cartel[2].trim());
            this.image = new Image(imagen);
            this.imageView.setImage(image);
            this.imageView.setFitHeight(400);
            this.imageView.setFitWidth(350);
            this.imageView.setVisible(true);
            
        } catch (FileNotFoundException ex) {
            this.mensaje("No es posible mostrar el cartel de la película.");
            
        }

    }
    
    /**
     * Método que guarda la lista de películas en un fichero serializado
     */
    public void guardar() {
        
        // Seleciono el fichero donde se guardará y lo formalizo con los datos de la lista LinkedList
        try(FileOutputStream fichero = new FileOutputStream(Cartel.RUTA_FICHERO);
            ObjectOutputStream oos = new ObjectOutputStream(fichero)){
            oos.writeObject(this.lista);
            oos.close();
            this.mensaje("Se ha guardado el fichero correctamente.");
        } catch (FileNotFoundException es) {
            this.mensaje("No es posible encontrar el fichero.");
        } catch (IOException ex) {
            this.mensaje("No es posible guardar el fichero.");
        }
        
    }
    
    /**
     * Método que cierra la aplicación. Detecta en el cierre si ha habido cambios para preguntar si queremos guardar los cambios.
     * 
     * @param primaryStage Stage principal
     */
    public void cerrar(Stage primaryStage) {
        
        // Si detecto que la longitud de la lista ha variado pregunto si quiero guardar el fichero actualizado, si no se cierra sin más
        if(longitudLista != lista.size()) {
            System.out.println("Ha cambiado la lista");
            Alert alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("¿Deseas guardar los cambios?");
            alerta.setContentText("Se han producido cambios que no han sido guardados.\n¿Deseas conservar los cambios?");
            Optional<ButtonType> resultado = alerta.showAndWait();
            
            if(resultado.get() == ButtonType.OK) 
                this.guardar();
                primaryStage.close();
        } else {
            primaryStage.close();
        }
        
    }
    
    /**
     * Método que genera un fichero de prueba con datos.
     */
    private void generarFicheroDatos() {
        Cartel.crearFicheroSerializado();
        this.mensaje("Reinicia el programa para que tomen efecto los cambios.");
    }
    
    /**
     * Método que genera un fichero de prueba sin datos
     */
    private void generarFicheroVacio() {
        Cartel.crearFicheroSerializadoVacio();
        this.mensaje("Reinicia el programa para que tomen efecto los cambios.");
    }
    
    /**
     * Método que genera un mensaje de alerta con texto personalizado según el parámetro error
     * @param error Texto del mensaje de error que se le muestra al usuario
     */
    private void mensaje(String error) {
        
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Listado de películas");
        alerta.setContentText(error);
        alerta.showAndWait();
        
    }
    
}
