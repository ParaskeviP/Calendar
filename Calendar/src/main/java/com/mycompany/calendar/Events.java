/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author panagopoulou
 */
public class Events {
    
    private Scanner input = new Scanner(System.in);
    private LocalDateTime date;
    private String title;
    String description;
    
    public Events(LocalDateTime date , String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addEvent() throws ParseException {
        
        
        System.out.println("Please enter the title of the event:");
        String titleEvent = input.nextLine();
        
        System.out.println("Please enter the date of the event (dd/mm/yyyy):");
        String dateInput = input.nextLine();
 /*       
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        Date dateEvent = dateFormat.parse(dateInput);
*/        
        System.out.println("Do you want to add the hour of the event? (YES/NO)");
        String answer = input.nextLine();
        
        if (answer.equalsIgnoreCase("YES")) { 
            System.out.println("Please enter the time of the event (hh:mm:ss):");
            String time = input.nextLine();
            
            while(!time.contains(":")) {
                System.out.println("Please enter the correct time(hh:mm:ss)");
                time = input.nextLine();
            }
        }
        
        System.out.println("Please enter the description of the event:");
        String descEvent = input.nextLine();
   
        System.out.println("Event added successfully!");
        
    }
    
    public void updateEvent() {
        
        int option;
        
        do{
            System.out.println("What do you want to update?");
            System.out.println("Choose 1 for title, 2 for date, 3 for time, 4 for description");
            option = input.nextInt();
            System.out.println("Please enter the date and time of the event before changes:");
            System.out.println(" (dd/mm/yyyy) for date and (hh:mm:ss) for time");
            String dt = input.nextLine();
            String tm = input.nextLine();
            if (option == 1) {
                System.out.println("Please enter the new title:");
                
            } else if (option == 2) {
                System.out.println("Please enter the new date: (dd/mm/yyyy)");              
            } else if (option == 3) {
                System.out.println("Please enter the new time: (hh:mm:ss)");
            } else if (option == 4) {
                System.out.println("Please enter the new description:");
            } else {
                System.out.println("You entered wrong input");
                System.exit(1);
            }
                
        }while (option != 1 || option != 2 || option != 3 || option != 4);
        
    }
    
    
    public void printEvent() {
        System.out.println("The title of the event is " + title);
        System.out.println("The description is " + description);
    }
    
}
