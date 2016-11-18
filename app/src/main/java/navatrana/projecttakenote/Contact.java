package navatrana.projecttakenote;

/**
 * Created by Vamsee on 8/26/2016.
 */
public class Contact {

    public Contact(int id, String name, String phNumber, String msg) {
        this.id = id;
        this.name = name;
        this.phNumber = phNumber;
        this.msg = msg;
    }

    public Contact(String name, String phNumber, String msg) {
        this.name = name;
        this.phNumber = phNumber;
        this.msg = msg;
    }

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    String phNumber;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    String msg;
}
