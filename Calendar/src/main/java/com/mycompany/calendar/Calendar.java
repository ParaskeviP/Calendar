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


/* 
ΤΙ ΜΕΝΕΙ ΝΑ ΚΑΝΩ
    add
    description
    μηνυμα αν δεν εχει να εμφανισει γεγονότα !!!!
    χρεαιζεται το task σε day, week, ....
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
                                    d += "T000000Z"; // Προσθήκη ώρας προεπιλογής
                                }
                                 // Καθορίστε τον φορματ της ημερομηνίας
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                
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
                    
                        
                    LocalDateTime dateTime = teller.now();
                    boolean check = false; 
                    if (args[0].equals("day")) {                       
                        
                        // Find the end of the day                          
                        LocalDateTime endOfDay = dateTime.toLocalDate().atTime(LocalTime.MAX);                        
                        
                        for(Appointments appointment : appointments) {
                            
                           if(dateTime.compareTo(appointment.getDate()) < 0) {
  
                              int result = appointment.getDate().compareTo(endOfDay);
                              
                                if(result < 0) { //dateTime < endOfDay
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                }  

                            }
                        } 
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {

                                int result = task.getDate().compareTo(endOfDay); 

                                if(result < 0) { //dateTime < startOfMonth
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        if (check == false) {
                            System.out.println("There are no events for today.");
                        }
                        
                    } else if (args[0].equals("week")) {
                        
                        //βρισκει το τελος της εβδομαδας χρησιμοποιοντας το temporalAdjusters
                        LocalDateTime endOfWeek = dateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                        endOfWeek = endOfWeek.withHour(23).withMinute(59).withSecond(59);

                        for(Appointments appointment : appointments) {
                                                
                            if(dateTime.compareTo(appointment.getDate()) < 0) {
                                
                                int result = appointment.getDate().compareTo(endOfWeek);
                                
                                if(result < 0) { //dateTime < endOfWeek
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                }                               
                            
                            }   
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {
   
                            int result = task.getDate().compareTo(endOfWeek); 

                                if(result < 0) { //dateTime < endOfWeek
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        if (check == false) {
                            System.out.println("There are no events for this week.");
                        }
                        
                    } else if (args[0].equals("month")) {
                    
                        LocalDateTime endOfMonth = dateTime.with(TemporalAdjusters.lastDayOfMonth());// Find the end of the month using TemporalAdjusters
                        endOfMonth = endOfMonth.withHour(23).withMinute(59).withSecond(59);
                        
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) < 0)  {
                                int result = appointment.getDate().compareTo(endOfMonth); 
                                
                                if(result < 0) { //dateTime < endOfMonth
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                }  
                                
                            }
                            
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {

                                int result = task.getDate().compareTo(endOfMonth); 

                                if(result < 0) { //dateTime < endOfMonth
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        }
                        if (check == false) {
                            System.out.println("There are no events for this month.");
                        }
                                                
                    } else if (args[0].equals("pastday")) {
                        
                        // Find the start of the day
                        LocalDateTime startOfDay = dateTime.toLocalDate().atTime(LocalTime.MIN);
                            
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) > 0) {
                                
                                int result = appointment.getDate().compareTo(startOfDay); 

                                if(result > 0) { //dateTime > startOfDay
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) > 0) {

                                int result = task.getDate().compareTo(startOfDay); 

                                if(result > 0) { //dateTime > startOfDay
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        }
                        if (check == false) {
                            System.out.println("There were no events for today.");
                        }
                        
                    } else if (args[0].equals("pastweek")) {
                        
                        // Find the start of the day
                        LocalDateTime startOfWeek = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toLocalDate().atTime(LocalTime.MIN);
                        
                              
                        for(Appointments appointment : appointments) {
                                
                            if(dateTime.compareTo(appointment.getDate()) > 0) {

                                int result = appointment.getDate().compareTo(startOfWeek); 

                                if(result > 0) { //dateTime > startOfWeek
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                    
                                }                               

                            } 
                        }
                        for(Tasks task : tasks) {
                            System.out.println(task.getDate());
                            
                            if(dateTime.compareTo(task.getDate()) > 0) {

                                int result = task.getDate().compareTo(startOfWeek); 

                                if(result > 0) { //dateTime > startOfWeek
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        if (check == false) {
                            System.out.println("There were no events for this week.");
                        }
                        
                    } else if (args[0].equals("pastmonth")) {
                        
                        // Find the start of the day
                        LocalDateTime startOfMonth = dateTime.toLocalDate().atTime(LocalTime.MIN);
                            
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) > 0) {

                                int result = appointment.getDate().compareTo(startOfMonth); 

                                if(result > 0) { //dateTime > startOfMonth
                                    appointment.printAppointment();
                                    check = true;
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        } 
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) > 0) {

                                int result = task.getDate().compareTo(startOfMonth); 

                                if(result > 0) { //dateTime > startOfMonth
                                    task.printTask();
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        if (check == false) {
                            System.out.println("There were no events for this month.");
                        }
                    //για να εκτυπώσει τις εργασίες που δεν ολοκληρώθηκαν και δεν πέρασε η προσθεσμία.    
                    } else if (args[0].equals("todo")) { 
                                        
                        for(Tasks task : tasks ) {
                                                      
                            int result = dateTime.compareTo(task.getDeadline());
                            System.out.println(task.isStatus());
                            System.out.println(task.getDeadline()); 
                            
                            if( (result < 0) && (!status.equals("COMPLETED")) ) { // dateTime > deadline & STATUS isn't completed
                                task.printTask();
                                check = true;
                                System.out.println(" ");
                                result = dateTime.compareTo(task.getDeadline()); //ενημερωνει την result για το νεο deadline
                            }
                                    
                        }
                        if (check == false) {
                            System.out.println("There are no events that were not completed and the deadline has not passed.");
                        }
                    //για να εκτυπώσει τις εργασίες που δεν ολοκληρώθηκαν και πέρασε η προσθεσμία.    
                    } else if (args[0].equals("due")) {
                                                   
                        for(Tasks task : tasks ) {
                            
                            int result = dateTime.compareTo(task.getDeadline());
                            
                            if( (result > 0) && (!status.equals("COMPLETED")) ) { // dateTime < deadline & STATUS isn't completed
                                task.printTask();
                                check = true;
                                System.out.println(" ");
                            }
                                
                        }
                        if (check == false) {
                        System.out.println("There are no events that were not completed and the deadline has passed.");
                        }
                    }     
                    
                    sc.close();
                    TimeService.stop();
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