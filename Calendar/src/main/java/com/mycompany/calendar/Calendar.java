/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.Duration;

import gr.hua.dit.oop2.calendar.TimeTeller;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import gr.hua.dit.oop2.calendar.TimeService;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author panagopoulou
 */

public class Calendar {

    public static void main(String[] args) throws FileNotFoundException  {
            Scanner input = new Scanner(System.in);

            TimeTeller teller = TimeService.getTeller();
         
            if (args.length == 2) { // Μπαινει στην πρωτη λειτουργια
            
                if (!("day".equals(args[0]) || "week".equals(args[0]) || "month".equals(args[0]) || "pastday".equals(args[0]) || "pastweek".equals(args[0]) || "pastmonth".equals(args[0]) || "todo".equals(args[0]) || "due".equals(args[0]) || args[1].contains(".ics"))) {
                    System.out.println("Arguments aren't correct!");
                    System.exit(1);
                } else {
                    List<Appointments> appointments = new ArrayList<Appointments>();
                    List<Tasks> tasks = new ArrayList<Tasks>();
                   
                    boolean vevent = false;
                    String desc = "";
                    LocalDateTime date = null;
                    String title = "";
                    LocalDateTime deadline = null;
                    String status = "";
                    Duration duration = null;

                    String firstArgument = args[0];
                    String secArgumnet = args[1];

                    File file = new File(args[1]);
                    Scanner sc = new Scanner(file); //stack overflow              
                    //String line = sc.nextLine();
                    String line;
                    System.out.println("Do you want to add an appointment or a task?");
                    String answer = input.nextLine();
                    while (sc.hasNextLine()) { 
                        line = sc.nextLine();
                        if ( answer.equalsIgnoreCase("Appointment")) {                           
                           
                            if (line.contains("BEGIN:VEVENT")) {
                                                            
                                vevent = true;
                                
                            } else if (line.contains("DESCRIPTION:")) {
                            
                                desc = line;
                                desc = desc.replace("DESCRIPTION:",""); //stackoverflow
                                
                            } else if (line.contains("DTSTART")) {
                            
                                String[] parts = line.split(":");
                                String dt = parts[1].trim(); 
                                //System.out.println(dt);
                                // Έλεγχος εάν περιέχει ώρα
                                if (!dt.contains("T")) {
                                    dt += "T000000"; // Προσθήκη ώρας προεπιλογής
                                }
                                //System.out.println(dt);
                                // Καθορίστε τον φορματ της ημερομηνίας
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");                                
                                // Μετατρέψτε το String σε LocalDateTime
                                date = LocalDateTime.parse(dt, formatter);                                
                                //System.out.println(date);
                           
                            } else if (line.contains("DURATION")) {
                               
                                String[] parts = line.split(":");
                                String d = parts[1].trim();                            
                                duration = Duration.parse(d);
                            
                            } else if (line.contains("SUMMARY")) {
                            
                                String[] parts = line.split(":");
                                title = parts[parts.length-1].trim(); 
                            
                            } else if (line.contains("END:VEVENT")) {
                            
                                vevent = false;
                                Appointments appointment = new Appointments(date, title, desc, duration);
                                appointments.add(appointment);    
                                
                            }
                        //    System.out.println("Appointment added succesfully!");
                        } else if (answer.equalsIgnoreCase("Task")) {
 
                            if (line.contains("BEGIN:VTODO")) {
                                                                
                                vevent = true;
                                
                            } else if (line.contains("DTSTAMP")) {
                                
                                String[] parts = line.split(":");
                                String dt = parts[1].trim(); 
                                if (!dt.contains("T")) {
                                    dt += "T000000Z"; // Προσθήκη ώρας προεπιλογής
                                }
                                 // Καθορίστε τον φορματ της ημερομηνίας
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                
                                // Μετατρέψτε το String σε LocalDateTime                               
                                date = LocalDateTime.parse(dt, formatter);   
                                
                            } else if (line.contains("DUE;VALUE=DATE")) {   
                                
                                String[] parts = line.split(":");
                                String d = parts[1].trim();
                                if (!d.contains("T")) {
                                    d += "T000000"; // Προσθήκη ώρας προεπιλογής
                                }
                                 // Καθορίστε τον φορματ της ημερομηνίας
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");                                
                                // Μετατρέψτε το String σε LocalDateTime
                                deadline = LocalDateTime.parse(d, formatter);
                                
                            } else if (line.contains("SUMMARY")) {
                                
                                String[] parts = line.split(":");
                                title = parts[parts.length-1].trim(); 
                            
                            } else if (line.contains("DESCRIPTION:")) {
                            
                                desc = line;
                                String[] parts = line.split(":");
                                desc = parts[1].trim(); 
                            
                            } else if (line.contains("STATUS")) {
                                                            
                                String[] parts = line.split(":");
                                String s = parts[1].trim(); 
                                if(s.equals("NEEDS-ACTION") || s.equals("IN-PROCESS") || s.equals("CANCELLED")) {
                                    status = "INCOMPLETED";
                                } else {
                                    status = "'COMPLETED";
                                }
                            
                            } else if (line.contains("END:VTODO")) {
                            
                                vevent = false;
                                Tasks task = new Tasks(date,deadline, title, desc, status);
                                tasks.add(task);
                                
                            }   
                             
                        } else {
                            System.out.println("You entered wrong answer.");
                            System.exit(1);
                        }
                        
                    }
                //    for(Appointments appointment : appointments) {
                //        System.out.println(appointment.getDate());
                //    }
                        for(Tasks task : tasks) {
                            System.out.println(task.getDate());
                        }
                    LocalDateTime today = teller.now();
                    
                    if (args[0].equals("day")) {                       
                           
                        LocalDateTime dateTime = teller.now();
                        // Find the end of the day                          
                        LocalDateTime endOfDay = dateTime.toLocalDate().atTime(LocalTime.MAX);                        
                        
                        for(Appointments appointment : appointments) {
                            
                           if(dateTime.compareTo(appointment.getDate()) < 0) {
  
                              int result = appointment.getDate().compareTo(endOfDay);
                              
                                if(result < 0) { //dateTime < endOfDay
                                    appointment.printAppointment();
                                }  

                            }
                        } 
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {

                                int result = task.getDate().compareTo(endOfDay); 

                                if(result < 0) { //dateTime > startOfMonth
                                    task.printTask();
                                }                               

                            } 
                        } 
                        
                    } else if (args[0].equals("week")) {
                        
                        LocalDateTime dateTime = teller.now();
                        //βρισκει το τελος της εβδομαδας χρησιμοποιοντας το temporalAdjusters
                        LocalDateTime endOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                        endOfWeek = endOfWeek.withHour(23).withMinute(59).withSecond(59);
                        System.out.println(endOfWeek);
                        for(Appointments appointment : appointments) {
                                                
                            if(dateTime.compareTo(appointment.getDate()) < 0) {
                                
                                int result = appointment.getDate().compareTo(endOfWeek);
                                
                                if(result < 0) { //dateTime < endOfWeek
                                    appointment.printAppointment();
                                    System.out.println(" ");
                                }                               
                            
                            }   
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {
   
                            int result = task.getDate().compareTo(endOfWeek); 

                                if(result < 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        
                    } else if (args[0].equals("month")) {
                    
                        LocalDateTime dateTime = teller.now();
                        LocalDateTime endOfMonth = dateTime.with(TemporalAdjusters.lastDayOfMonth());// Find the end of the month using TemporalAdjusters
                        endOfMonth = endOfMonth.withHour(23).withMinute(59).withSecond(59);
                        
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) < 0)  {
                                int result = appointment.getDate().compareTo(endOfMonth); 
                                
                                if(result < 0) { //dateTime < endOfMonth
                                    appointment.printAppointment();
                                    System.out.println(" ");
                                }  
                                
                            }
                            
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {

                                int result = task.getDate().compareTo(endOfMonth); 

                                if(result < 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                                                
                    } else if (args[0].equals("pastday")) {
                        
                        LocalDateTime dateTime = teller.now();
                        // Find the start of the day
                        LocalDateTime startOfDay = dateTime.toLocalDate().atTime(LocalTime.MIN);
                            
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) > 0) {
                                
                                int result = appointment.getDate().compareTo(startOfDay); 

                                if(result > 0) { //dateTime > startOfDay
                                    appointment.printAppointment();
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.equals(task.getDate())) {

                                int result = task.getDate().compareTo(startOfDay); 

                                if(result > 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        } 
                        
                    } else if (args[0].equals("pastweek")) {
                        
                        LocalDateTime dateTime = teller.now();
                            // Find the start of the day
                        LocalDateTime startOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atTime(LocalTime.MIN);
                        
                              
                        for(Appointments appointment : appointments) {
                                
                            if(dateTime.compareTo(appointment.getDate()) > 0) {

                                int result = appointment.getDate().compareTo(startOfWeek); 

                                if(result > 0) { //dateTime > startOfDay
                                    appointment.printAppointment();
                                    System.out.println(" ");
                                }                               

                            } 
                        }
                        for(Tasks task : tasks) {
                            System.out.println(task.getDate());
                            if(dateTime.equals(task.getDate())) {

                                int result = task.getDate().compareTo(startOfWeek); 

                                if(result > 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        
                    } else if (args[0].equals("pastmonth")) {
                        
                        LocalDateTime dateTime = teller.now();
                        // Find the start of the day
                        LocalDateTime startOfMonth = dateTime.toLocalDate().atTime(LocalTime.MIN);
                            
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) > 0) {

                                int result = appointment.getDate().compareTo(startOfMonth); 

                                if(result > 0) { //dateTime > startOfMonth
                                    appointment.printAppointment();
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        } 
                        for(Tasks task : tasks) {
                            
                            if(dateTime.equals(task.getDate())) {

                                int result = task.getDate().compareTo(startOfMonth); 

                                if(result > 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                    //για να εκτυπώσει τις εργασίες που δεν ολοκληρώθηκαν και δεν πέρασε η προσθεσμία.    
                    } else if (args[0].equals("todo")) { 
                        
                        LocalDateTime dateTime = teller.now();
                       
                        for(Tasks task : tasks ) {
                                                       
                            int result = task.getDate().compareTo(task.getDeadline());
                            
                            while( (result > 0) && (!status.equals("COMPLETED")) ) { // dateTime > deadline & STATUS isn't completed
                                task.printTask();
                                
                                result = dateTime.compareTo(task.getDeadline()); //ενημερωνει την result για το νεο deadline
                            }
                                
                    }
                    //για να εκτυπώσει τις εργασίες που δεν ολοκληρώθηκαν και πέρασε η προσθεσμία.    
                    } else if (args[0].equals("due")) {
                            
                        LocalDateTime dateTime = teller.now();
                       
                        for(Tasks task : tasks ) {
                            
                            int result = task.getDate().compareTo(task.getDeadline());
                            
                            while( (result < 0) && (!status.equals("COMPLETED")) ) { // dateTime < deadline & STATUS isn't completed
                                task.printTask();
                            }
                                
                        }
                      
                    }                  
                    sc.close();
                    
                }

            } else if (args.length == 1 ){ // μπαινει στην δευτερη λειτουργια

                if(!args[0].contains(".ics")) {
                    System.out.println("Argument isn't correct!");
                    System.exit(1);
                } else {
                    //todo
                }

            } else {

                System.out.println("You entered wrong arguments");
                System.exit(1);

            }
    }
        
}