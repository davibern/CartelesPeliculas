package carteles;

import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase que hereda de Stage y que crea una nueva ventana para añdir datos de nuevas películas
 * 
 * @author David Bernabé Palanco
 * @version 1.0
 */
public class DialogAltaCartel extends Stage {
    
    // Atributos
    private GridPane gridPane = new GridPane();
    private VBox vBox = new VBox(10);
    private HBox hBox = new HBox(10);
    private final Label lblNombre = new Label("Película:");
    private final Label lblEstreno = new Label("Estreno:");
    private final TextField txtNombre = new TextField();
    private final TextField txtEstreno = new TextField();
    private final TextField txtCartel = new TextField();
    private final Button btnCartel = new Button("Elegir imagen");
    private final Button btnAceptar = new Button("Aceptar");
    private final Button btnCancelar = new Button("Cancelar");
    private final FileChooser btnAdjuntarCartel = new FileChooser();
    private File capturaCartel;
    private Cartel pelicula;
    private Scene scene;
    private boolean cancelado = true;
    
    /**
     * Constructor de la clase que genera el formulario para obtener los datos de nuevas películas
     * @param parentStage Stage principal
     */
    public DialogAltaCartel (Stage parentStage) {
        
        // Método que configura la ventana para añadir nuevas películas
        this.crearVentanaAlta();
        
        // Configuración de la ventana
        this.initModality(Modality.APPLICATION_MODAL);      // Bloquear eventos para que no se vayan a otras ventanas
        this.initOwner(parentStage.getOwner());             // El propietario es la clase padre, es decir, el formulario para añadir películas
        this.resizableProperty().setValue(false);           // Se evita que se pueda redimensaionar la ventana
        this.setTitle("Añadir nueva película");             // Título de la ventana
        
    }
    
    /**
     * Método que crea la ventana y la configura
     */
    private void crearVentanaAlta() {
        
        // Configuración de las columnas del GridPane
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10));
        ColumnConstraints columnaIzquierda = new ColumnConstraints();
        ColumnConstraints columnaDerecha = new ColumnConstraints();
        columnaIzquierda.setPercentWidth(25);
        columnaDerecha.setPercentWidth(60);
        gridPane.getColumnConstraints().addAll(columnaIzquierda, columnaDerecha);

        // Elementos que forman parte del GridPane
        gridPane.add(lblNombre, 0, 0);
        gridPane.add(txtNombre, 1, 0);
        gridPane.add(lblEstreno, 0, 1);
        gridPane.add(txtEstreno, 1, 1);
        gridPane.add(btnCartel, 0, 2);
        gridPane.add(txtCartel, 1, 2);

        // Layouts que contiene la ventana
        hBox.getChildren().addAll(btnAceptar, btnCancelar);
        hBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(gridPane, hBox);
        
        // Eventos
        btnCancelar.setOnAction(e -> this.cerrar());
        btnAceptar.setOnAction(e -> this.aceptar());
        btnCartel.setOnAction(e -> this.adjuntarCartel());
        
        // Montaje de la escena
        scene = new Scene(vBox, 580, 150);
        setScene(scene);
        
    }
    
    /**
     * Método que cierra la ventana actual
     */
    private void cerrar(){
        this.close();
    }
    
    /**
     * Método que permite añadir una nueva entrada de película.
     */
    private void aceptar() {
        
        try {
            String nombre = txtNombre.getText();
            int estreno = Integer.parseInt(txtEstreno.getText());
            String cartel = txtCartel.getText();
            
            if(nombre.length() == 0)
                this.mensajeError("Es necesario introducir el nombre de la película.");
            else if(estreno <= 0)
                this.mensajeError("Es necesario que el año de estreno un número válido.");
            else if(cartel.length() == 0)
                this.mensajeError("Es necesario adjuntar el cartel de la película.");
            else
                pelicula = new Cartel(nombre, estreno, cartel);
                this.cancelado = false;
                this.close();
        } catch (NumberFormatException e) {
            this.mensajeError("El formato de la edad no es válido.");
            this.txtEstreno.setText("");
        }
        
    }
    
    /**
     * Método que permite elegir una imagen del disco duro para añadirla a la película como cartel
     */
    private void adjuntarCartel() {
        
        // Configuración del FileChooser
        this.btnAdjuntarCartel.setTitle("Elige el cartel...");
        this.btnAdjuntarCartel.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg*"),
                new FileChooser.ExtensionFilter("PNG", ".png")
        );
        
        // Se abre la ventana de diálogo para elegir una imagen
        this.capturaCartel = btnAdjuntarCartel.showOpenDialog(this);
        
        // Si se elige un carte se guarda su dirección absoluta para añadirla al formulario
        if(this.capturaCartel != null)
            this.txtCartel.setText(this.capturaCartel.getAbsolutePath());
        else
            this.mensajeError("No se ha seleccionado ningún cartel.");
        
    }
    
    /**
     * Método que muestra un mensaje personalizado de aviso al usuario si se produce algún error
     * @param error Mensaje de error que se muestra en la ventana
     */
    private void mensajeError(String error) {
        
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Error en los datos introducidos");
        alerta.setContentText(error);
        alerta.showAndWait();
        
    }
    
    /**
     * Método que devuelve los datos de la película 
     * 
     * @return Datos de la película que se usará para pasar los datos a la ListView
     */
    public Cartel getCartel() {
        return this.pelicula;
    }
    
    /**
     * Método que comprueba si se ha finalizado el alta de una nueva película
     * @return Booleano que indica si se ha dado de alta una nueva película: False se ha dado de alta, true se ha cancelado
     */
    public boolean esCancelado() {
        return cancelado;
    }
    
}
