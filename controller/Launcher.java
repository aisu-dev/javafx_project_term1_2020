package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.business_model;
import model.manager_model;

import java.io.IOException;
import java.util.ArrayList;

public class Launcher extends Application {

    private Parent main_root;
    private static Stage main_stage;
    private Scene main_scene;
    private static manager_model manager;
    private static ArrayList<business_model>business = new ArrayList<>();
    private static int count = 0;

    @Override
    public void start(Stage primaryStage) {
        try {
            manager = new manager_model("Ice");
            this.main_root = FXMLLoader.load(getClass().getResource("../view/main_pane.fxml"));
            this.main_stage = primaryStage;
            this.main_stage.setTitle("Investor Game");
            this.main_scene = new Scene(this.main_root);
            this.main_stage.setScene(this.main_scene);
            main_stage.getIcons().add(new Image("assets/icon.png"));
            this.main_stage.show();
        }catch(IOException e){
            System.out.println("Encountered an IO Exception");
        }
    }


    public static manager_model getManager() { return manager;}
    public static void setManager(manager_model manager) { Launcher.manager = manager;}
    public static void setCount(int count) {
        Launcher.count = count;
    }
    public static int getCount() {
        return count;
    }
    public static void CloseWindow(){ main_stage.close();}
    public static void main(String[] args) {
        launch(args);
    }
}
