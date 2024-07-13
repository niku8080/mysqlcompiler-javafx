package com.mysqlcompiler;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class homePage extends Application {
    private String DataBase_Name;
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;

    void setCredentials(String username, String password){
        this.DB_USER = username;
        this.DB_PASSWORD = password;
    }

    void setDataBase(String Database){
        this.DataBase_Name = Database;
        System.out.println("MY Database "+DataBase_Name);
        this.DB_URL = "jdbc:mysql://localhost:3306/"+ DataBase_Name;
    }
    String getDataBase (){
        System.out.println("MY Database2 "+DataBase_Name);
        this.DB_URL = "jdbc:mysql://localhost:3306/"+ DataBase_Name;
        return this.DataBase_Name;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        authdialog();
        // Ensure the username and password is set
        if (DB_USER == null || DB_PASSWORD == null) {
            System.out.println("Please enter username and password!!!");
            return;
        }

        openDialog();

        // Ensure the database name is set
        if (getDataBase() == null || getDataBase().isEmpty()) {
            System.out.println("Database name must be set before proceeding!!!");
            return;
        }

        

          ///Dailogue window
        HBox DataBaseDialougeBox = new HBox();

        TextArea queryArea = new TextArea();
        VBox.setMargin(queryArea, new Insets(0, 0, 0, 350)); 
        queryArea.setPrefHeight(400);
        queryArea.setMaxWidth(1000);
        queryArea.setWrapText(true);

        TextArea resultArea = new TextArea();
        VBox.setMargin(resultArea, new Insets(0, 0, 100, 350)); 
        resultArea.setPrefHeight(600);
        resultArea.setPrefWidth(1000);
        resultArea.setWrapText(true);


        //Execute button
        Button executeQueryButton = new Button("Execute Query");
        executeQueryButton.applyCss();
        executeQueryButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white;");
        executeQueryButton.setOnAction(e -> executeQuery(queryArea.getText(), resultArea));


        //clear button
        Button clearButton = new Button("Clear Query");
        clearButton.applyCss();
        clearButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white;");
        clearButton.setOnAction(e-> {
             queryArea.clear();
             resultArea.clear();
        });

        Button openDialogButton = new Button("Use DataBase");
        openDialogButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white;");
        openDialogButton.applyCss();
        openDialogButton.setOnAction(e -> openDialog());
    

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(openDialogButton, new Insets(0, 0, 0, 300));
        HBox.setMargin(executeQueryButton, new Insets(0, 0, 0, 50)); 
        HBox.setMargin(clearButton, new Insets(0, 0, 0, 50));
        
        hBox.getChildren().add(openDialogButton);
        hBox.getChildren().add(executeQueryButton);
        hBox.getChildren().add(clearButton);
        

        VBox vbox = new VBox(10, queryArea, hBox, DataBaseDialougeBox,resultArea);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(70)); 

        Pane root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(vbox);

        Scene scene = new Scene(root);

        primaryStage.setTitle("MySQL Query Executor");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /*
     * Use DataBase function
     *  Author : AKshay
     *  Date:9-7-2024
     * 
     */
    private void authdialog() {
        // Create a new stage for the pop-up dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Enter Details");
    
        // Create layout for the dialog
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);

        TextField userNameTextField = new TextField();
        userNameTextField.setMaxWidth(400);

        TextField passwordTextField = new TextField();
        passwordTextField.setMaxWidth(400);

        Button okButton = new Button("OK");
        okButton.setMaxWidth(100);
    
        // Set action for the button
        okButton.setOnAction(e -> {
            // Handle button click (e.g., get text from textField)
            String username = userNameTextField.getText();
            String password = passwordTextField.getText();
            setCredentials(username, password);
            // Close the dialog
            dialogStage.close();
        });
        // Add components to the layout
        dialogVbox.getChildren().addAll(new Label("Enter User Name:"), userNameTextField);
        dialogVbox.getChildren().addAll(new Label("Enter Password:"), passwordTextField, okButton);
        // Create a scene with layout for the dialog
        Scene dialogScene = new Scene(dialogVbox, 800, 600);
    
        // Set the scene for the dialog stage
        dialogStage.setScene(dialogScene);
        // Set modality of the dialog (blocking interactions with primary stage)
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        // Show the dialog and wait until it is closed
        dialogStage.showAndWait();
    }

    
    private void openDialog() {
    // Create a new stage for the pop-up dialog
    Stage dialogStage = new Stage();
    dialogStage.setTitle("DataBase Name");

    // Create layout for the dialog
    VBox dialogVbox = new VBox(20);
    dialogVbox.setAlignment(Pos.CENTER);
    TextField textField = new TextField();
    textField.setMaxWidth(400);

    Button okButton = new Button("OK");
    okButton.setMaxWidth(100);

    // Set action for the button
    okButton.setOnAction(e -> {
        // Handle button click (e.g., get text from textField)
        String inputText = textField.getText();
        setDataBase(inputText);
        // Close the dialog
        dialogStage.close();
    });
    // Add components to the layout
    dialogVbox.getChildren().addAll(new Label("Enter Database name:"), textField, okButton);
    // Create a scene with layout for the dialog
    Scene dialogScene = new Scene(dialogVbox, 800, 600);

    // Set the scene for the dialog stage
    dialogStage.setScene(dialogScene);
    // Set modality of the dialog (blocking interactions with primary stage)
    dialogStage.initModality(Modality.APPLICATION_MODAL);
    // Show the dialog and wait until it is closed
    dialogStage.showAndWait();
}


 // 
    private void executeQuery(String query, TextArea resultArea) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver is loaded
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 Statement statement = connection.createStatement()) {

                boolean isResultSet = statement.execute(query);

                if (isResultSet) {
                    ResultSet resultSet = statement.getResultSet();
                    displayResultSet(resultSet, resultArea);
                } else {
                    int updateCount = statement.getUpdateCount();
                    resultArea.setText("Query OK, " + updateCount + " rows affected.");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void displayResultSet(ResultSet resultSet, TextArea resultArea) throws SQLException {
        StringBuilder result = new StringBuilder();
        int columnCount = resultSet.getMetaData().getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                result.append(resultSet.getString(i)+ "  |").append("\t");
            }
            result.append("\n");    
        }

        resultArea.setText(result.toString());
    }

}

