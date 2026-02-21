package org.example.dao;

import com.mysql.cj.jdbc.exceptions.ConnectionFeatureNotAvailableException;
import org.example.model.SaveUser;
import org.example.model.User;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class SaveUserDAO {


    public boolean CheckUserExits(int usernameid, int vocabularyid) {
        String sql = "select 1 from SaveUser where user_id = ? and vocab_id = ?";
        try{
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,usernameid);
            ps.setInt(2,vocabularyid);
            return ps.executeQuery().next();
        }catch(Exception e){
            return false;
        }
    }

    public boolean SaveLearningUser(int usernameid,int vocabularyid){
        if(CheckUserExits(usernameid,vocabularyid)){
            return false;
        }
        String sql = "insert into SaveUser (user_id,vocab_id,status,count_correct,date_added) values(?,?,?,?,?)";

        try{
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,usernameid);
            ps.setInt(2,vocabularyid);
            ps.setInt(3,1);
            ps.setInt(4,1);
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            int check = ps.executeUpdate();
            return  check > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }



    public void updateProgress(int userId, int vocabId, boolean correct) {
        String sql;
        if (correct) {
            sql = "UPDATE SaveUser " +
                    "SET count_correct = count_correct + 1, " +
                    "    status = CASE WHEN count_correct + 1 >= 3 THEN 2 ELSE status END " +
                    "WHERE user_id = ? AND vocab_id = ?";
        } else {
            sql = "UPDATE SaveUser SET count_correct = 0 WHERE user_id = ? AND vocab_id = ?";
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, vocabId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
