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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "id")
    private int id;

    @Column(name = "addressLine")
    private String addressLine;

    /*@Column(name = "area")*/
    @OneToOne(fetch = FetchType.LAZY, /*mappedBy = "address",*/ cascade = CascadeType.ALL)
    private Area area;

    /*@Column(name = "city")*/
    @OneToOne(fetch = FetchType.LAZY, /*mappedBy = "address",*/ cascade = CascadeType.ALL)
    private City city;

    /*@Column(name = "country")*/
    @OneToOne(fetch = FetchType.LAZY, /*mappedBy = "address",*/ cascade = CascadeType.ALL)
    private Country country;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public Address() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
