package pos;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class SalesOpreation implements Initializable {

	@FXML
	private TableView<Sales> tvSales;
	@FXML
	private TableColumn<Sales, Integer> colBarcode;
	@FXML
	private TableColumn<Sales, String> colName;
	@FXML
	private TableColumn<Sales, Double> colPrice;
	@FXML
	private TableColumn<Sales, Integer> colQuantity;
	@FXML
	private TableColumn<Sales, Double> colTotal;
	@FXML
	private TextField tfBarcode;
	@FXML
	private TextField tfName;
	@FXML
	private TextField tfPrice;
	@FXML
	private TextField tfQuantity;
	@FXML
	private TextField tfTotal;
	@FXML
	private TextField tfDescont;
	@FXML
	private TextField tfPayment;
	@FXML
	private TextField tfSearch;

	static Connection con = null;
	PreparedStatement preparedStatement;
	ResultSet rs;

	public static Connection getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/parmacy", "root", "");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public void pressedKey(KeyEvent even) throws IOException {

		if (even.getCode().equals(KeyCode.ENTER)) {

			String pressCode = tfBarcode.getText();

			try {

				Connection con = SalesOpreation.getConnection();
				preparedStatement = con.prepareStatement("SELECT * FROM `medicine` WHERE barcode=?");
				preparedStatement.setString(1, pressCode);
				rs = preparedStatement.executeQuery();

				if (rs.next() == false) {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Data Insert");
					alert.setHeaderText("Information Dialog");
					alert.setContentText("Record not fund !");
					alert.showAndWait();
				} else {

					String productName = rs.getString("medicine_name");
					String productPrice = rs.getString("price");

					tfName.setText(productName.trim());
					tfPrice.setText(productPrice.trim());
					tfQuantity.requestFocus();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private Boolean validateInput() {

		String checkValid = tfBarcode.getText();

		try {

			Connection con = SalesOpreation.getConnection();
			preparedStatement = con.prepareStatement("SELECT * FROM `medicine` WHERE barcode=?");
			preparedStatement.setString(1, checkValid);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int currentQty = rs.getInt("quantity");
//			    			System.out.println(currentQty);
				int inputQty = Integer.parseInt(tfQuantity.getText());
//			    			System.out.println(inputQty);

				if (inputQty > currentQty) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Stocks Status");
					alert.setContentText("Out Of Stocks! Current Stock is " + currentQty);
					alert.showAndWait();
					tfQuantity.clear();
					tfBarcode.requestFocus();

					return false;
				} else {
					return true;

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ObservableList<Sales> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		colBarcode.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("barcode"));
		colName.setCellValueFactory(new PropertyValueFactory<Sales, String>("name"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Sales, Double>("price"));
		colQuantity.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("quantity"));
		colTotal.setCellValueFactory(new PropertyValueFactory<Sales, Double>("total"));
		tvSales.setItems(data);

		// Wrap the ObservableList in a FilteredList (initially display all data).
//				FilteredList<Medicine> filteredData = new FilteredList<>(list, b -> true);
//				
//				// 2. Set the filter Predicate whenever the filter changes.
//				tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
//					
//					filteredData.setPredicate(Medicine -> {
//						// If filter text is empty, display all Items.
//						
//						if(newValue == null || newValue.isEmpty()) {
//							return true;
//						}
//						
//						// Compare first name and last name of every person with filter text.
//						
//						String lowerCaseFilter = newValue.toLowerCase();
//						
//						if(Medicine.getName().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
//							return true;
//							
//						}else if(Medicine.getType().toLowerCase().indexOf(lowerCaseFilter) !=-1) {
//							return true;
//							
//						}else if(String.valueOf(Medicine.getBarcode()).indexOf(lowerCaseFilter) !=-1) 
//							
//						return true;
//						
//						else
//							
//							return false; // Dose not match
//						
//					});
//					
//				});
//				
//				// 3. Wrap the FilteredList in a SortedList.
//				SortedList<Medicine> sortedData = new SortedList<>(filteredData);
//				
//				// 4. Bind the SortedList comparator to the TableView comparator.
//				// 	  Otherwise, sorting the TableView would have no effect.
//				sortedData.comparatorProperty().bind(tvMedicine.comparatorProperty());
//				
//				// 5. Add sorted (and filtered) data to the table.
//				tvSales.setItems(sortedData);

	}

	public void addItems(ActionEvent e) throws SQLException {

		if (validateInput()) {
			double price = Double.parseDouble(tfPrice.getText());
			double qtynew = Double.parseDouble(tfQuantity.getText());

			double total = price * qtynew;

			Sales sale = new Sales();
			sale.setBarcode(Integer.parseInt(tfBarcode.getText()));
			sale.setName(tfName.getText());
			sale.setPrice(Double.parseDouble(tfPrice.getText()));
			sale.setQuantity(Integer.parseInt(tfQuantity.getText()));
			sale.setTotal(total);

			tvSales.getItems().add(sale);
			calculation();
			clearItemsField();

		}

	}

	private void calculation() {

		double subTotalPrice = 00.0;
		subTotalPrice = tvSales.getItems().stream().map((item) -> item.getTotal()).reduce(subTotalPrice,
				(accumulator, _item) -> accumulator + _item);

		if (subTotalPrice > 0) {

			tfTotal.setText(String.valueOf(subTotalPrice));
			tfPayment.setText(String.valueOf(subTotalPrice));

		}
	}

	public void deleteItems(ActionEvent e) throws SQLException {

		int index = tvSales.getSelectionModel().getSelectedIndex();

		if (index > 0) {
			tvSales.getItems().remove(index);
			calculation();

		} else if (index == 0) {
			tvSales.getItems().remove(index);
			tfTotal.setText(null);
		}
	}

	public void selectByMouse(MouseEvent evet) {
		tvSales.getSelectionModel().getSelectedItem();
	}

	public void PayInvoice(ActionEvent e) throws SQLException {

		double subtotal = Double.parseDouble(tfTotal.getText());
		double descont = Double.parseDouble(tfDescont.getText());
		double payInvo = subtotal - descont;

		tfPayment.setText(Double.toString(payInvo));
		sales();
		clearPayField();

	}

	public void sales() {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now);
		double subtotal = Double.parseDouble(tfTotal.getText());
		double descont = Double.parseDouble(tfDescont.getText());
		double payment = Double.parseDouble(tfPayment.getText());
		int lastinstance = 0;

		try {
//				 // here you can write the code for inserting into sales product.

			Connection con = SalesOpreation.getConnection();
			String sql = "INSERT INTO `sales`(`date`, `subtotal`, `descont`, `payment`) VALUES (?,?,?,?)";

			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, date);
			preparedStatement.setDouble(2, subtotal);
			preparedStatement.setDouble(3, descont);
			preparedStatement.setDouble(4, payment);
			preparedStatement.executeUpdate();

			ResultSet genterateKeyResult = preparedStatement.getGeneratedKeys();

			if (genterateKeyResult.next()) {

				lastinstance = genterateKeyResult.getInt(1);
			}
			for (int i = 0; i < tvSales.getItems().size(); i++) {

//						System.out.println(tvSales.getItems().get(i).getCode() +" "+tvSales.getItems().get(i).getPrice());

				String sql1 = "INSERT INTO `sales_product`(`sales_id`,`product_code`, `sell_price`, `quantity`, `total`) VALUES (?,?,?,?,?)";

				PreparedStatement preparedStatement1 = con.prepareStatement(sql1);

				preparedStatement1.setInt(1, lastinstance);
				preparedStatement1.setInt(2, tvSales.getItems().get(i).getBarcode());
				preparedStatement1.setDouble(3, tvSales.getItems().get(i).getPrice());
				preparedStatement1.setInt(4, tvSales.getItems().get(i).getQuantity());
				preparedStatement1.setDouble(5, tvSales.getItems().get(i).getTotal());
				preparedStatement1.executeUpdate();

			}

			for (int i = 0; i < tvSales.getItems().size(); i++) {

//						System.out.println(tvSales.getItems().get(i).getCode() +" "+tvSales.getItems().get(i).getPrice());

				String sql2 = "UPDATE `medicine` SET`quantity`=`quantity`-? WHERE `barcode`=?";

				PreparedStatement preparedStatement2 = con.prepareStatement(sql2);
				preparedStatement2.setInt(1, tvSales.getItems().get(i).getQuantity());
				preparedStatement2.setInt(2, tvSales.getItems().get(i).getBarcode());
				preparedStatement2.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void clearItemsField() {

		tfBarcode.clear();
		tfName.clear();
		tfPrice.clear();
		tfQuantity.clear();
		tfBarcode.requestFocus();

	}

	public void clearPayField() {

		tfTotal.clear();
		tfDescont.clear();
		tfPayment.clear();
		tvSales.getItems().clear();

	}

	public void clearField(ActionEvent e) {

		clearItemsField();
		clearPayField();
	}
	
	
	//this is barcode Scanner method
//	public void barcodeScanner(KeyEvent even) throws IOException, SQLException {
//		
//		Connection con = SalesOpreation.getConnection();
//		preparedStatement = con.prepareStatement("SELECT * FROM `medicine` WHERE barcode=?");
//		preparedStatement.setString(1, tfBarcode.getText());
//		rs = preparedStatement.executeQuery();
//		
//		if(rs.next()) {
//			
//			String productName = rs.getString("medicine_name");
//			String productPrice = rs.getString("price");
//
//			tfName.setText(productName.trim());
//			tfPrice.setText(productPrice.trim());
//			tfQuantity.requestFocus();
//		}
//		
//		con.close();
//	}

}
