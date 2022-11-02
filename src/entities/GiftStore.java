package entities;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;

public class GiftStore 
{
	private ArrayList<Customer> customers;
	private ArrayList<Admin> admins;
	private ArrayList<Item> items;
	private Person currentLogin;
	
	
	public GiftStore() throws Exception
	{	
		
		try 
		{
			initializeCustomers();
			initializeAdmins();
			initializeItems();
		}
		catch (FileNotFoundException e)
		{
			String message = "One or more files not found, please repair before use.";
			String title = "File Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}
		
		currentLogin = null;
	}
	
	public static Connection letConnect() {
		String url="jdbc:mysql://localhost:3306/projdatabase";
		String userName="root";
		String password="Admin123*";
		Connection con = null;
				
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection(url,userName,password);
			System.out.println("Connected");
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
		
	}
	
		
	// Customer file format: realname
	//						 username password phone
	// NOTE: file must have a blank line at the end
	public void initializeCustomers() throws SQLException
	{
		
		customers = new ArrayList<Customer>();
		
		/*
		Scanner customerFile = new Scanner(new FileReader("src\\entities\\data\\customers.txt"));
		while(customerFile.hasNext())
		{
			String realname = customerFile.nextLine();
			String username = customerFile.next();
			String password = customerFile.next();
			String phone = customerFile.next();
			customerFile.nextLine();
			
		}
		customerFile.close();*/
		
		Connection conn=letConnect();
		String query = "select * from users where Role='Customer'"; 
		
			try{
					Statement st=conn.createStatement();
					ResultSet rs=st.executeQuery(query);
					while(rs.next()) {
						customers.add(new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4))); // to the array list
					}
					rs.close();
					st.close();
			} 
			catch (PersonException e) 
			{
				String message = "Customer file corrupted, please repair before use.";
				String title = "Data Error";
				int icon = JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, title, icon);
				System.exit(0);
			}
		}
	
	
	// Admin file format: realname
	//					  username password 
	// NOTE: file must have a blank line at the end
	public void initializeAdmins() throws SQLException
	{
		admins = new ArrayList<Admin>();
		
		/*
		Scanner adminFile = new Scanner(new FileReader("src\\entities\\data\\admins.txt"));
		while(adminFile.hasNext())
		{
			String realname = adminFile.nextLine();
			String username = adminFile.next();
			String password = adminFile.next();
			adminFile.nextLine();
			
			try 
			{
				admins.add(new Admin(username, password, realname));
			} 
			catch (PersonException e) 
			{
				String message = "Admin file corrupted, please repair before use.";
				String title = "Data Error";
				int icon = JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, title, icon);
				System.exit(0);
			}
		}
		adminFile.close();*/
		
		Connection conn=letConnect();
		String query = "select * from users where Role='Admin'"; 
		try 
		{
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			while(rs.next()) {
				admins.add(new Admin(rs.getString(1), rs.getString(2), rs.getString(3))); // to the arrayList
			}
			rs.close();
			st.close();
			
		} 
		catch (PersonException e) 
		{
			String message = "Admin file corrupted, please repair before use.";
			String title = "Data Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}
	}
	
	// Item file format: itemname 
	// 					 itemprice numStock numSold 
	//					 path_to_itemimage
	// NOTE: file must have a blank line at the end
	public void initializeItems() throws FileNotFoundException
	{
		
		items = new ArrayList<Item>();
		
		
		Scanner itemFile = new Scanner(new FileReader("src\\entities\\data\\items.txt"));
		while(itemFile.hasNext())
		{
			String itemname = itemFile.nextLine();
			double itemprice = itemFile.nextDouble();
			int numStock = itemFile.nextInt();
			int numSold = itemFile.nextInt();
			itemFile.nextLine();
			String itempath = itemFile.nextLine();
			
			try 
			{
				items.add(new Item(itemname, itemprice, numStock, numSold, itempath));
			} 
			catch (ItemException e) 
			{
				String message = "Item file corrupted, please repair before use.";
				String title = "Data Error";
				int icon = JOptionPane.ERROR_MESSAGE;
				JOptionPane.showMessageDialog(null, message, title, icon);
				System.exit(0);
			}
		}
		itemFile.close();		
		
		/*
		Connection conn=letConnect();
		String query = "select * from movies"; 
		try 
		{
			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery(query);
			String Title, Genre, Director, Producer, Actor,ActorRole, Actress, ActressRole, RunningTime, MovieStudio, Description, ImagePath;
			int Year;
			double price;
			
			while(rs.next()) {
				Title = rs.getString(2);
				Genre = rs.getString(3);
				Director = rs.getString(4);
				Producer = rs.getString(5);
				Actor = rs.getString(6);
				ActorRole = rs.getString(7);
				Actress = rs.getString(8);
				ActressRole = rs.getString(9);
				Year = rs.getInt(10);
				RunningTime = rs.getString(11);
				MovieStudio = rs.getString(12);
				Description  = rs.getString(13);
				price = rs.getFloat(14);
				ImagePath = rs.getString(15);
				//items.add(new Item(itemname, itemprice, numStock, numSold, itempath));
				items.add(new Item(Title, Genre, Director, Producer, Actor,ActorRole, Actress, ActressRole, Year, RunningTime, MovieStudio, Description, price, ImagePath)); // to the arrayList
			}
			rs.close();
			st.close();
			
		} 
		catch (SQLException | PersonException e) 
		{
			String message = "Customer file corrupted, please repair before use.";
			String title = "Data Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}*/
		
	}
	
	public ArrayList<Customer> getCustomers() {return customers;}
	public ArrayList<Admin> getAdmins() {return admins;}
	public ArrayList<Item> getItems() {return items;}
	public Person getCurrentLogin() {return currentLogin;}
	
	// helper methods
	public Customer findCustomer(String user)
	{
		for(Customer c : customers)
		{
			if(c.getUsername().equals(user))
			{
				return c;
			}
		}
		return null;
	}
	public Admin findAdmin(String user)
	{
		for(Admin a : admins)
		{
			if(a.getUsername().equals(user))
			{
				return a;
			}
		}
		return null;
	}
	public Item findItem(String name)
	{
		for(Item i : items)
		{
			if(i.getItemName().equals(name))
			{
				return i;
			}
		}
		return null;
	}
		
	// Cast to currentLogin must be implemented to use Customer methods.
	public void customerLogin(String user, String pass) throws GiftStoreException
	{
		Customer temp = findCustomer(user);
		if(temp != null)
		{
			if(temp.getPassword().equals(pass))
			{
				currentLogin = temp;
			}
			else
				throw new InvalidCredentials();
		}
		else
			throw new InvalidCredentials();
	}

	// Cast to currentLogin must be implemented to use Customer methods.
	public void registerCustomer(String user, String pass, String real, String phone) throws GiftStoreException, PersonException
	{
		Customer temp = findCustomer(user);
		if(temp == null)
		{			
			Customer c = new Customer(user, pass, real, phone);
			customers.add(c);
			currentLogin = c;
			
			updateCustomers();
		}
		else
			throw new CustomerUsernameTaken();
	}
	
	// Cast to adminLogin must be implemented to use Admin methods.
	public void adminLogin(String user, String pass) throws GiftStoreException
	{
		Admin temp = findAdmin(user);
		if(temp != null)
		{
			if(temp.getPassword().equals(pass))
			{
				currentLogin = temp;
			}
			else
				throw new InvalidCredentials();
		}
		else
			throw new InvalidCredentials();
	}
	
	public void deleteCustomer(String user)
	{
		if(currentLogin instanceof Admin)
		{
			Customer c = findCustomer(user);
			c.getCart().clear();
			customers.remove(c);
			updateCustomers();
		}
	}
	
	public void registerItem(String name, double price, int stock, BufferedImage img) throws GiftStoreException, ItemException 
	{
		if(currentLogin instanceof Admin)
		{
			Item temp = findItem(name);
			if(temp == null)
			{			
				String path = String.format("src\\entities\\data\\images\\%s.jpg", name);
				Item i = new Item(name, price, stock, 0, path);
				try 
				{
					ImageIO.write(img, "jpg", new File(path));
				} 
				catch (IOException e) 
				{
					String message = "Image file not found, please repair before use.";
					String title = "File Error";
					int icon = JOptionPane.ERROR_MESSAGE;
					JOptionPane.showMessageDialog(null, message, title, icon);
					System.exit(0);
				}
				
				items.add(i);
				
				updateItems();
			}
			else
				throw new ItemNameTaken();
		}
	}
	
	public void deleteItem(String name) 
	{
		if(currentLogin instanceof Admin)
		{
			Item i = findItem(name);
			(new File(i.getImagePath())).delete();
			items.remove(i);
			updateItems();				
		}
	}
	
	public ArrayList<Item> searchItem(String key)
	{
		ArrayList<Item> temp = new ArrayList<Item>();
		for(Item i : items)
		{
			if(i.getItemName().toUpperCase().contains(key.toUpperCase()))
				temp.add(i);
		}
		return(temp);
	}
		
	public void deleteMyAccount() 
	{
		if(currentLogin instanceof Customer) 
		{
			customers.remove(currentLogin);
			updateCustomers();
			logout();
		}
	}
	
	public void logout()
	{
		currentLogin = null;
	}
	
	public String checkout(String address) throws FileNotFoundException
	{
		if(currentLogin instanceof Customer)
		{
			int transactionID = (int) (100000 * Math.random());
			String receipt = "Transaction ID: " + transactionID  + "\n\n";
			receipt += "Purchases: \n";
			
			for(Item i : ((Customer) currentLogin).getCart().keySet())
			{
				receipt += i.getItemName() + "\n";
				receipt += i.getPrice() + " AED, ";
				receipt += ((Customer) currentLogin).getCart().get(i) + " units.\n";
			}
			
			receipt += "\nTotal: " + ((Customer) currentLogin).computeTotal() + " AED";
			receipt += " + 5% VAT = " + 1.05 * ((Customer) currentLogin).computeTotal() + " AED.\n\n";
			receipt += "Date and time completed: ";
			receipt += DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").format(LocalDateTime.now()) + "\n\n";
			receipt += "Order will be delivered to: \n" + address + "\n\n";
			receipt += "Customer: " + currentLogin.getRealName() + "\n";
			receipt += "Customer Phone No.: " + ((Customer) currentLogin).getPhoneNum() + "\n\n";
			receipt += "Thank you for shopping at KU Gift Store!";
			
			PrintWriter receiptFile = new PrintWriter("src\\entities\\data\\receipts\\receipt" + transactionID +".txt");
			receiptFile.print(receipt);
			receiptFile.close();
			
			((Customer) currentLogin).checkout();
			updateItems();
			return receipt;
		}
		return null;
	}

	// Customer file format: realname
	//						 username password phone 
	// NOTE: file must have a blank line at the end
	public void updateCustomers()
	{
		try 
		{
			PrintWriter customerFile = new PrintWriter("src\\entities\\data\\customers.txt");
			
			for(Customer c : customers)
			{
				customerFile.println(c.getRealName());
				customerFile.println(c.getUsername() + " " + c.getPassword() + " " + c.getPhoneNum());
			}
			
			customerFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			String message = "Customer file not found, please repair before use.";
			String title = "File Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}
	}
	
	// Item file format: itemname 
	// 					 itemprice numStock numSold 
	//					 path_to_itemimage
	// NOTE: file must have a blank line at the end
	public void updateItems() 
	{
		try 
		{
			PrintWriter itemFile = new PrintWriter("src\\entities\\data\\items.txt");
			
			for(Item i : items)
			{
				itemFile.println(i.getItemName());
				itemFile.println(i.getPrice() + " " + i.getNumStock() + " " + i.getNumSold());			
				itemFile.println(i.getImagePath());
			}
			
			itemFile.close();
		} 
		catch (FileNotFoundException e) 
		{
			String message = "Item file not found, please repair before use.";
			String title = "File Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}
	}
	
	/*public static void main(String[] args) {
		GiftStore.letConnect();
	}*/
}