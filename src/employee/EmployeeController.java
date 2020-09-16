package employee;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class EmployeeController implements Initializable {
	
	    @FXML
	    private TableView<Employee> tvEmployee;
	    @FXML
	    private TableColumn<Employee, Integer> colId;

	    @FXML
	    private TableColumn<Employee, String> colName;

	    @FXML
	    private TableColumn<Employee, String> colUsername;

	    @FXML
	    private TableColumn<Employee, String> colPassword;

	    @FXML
	    private TableColumn<Employee, String> colDate;

	    @FXML
	    private TableColumn<Employee, String> colJob;

	    @FXML
	    private TableColumn<Employee, Integer> colPhone;
	    
	    @FXML
	    private JFXTextField tfId;

	    @FXML
	    private JFXTextField tfName;

	    @FXML
	    private JFXTextField tfUsername;

	    @FXML
	    private JFXPasswordField pfPassword;

	    @FXML
	    private JFXTextField tfJob;

	    @FXML
	    private JFXTextField tfPhone;

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			showEmployee();
			
		}
		
		public ObservableList<Employee> getEmployeeList() {

			ObservableList<Employee> data = FXCollections.observableArrayList();

			try {
				String sql = "SELECT * FROM `employee` WHERE 1";
				Connection con = (Connection) EmployeeDB.getConnection();
				PreparedStatement preparedStatement;
				preparedStatement = (PreparedStatement) con.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();

				while (rs.next()) {

					data.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6),
							rs.getInt(7)));
				}

				con.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return data;
		}

		public void showEmployee() {

			ObservableList<Employee> list = getEmployeeList();

			colId.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));
			colName.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
			colUsername.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
			colPassword.setCellValueFactory(new PropertyValueFactory<Employee, String>("password"));
			colDate.setCellValueFactory(new PropertyValueFactory<Employee, String>("date"));
			colJob.setCellValueFactory(new PropertyValueFactory<Employee, String>("job"));
			colPhone.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("phone"));
			
			tvEmployee.setItems(list);
			
		}

		public void insertData(ActionEvent e) throws IOException, ParseException {

			String name = tfName.getText();
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			String job = tfJob.getText();
			String phone = tfPhone.getText();
			

			int iphone = Integer.parseInt(phone);
			

			Employee emp = new Employee();
			emp.setName(name);
			emp.setUsername(username);
			emp.setPassword(password);
			emp.setJob(job);
			emp.setPhone(iphone);

			int status = EmployeeDB.save(emp);

			if (status > 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Data Insert");
				alert.setHeaderText("Information Dialog");
				alert.setContentText("Record saved successfully!");
				alert.showAndWait();
				showEmployee();
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

		
			
			String sid = tfId.getText();
			int id = Integer.parseInt(sid);

			String name = tfName.getText();
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			String job = tfJob.getText();
			int phone = Integer.parseInt(tfPhone.getText());
			
		    		

			Employee emp = new Employee();

			emp.setId(id);
			emp.setName(name);
			emp.setUsername(username);
			emp.setPassword(password);
			emp.setJob(job);
			emp.setPhone(phone);

			int status = EmployeeDB.update(emp);

			if (status > 0) {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Data Insert");
				alert.setHeaderText("Information Dialog");
				alert.setContentText("Record Update successfully!");
				alert.showAndWait();
				showEmployee();
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

				EmployeeDB.delete(id);
				showEmployee();
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

			Employee emp = tvEmployee.getSelectionModel().getSelectedItem();
			
			
		    tfId.setText(""+emp.getId());
		    tfName.setText(emp.getName());
		    tfUsername.setText(emp.getUsername());
		    pfPassword.setText(emp.getPassword());
		    tfJob.setText(emp.getJob());
		    tfPhone.setText("" + emp.getPhone());
			
			

		}
	public void clearField() {
			
			tfId.clear();
			tfName.clear();
			tfUsername.clear();
			pfPassword.clear();
			tfJob.clear();
			tfPhone.clear();
			
			
		}
	     
	     public void clearField(ActionEvent e) {
	    	 
	    	 clearField();
	    	 
	     }


}
