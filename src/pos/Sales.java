package pos;



public class Sales {
	
	  private int Barcode;
	  private String Name;
	  private double Price;
	  private int Quantity;
      private double Total;
      private double Descont;
      private double Payment;
      
	public Sales() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sales(int barcode, String name, double price, int quantity, double total, double descont, double payment) {
		super();
		Barcode = barcode;
		Name = name;
		Price = price;
		Quantity = quantity;
		Total = total;
		Descont = descont;
		Payment = payment;
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

	public double getTotal() {
		return Total;
	}

	public void setTotal(double total) {
		Total = total;
	}

	public double getDescont() {
		return Descont;
	}

	public void setDescont(double descont) {
		Descont = descont;
	}

	public double getPayment() {
		return Payment;
	}

	public void setPayment(double payment) {
		Payment = payment;
	}

	

}
