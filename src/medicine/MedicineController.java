package medicine;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MedicineController implements Initializable {

	@FXML
	private TableView<Medicine> tvMedicine;

	@FXML
	private TableColumn<Medicine, Integer> colId;

	@FXML
	private TableColumn<Medicine, Integer> colbarcode;

	@FXML
	private TableColumn<Medicine, String> colMname;

	@FXML
	private TableColumn<Medicine, String> colDate;

	@FXML
	private TableColumn<Medicine, String> colMtype;

	@FXML
	private TableColumn<Medicine, Double> colPrice;

	@FXML
	private TableColumn<Medicine, Integer> colQuantity;

	@FXML
	private TableColumn<Medicine, String> colPdate;

	@FXML
	private TableColumn<Medicine, String> colEdate;
	
	@FXML
    private TextField tfId;

	@FXML
	private TextField tfMname;

	@FXML
	private TextField tfMtype;

	@FXML
	private TextField tfPrice;

	@FXML
	private TextField tfQuantity;

	@FXML
	private TextField tfBarcode;

	@FXML
	private DatePicker dpPdate;

	@FXML
	private DatePicker dpEdate;

	@FXML
	private TextField tfSearch;

	PreparedStatement preparedStatement;
	ResultSet rs;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		showMedicine();

	}

	public ObservableList<Medicine> getMedicineList() {

		ObservableList<Medicine> data = FXCollections.observableArrayList();

		try {
			String sql = "SELECT * FROM `medicine` WHERE 1";
			Connection con = (Connection) MedicineDB.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				data.add(new Medicine(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getInt(6), rs.getInt(7), rs.getString(8), rs.getString(9)));
			}

			con.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public void showMedicine() {

		ObservableList<Medicine> list = getMedicineList();

		colId.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("id"));
		colbarcode.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("barcode"));
		colMname.setCellValueFactory(new PropertyValueFactory<Medicine, String>("name"));
		colDate.setCellValueFactory(new PropertyValueFactory<Medicine, String>("date"));
		colMtype.setCellValueFactory(new PropertyValueFactory<Medicine, String>("type"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Medicine, Double>("price"));
		colQuantity.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("quantity"));
		colPdate.setCellValueFactory(new PropertyValueFactory<Medicine, String>("production"));
		colEdate.setCellValueFactory(new PropertyValueFactory<Medicine, String>("expiry"));
		tvMedicine.setItems(list);
		
		// Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Medicine> filteredData = new FilteredList<>(list, b -> true);
		
		// 2. Set the filter Predicate whenever the filter changes.
		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			
			filteredData.setPredicate(Medicine -> {
				// If filter text is empty, display all Items.
				
				if(newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if(Medicine.getName().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
					return true;
					
				}else if(Medicine.getType().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
					return true;
					
				}else if(String.valueOf(Medicine.getBarcode()).indexOf(lowerCaseFilter) !=-1) 
					
				return true;
				
				else
					
					return false; // Dose not match
				
			});
			
		});
		
		// 3. Wrap the FilteredList in a SortedList.
		SortedList<Medicine> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(tvMedicine.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		tvMedicine.setItems(sortedData);

	}

	public void insertData(ActionEvent e) throws IOException, ParseException {

		LocalDate Pdate = dpPdate.getValue();
		LocalDate Edate = dpEdate.getValue();

		String barcode = tfBarcode.getText();
		String mName = tfMname.getText();
		String mType = tfMtype.getText();
		String price = tfPrice.getText();
		String quantity = tfQuantity.getText();
		String productionDate = Pdate.toString();
		String expiryDate = Edate.toString();

		int ibarcode = Integer.parseInt(barcode);
		int iquantity = Integer.parseInt(quantity);
		double iprice = Double.parseDouble(price);

		Medicine med = new Medicine();
		med.setBarcode(ibarcode);
		med.setName(mName);
		med.setType(mType);
		med.setPrice(iprice);
		med.setQuantity(iquantity);
		med.setProduction(productionDate);
		med.setExpiry(expiryDate);

		int status = MedicineDB.save(med);

		if (status > 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Data Insert");
			alert.setHeaderText("Information Dialog");
			alert.setContentText("Record saved successfully!");
			alert.showAndWait();
			showMedicine();
			clearField();

		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Data Insert");
			alert.setHeaderText("ERROR Dialog");
			alert.setContentText("Place Insert Valid Input!");
			alert.showAndWait();
			
		}

	}

	public void updatData(ActionEvent e) throws IOException, ParseException {

		LocalDate Pdate = dpPdate.getValue();
		LocalDate Edate = dpEdate.getValue();
		
		String sid = tfId.getText();
		int id = Integer.parseInt(sid);

		int barcode = Integer.parseInt(tfBarcode.getText());
		String name =tfMname.getText();
		String type = tfMtype.getText();
		double price = Double.parseDouble(tfPrice.getText());
		int quantity = Integer.parseInt(tfQuantity.getText());
		String productionDate = Pdate.toString();
		String expiryDate = Edate.toString();
	    		

		Medicine med = new Medicine();

		med.setId(id);
		med.setBarcode(barcode);
		med.setName(name);
		med.setType(type);
		med.setPrice(price);
		med.setQuantity(quantity);
		med.setProduction(productionDate);
		med.setExpiry(expiryDate);

		int status = MedicineDB.update(med);

		if (status > 0) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Data Insert");
			alert.setHeaderText("Information Dialog");
			alert.setContentText("Record Update successfully!");
			alert.showAndWait();
			showMedicine();
			clearField();

		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Data Update");
			alert.setHeaderText("ERROR Dialog");
			alert.setContentText("Sorry! unable to Update Record");
			alert.showAndWait();
			
		}
	}

	public void deleteData(ActionEvent e) throws IOException, ParseException {

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Delete Item ");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {

			String sid = tfId.getText();
			int id = Integer.parseInt(sid);

			MedicineDB.delete(id);
			showMedicine();
			clearField();
		} else {

			Alert alert1 = new Alert(AlertType.ERROR);
			alert1.setTitle("Data Update");
			alert1.setHeaderText("ERROR Dialog");
			alert1.setContentText("Sorry! unable to Delete Record");
			alert.showAndWait();
			
		}
	}

	public void selectByMouse(MouseEvent evet) {

		Medicine med = tvMedicine.getSelectionModel().getSelectedItem();
		
		
	    tfId.setText(""+med.getId());
		tfMname.setText(med.getName());
		tfMtype.setText(med.getType());
		tfBarcode.setText("" + med.getBarcode());
		tfQuantity.setText("" + med.getQuantity());
		tfPrice.setText("" + med.getPrice());
		dpPdate.getEditor().setText(med.getProduction());
		dpEdate.getEditor().setText(med.getExpiry());
		

	}
public void clearField() {
		
		tfId.clear();
		tfBarcode.clear();
		tfMname.clear();
		tfMtype.clear();
		tfPrice.clear();
		tfQuantity.clear();
		dpPdate.setValue(null);
		dpEdate.setValue(null);
		
	}
     
     public void clearField(ActionEvent e) {
    	 
    	 clearField();
    	 
     }

}
