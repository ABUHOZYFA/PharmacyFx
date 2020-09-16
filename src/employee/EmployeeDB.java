package employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class EmployeeDB {
	
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

	public static int save(Employee emp) {

		int st = 0;
		try {
			String sql = "INSERT INTO `employee`(`employee_name`, `user_name`, `password`,`job`, `phone`) VALUES (?,?,?,?,?)";

			Connection con = EmployeeDB.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, emp.getName());
			preparedStatement.setString(2, emp.getUsername());
			preparedStatement.setString(3, emp.getPassword());
			preparedStatement.setString(4, emp.getJob());
			preparedStatement.setInt(5, emp.getPhone());
			st = preparedStatement.executeUpdate();

			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return st;
	}

	public static int update(Employee emp) {
		int st = 0;
		try {

			String sql = "UPDATE `employee` SET `employee_name`=?,`user_name`=?,`password`=?,`job`=?,`phone`=? WHERE `id`=?";

			Connection con = EmployeeDB.getConnection();
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			preparedStatement.setString(1, emp.getName());
			preparedStatement.setString(2, emp.getUsername());
			preparedStatement.setString(3, emp.getPassword());
			preparedStatement.setString(4, emp.getJob());
			preparedStatement.setInt(5, emp.getPhone());
			preparedStatement.setInt(6, emp.getId());
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
			String sql = "DELETE FROM `employee` WHERE `id`=?";
			Connection con = EmployeeDB.getConnection();
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
