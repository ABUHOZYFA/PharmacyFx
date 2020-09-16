package medicine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicineDB {

	static Connection con = null;

	public static Connection getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/parmacy", "root", "");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static int save(Medicine med) {

		int st = 0;
		try {
			String sql = "INSERT INTO `medicine`(`barcode`, `medicine_name`, `medicine type`, `price`, `quantity`, `production_date`, `expiry_date`) VALUES (?,?,?,?,?,?,?)";

			Connection con = MedicineDB.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setInt(1, med.getBarcode());
			preparedStatement.setString(2, med.getName());
			preparedStatement.setString(3, med.getType());
			preparedStatement.setDouble(4, med.getPrice());
			preparedStatement.setInt(5, med.getQuantity());
			preparedStatement.setString(6, med.getProduction());
			preparedStatement.setString(7, med.getExpiry());
			st = preparedStatement.executeUpdate();

			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return st;
	}

	public static int update(Medicine med) {
		int st = 0;
		try {

			String sql = "UPDATE `medicine` SET `barcode`=?,`medicine_name`=?,`medicine type`=?,`price`=?,`quantity`=?,`production_date`=?,`expiry_date`=? WHERE `medicine_id`=?";

			Connection con = MedicineDB.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			preparedStatement.setInt(1, med.getBarcode());
			preparedStatement.setString(2, med.getName());
			preparedStatement.setString(3, med.getType());
			preparedStatement.setDouble(4, med.getPrice());
			preparedStatement.setInt(5, med.getQuantity());
			preparedStatement.setString(6, med.getProduction());
			preparedStatement.setString(7, med.getExpiry());
			preparedStatement.setInt(8, med.getId());

			st = preparedStatement.executeUpdate();

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return st;
	}

	public static int delete(int id) {

		int st = 0;
		try {
			String sql = "DELETE FROM `medicine` WHERE `medicine_id`=?";
			Connection con = MedicineDB.getConnection();
			PreparedStatement preparedStatement = (PreparedStatement) con.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			st = preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return st;
	}
	
	

}
