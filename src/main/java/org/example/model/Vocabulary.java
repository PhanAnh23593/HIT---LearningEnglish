package org.example.model;

import lombok.*;
        import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Vocabulary {
    int id;
    String word;
    String ipa;
    String meaning;
    String example ;
    String exampleMeaning;
    String Audio;
    String Tag;
    int status;
    int countCorrect;
}
