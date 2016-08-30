package lei.k.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import lei.k.application.model.MediaInfo;
import lei.k.application.view.RootLayoutController;
import lei.k.application.view.VideoViewController;

public class Main extends Application {
	
	private ObservableList<MediaInfo> mediaInfoList = FXCollections.observableArrayList();
	
	private Media media;
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	private BorderPane videoViewPane;
	private RootLayoutController rootLayoutController;
	private VideoViewController videoViewController;
	
	public Main(){
		media = new Media("file:/home/k/Videos/Luna_Haruna.mp4");
		File file = new File(Main.class.getResource("resources/play.list.example").getPath());
		loadMediaInfoList(file);
	}
	
	
	public void loadMediaInfoList(File file){
		try (BufferedReader bufferReader = new BufferedReader(new FileReader(file))){
			String line = null;
			while((line = bufferReader.readLine()) != null){
				String[] elements = line.split(" ");
				if(elements[0].charAt(0) == '#') continue;
				if(elements.length == 0)
					break;
			    MediaInfo mediaInfo = new MediaInfo();
			    mediaInfo.setId(Integer.parseInt(elements[0]));
			    mediaInfo.setName(elements[1]);
			    mediaInfo.setURI(new SimpleStringProperty(elements[2]));
			    mediaInfoList.add(mediaInfo);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(MediaInfo info : mediaInfoList){
			System.out.println("id: " + info.getId() + ", name: " + info.getName() + ", URI: " + info.getURI());
		}
	}
	
	public void initRootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();
			
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			rootLayoutController = loader.getController();
			rootLayoutController.setMain(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initVideoView(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/VideoView.fxml"));
			
			videoViewPane = (BorderPane)loader.load();
			rootLayout.setCenter(videoViewPane);
			
			videoViewController = loader.getController();
			videoViewController.setMain(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resetMedia(String URL){
		media = new Media(URL);
		
		videoViewController.reloadMedia(media);
	}
	
	public Media getMedia(){
		return media;
	}
	
	public ObservableList<MediaInfo> getMediaInfoList(){
		return mediaInfoList;
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public BorderPane getRootLayout(){
		return rootLayout;
	}
	
	public BorderPane getVideoViewPane(){
		return videoViewPane;
	}
	
	public void start(Stage primaryStage){
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Video");
		
		initRootLayout();
		initVideoView();
		
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
