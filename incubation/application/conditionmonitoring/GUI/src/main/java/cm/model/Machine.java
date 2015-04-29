package cm.model;

/**
 * Created by Tobi on 15.03.2015.
 */
public class Machine {

    public static final String OK_STATE = "OK";
    public static final String BAD_STATE = "BAD";

    private int id;
    private String name;
    private String state;

    public Machine(int id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Machine machine = (Machine) o;

        return id == machine.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
