/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author panagopoulou
 */
public class Appointments {
    
    private LocalDateTime date;
    private String title;
    private String description;
    private Duration duration;
    private Scanner input = new Scanner(System.in);
    
    public Appointments(LocalDateTime date, String title, String description, Duration duration) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.duration = duration;
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

    public Scanner getInput() {
        return input;
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    public void addAppointment() throws ParseException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please enter the title of the appointment:");
        title = input.nextLine();
        
        System.out.println("Please enter the date and time of task(dd/MM/yyyy HH:mm:ss):");
        String dateTime = input.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateT = dateFormat.parse(dateTime);
        // Μετατροπή από Date σε LocalDateTime
        date = dateT.toInstant().atZone(dateFormat.getTimeZone().toZoneId()).toLocalDateTime();
        
        System.out.println("Please enter the duration of the appointment (μορφή PT2H30M):");
        String userInput = scanner.nextLine();

            try {
                // Δημιουργία της διάρκειας από τη συμβολοσειρά που εισήγαγε ο χρήστης
                duration = Duration.parse(userInput);

            } catch (Exception e) {
                System.out.println("Λανθασμένη μορφή διάρκειας. Χρησιμοποιήστε τη μορφή PT2H30M.");
            } finally {
                scanner.close();
            }
        
        System.out.println("Please enter the description of the appointment:");
        description = input.nextLine();
   
        System.out.println("Appointment added successfully!");
    }
    
    public void updateAppointment() {
        
        int option;
        Scanner scanner = new Scanner(System.in);
        
        do{
            System.out.println("What do you want to update?");
            System.out.println("Choose 1 for title, 2 for date, 3 for time, 4 for duration, 5 for description");
            option = input.nextInt();
            if (option == 1) {
                System.out.println("Please enter the new title:");
               
            } else if (option == 2) {
                System.out.println("Please enter the new date: (dd/mm/yyyy)");
                
            } else if (option == 3) {
                System.out.println("Please enter the new time: (hh:mm:ss)");
            } else if (option == 4) {
                System.out.println("Please enter the new duration: (μορφή PT2H30M)");
            } else if (option == 5) {
                System.out.println("Please enter the new description:");
            } else {    
                System.out.println("You entered wrong input");
                System.exit(1);
            }

            }while (option != 1 || option != 2 || option != 3 || option != 4);

        }
    
    public void printAppointment() {
        System.out.println("The title of the appointment is " + title);
        System.out.println("The description is " + description);
        System.out.println("The date is " + date);
        System.out.println("The duration is " + duration);
    }
    
}
