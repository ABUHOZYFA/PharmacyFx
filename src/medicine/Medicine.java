package medicine;

public class Medicine {
	 
	private int Id ;
	private int Barcode;
	private String Name;
	private String Date;
	private String Type;
	private double Price;
	private int Quantity;
	private String Production;
	private String Expiry;
	public Medicine() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Medicine(int id, int barcode, String name, String date, String type, double price, int quantity,
			String production, String expiry) {
		super();
		Id = id;
		Barcode = barcode;
		Name = name;
		Date = date;
		Type = type;
		Price = price;
		Quantity = quantity;
		Production = production;
		Expiry = expiry;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getBarcode() {
		return Barcode;
	}
	public void setBarcode(int barcode) {
		Barcode = barcode;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public String getProduction() {
		return Production;
	}
	public void setProduction(String production) {
		Production = production;
	}
	public String getExpiry() {
		return Expiry;
	}
	public void setExpiry(String expiry) {
		Expiry = expiry;
	}
	

	
}
