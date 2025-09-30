import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class User{

    private static Scanner sc;
    private Connection cn;
    public User(Scanner sc, Connection cn){
        this.sc=sc;
        this.cn=cn;
    }

    // user register section

        public void register() {
            sc.nextLine();
            System.out.println(" Enter your name : ");
            String name = sc.nextLine();
            System.out.println(" Enter your email : ");
            String email = sc.nextLine();
            System.out.println(" Enter your Password : ");
            String password = sc.nextLine();

            if (user_exist(email)) {
                System.out.println("USer already exists !!");
                return;
            }
            String query1 = "insert into user(full_name , email, password) values (?,?,?);";
            try {
                PreparedStatement pp = cn.prepareStatement(query1);
                pp.setString(1, name);
                pp.setString(2, email);
                pp.setString(3, password);
                int rowsaffected = pp.executeUpdate();
                if (rowsaffected > 0) {
                    System.out.println("Registration SucessFull !!!");
                } else {
                    System.out.println("Something went wrong !!");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        // log in section

            public String log_in(){
            sc.nextLine();
            System.out.println(" enter email : ");
            String email= sc.nextLine();
                System.out.println(" Enter your password : ");
                String password = sc.nextLine();
                String query2= " Select * from user where email=? and password=?;";
                try {
                    PreparedStatement pp = cn.prepareStatement(query2);
                    pp.setString(1,email);
                    pp.setString(2,password);
                    ResultSet rs = pp.executeQuery();
                    if( rs.next()){
                        return email;
                    }
                    else{
                        return null;
                    }

                }

                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                //????
               // return null;
            }

            // valid user getter

         public boolean user_exist(String email ) {
             // 1. Pehle user ko find karo
             String query="select * from user where email=?";
             try{
                 PreparedStatement pp=cn.prepareStatement(query);
                 pp.setString(1,email);
                 ResultSet rs= pp.executeQuery();
                 if(rs.next()){
                     return true;
                 }
                 else{
                     return false;
                 }
             } catch (SQLException ex) {
                 throw new RuntimeException(ex);
             }


}
}