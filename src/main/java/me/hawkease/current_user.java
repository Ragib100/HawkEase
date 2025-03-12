package me.hawkease;

public class current_user {
    private static current_user user;
    private String email;
    private String type;

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

    public void set_type(String type){
        this.type = type;
    }

    public String get_type(){
        return type;
    }

    public void clear_type(){
        type=null;
    }
}
