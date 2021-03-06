package psp.c_calc2.ui;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import psp.c_calc2.Servidor;

public class ServerControl extends VBox implements IServerStatusListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtFieldHostname;

    @FXML
    private TextField txtFieldPort;

    @FXML
    private Button btnBroadcastStart;

    @FXML
    private Button btnBroadcastStop;

    @FXML
    private Circle circleBroadcastStatus;

    @FXML
    private Button btnServerStart;

    @FXML
    private Button btnServerStop;

    @FXML
    private Circle circleServerStatus;

    @FXML
    private TextArea txtAreaServerLog;

    @FXML
    private Button btnExit;


    @FXML
    void btnBroadcastStartAction(ActionEvent event) {

    }

    @FXML
    void btnBroadcastStopAction(ActionEvent event) {

    }

    @FXML
    void btnExitAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void initialize() {
        Servidor.getInstance().getServerStatusListeners().add(this);
        try {
            txtFieldHostname.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            onLogOutput("Cant find default localhost IP");
            txtFieldHostname.setText(Servidor.DEFAULT_HOSTNAME);
        }
        txtFieldPort.setText(Servidor.DEFAULT_PORT + "");

    }

    @FXML
    void btnServerStartAction(ActionEvent event) {
        if (Servidor.isValidPort(txtFieldPort.getText())) {
            Servidor.getInstance().setHostname(txtFieldHostname.getText());
            Servidor.getInstance().setPort(Integer.parseInt(txtFieldPort.getText()));
            Servidor.getInstance().startServer();
        } else
            onLogOutput("Puerto invalido");
    }

    @FXML
    void btnServerStopAction(ActionEvent event) {
        Servidor.getInstance().stopServer();
    }


    @Override
    public void onStatusChanged() {
        if (Servidor.getInstance().isServerAlive()) {
            Platform.runLater(() -> {
                circleServerStatus.setFill(Color.LIMEGREEN);
                btnServerStart.setDisable(true);
            });
        } else {
            Platform.runLater(() -> {
                circleServerStatus.setFill(Color.ORANGERED);
                btnServerStart.setText("Start");
                btnServerStart.setDisable(false);
            });
        }
    }

    @Override
    public void onActiveClientsChange(int activeClients) {
        Platform.runLater(() -> btnServerStart.setText(Integer.toString(activeClients)));

    }

    @Override
    public void onLogOutput(String string) {
        txtAreaServerLog.appendText(string + "\n");
    }
}
