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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author panagopoulou
 */

public class Calendar {

    public static void main(String[] args) throws FileNotFoundException  {
            Scanner input = new Scanner(System.in);

            TimeTeller teller = TimeService.getTeller();
            List<Appointments> appointments = new ArrayList<Appointments>();
            List<Tasks> tasks = new ArrayList<Tasks>();
            
         
            if (args.length == 2) { // Μπαινει στην πρωτη λειτουργια, δηλαδή δέχεται στο terminal δύο ορίσματα      
                
                if (!("day".equals(args[0]) || "week".equals(args[0]) || "month".equals(args[0]) || "pastday".equals(args[0]) || "pastweek".equals(args[0]) || "pastmonth".equals(args[0]) || "todo".equals(args[0]) || "due".equals(args[0]) || args[1].contains(".ics"))) {
                    System.out.println("Arguments aren't correct!");
                    System.exit(1);
                } else {
                    
                   
                    boolean vevent = true; // έλεγχος για αν είναι σε vevent
                    boolean vtask = true; // έλεγχος για αν ειναι σε vtask
                    String desc = "";
                    LocalDateTime date = null;
                    LocalDateTime dateEnd = null;
                    String title = "";
                    LocalDateTime deadline = null;
                    String status = "";
                    Duration duration = null;

                    String firstArgument = args[0];
                    String secArgumnet = args[1];

                    File file = new File(args[1]); //το όνομα του file καθοριζεται απο το δεύτερο όρισμα
                    Scanner sc = new Scanner(file); // scanner για να ανοίξει και να διαβάσει το αρχείο line by line        
         
                    String line;

                    while (sc.hasNextLine()) { 
                        line = sc.nextLine(); //αποθήκευση μίας γραμμής του αρχείου στο string line για μετέπειτα επεξεργασία                       
                           
                        if (vevent) {
                            if (line.contains("BEGIN:VEVENT")) { // εάν μέσα στη γραμμή περιέχεται το "BEGIN:VEVENT"
                                                            
                                vevent = true; // γίνεται true επειδή μπήκε σε appointment
                                vtask = false; // γίνεται false επειδή μπαινει σε appointment
                                
                            } else if (line.contains("DESCRIPTION:")) {
                            
                                String[] parts = line.split(":"); //χωρίζει την εντολή στο μέρος πριν το " : " και μετά
                                desc = parts[1].trim(); // παίρνει το μέρος μετά το " : ", δηλαδή ολόκληρη την περιγραφή
                                
                            } else if (line.contains("DTSTART")) {
                            
                                String[] parts = line.split(":"); //χωρίζει την εντολή στο μέρος πριν το " : " και μετά
                                String dt = parts[1].trim(); // παίρνει το μέρος μετά το " : ", δηλαδή την ημερομηνία

                                if (!dt.contains("T")) { // ελέγχει εάν υπάρχει η ώρα
                                    dt += "T000000"; // προσθέτει ώρα εαν δεν υπάρχει
                                }                               
                                
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"); // καθορίζει το φορματ της ημερομηνίας                                                               
                                date = LocalDateTime.parse(dt, formatter); // μετατρέπει το String σε LocalDateTime                          
                           
                            } else if (line.contains("DURATION")) {
                               
                                String[] parts = line.split(":"); //χωρίζει την εντολή στο μέρος πριν το " : " και μετά
                                String d = parts[1].trim(); // παίρνει το μέρος μετά το " : ", δηλαδή την διάρκεια         
                                duration = Duration.parse(d); //μετατρέπει το String σε Duration
                                dateEnd = null; //εάν υπάρχει η διάρκεια δεν θα έχει DTEND 
                            
                            } else if(line.contains("DTEND")) { 
                                
                                String[] parts = line.split(":");//χωρίζει την εντολή στο μέρος πριν το " : " και μετά
                                //σε περίπτωση που το αρχείο περιέχει δίπλα απο το DTEND κατι άλλο, εμφανίζει το τελευταίο μέρος μόνο δηλαδή την ημερομηνία
                                //πχ. στο αρχείο greece.ics που περιέχει το VALUE=DATE δίπλα στο DTEND                                
                                String dtE = parts[parts.length - 1].trim(); 
                                if (!dtE.contains("T")) {
                                    dtE += "T000000"; // Προσθήκη ώρας προεπιλογής
                                }
                                
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"); // καθορίζει το φορματ της ημερομηνίας                                             
                                dateEnd = LocalDateTime.parse(dtE, formatter);// μετατρέπει το String σε LocalDateTime 
                                duration = null; //εάν υπάρχει το DTEND δεν θα υπλαρχει διάρκεια
                                
                            } else if (line.contains("SUMMARY")) {
                            
                                String[] parts = line.split(":");
                                title = parts[parts.length-1].trim(); 
                            
                            } else if (line.contains("END:VEVENT")) {
                                
                                vevent = true; //
                                vtask = true; //
                                //αποθήκευση και προσθήκη πληροφοριών στην λίστα
                                Appointments appointment = new Appointments(date, dateEnd, title, desc, duration);
                                appointments.add(appointment);    
                                
                            }
                        }
                        if (vtask) { //αντίστοιχα με τα παραπάνω
 
                            if (line.contains("BEGIN:VTODO")) {
                                                                
                                vevent = false;
                                vtask = true;
                                
                            } else if (line.contains("DTSTAMP")) {
                                
                                String[] parts = line.split(":");
                                String dt = parts[1].trim(); 
                                if (!dt.contains("T")) {
                                    dt += "T000000Z"; 
                                }

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                                                                         
                                date = LocalDateTime.parse(dt, formatter);   
                                
                            } else if (line.contains("DUE;VALUE=DATE")) {   
                                
                                String[] parts = line.split(":");
                                String d = parts[1].trim();
                                if (!d.contains("T")) {
                                    d += "T000000Z"; 
                                }
                              
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                
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
                                                              
                                vtask = true;
                                vevent = true;
                                Tasks task = new Tasks(date,deadline, title, desc, status);
                                tasks.add(task);
                                
                            }   
                             
                        } 
                        
                    }
                    
                    appointments.sort(Appointments :: compareTo); //μέθοδος που ταξινομεί την λίστα των αppointments
                    tasks.sort(Tasks :: compareTo); //μέθοδος που ταξινομεί την λίστα των tasks
                    
                    LocalDateTime dateTime = teller.now();//αποθήκευση στην dateTime την σημερινή ημερομηνία και ώρα
                    boolean check = false; //μεταβλητή για έλεγχος εκτύπωσης γεγονότων 
                    if (args[0].equals("day")) { //εάν το πρώτο όρισμα ειναι day                       
                                                
                        LocalDateTime endOfDay = dateTime.toLocalDate().atTime(LocalTime.MAX);//βρίσκει το τέλος της ημέρας χρησιμοποιοντας το temporalAdjusters                       
                        
                        for(Appointments appointment : appointments) {
                            
                            // εαν η σημερινή ημερομηνία και ώρα είναι μικρότερη απο την ημερομηνία και ώρα των γεγονότων
                            if(dateTime.compareTo(appointment.getDate()) < 0) { 
  
                              int result = appointment.getDate().compareTo(endOfDay); //συγκρίνει την σημερινή ημερομηνία και ώρα των γεγονότων με το τέλος της ημέρας
                              
                                if(result < 0) { //dateTime < endOfDay
                                    appointment.printAppointment(); //μέθοδος που εμφανίζει τα appointments
                                    check = true;
                                    System.out.println(" ");
                                }  

                            }
                        } 
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) < 0) {

                                int result = task.getDate().compareTo(endOfDay); 

                                if(result < 0) { //dateTime < startOfMonth
                                    task.printTask();//μέθοδος που εμφανίζει τα tasks
                                    check = true;
                                    System.out.println(" ");
                                }                               

                            } 
                        } 
                        if (check == false) { //εαν δεν έχει βρει κάτι να εμφανίσει 
                            System.out.println("There are no events for today.");
                        }
                        
                    } else if (args[0].equals("week")) { //αντίστοιχα με την day
                        
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
                        
                    } else if (args[0].equals("month")) { // αντίστοιχα με την day, week
                    
                        // βρίσκει το τέλος του μήνα χρησιμοποιοντας το temporalAdjusters
                        LocalDateTime endOfMonth = dateTime.with(TemporalAdjusters.lastDayOfMonth());
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
                        
                        // βρίσκει την αρχή της ημέρας χρησιμοποιοντας το temporalAdjusters
                        LocalDateTime startOfDay = dateTime.toLocalDate().atTime(LocalTime.MIN);
                            
                        for(Appointments appointment : appointments) {
                            
                            if(dateTime.compareTo(appointment.getDate()) > 0) { //εαν η σημερινή ημερομηνία και ώρα είναι μεγαλύτερη απο την ημερομηνία και ώρα των γεγονότων
                                
                                int result = appointment.getDate().compareTo(startOfDay); //συγκρίνει την ημερομηνία και ώρα των γεγονότων με την αρχή της ημέρας

                                if(result > 0) { //dateTime > startOfDay
                                    appointment.printAppointment();//μέθοδος που εμφανίζει τα appointments
                                    check = true;
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        }
                        for(Tasks task : tasks) {
                            
                            if(dateTime.compareTo(task.getDate()) > 0) {

                                int result = task.getDate().compareTo(startOfDay); 

                                if(result > 0) { //dateTime > startOfDay
                                    task.printTask();//μέθοδος που εμφανίζει τα tasks
                                    check = true;
                                    System.out.println(" ");
                                }                               
                                
                            } 
                        }
                        if (check == false) { //εαν δεν έχει βρει κάτι να εμφανίσει 
                            System.out.println("There were no events for today.");
                        }
                        
                    } else if (args[0].equals("pastweek")) { //αντίστοιχα με την pastday
                        
                        // βρίσκει την αρχή της εβδομάδας
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
                        
                    } else if (args[0].equals("pastmonth")) { //αντίστοιχα με τη pastday, pastweek
                        
                        // βρίσκει την αρχή του μήνα
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
                    //για να εκτυπώσει τις εργασίες που δεν ολοκληρώθηκαν και δεν πέρασε η προσθεσμία   
                    } else if (args[0].equals("todo")) { //εάν το πρώτο όρισμα ειναι το "todo"
                                        
                        for(Tasks task : tasks ) { //διατρέχει όλη την λίστα των tasks
                                                      
                            int result = dateTime.compareTo(task.getDeadline());//συγκρίνει την σημερινή ημερομηνία και ώρα με το deadline του task
                            
                            if( (result < 0) && (!status.equals("COMPLETED")) ) { // dateTime > deadline & STATUS isn't completed
                                task.printTask();
                                check = true;
                                System.out.println(" ");

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

            } else if (args.length == 1 ){ // μπαινει στην δευτερη λειτουργια, δηλαδή δέχεται στο terminal ένα όρισμα

                if(!args[0].contains(".ics")) { //εάν στο πρώτο όρισμα δεν περιέχεται το ".ics"
                    
                    System.out.println("Argument isn't correct!");
                    System.exit(1);
                    
                } else {
                    
                    Path path = Paths.get(args[0]);//το πρώτο όρισμα είναι το path name
                    
                    if(Files.exists(path)) { //εάν υπάρχει το αρχείο
                        
                        System.out.println("The file exists.");
                        
                    } else { //αν δεν υπαρχει το file
                        
                        System.out.println("The file does not exist.");
                        try {
                            
                            Files.createFile(path);  // δημιουργία αρχείου χρησιμοποιώντας το Files.createFile
                            System.out.println("File created successfully.");
                        } catch (IOException e) { //exceptions
                            
                            System.err.println("Unable to create the file: " + e.getMessage());
                            System.exit(1);
                        }
                    }
                    boolean rep = false;
                    System.out.println("Please provide your choice:");
                    System.out.println("(1)Add an Event");
                    System.out.println("(2)Change the status of the Task");
                    Scanner inpu3 = new Scanner(System.in);
                    int choice=input.nextInt();
                    if(choice==1){ //επιλέγει να προσθέσει γεγονότα
                        
                    System.out.println("Do you want to add an appointment or a task? (Appointment/Task)");
                    String answer = inpu3.nextLine();
                    do{
                        if (answer.equalsIgnoreCase("Appointment")) { //αν θέλει να εκτυπώσει appointments                    
                            
                            
                            Path mainFilePath = Paths.get(args[0]); // παίρνει το path του αρχείου

                            // Create a temporary file in the current folder with the name "replacement.ics"
                            Path tempFilePath = Paths.get(mainFilePath.getParent().toString(), "replacement.ics");

                            //για να γράψει στο αρχείο με append, δηλαδή  τα νέα δεδομένα θα προστεθούν στο τέλος του υπάρχοντος αρχείου χωρίς να διαγράφονται τα υπάρχοντα περιεχόμενα
                            //δημιουργώ reader για το main αρχειο
                            try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]));
                                // δημιουργώ writer για το main αρχείο      
                                BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {

                                if(Files.size(Paths.get(args[0])) == 0) { //εάν το αρχείο είναι άδειο

                                    writer.write("BEGIN:VCALENDAR");
                                    writer.newLine();
                                }
                                
                                String lineToRemove = "END:VCALENDAR";
                             
                               String line; //διαβάζει κάθε γραμμή στο αρχείο
                               while ((line = reader.readLine()) != null) { //όσο η γραμμή δεν είναι κενή

                                    String trimmedLine = line.trim(); //σε νέα μεταβλητή αποθηκεύει την γραμμή χωρίς περιττά κενά
                                    if(trimmedLine.equals(lineToRemove)) continue; //εάν η νέα μεταβλητή ισούται με την "END:VCALENDAR"
                                    writer.write(line + System.getProperty("line.separator")); // αλλάζει γραμμή
                                 }
                                writer.write("BEGIN:VEVENT");//γράφει στο αρχείο "BEGIN:VEVENT"
                                writer.newLine(); //αλλάζει γραμμή, πάει στην επόμενη

                                System.out.println("Please enter the decription of the appointment you want:");
                                String desc = inpu3.nextLine();
                                writer.write("DESCRIPTION:"  + desc);
                                writer.newLine(); 

                                System.out.println("Please enter the date start and time of the appointment (yyyyMMdd HHmmss):");
                                String dateS = inpu3.nextLine();

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss"); //καθορίζει το φορμάτ της ημερομηνίας

                                LocalDateTime dateStart = LocalDateTime.parse(dateS, formatter);// μετατρέπει το String σε LocalDateTime

                                DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");// καθορίζει το νεο φορματ της ημερομηνίας

                                // μετατρέπει το LocalDateTime στην επιθυμητή μορφή εξόδου και γράφει στο αρχείο
                                writer.write("DTSTART;VALUE=DATE:" + dateStart.format(formatterOutput));
                                writer.newLine(); //αλλάζει γραμμή, πάει στην επόμενη
                                
                                System.out.println("Do you want to add the duration of the appointment or the end date? (Duration/End date)");
                                String option = inpu3.nextLine();
                                if (option.equalsIgnoreCase("Duration")) {
                                    System.out.println("Please enter the duration of the appointment ( PT2H30M):");
                                    String d = inpu3.nextLine();

                                    try {
                                      
                                        Duration duration = Duration.parse(d); // μετατρέπει το String σε Duration
                                        writer.write("DURATION:" + duration); //γράφει στο αρχείο
                                    } catch (Exception e) {
                                        System.out.println("Incorrect duration format. Use PT2H30M format.");
                                    }

                                } else if (option.equalsIgnoreCase("End date")){
                                    System.out.println("Please enter the end date and time of the appointment(yyyyMMdd HHmmss):");
                                    String dateStr = inpu3.nextLine();
                                    
                                    try{
                                        formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
                                        LocalDateTime dateEnd = LocalDateTime.parse(dateStr, formatter);
                                        formatterOutput = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
                                        writer.write("DTEND;VALUE=DATE:"+dateEnd.format(formatterOutput));   
                                        writer.newLine();  
                                    } catch (Exception e) {
                                        System.out.println("Incorrect date format. Use yyyyMMdd HHmmss format.");
                                    }
                                } else {
                                    System.out.println("You entered wrong answer.");
                                    System.exit(1);
                                }

                                writer.write("DTSTAMP:"+dateStart.format(formatterOutput)+"Z");
                                writer.newLine();

                                System.out.println("Please enter the title of the appointment:");
                                String title = inpu3.nextLine();
                                writer.write("SUMMARY;LANGUAGE=en-us:Greece:"+title);
                                writer.newLine();

                                writer.write("END:VEVENT");
                                writer.newLine();
                                System.out.println("Data has been written to the file successfully.");
                                System.out.println("Do you want to add another event? (Yes/No)");
                                String repeat = inpu3.nextLine();
                                if (repeat.equalsIgnoreCase("Yes")) { //θέλει να προσθέσει και άλλα events
                                    rep = true; // boolean για μελλοντική προσθήκη εvent
                                    System.out.println("Do you want to add an appointment or a task?");
                                    answer = inpu3.nextLine();
                                } else {//δεν θέλει να προσθέσει κατι άλλο
                                    rep = false;
                                    writer.write("END:VCALENDAR");//άφου τελείωσαν οι προσθήκες γράφει στην τελευταία γραμμή "END:VCALENDAR"
                                    writer.newLine();
                                    writer.close();
                                }
                                
                                Files.delete(mainFilePath); //διαγράφει το αρχικό φάκελο
                                //μετονομάζει το προσωρινό file στο όνομα του αρχικού
                                Files.move(tempFilePath, tempFilePath.resolveSibling(mainFilePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                                
                                                              
                            } catch (IOException e) {

                                System.err.println("Error writing to the file: " + e.getMessage());

                            }
                        } else if (answer.equalsIgnoreCase("Task")) { //αντίστοιχα με την Appointments
                            
                            Path mainFilePath = Paths.get(args[0]);

                           
                            Path tempFilePath = Paths.get(mainFilePath.getParent().toString(), "replacement.ics");

                            //για να γράψει στο αρχείο με append, δηλαδή  τα νέα δεδομένα θα προστεθούν στο τέλος του υπάρχοντος αρχείου χωρίς να διαγράφονται τα υπάρχοντα περιεχόμενα
                                try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]));
                                BufferedWriter writer = Files.newBufferedWriter(tempFilePath)) {
                                    
                                if(Files.size(Paths.get(args[0])) == 0) {
                                    writer.write("BEGIN:VCALENDAR");
                                    writer.newLine();
                                }
                                
                                 String lineToRemove = "END:VCALENDAR";
                               // Check each line in the file                              
                               String line;
                               while ((line = reader.readLine()) != null) {
                                  
                                    String trimmedLine = line.trim();
                                     if(trimmedLine.equals(lineToRemove)) continue;
                                     writer.write(line + System.getProperty("line.separator"));
                                 }

                                writer.write("BEGIN:VTODO");
                                writer.newLine();

                                System.out.println("Please enter the decription of the task you want:");
                                String desc = inpu3.nextLine();
                                writer.write("DESCRIPTION:"+desc);
                                writer.newLine(); // Add a newline character

                                System.out.println("Please enter the date start and time of task (yyyyMMdd HHmmss):");
                                String dateS = inpu3.nextLine();
                                
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
                                    LocalDateTime dateStart = LocalDateTime.parse(dateS, formatter);
                                    DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

                                    writer.write("DTSTAMP:"+dateStart.format(formatterOutput)+"Z");
                                    writer.newLine();
                                } catch (Exception e) {
                                    System.out.println("Incorrect date format. Use yyyyMMdd HHmmss format.");
                                }
                                
                                System.out.println("Please enter the end date and time of the appointment(yyyyMMdd HHmmss):");
                                String dateStr = inpu3.nextLine();
                                
                                try {
                                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
                                    LocalDateTime dateEnd = LocalDateTime.parse(dateStr, formatter);
                                    DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
                                    writer.write("DUE;VALUE=DATE:"+dateEnd.format(formatterOutput)+'Z');   
                                    writer.newLine();
                                } catch (Exception e) {
                                    System.out.println("Incorrect date format. Use yyyyMMdd HHmmss format.");
                                }

                                System.out.println("Please enter the title of task:");
                                String title = inpu3.nextLine();
                                writer.write("SUMMARY;LANGUAGE=en-us:Greece:"+title);
                                writer.newLine();

                                System.out.println("Status is ΙΝ-PROCESS");
                                String status = "ΙΝ-PROCESS";
                                writer.write("STATUS:"+ status);
                                writer.newLine(); 

                                writer.write("END:VTODO");
                                writer.newLine();

                                System.out.println("Data has been written to the file successfully.");
                                System.out.println("Do you want to add another event? (Yes/No)");
                                String repeat = inpu3.nextLine();
                                if (repeat.equalsIgnoreCase("Yes")) {
                                    rep = true;
                                    System.out.println("Do you want to add an appointment or a task?");
                                    answer = inpu3.nextLine();
                                } else {
                                    rep = false;
                                    writer.write("END:VCALENDAR");
                                    writer.newLine();
                                    writer.close();
                                }
                                
                                
                                Files.delete(mainFilePath);
                                Files.move(tempFilePath, tempFilePath.resolveSibling(mainFilePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);

                            } catch (IOException e) {

                                System.err.println("Error writing to the file: " + e.getMessage());

                            }
                        } else {

                            System.out.println("You entered wrong answer."); // δεν εισάγει τις επιθυμητές απαντήσεις (Appointment, Task)
                            System.exit(1);
                        }
                    } while(rep == true); //επανάληψη όσο θέλει να προσθέτει εvents
                    
                }if(choice == 2){ //επιλέγει να αλλάξει το status σε ενα task
                    File file = new File(args[0]); //το όνομα του file καθοριζεται απο το δεύτερο όρισμα
                    Scanner sc = new Scanner(file); // scanner για να ανοίξει και να διαβάσει το αρχείο line by line        
         
                    boolean vevent = true; 
                    boolean vtask = true;
                    String desc = "";
                    LocalDateTime date = null;
                    LocalDateTime dateEnd = null;
                    String title = "";
                    LocalDateTime deadline = null;
                    String status = "";
                    Duration duration = null;
                    
                    String line;

                    while (sc.hasNextLine()) { 
                        line = sc.nextLine(); //αποθήκευση μίας γραμμής του αρχείου στο string line για μετέπειτα επεξεργασία                       
                    
                    if (vtask) {
                            
                            //αντίστοιχα με τα παραπάνω
 
                            if (line.contains("BEGIN:VTODO")) {
                                                                
                                vevent = false;
                                vtask = true;
                                
                            } else if (line.contains("DTSTAMP")) {
                                
                                String[] parts = line.split(":");
                                String dt = parts[1].trim(); 
                                if (!dt.contains("T")) {
                                    dt += "T000000Z"; 
                                }

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                                                                         
                                date = LocalDateTime.parse(dt, formatter);   
                                
                            } else if (line.contains("DUE;VALUE=DATE")) {   
                                
                                String[] parts = line.split(":");
                                String d = parts[1].trim();
                                if (!d.contains("T")) {
                                    d += "T000000Z"; 
                                }
                              
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");                                
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
                                                              
                                vtask = true;
                                vevent = true;
                                Tasks task = new Tasks(date,deadline, title, desc, status);
                                tasks.add(task);
                                
                            }   
                             
                        }
                    }
                    sc.close();
                    
                    Scanner input2 = new Scanner(System.in);
                    for(Tasks task : tasks){ //διατρέχει την λίστα των tasks
                        task.printTask(); // εμφανίζει όλα τα tasks
                        System.out.println(" ");
                    }
                    System.out.println("Give the title of the task you want to change the status");
                    String statusTitle = input2.nextLine();
                   
                    for(Tasks task : tasks){
                        if(statusTitle.equals(task.getTitle())){ 
                            System.out.println("This is the current status:" + task.isStatus());
                            System.out.println("Give the new status");
                            String chStatus = input2.nextLine();
                            if ( chStatus.equalsIgnoreCase("COMPLETED") || chStatus.equalsIgnoreCase("NEEDS-ACTION") || chStatus.equalsIgnoreCase("IN-PROCESS") || chStatus.equalsIgnoreCase("CANCELLED")){
                                task.setStatus(chStatus); //αποθηκεύει το νέο status στο παλιό
                                task.printTask();
                                System.out.println(" ");
                            } else {
                                System.out.println("You entered wrong status.");
                                System.exit(1);
                            }
                        }
                        
                    }
                }
                }     
            } else { //δεν εισάγει τα επιθυμητά ορίσματα (1 για εκτύπωση γεγονότων και 2 για προσθήκη γεγονότων)

                System.out.println("You entered wrong arguments");
                System.exit(1);

            }
            TimeService.stop();
    }
        
}