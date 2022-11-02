package gui;

import entities.GiftStore;
import entities.Item;
import entities.Customer;
import entities.ItemException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CustomerItemCard extends ItemCard
{
	public CustomerItemCard(Item i, GiftStore store) 
	{
		super(i, store);
		
		itemname.setEditable(false);
		itemname.setOpaque(false);
		itemprice.setEditable(false);
		itemprice.setOpaque(false);
		save.setText("Add to Cart");
		stock.setText("<html>In Stock:<br>" + item.getNumStock() + "</html>");
		int num = 0;
		try
		{
			num = ((Customer) kustore.getCurrentLogin()).getCart().get(i);
		}
		catch(NullPointerException e)
		{ 
			// nothing, num already set to 0
		}
		added.setText("<html>Added:<br>" + num + "</html>");
	}

	public void setSaveAction() 
	{
		save.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						((Customer) kustore.getCurrentLogin()).addToCart(item, Integer.parseInt(amount.getText()));
						int quantity = ((Customer) kustore.getCurrentLogin()).getCart().get(item);
						added.setText("<html>Added:<br>" + quantity + "</html>");
						error.setText(" ");
					}
					catch (ItemException err)
					{
						error.setText(err.getMessage());
					}
					catch(NumberFormatException err2)
					{
						error.setText("Please enter a number.");
					}
					amount.setText("1");
				}
			});
	}

	public void setImageAction() {/*no action*/}
}