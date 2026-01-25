package org.example.dao;

import org.example.model.Vocabulary;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VocabularyDAO {
    private Vocabulary getVocabularyByRs(ResultSet rs) throws SQLException {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(rs.getInt("id"));
        vocabulary.setWord(rs.getString("word"));
        vocabulary.setMeaning(rs.getString("meaning"));
        vocabulary.setAudio(rs.getString("audio_url"));
        vocabulary.setTag(rs.getString("tag"));
        vocabulary.setIpa(rs.getString("ipa"));
        vocabulary.setExample(rs.getString("example"));
        vocabulary.setExampleMeaning(rs.getString("example_meaning"));
        return vocabulary;
    }


    public List<Vocabulary> getVocabularyByTag(String tag) {
        List<Vocabulary> VocabListByTag = new ArrayList<>();
        String sql = "select * from vocabularies where tag = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tag);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                VocabListByTag.add(getVocabularyByRs(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return VocabListByTag;
    }


    public List<Vocabulary> getNewVocabularies(int userId, String tag) {

        List<Vocabulary> list = new ArrayList<>();
        String sql = "select * from vocabularies v" +
                "where v.id not in (select s.vocab_id from SaveUser s where s.user_id = ?) " +
                "and v.tag = ? " +
                "order by rand() LIMIT ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, tag);
            ps.setInt(3, 10);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getVocabularyByRs(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<String> getAllTags() {
        List<String> tags = new ArrayList<>();

        String sql = "select distinct tag from vocabularies where tag is not null and tag != ''";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tagName = rs.getString("tag");
                tags.add(tagName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;
    }
}