package me.hawkease;

public class current_user {
    private static current_user user;
    private String email;

    public static current_user get_user(){
        if(user == null) user = new current_user();
        return user;
    }

    public void set_email(String email){
        this.email = email;
    }

    public String get_email(){
        return email;
    }

    public void clear_email(){
        email=null;
    }
}
