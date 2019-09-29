package Portfel.model;

import Portfel.controller.AMController;
import Portfel.controller.Controller;

import java.sql.SQLException;
import java.util.Scanner;

public class AccManagement {

    Scanner input = new Scanner(System.in);
    AMController amc = new AMController();
    Controller ctr = new Controller();

    boolean end = true;

    public void accMan() throws SQLException {
        System.out.println(amc.getAccName());
        System.out.println("====================");
        System.out.println("1) dodawanie pensji");
        System.out.println("2) lista pensji");
        System.out.println("3) usuwanie pozycji");
        System.out.println("4) koniec testu");
        System.out.println("===================");
        System.out.println("5) pokaż datę");
        System.out.println("6) nameAcc");
        String wyborT = input.nextLine();
        switch (wyborT) {
            case "1":
                System.out.println("Podaj wysokość pensji");
                int salaryInput = input.nextInt();
                input.nextLine();
                System.out.println("Podać dzisiejszą datę(1) czy chcesz wprowadzić ją własnoręcznie(2)?");
                int dateChoice = input.nextInt();
                input.nextLine();
                if(dateChoice == 1){
                    String actualData = amc.setDate();
                    amc.addSalary(salaryInput, actualData);
                }
                else if(dateChoice == 2){
                    System.out.print("Podaj rok: ");
                    String rok = input.nextLine();
                    System.out.print("Podaj miesiąc: ");
                    String miesiac = input.nextLine();
                    System.out.print("Podaj dzień: ");
                    String dzien = input.nextLine();

                    String date = rok+"-"+miesiac+"-"+dzien;
                    amc.addSalary(salaryInput, date);
                }
                break;
            case "2":
                amc.showSalary();
                break;
            case "3":
                break;
            case "4":
                end = false;
                break;
            case "5":
                String data = amc.setDate();
                System.out.println(data);
                break;
            case "6":
                System.out.println(ctr.getNameAcc());
                break;

        }
    }
}
