/**
 * @author Alejandro Duarte.
 */
public class Group {

    private static Long nextId = 1l; // just for testing ;)

    private Long id;

    private String name;

    private Boolean admin;

    public Group() {
        this.id = nextId++;
    }

    public Group(String name, Boolean admin) {
        this();
        this.name = name;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
