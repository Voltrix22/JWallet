package Portfel.controller;

import Portfel.dataBase.DBConnect;
import Portfel.model.AccManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Controller {

    DBConnect dao = new DBConnect();
    Scanner input = new Scanner(System.in);
    GregorianCalendar calendar = new GregorianCalendar();
    AccManagement am = new AccManagement();
    AMController amc = new AMController();

    String nameAcc;
    int idAcc = 0;
    String password;
    String newAccName;

    public String getNameAcc() {
        return nameAcc;
    }

    public void setNameAcc(String nameAcc) {
        this.nameAcc = nameAcc;
    }

    public int getIdAcc() {
        return idAcc;
    }

    public void setIdAcc(int idAcc) {
        this.idAcc = idAcc;
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getNewAccName() {
        return newAccName;
    }

    public void setNewAccName(String newAccName) {
        this.newAccName = newAccName;
    }

    //POKAZYWANIE KONT BANKOWYCH / UKRYWANIE TABELI HASEŁ

    public void showAcc() throws SQLException {
        Statement st = dao.getCon().createStatement();
        ResultSet rs = st.executeQuery("show tables");
        int count = 1;
        while(rs.next()){
            if(!rs.getString("Tables_in_portfel").equals("password")){
                System.out.println(count+") "+rs.getString("Tables_in_portfel"));
                count++;
            }
        }
        st.close();
    }

    //POBRANIE NAZWY KONTA

    public void choiceAcc() throws SQLException {
        Statement st = dao.getCon().createStatement();
        ResultSet rs = st.executeQuery("show tables");
        int count = 1;
        while(rs.next()){
            if(count == getIdAcc()){
                setNameAcc(rs.getString("Tables_in_portfel"));
            }
            count++;
        }
        st.close();
    }

    //DODAWANIE NOWEGO KONTA

    public void addAcc() throws SQLException {
        System.out.print("Podaj nazwę nowego konta: ");
        setNewAccName(input.nextLine());
        System.out.print("Podaj hasło: ");
        setPassword(input.nextLine());
        System.out.println("NAZWA KONTA: "+getNewAccName());
        System.out.println("HASŁO: "+getPassword());
        System.out.println("Zatwierdzasz zmiany? (T/N)");
        String choice = input.nextLine().toUpperCase();
        if(choice.equals("T")){
            String insertSQL = "CREATE TABLE `portfel`.`"+getNewAccName()+"` (`salaryID` INT NOT NULL AUTO_INCREMENT, `amount` INT NOT NULL, `date` DATE NOT NULL, PRIMARY KEY (`salaryID`));";
            PreparedStatement st = dao.getCon().prepareStatement(insertSQL);
            System.out.println("25%");
            st.execute();
            st.close();
            dao.getCon().commit();
            System.out.println("50%");
            addPassword();
            System.out.println("100%");
            System.out.println("Nowa pozycja została dodana");
        }
        else{
            dao.getCon().rollback();
            System.out.println("Pozycja cofnięta");
        }
    }

    //USTAWIENIA HASŁA

    private void addPassword() throws SQLException {
        String insertSQL = "insert into portfel.password (nameAcc , password) values ( '"+getNewAccName()+"' , '"+getPassword()+"' );";
        PreparedStatement st = dao.getCon().prepareStatement(insertSQL);
        System.out.println("75%");
        st.execute();
        st.close();
        dao.getCon().commit();
    }

    //USUWANIE KONTA

    public void deleteAcc() throws SQLException {
        showAcc();
        System.out.print("Podaj numer konta które chcesz usunąć: ");
        setIdAcc(input.nextInt());
        input.nextLine();
        choiceAcc();
        lockPassword();
        String password = getPassword();
        int limit = 0;
        while(limit != 3) {
            System.out.print("Hasło: ");
            String tryHard = input.nextLine();
            if (tryHard.equals(password)) {
                String insertSQL = "drop table portfel." + getNameAcc() + ";";
                PreparedStatement st = dao.getCon().prepareStatement(insertSQL);
                st.execute();
                st.close();
                System.out.println("Chcesz usunąć konto " + getNameAcc() + "? (T/N)");
                String choiceD = input.nextLine().toUpperCase();
                if (choiceD.equals("T")) {
                    dao.getCon().commit();
                    delPassword();
                    limit = 3;
                }
            }
            else{
                System.out.println("Błędne Hasło");
                limit++;
            }
        }
    }

    private void lockPassword() throws SQLException {
        Statement st = dao.getCon().createStatement();
        ResultSet rs = st.executeQuery("select * from portfel.password;");
        while(rs.next()){
            if(getNameAcc().equals(rs.getString("nameAcc"))){
                setPassword(rs.getString("password"));
                break;
            }
        }
        st.close();
    }

    private void delPassword () throws SQLException {
        Statement st = dao.getCon().createStatement();
        ResultSet rs = st.executeQuery("select * from portfel.password;");
        while(rs.next()){
            if(getNameAcc().equals(rs.getString("nameAcc"))){
                int idPassword = rs.getInt("idpassword");
                String insertSQL = "DELETE FROM `portfel`.`password` WHERE (`idpassword` = '"+idPassword+"');";
                PreparedStatement pst = dao.getCon().prepareStatement(insertSQL);
                pst.execute();
                pst.close();
                dao.getCon().commit();
                break;
            }
        }
    }

    //ZMIANA HASŁA KONTA

    public void changePassword() throws SQLException {
        showAcc();
        System.out.println("Ktore konto wybrać?");
        setIdAcc(input.nextInt());
        input.nextLine();
        choiceAcc();
        lockPassword();
        String password = getPassword();
        int limit = 0;
        while(limit != 3) {
            System.out.print("Hasło: ");
            String tryHard = input.nextLine();
            if (tryHard.equals(password)) {
                System.out.print("Podaj nowe hasło:");
                String newPassword = input.nextLine();
                int passPassword = 0;
                while(passPassword != 3){
                    System.out.print("Powtórz nowe hasło:");
                    String newPassword2 = input.nextLine();
                    if(newPassword.equals(newPassword2)){
                        Statement st = dao.getCon().createStatement();
                        ResultSet rs = st.executeQuery("select * from portfel.password;");
                        while(rs.next()){
                            if(getNameAcc().equals(rs.getString("nameAcc"))){
                                int idPassword = rs.getInt("idpassword");
                                String insertSQL = "UPDATE `portfel`.`password` SET `password` = '"+newPassword+"' WHERE (`idpassword` = '"+idPassword+"');";
                                PreparedStatement pst = dao.getCon().prepareStatement(insertSQL);
                                pst.execute();
                                pst.close();
                                dao.getCon().commit();
                                System.out.println("HASŁO ZOSTAŁO POMYŚLNIE ZMIENIONE");
                                break;
                            }
                        }
                        break;
                    }
                    else{
                        System.out.println("Błędnie powtórzone hasło!");
                        passPassword++;
                    }
                }
                break;
            }
            else{
                System.out.println("Błędne hasło!");
                limit++;
            }
        }
    }

    public void goToAcc() throws SQLException {
        showAcc();
        System.out.print("Wybierz konto: ");
        setIdAcc(input.nextInt());
        input.nextLine();
        choiceAcc();
        lockPassword();
        String password = getPassword();
        int limit = 0;
        while(limit != 3) {
            System.out.print("Hasło: ");
            String tryHard = input.nextLine();
            if (tryHard.equals(password)) {
                am.accMan();
                amc.setAccName(getNameAcc());
            }
            else{
                System.out.println("Błędne hasło!");
                limit++;
            }
        }
    }
}