package org.example.dao;

import org.example.model.Vocabulary;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VocabularyDAO {
    public List<Vocabulary> getAllVocabulary()
    {
        List<Vocabulary> vocabularyList = new ArrayList<>();
        String sql = "select * from vocabularies";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setId(rs.getInt("id"));
                vocabulary.setWord(rs.getString("word"));
                vocabulary.setMeaning(rs.getString("meaning"));
                vocabulary.setAudio(rs.getString("audio_url"));
                vocabulary.setTag(rs.getString("tag"));
                vocabulary.setIpa(rs.getString("ipa"));
                vocabulary.setExample(rs.getString("example"));
                vocabulary.setExampleMeaning(rs.getString("example_meaning"));
                vocabularyList.add(vocabulary);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return vocabularyList;
    }



}
