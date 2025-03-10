package me.hawkease;

public class password_manager {
    private static password_manager instance = new password_manager();
    public static password_manager getInstance() {
        if (instance == null) {
            instance = new password_manager();
        }
        return instance;
    }
}
