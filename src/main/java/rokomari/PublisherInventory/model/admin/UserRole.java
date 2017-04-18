package rokomari.PublisherInventory.model.admin;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "role", unique = true)
    private String role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "roles")
    private List<EndUser> endUsers = new ArrayList<>();

    public List<EndUser> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<EndUser> endUsers) {
        this.endUsers = endUsers;
    }

    public UserRole() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
