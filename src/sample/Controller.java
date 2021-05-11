package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    Socket socket;
    DataOutputStream out;
    Thread thread;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void onSubmit(){
        String text = textField.getText();
        textArea.appendText(text+"\n");
        textField.clear();
        try {
            out.writeUTF(text);
        } catch (IOException exception) {
            textArea.appendText("Произошла ошибка");
            exception.printStackTrace();
        }
    }

    @FXML
    private void buttonOn() throws Exception{

        stage.setTitle("Клиент сетевого чата");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root, 500, 300));

        try {
            socket = new Socket("45.80.70.161",8189);
            DataInputStream in =new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
            String response = in.readUTF(); // Ждём сообщение от сервера
            textArea.appendText(response+"\n"); // Добро пожаловать на сервер
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            String response = in.readUTF(); // ждём сообщение от сервера
                            textArea.appendText(response+"\n");
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

     
    }

}
