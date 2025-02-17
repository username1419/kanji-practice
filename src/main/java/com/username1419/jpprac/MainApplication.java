package com.username1419.jpprac;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class MainApplication extends Application {

	public static final Logger LOGGER = Logger.getLogger("JPAPP");
	private Stage stage;
	public static int gradeLevel;

	@FXML private QuizSceneController quizSceneController;

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("difficulty-selector.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 320, 240);
		stage.setTitle("JP Practice");
		stage.setScene(scene);
		stage.show();

		this.stage = stage;
	}

	@Override
	public void stop() throws Exception {
		QuizSceneController.saveTimeDelay();
		super.stop();
	}

	public static void main(String[] args) {
		launch();
	}

	public Stage getStage() {
		return stage;
	}
}