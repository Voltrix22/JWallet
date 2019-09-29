package Portfel;

import Portfel.controller.Controller;
import Portfel.model.AccManagement;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Scanner input = new Scanner(System.in);
        Controller ctr = new Controller();

        System.out.println("TEST PENSJI");

        boolean end = true;

        while (end) {
            System.out.println("=====================");
            System.out.println("1) Dodaj Konto");
            System.out.println("2) Usuń konto");
            System.out.println("3) Zmień hasło");
            System.out.println("4) Wybierz konto");
            System.out.println("5) Koniec");
            System.out.println("=====================");
            String cAcc = input.nextLine();
            switch (cAcc){
                case "1":
                    ctr.addAcc();
                    break;
                case "2":
                    ctr.deleteAcc();
                    break;
                case "3":
                    ctr.changePassword();
                    break;
                case "4":
                    ctr.goToAcc();
                    break;
                case "5":
                    end = false;
                    break;
            }
        }
    }
}