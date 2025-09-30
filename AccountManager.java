import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private final Scanner sc;
    private final Connection cn;
     public AccountManager(Scanner sc, Connection cn){
        this.sc=sc;
        this.cn=cn;
    }

    public void  credit_balance ( long account_number){

        System.out.println("Enter Amount : ");
        int balance = sc.nextInt();
        System.out.println(" Enter Your PIN : ");
        int pin = sc.nextInt();
        if(account_number!=0){
            String query3= " select * from Accounts where account_number=? and security_pin = ? ";
            try {
                cn.setAutoCommit(false);
                PreparedStatement pp = cn.prepareStatement(query3);
                pp.setLong(1,account_number);
                pp.setInt(2,pin);
                ResultSet rs = pp.executeQuery();
                if (rs.next())
                {
                    String quer4 = " Update accounts set balance = balance+? where account_number=?; ";
                    PreparedStatement pp1=cn.prepareStatement(quer4);
                    pp1.setInt(1,balance);
                    pp1.setLong(2,account_number);
                    int rowAffected = pp1.executeUpdate();
                    if (rowAffected>0){

                        System.out.println("the amount"+balance+" has been credited to your account ");
                        cn.commit();
                        cn.setAutoCommit(true);
                    }
                    else {
                        System.out.println("Something went wrong ");
                        cn.rollback();
                        cn.setAutoCommit(true);
                    }
                }else{
                    System.out.println(" invalid pin ");

                }

                cn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



        }



    }


    public void debit_balance (long account_number ){
        System.out.println("Enter amount :");
        double amount = sc.nextDouble();
        System.out.println("Enter security pin : ");
        int pin = sc.nextInt();

         String queryy= " select * from Accounts where account_number=? and security_pin=?";
         try {
             if (account_number != 0) {
                 cn.setAutoCommit(false);
                 PreparedStatement pp = cn.prepareStatement(queryy);
                 pp.setLong(1, account_number);
                 pp.setInt(2, pin);
                 ResultSet rs = pp.executeQuery();
                 if (rs.next()) {
                     double current_balance = rs.getDouble("balance");
                     String q1 = " Update accounts set balance =?-? where account_number=? ";
                     if (amount <= current_balance) {
                         PreparedStatement p = cn.prepareStatement(q1);
                         p.setDouble(1,current_balance);
                         p.setDouble(2,amount);
                         p.setLong(3,account_number);
                         int rowaff = p.executeUpdate();
                         if (rowaff > 0) {
                             System.out.println(" the amount " + amount + "debited sucessfuly ");
                             cn.commit();
                             cn.setAutoCommit(true);
                         } else {
                             System.out.println(" transaction Failed ");
                             cn.setAutoCommit(true);
                         }

                     }
                     else {
                         System.out.println(" insuffient balance  ");

                     }
                 }else{
                     System.out.println(" Wrong PIN number ");

                 }
             }

             }

             catch(SQLException e){
                 throw new RuntimeException(e);
             }
         try {
             cn.setAutoCommit(true);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    public void transfer_money(long account_number ) throws SQLException {
            sc.nextLine();
            System.out.print("Enter Receiver Account Number: ");
            long receiver_account_number = sc.nextLong();
            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = sc.nextLine();
            try{
                cn.setAutoCommit(false);
                if(account_number!=0 && receiver_account_number!=0){
                    PreparedStatement preparedStatement = cn.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, security_pin);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        double current_balance = resultSet.getDouble("balance");
                        if (amount<=current_balance){

                            // Write debit and credit queries
                            String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                            String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

                            // Debit and Credit prepared Statements
                            PreparedStatement creditPreparedStatement = cn.prepareStatement(credit_query);
                            PreparedStatement debitPreparedStatement = cn.prepareStatement(debit_query);

                            // Set Values for debit and credit prepared statements
                            creditPreparedStatement.setDouble(1, amount);
                            creditPreparedStatement.setLong(2, receiver_account_number);
                            debitPreparedStatement.setDouble(1, amount);
                            debitPreparedStatement.setLong(2, account_number);
                            int rowsAffected1 = debitPreparedStatement.executeUpdate();
                            int rowsAffected2 = creditPreparedStatement.executeUpdate();
                            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                                System.out.println("Transaction Successful!");
                                System.out.println("Rs."+amount+" Transferred Successfully");
                                cn.commit();
                                cn.setAutoCommit(true);
                                return;
                            } else {
                                System.out.println("Transaction Failed");
                                cn.rollback();
                                cn.setAutoCommit(true);
                            }
                        }else{
                            System.out.println("Insufficient Balance!");
                        }
                    }else{
                        System.out.println("Invalid Security Pin!");
                    }
                }else{
                    System.out.println("Invalid account number");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            cn.setAutoCommit(true);
        }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        try{
            PreparedStatement preparedStatement = cn.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid Pin!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}














