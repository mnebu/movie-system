package entities;	// no changes


import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Item 
{
	private String itemname, imagepath;
	private double itemprice;
	private int numStock, numSold;
	
	public Item(String name, double price, int stock, int sold, String path) throws ItemException
	{
		if(name.length() >= 3 && name.length() <= 35)
			itemname = name;
		else
			throw new InvalidItemName();

		if(price > 0)
			itemprice = price;
		else
			throw new InvalidPrice();
		
		if(stock >= 0)
			numStock = stock;
		else
			throw new InvalidStockNum();
			
		if(sold >= 0)
			numSold = sold;
		else
			throw new InvalidSoldNum();
		
		imagepath = path;
	}
	
	public String getItemName() {return itemname;}
	public double getPrice() {return itemprice;}
	public int getNumStock() {return numStock;}
	public int getNumSold() {return numSold;}
	public String getImagePath() {return imagepath;}
	
	public void changeItemName(String name) throws InvalidItemName
	{
		if(name.length() >= 3 && name.length() <= 35)
		{
			itemname = name;
			String newpath = String.format("src\\entities\\data\\images\\%s.jpg", itemname); // change path to match item name
			(new File(imagepath)).renameTo(new File(newpath)); // for changing file name
			imagepath = newpath;
		}
		else
			throw new InvalidItemName();
	}
	public void changeItemPrice(double price) throws InvalidPrice
	{
		if(price > 0)
			itemprice = price;
		else
			throw new InvalidPrice();
	}
	public void changeNumStock(int stock) throws InvalidStockNum
	{
		if(stock >= 0)
			numStock = stock;
		else
			throw new InvalidStockNum();
	}
	public void changeImage(BufferedImage img)
	{
		// images are saved in entities.data.images folder with itemname.png as file name
		try 
		{
			ImageIO.write(img, "jpg", new File(imagepath));
		} 
		catch (IOException e) 
		{
			String message = "Image file not found, please repair before use.";
			String title = "File Error";
			int icon = JOptionPane.ERROR_MESSAGE;
			JOptionPane.showMessageDialog(null, message, title, icon);
			System.exit(0);
		}
	}
	
	public void buyItem(int quantity)
	{
		numStock -= quantity;
		numSold += quantity;
	}
}