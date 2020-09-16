package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class DashboardController {
	
	  @FXML
      private BorderPane borderpane;
	  
	  public void Home(MouseEvent e) {
	    	
	    	borderpane.setCenter(null);
	    }
	    
      public void Medicine(MouseEvent e) {
	    	
	    	loadUI("MedicineFX");
	    }
	
	  private void loadUI(String ui) {
    	
    	Parent root = null;
    	
    	try {
			root = FXMLLoader.load(getClass().getResource(ui+".fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	borderpane.setCenter(root);
    }
	

}
