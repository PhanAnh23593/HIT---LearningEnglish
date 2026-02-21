package org.example.service;

public interface DailyProgressService {
    void checkAndResetDailyProgress();

    void markLearningDone();
    boolean isLearningDone();

    void markFlashcardDone();
    boolean isFlashcardDone();

    void markQuizDone();
    boolean isQuizDone();
}