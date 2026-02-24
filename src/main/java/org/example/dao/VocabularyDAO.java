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


        public List<Vocabulary> getNewVocabularies(int userId, String tag) {
            List<Vocabulary> list = new ArrayList<>();
            String sql = "select * from vocabularies v" +
                    " where v.id not in (select s.vocab_id from SaveUser s where s.user_id = ?) " +
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


        public List<Vocabulary> getAllVocabularyReview(int userId, String tag) {
            List<Vocabulary> list = new ArrayList<>();


            String sql = "SELECT v.*, s.status, s.count_correct " +
                    "FROM vocabularies v " +
                    "JOIN SaveUser s ON v.id = s.vocab_id " +
                    "WHERE s.user_id = ? AND v.tag = ? AND s.status = 1";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setString(2, tag);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Vocabulary vocab = Vocabulary.builder()
                                .id(rs.getInt("id"))
                                .word(rs.getString("word"))
                                .ipa(rs.getString("ipa"))
                                .meaning(rs.getString("meaning"))
                                .example(rs.getString("example"))
                                .exampleMeaning(rs.getString("example_meaning"))
                                .Audio(rs.getString("audio_url"))
                                .Tag(rs.getString("tag"))
                                .status(rs.getInt("status"))
                                .countCorrect(rs.getInt("count_correct"))
                                .build();

                        list.add(vocab);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }


        public List<Vocabulary> getAllVocabularySavebyTag(int userId, String tag) {
            List<Vocabulary> list = new ArrayList<>();
            String sql = "SELECT v.*, s.status, s.count_correct " +
                    "FROM vocabularies v " +
                    "JOIN SaveUser s ON v.id = s.vocab_id " +
                    "WHERE s.user_id = ? AND v.tag = ?";
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setString(2, tag);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Vocabulary vocab = Vocabulary.builder()
                                .id(rs.getInt("id"))
                                .word(rs.getString("word"))
                                .ipa(rs.getString("ipa"))
                                .meaning(rs.getString("meaning"))
                                .example(rs.getString("example"))
                                .exampleMeaning(rs.getString("example_meaning"))
                                .Audio(rs.getString("audio_url"))
                                .Tag(rs.getString("tag"))
                                .status(rs.getInt("status"))
                                .countCorrect(rs.getInt("count_correct"))
                                .build();

                        list.add(vocab);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }


        public List<Vocabulary> getAllVocabularyTest(int userId, String tag) {
            List<Vocabulary> list = new ArrayList<>();
            String sql = "select v.* from vocabularies v" +
                    " join SaveUser s on v.id = s.vocab_id " +
                    "where s.user_id = ? and s.status = 2 and v.tag = ? " +
                    "ORDER BY rand() LIMIT 20";
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setString(2, tag);

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

        public int countTestVocabularies(int userId, String tag) {
            int count = 0;
            String sql = "SELECT COUNT(*) FROM SaveUser s " +
                    "JOIN vocabularies v ON s.vocab_id = v.id " +
                    "WHERE s.user_id = ? AND s.status = 2 AND v.tag = ?";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ps.setString(2, tag);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return count;
        }


    }