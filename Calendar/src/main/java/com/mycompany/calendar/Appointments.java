/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.calendar;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private LocalDateTime dateEnd;
    private Scanner input = new Scanner(System.in);
    
    public Appointments(LocalDateTime date, LocalDateTime dateEnd, String title, String description, Duration duration) {
        this.date = date;
        this.dateEnd = dateEnd;
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

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }
    
    public void printAppointment() {
        System.out.println("The title of the appointment is " + title + ".");
        System.out.println("This event is an appointment.");
        System.out.println("The description is " + description + ".");
        System.out.println("The date is " + date + ".");
        System.out.println("The end date is " + dateEnd + ".");
        System.out.println("The duration is " + duration + ".");
    }
    
    public int compareTo(Appointments other) {
        return this.date.compareTo(other.date);
    }
    
}
