package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SaveUser {
    int id;
    int usernameid;
    int vocabularyid;
    int Status;
    int CountCorrect;
    LocalDate date;



}
