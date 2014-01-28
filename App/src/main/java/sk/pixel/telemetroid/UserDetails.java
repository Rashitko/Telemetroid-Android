package sk.pixel.telemetroid;

public class UserDetails {

    private int id;
    private boolean public_email;
    private String username, mail, name, comment;

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public int getId() {
        return id;
    }

    public boolean hasPublicMail() {
        return public_email;
    }

    @Override
    public String toString() {
        return "username: " + username + "\nmail: " + mail + "\nname: " + name + "\ncomment: " + comment + "\nid: " + id + "\npublic mail: " + public_email;
    }
}
