package rokomari.PublisherInventory.model.admin;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Indexed
// "User" is a reserved keyword for postgresql, therefore used "EndUser"

@AnalyzerDef(name = "endUser_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class ),
        filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "2"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "2") } )
        }
)
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "id")
    @Field(index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="endUser_analyzer"))
    private int id;

    @NotNull
    @Column(name = "name", unique = true)
    @Field(index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="endUser_analyzer"))
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /*@Column(name="status")
    @NotNull
    private String status;*/

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    @ManyToMany
    @JoinTable(name = "eUserRole", joinColumns = {
            @JoinColumn(name = "endUserId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "roleId", insertable = false, updatable = false)})
    private List<UserRole> roles = new ArrayList<>();

    @Column(name="status")
    @NotNull
    private boolean isEnabled;

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    /*public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public EndUser() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
