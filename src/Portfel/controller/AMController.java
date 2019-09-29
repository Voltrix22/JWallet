package Portfel.controller;

import Portfel.dataBase.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class AMController {

    Scanner input = new Scanner(System.in);
    Controller ctr = new Controller();

    String accName;

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    //POBIERANIE AKTUALNEJ DATY

    public String setDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    //PROCES DODAWANIA POZYCJI DO PENSJI

    public void addSalary(int salary, String date) throws SQLException {
        String insertSQL = "insert into portfel.salary (amount , date) values (? , ?);";
        PreparedStatement st = dao.getCon().prepareStatement(insertSQL);
        st.setInt(1, salary);
        st.setString(2, date);
        st.execute();
        st.close();
        System.out.println("Zatwierdzasz zmiany? (T/N)");
        String choice = input.nextLine().toUpperCase();
        if(choice.equals("T")){
            dao.getCon().commit();
            System.out.println("Nowa pozycja została dodana");
        }
        else{
            dao.getCon().rollback();
            System.out.println("Pozycja cofnięta");
        }
    }

    //POKAZYWANIE LISTY PENSJI

    public void showSalary() throws SQLException {
        Statement st = dao.getCon().createStatement();
        ResultSet rs = st.executeQuery("select * from salary");
        while(rs.next()){
            System.out.println(rs.getString("date")+"  "+rs.getInt("amount")+" zł");
        }
        st.close();
    }

}
