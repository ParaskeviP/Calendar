/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.time.LocalDateTime;
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
    
    public void printTask() {
        System.out.println("The title of the task is " + title + ".");
        System.out.println("This event is a task.");        
        System.out.println("The description is " + description + ".");
        System.out.println("The date is " + date + ".");
        System.out.println("The deadline is " + deadline + ".");
        System.out.println("The status is " + status + ".");

    }
    
    public int compareTo(Tasks other) { // μέθοδος για ταξινόμηση πίνακα
        return this.date.compareTo(other.date);
    }
}
