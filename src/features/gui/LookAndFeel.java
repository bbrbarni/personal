/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    LookAndFeel.java
 *    Copyright (C) 2005 University of Waikato, Hamilton, New Zealand
 *
 */


package features.gui;


import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import features.core.Utils;

/**
 * A little helper class for setting the Look and Feel of the user interface.
 * Was necessary, since Java 1.5 sometimes crashed the WEKA GUI (e.g. under 
 * Linux/Gnome). Running this class from the commandline will print all
 * available Look and Feel themes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 7059 $
 */
public class LookAndFeel {
  
  /** The name of the properties file */
  public static String PROPERTY_FILE = "features/gui/LookAndFeel.props";

  /** Contains the look and feel properties */
  protected static Properties LOOKANDFEEL_PROPERTIES;

  static {
    try {
      LOOKANDFEEL_PROPERTIES = Utils.readProperties(PROPERTY_FILE);
    } 
    catch (Exception ex) {
      JOptionPane.showMessageDialog(null,
       Messages.getInstance().getString("LookAndFeel_Exception_JOptionPaneShowMessageDialog_Text_First") 
       + PROPERTY_FILE 
       + Messages.getInstance().getString("LookAndFeel_Exception_JOptionPaneShowMessageDialog_Text_Second") 
       + System.getProperties().getProperty("user.home") 
       + Messages.getInstance().getString("LookAndFeel_Exception_JOptionPaneShowMessageDialog_Text_Third"),
       Messages.getInstance().getString("LookAndFeel_Exception_JOptionPaneShowMessageDialog_Text_Fourth"),
       JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * sets the look and feel to the specified class
   * 
   * @param classname      the look and feel to use
   * @return               whether setting was successful   
   */
  public static boolean setLookAndFeel(String classname) {
    boolean          result;

    try {
      UIManager.setLookAndFeel(classname);
      result = true;
    }
    catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    return result;
  }

  /**
   * sets the look and feel to the one in the props-file or if not set the 
   * default one of the system
   * 
   * @return               whether setting was successful
   */
  public static boolean setLookAndFeel() {
    String           classname;

    classname = LOOKANDFEEL_PROPERTIES.getProperty(Messages.getInstance().getString("LookAndFeel_SetLookAndFeel_ClassName_Text"), "");
    if (classname.equals("")) {
      // Java 1.5 crashes under Gnome if one sets it to the GTKLookAndFeel 
      // theme, hence we don't set any theme by default if we're on a Linux 
      // box.
      if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
	return true;
      }
      else {
	classname = getSystemLookAndFeel();
      }
    }

    return setLookAndFeel(classname);
  }

  /**
   * returns the system LnF classname
   * 
   * @return               the name of the System LnF class
   */
  public static String getSystemLookAndFeel() {
    return UIManager.getSystemLookAndFeelClassName();
  }

  /**
   * returns an array with the classnames of all the installed LnFs
   * 
   * @return               the installed LnFs
   */
  public static String[] getInstalledLookAndFeels() {
    String[]               result;
    LookAndFeelInfo[]      laf;
    int                    i;

    laf    = UIManager.getInstalledLookAndFeels();
    result = new String[laf.length];
    for (i = 0; i < laf.length; i++)
      result[i] = laf[i].getClassName();

    return result;
  }
  
  /**
   * prints all the available LnFs to stdout
   * 
   * @param args	the commandline options
   */
  public static void main(String[] args) {
    String[]	list;
    int		i;
    
    System.out.println(Messages.getInstance().getString("LookAndFeel_Main_Text_First"));
    list = getInstalledLookAndFeels();
    for (i = 0; i < list.length; i++)
      System.out.println((i+1) + ". " + list[i]);

    System.out.println(Messages.getInstance().getString("LookAndFeel_Main_Text_Second") + PROPERTY_FILE + Messages.getInstance().getString("LookAndFeel_Main_Text_Third"));
  }
}
