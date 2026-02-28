package org.example.service.impl;

import org.example.service.DailyProgressService;
import org.example.utils.UserSession;
import java.time.LocalDate;
import java.util.prefs.Preferences;

public class DailyProgressServiceImpl implements DailyProgressService {

    private final Preferences prefs = Preferences.userNodeForPackage(DailyProgressServiceImpl.class);
    private static final String KEY_LAST_DATE = "last_check_date_";
    private static final String KEY_LEARNING  = "learning_done_";
    private static final String KEY_FLASHCARD = "flashcard_done_";
    private static final String KEY_QUIZ      = "quiz_done_";

    @Override
    public void checkAndResetDailyProgress() {
        if (UserSession.currentUser == null) return;
        int userId = UserSession.currentUser.getId();

        String today = LocalDate.now().toString();
        String savedDate = prefs.get(KEY_LAST_DATE + userId, "");
        if (!savedDate.isEmpty() && !today.equals(savedDate)) {
            prefs.putBoolean(KEY_LEARNING + userId, false);
            prefs.putBoolean(KEY_FLASHCARD + userId, false);
            prefs.putBoolean(KEY_QUIZ + userId, false);
            prefs.put(KEY_LAST_DATE + userId, today);
        } else if (savedDate.isEmpty()) {
            prefs.put(KEY_LAST_DATE + userId, today);
        }
    }

    @Override
    public void markLearningDone() {
        if (UserSession.currentUser == null) return;
        int userId = UserSession.currentUser.getId();
        prefs.putBoolean(KEY_LEARNING + userId, true);
        String today = LocalDate.now().toString();
        prefs.put(KEY_LAST_DATE + userId, today);

    }

    @Override
    public void markFlashcardDone() {
        if (UserSession.currentUser == null) return;
        int userId = UserSession.currentUser.getId();

        prefs.putBoolean(KEY_FLASHCARD + userId, true);
        prefs.put(KEY_LAST_DATE + userId, LocalDate.now().toString());
    }

    @Override
    public void markQuizDone() {
        if (UserSession.currentUser == null) return;
        int userId = UserSession.currentUser.getId();

        prefs.putBoolean(KEY_QUIZ + userId, true);
        prefs.put(KEY_LAST_DATE + userId, LocalDate.now().toString());
    }

    @Override public boolean isLearningDone() {
        return UserSession.currentUser != null && prefs.getBoolean(KEY_LEARNING + UserSession.currentUser.getId(), false);
    }
    @Override public boolean isFlashcardDone() {
        return UserSession.currentUser != null && prefs.getBoolean(KEY_FLASHCARD + UserSession.currentUser.getId(), false);
    }
    @Override public boolean isQuizDone() {
        return UserSession.currentUser != null && prefs.getBoolean(KEY_QUIZ + UserSession.currentUser.getId(), false);
    }
}