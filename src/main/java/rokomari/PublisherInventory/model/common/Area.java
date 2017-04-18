package rokomari.PublisherInventory.model.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Entity
/*@JsonAutoDetect
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})*/
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    /*@OneToOne(fetch = FetchType.LAZY)
    *//*@PrimaryKeyJoinColumn
    @Column(name = "address")*//*
    @JoinColumn(name = "addressId")
    private Address address;*/

    public Area() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    /*public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }*/

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
}
