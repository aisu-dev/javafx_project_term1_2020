package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import model.business_model;
import model.manager_model;
import controller.Launcher.*;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class main_view_controller {

    @FXML
    private Label name_label;
    @FXML
    private Label time_label;
    @FXML
    private Label balance_label;
    @FXML
    private ListView<business_model> business_list = new ListView<>();
    @FXML
    private ListView<business_model> own_business_list = new ListView<>();
    @FXML
    private Button upgrade_btn;
    static final DataFormat MODEL_LIST = new DataFormat("ModelList");

    @FXML
    public void initialize(){
//      TODO: Repeating
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
//                    @Override
                    public void run() {
//                        //TODO: Countion Time
                        Launcher.setCount(Launcher.getCount()+1);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY,0);
                        cal.set(Calendar.MINUTE,0);
                        cal.set(Calendar.SECOND,Launcher.getCount());
                        time_label.setText("Time: "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));

                        manager_model manager_temp = Launcher.getManager();
                        for (business_model item:own_business_list.getItems()) {
                            item.setCount(item.getCount()+1);
                            if(item.getCount()%item.getPeriod()==0){
//                                System.out.println(item.getName()+" : "+0);
                                item.setCount(0);
                                manager_temp.setBalance(manager_temp.getBalance()+item.getProfit());
                                Launcher.setManager(manager_temp);
                            }else{
//                                System.out.println(item.getName()+" : "+Launcher.getCount()%item.getPeriod());
                            }
                        }
                        balance_label.setText("Balance: "+Launcher.getManager().getBalance()+"฿");
                        update_ownList();
                    }
                };

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Encountered an Interrupted Exception");
                    }
                    Platform.runLater(updater);
                }
            }

        });
        thread.setDaemon(true);
        thread.start();
//      ----------------------------------------------------------------------------------------------------------------
//      TODO: set name
        name_label.setText("Manager Name: "+Launcher.getManager().getName());
//      TODO: business list manager

        business_list.getItems().addAll(getModelList());
        business_list.setCellFactory(params->{
            ListCell<business_model> cell = new ListCell<business_model>(){
                private ImageView imageView = new ImageView();
                @Override
                protected void updateItem(business_model business_model, boolean b) {
                    super.updateItem(business_model, b);
                    if(!b){
                        setText(business_model.getName()+" Cost: "+business_model.getPrice()+" ฿");
                        imageView.setImage(new Image(business_model.getImgLink()));
                        setGraphic(imageView);
                    }else{
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            return  cell;
        });
        update_ownList();

        business_list.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDetected(mouseEvent,business_list);
            }
        });
        business_list.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragOver(dragEvent,business_list);
            }
        });
        business_list.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragDone(dragEvent,business_list);
            }
        });
        business_list.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode.equals(KeyCode.ESCAPE)){
                Launcher.CloseWindow();
            }
        });
        own_business_list.setOnDragOver(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                dragOver(event,own_business_list);
            }
        });
        own_business_list.setOnDragDropped(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                dragDropped(event,own_business_list);
            }
        });
        own_business_list.setOnKeyPressed(keyEvent -> {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode.equals(KeyCode.ESCAPE)){
                Launcher.CloseWindow();
            }
        });

        upgrade_btn.setOnAction(event -> {
            if(own_business_list.getSelectionModel().getSelectedItem()!=null){
                if(Launcher.getManager().getBalance()>=own_business_list.getSelectionModel().getSelectedItem().getPrice()){
                    System.out.println(own_business_list.getSelectionModel().getSelectedItem()+" got upgraded :D");
                    Launcher.getManager().setBalance(Launcher.getManager().getBalance()-own_business_list.getSelectionModel().getSelectedItem().getPrice());
                    own_business_list.getSelectionModel().getSelectedItem().setLevel(own_business_list.getSelectionModel().getSelectedItem().getLevel()+1);
                    own_business_list.getSelectionModel().getSelectedItem().setPrice(Math.round(own_business_list.getSelectionModel().getSelectedItem().getPrice()*(1+own_business_list.getSelectionModel().getSelectedItem().getLevel()/10f)));
                    own_business_list.getSelectionModel().getSelectedItem().setProfit(Math.round((1+own_business_list.getSelectionModel().getSelectedItem().getInterest())*own_business_list.getSelectionModel().getSelectedItem().getProfit()));
                        update_ownList();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Money");
                    alert.setHeaderText(null);
                    alert.setContentText(String.format("Not enough balance"));
                    alert.showAndWait();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Business Model");
                alert.setHeaderText(null);
                alert.setContentText(String.format("You must select your own business first!!"));
                alert.showAndWait();
            }
        });
    }

//  Function
    private void update_ownList(){
        own_business_list.setCellFactory(params->{
            ListCell<business_model> cell = new ListCell<business_model>(){
                private ImageView imageView = new ImageView();
                @Override
                protected void updateItem(business_model business_model, boolean b) {
                    super.updateItem(business_model, b);
                    if(!b){
                        setText(business_model.getName()+" Level: "+business_model.getLevel()+"\n"
                                +"Profit: "+business_model.getProfit()+"\n"
                                +"Upgrade: "+business_model.getPrice()+"฿\n"
                                +"Count: "+business_model.getCount()+"/"+business_model.getPeriod());
                        imageView.setImage(new Image(business_model.getImgLink()));
                        setGraphic(imageView);
                    }
                    else{
                        setText(null);
                        setGraphic(null);
                    }
                }
            };
            return  cell;
        });
    }
    private ObservableList<business_model> getModelList(){
        ObservableList<business_model> Model_List = FXCollections.<business_model>observableArrayList();
        try{
            business_model haircut = new business_model("Haircut",10000,.25f,100,5,"assets/haircut.png");
            business_model market = new business_model("Market",20000,.25f,1000,8,"assets/market.png");
            business_model foodtruck = new business_model("Food Truck",50000,.25f,1000,5,"assets/foodtruck.png");
            business_model restaurant = new business_model("Restaurant",100000,.25f,3000,7,"assets/restaurant.png");
            business_model mobileapp = new business_model("Mobile-app",500000,.25f,7000,10,"assets/mobileapp.png");
            business_model factory = new business_model("Factory",1000000,.25f,50000,18,"assets/factory.png");
            business_model school = new business_model("School",10000000,.25f,500000,30,"assets/school.png");
            Model_List.addAll(haircut,market,foodtruck,restaurant,mobileapp,factory,school);
        }catch(IllegalArgumentException e){
            System.out.println("Found an IllegalArgument Exception");
        }
        return Model_List;
    }
    private ArrayList<business_model> getSelectedModel(ListView<business_model> listView){
        ArrayList<business_model> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
        return list;
    }
    private void removeSelectedModel(ListView<business_model> listView){
        List<business_model> selectedList = new ArrayList<>();
        selectedList.add(listView.getSelectionModel().getSelectedItems().get(0));
        listView.getSelectionModel().clearSelection();
        listView.getItems().removeAll(selectedList);
    }
    private void dragDetected(MouseEvent event,ListView<business_model> listView){
        int selectedCount = listView.getSelectionModel().getSelectedIndices().size();
        if (selectedCount==0){
            event.consume();
            return;
        }
        Dragboard db = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
        db.setDragView(new Image(listView.getItems().get(0).getImgLink()));
        ArrayList<business_model> selectedModels = this.getSelectedModel(listView);
        ClipboardContent content = new ClipboardContent();
        content.put(MODEL_LIST,selectedModels);
        db.setContent(content);
        event.consume();
    }
    private void dragOver(DragEvent event, ListView<business_model> listView) {
        Dragboard db = event.getDragboard();
        if (event.getGestureSource() != listView && db.hasContent(MODEL_LIST)) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }
    private void dragDropped(DragEvent event, ListView<business_model> listView) {
        boolean dragCompleted = false;
        Dragboard db = event.getDragboard();
        if(db.hasContent(MODEL_LIST)) {
            ArrayList<business_model> list = (ArrayList<business_model>)db.getContent(MODEL_LIST);
            if(Launcher.getManager().getBalance()>=list.get(0).getPrice()){
                listView.getItems().addAll(list);
                dragCompleted = true;
            }
        }
        event.setDropCompleted(dragCompleted);
        event.consume();
    }
    private void dragDone(DragEvent event, ListView<business_model> listView) {
        TransferMode tm = event.getTransferMode();
        if (tm == TransferMode.MOVE&&Launcher.getManager().getBalance()>=listView.getSelectionModel().getSelectedItems().get(0).getPrice()) {
            Launcher.getManager().setBalance(Launcher.getManager().getBalance()-listView.getSelectionModel().getSelectedItems().get(0).getPrice());
            removeSelectedModel(listView);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Money");
            alert.setHeaderText(null);
            alert.setContentText(String.format("Not enough balance"));
            alert.showAndWait();
        }
        event.consume();
    }

}
