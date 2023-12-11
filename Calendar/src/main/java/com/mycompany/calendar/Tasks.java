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
public class Tasks {
    
    private Scanner input = new Scanner(System.in);
    private LocalDateTime date;
    private String title;
    private String description;
    private String status;
    private LocalDateTime deadline;
    
    public Tasks(LocalDateTime date, LocalDateTime deadline, String title, String description, String status) {
        this.date = date;
        this.deadline = deadline;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Scanner getInput() {
        return input;
    }

    public void setInput(Scanner input) {
        this.input = input;
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

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    public void addTask() throws ParseException  {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Please enter the title of the project:");
        title = input.nextLine();
        
        System.out.println("Please enter the date and time of task(dd/MM/yyyy HH:mm:ss):");
        String dateTime = input.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateT = dateFormat.parse(dateTime);
        // Μετατροπή από Date σε LocalDateTime
        date = dateT.toInstant().atZone(dateFormat.getTimeZone().toZoneId()).toLocalDateTime();
        
        
        System.out.println("Please enter the deadline date and time of the project ( hh:mm:ss ):");       
        String deadlineTime = input.nextLine();
        SimpleDateFormat dateF = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date deadlineDateTime = dateF.parse(deadlineTime);
        // Μετατροπή από Date σε LocalDateTime
        deadline = deadlineDateTime.toInstant().atZone(dateFormat.getTimeZone().toZoneId()).toLocalDateTime();
        
        System.out.println("Please enter the description of the project:");
        description = input.nextLine();
        
        System.out.println("Is the project completed? (YES/NO)");
        String answer = input.nextLine();
        
        if (answer.equalsIgnoreCase("YES")) {
            status = "Completed";
        }
   
        System.out.println("Appointment added successfully!");
    }
    
    public void printTask() {
        System.out.println("The title of the task is " + title);
        System.out.println("The description is " + description);
        System.out.println("The date is " + date);
        System.out.println("The deadline is " + deadline);
        System.out.println("The status is " + status);

    }
    
}
