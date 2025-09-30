import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    private final  Scanner sc;
    private final Connection cn;

    public Accounts(Scanner sc, Connection cn) {
        this.sc = sc;
        this.cn = cn;
    }

    public long Openaccount(String email) {
        sc.nextLine();
        System.out.println(" Enter Name : ");
        String name = sc.nextLine();
        System.out.println("Enter initial balance : ");
        double balance = sc.nextDouble();
        System.out.println(" Enter security PIN : ");
        int pin = sc.nextInt();
        System.out.println(user_exists(email));
        if (user_exists(email)) {
            String query1 = " insert into accounts(account_number, full_name, email, balance, security_pin) values (?,?,?,?,?);";
            try {
                long account_number = genrateAccountNumber();
                PreparedStatement pp1 = cn.prepareStatement(query1);
                pp1.setLong(1, account_number);
                pp1.setString(2, name);
                pp1.setString(3, email);
                pp1.setDouble(4, balance);
                pp1.setInt(5, pin);

                int rowaffected = pp1.executeUpdate();

                if (rowaffected > 0) {
                    return account_number;
                } else {
                    System.out.println(" Accounts creation failed !!!!");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
       throw  new RuntimeException(" failed ");
    }

    public long get_account_number (String email ) {
        String query3 = " select account_number from Accounts where email= ?;";
        try {
            PreparedStatement pp = cn.prepareStatement(query3);
            pp.setString(1, email);
            ResultSet rs = pp.executeQuery();
            if (rs.next()) {

                return rs.getLong("account_number");
            }
            else{
                return 0;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //throw new RuntimeException("Account Number Doesn't Exist!");

    }



 public long genrateAccountNumber (){
     String query2= " select account_number from accounts order by account_number desc limit 1";
     try{
         PreparedStatement pp = cn.prepareStatement(query2);
         ResultSet rs= pp.executeQuery();
         if(rs.next()) {
             long ac_no = rs.getLong("account_number");
             return ac_no+1;
         }
             else {
                 return 10000101;
             }
     }
     catch (SQLException e){
         throw new  RuntimeException(e);
     }


 }

    public boolean user_exists(String email ){
      String query = "select * from user where email=?;";
      try{
          PreparedStatement pp = cn.prepareStatement(query);
          pp.setString(1,email);
          ResultSet rs= pp.executeQuery();
          return rs.next();

      } catch (SQLException e) {
          throw new RuntimeException(e);
      }

    }
}
