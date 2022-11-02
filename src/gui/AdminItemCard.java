package gui;

import entities.GiftStore;
import entities.Item;
import entities.ItemException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminItemCard extends ItemCard
{
	public AdminItemCard(Item i, GiftStore store) 
	{
		super(i, store);
		
		stock.setText("In Stock: ");
		stock.setVerticalAlignment(JLabel.BOTTOM);
		added.setText("<html>Sold: <br>" + item.getNumSold() + "</html>");
		save.setText("Save Changes");
		amount.setText("" + item.getNumStock());
	}
	
	public void setSaveAction() 
	{
		save.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						String iname = itemname.getText();
						double iprice = Double.parseDouble(itemprice.getText());
						int istock = Integer.parseInt(amount.getText());
						
						item.changeItemName(iname);
						item.changeItemPrice(iprice);
						item.changeNumStock(istock);
						
						kustore.updateItems();
						
						error.setText(" ");
					}
					catch (ItemException err1)
					{
						error.setText(err1.getMessage());
					}
					catch(NumberFormatException err2)
					{
						error.setText("Please enter valid numerical values");
					}
					itemname.setText(item.getItemName());
					itemprice.setText("" + item.getPrice());
					amount.setText("" + item.getNumStock());
				}
			});
	}

	public void setImageAction()
	{
		itemimage.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					try 
					{
						BufferedImage chosen = (new ImageSelector()).getImage();
						item.changeImage(chosen);
						error.setText(" ");
						
						Image img = (new ImageIcon(item.getImagePath())).getImage();
						if(250 > 130 * img.getWidth(null) / img.getHeight(null))
							itemimage.setIcon(new ImageIcon(img.getScaledInstance(130 * img.getWidth(null) / img.getHeight(null), 130, Image.SCALE_DEFAULT)));
						else
							itemimage.setIcon(new ImageIcon(img.getScaledInstance(250, 250 * img.getHeight(null) / img.getWidth(null), Image.SCALE_DEFAULT)));
					} 
					catch (IOException err1) 
					{
						error.setText("Your image file does not exist."); 
					}
					catch (IllegalArgumentException err2)
					{
						// do nothing, user has not selected an image
					}
					catch (NullPointerException err3)
					{
						// user has selected something other than image file
						error.setText("Please select an image file");
					}
				}
			});
	}
}