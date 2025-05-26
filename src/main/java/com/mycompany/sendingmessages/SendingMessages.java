/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sendingmessages;

import java.util.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.Font;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Adhir
 */
public class SendingMessages {
    private static String storedUsername;
     private static String storedPassword;
     private static String registeredCellPhoneNumber;
      private static boolean isLoggedIn = false;
      
      private static List<Message> messages = new ArrayList<>();
      private static int messageCount = 0;
      private static Random random = new Random();
      private static JSONArray jsonMessages = new JSONArray();
    
//Color and font styling 

      static {
          UIManager.put("OptionPane.background", new ColorUIResource(230,245,255));
          UIManager.put("Panel.background", new ColorUIResource(230,245,255));
          UIManager.put("OptionPane.messageForeground", new ColorUIResource(10,0,80));
          UIManager.put("OptionPane.messageFont", new Font("Tahoma", Font.BOLD, 14));
          }
//Username Validation
      public static boolean checkUserName(String username) {
          return username.contains("_") && username.length() <= 5;
      }
    
      //Password Validation
      public static boolean checkPasswordComplexity(String password) {
          boolean hasCapital = !password.equals(password.toLowerCase());
          boolean hasNumber = password.matches(".*\\d*");
          boolean hasSpecial = !password.matches("[A-Za-z0-9]*");
          boolean isLongEnough = password.length()>= 8;
          return hasCapital && hasNumber && hasSpecial && isLongEnough;
      }
      
      //Phone Number Validation
      public static boolean checkCellPhoneNumber(String phoneNumber) {
          return Pattern.matches("^\\+27\\d{9}$", phoneNumber) && phoneNumber.length() == 12;
      }
    public String generateStrongPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=[]{}";
        
        String all = upper + lower + digits + special;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        for (int i = 4; i< 10; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }
        return password.toString();
    }
    public static String registerUser (String username,String password,String phoneNumber) {
        if(!checkUserName(username)){
            return "Username is not correctly formatted. Please ensure it contains an underscore(_) "
                    + "and is no more than five(5) characters";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted. It must be at least 8 characters"
                    + "and include a capital letter, number and special character.";
        }
        if (!checkCellPhoneNumber(phoneNumber)) {
        return "Phone number is incorrectly formatted. It must start with +27 and be 12 characters long.";
    }
        storedUsername = username;
        storedPassword = password;
        return "User has been registered successfully.";
        
    }
    
    public static boolean loginUser(String username,String password){
        isLoggedIn = username.equals(storedUsername) &&
                password.equals(storedPassword);
        return isLoggedIn;
    }
   
    public static String returnLoginStatus (boolean success, String firstName, String lastName)  {
        return success
                ?" Welcome " + firstName + " " + lastName + " , it is great to see you again!"
                :"Username or password incorrect, please try again.";
        
            
    }
    
    //Message Class 
    
    static class Message {
        String id, recipient,content,hash;
        Message (String recipient,String content) {
            this.id = String.valueOf(1000000000 + random.nextInt(900000000));
            this.recipient = recipient;
            this.content = content;
            this.hash = generateHash(this.id,this.content);
        }
        static String generateHash(String id, String message) {
            String[] words = message.trim().split("\\s+");
            String first = words.length > 0 ? words[0].toUpperCase() : " ";
            String last = words.length > 1 ? words[words.length - 1].toUpperCase() : " ";
            return id.substring(0,2) + ":0:" + first + last;
        }
        
        String getFormattedMessage() {
            return "Message ID: " + id + "\n" + "Message Hash: " + hash + "\n " + "Recipient: " + recipient + "\n" + "Message: " + content + "\n";
        }

JSONObject toJSON() {
JSONObject obj = new JSONObject();
obj.put("MessageID", id);
obj.put("MessageHash", hash);
obj.put("Message", content);
return obj;
}

}
    
    public static void storeMessageToJSON(Message msg) {
        jsonMessages.add(msg.toJSON());
        try (FileWriter file = new FileWriter("messages.json")) {
            
            file.write(jsonMessages.toJSONString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Error saving message to JSON.");
        }
    }


    
    //Send Message 
    public static void sendMessages() {
try { int total = Integer.parseInt(JOptionPane.showInputDialog("How many messages do you want to send?"));
for (int i = 0; i < total; i++) {
String recipient = JOptionPane.showInputDialog("Enter recipient number (+27XXXXXXXXX)");
if (! checkCellPhoneNumber(recipient)) {
JOptionPane.showMessageDialog(null, "Phone number is incorrectly formatted!");
i--;
continue;
}
String msg = JOptionPane.showInputDialog("Enter message (max 250 characters)");
if (msg.length()> 250){
    JOptionPane.showMessageDialog(null,"Message exceeds 250 characters.Please shorten.");
i--; 
continue;
}
Message m = new Message(recipient, msg);
String[] choices = {"Send Message", "Disregard Message", "Store Message"};
int action = JOptionPane.showOptionDialog(null, "Choose what to do with this message:", "Message Options",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
if (action == 0) {
messages.add(m);
messageCount++;

JOptionPane.showMessageDialog(null, "Message sent \n\n" + m.getFormattedMessage());
} else if (action == 2) {
storeMessageToJSON(m);

JOptionPane.showMessageDialog(null, "Message successfully stored to send later.");
} else {
JOptionPane.showMessageDialog(null, "Message discarded");
}
}
} catch (NumberFormatException e)
{
    JOptionPane.showMessageDialog(null, "Invalid number of messages.");
}
    }
        



                        
                    
       
    
  //Main Application  
  
    public static void main(String[] args) {
       //Registeration
       String Firstname = JOptionPane.showInputDialog("First Name - Please Enter Name:");
       String Lastname = JOptionPane.showInputDialog("Surname - Please Enter Surname:");
       String username = JOptionPane.showInputDialog("Register - Enter Username:");
       String password = JOptionPane.showInputDialog("Enter password");
       String phone = JOptionPane.showInputDialog("Enter phone number (+27XXXXXXXXX)");
       
       String regMsg = registerUser(username, password, phone);
       
       JOptionPane.showMessageDialog(null,regMsg);
       if (! regMsg.contains("successfully"))
           return;
       
       //Login
       String user2 = JOptionPane.showInputDialog("Login - Enter username:");
       String pass2 = JOptionPane.showInputDialog("Enter Password:");
String fn = JOptionPane.showInputDialog("Enter first name: ");
String ln = JOptionPane.showInputDialog("Enter last name:");
       
       
       boolean loggedIn = loginUser(user2, pass2);
               
       JOptionPane.showMessageDialog(null, returnLoginStatus(loggedIn, fn ,ln));
       if (!loggedIn) return;
       
       //Menu 
       while (true) {
           String[] options = {
               "Send Message", "Show Messages (Coming soon)", "Quit"};
           int choice = JOptionPane.showOptionDialog(null,"Welcome to QuickChat! \nChoose an option: ", "QuickChat Menu",
                   JOptionPane.DEFAULT_OPTION,
                   JOptionPane.INFORMATION_MESSAGE,
                   null,
                   options,
           options[0]);
           
           if (choice == 0) {
               sendMessages ();
           } else if (choice == 2 || choice == JOptionPane.CLOSED_OPTION){
               JOptionPane.showMessageDialog(null,"Total messages sent: " + messageCount);
               break;
               
           } else {
               JOptionPane.showMessageDialog(null, "Feature coming soon!");
           }
               
           
       }
    }
}
