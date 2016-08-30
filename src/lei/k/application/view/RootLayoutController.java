package lei.k.application.view;

import java.io.File;
import java.util.Optional;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import lei.k.application.Main;
import lei.k.application.model.MediaInfo;

public class RootLayoutController {
	
	private Main main;
	
	@FXML private ListView<MediaInfo> listView;
	
	public void setMain(Main main){
		this.main = main;
		main.getRootLayout().heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			main.getVideoViewPane().setPrefHeight((Double)newValue - 30);
		});
		main.getRootLayout().widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			main.getVideoViewPane().setPrefWidth((Double)newValue - 100);
		});
		
		listView.setItems(main.getMediaInfoList());
		
		listView.setCellFactory(new StringListCellFactory());
		
	}
	
	class StringListCellFactory implements  Callback<ListView<MediaInfo>, ListCell<MediaInfo>> {
	    @Override
	    public ListCell<MediaInfo> call(ListView<MediaInfo> mediaInfoListView) {
	        return new StringListCell();
	    }

	    class StringListCell extends ListCell<MediaInfo> {
	        @Override
	        protected void updateItem(MediaInfo mediaInfo, boolean b) {
	            super.updateItem(mediaInfo, b);

	            if (mediaInfo != null) {
	                setText(mediaInfo.getName());
	            }
	        }
	    }
	}
	
	@FXML
	private void handleOpenFile(){
		FileChooser fileChooser = new FileChooser();
		
		File file = fileChooser.showOpenDialog(main.getPrimaryStage());
		
		main.resetMedia(file.toURI().toString());
	}
	
	@FXML
	private void handleOpenURL(){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Open URL");
		dialog.setHeaderText("URL Resource:");
		
		Optional<String> result = dialog.showAndWait();
		
		result.ifPresent(URL -> {
			main.resetMedia(URL);
		});
	}
}
