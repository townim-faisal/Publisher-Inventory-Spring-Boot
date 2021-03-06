package rokomari.PublisherInventory.model.user.purchase;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.common.Address;
import rokomari.PublisherInventory.service.user.annotations.binder.BinderPhoneDigitValidate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "binder_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class ),
        filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "3"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "3") } )
        }
)
@SequenceGenerator(name = "generateId",initialValue = 100)
/*@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonAutoDetect*/
public class Binder implements Comparator<Binder> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binder_analyzer"))
    private int id;

    @NotNull
    @NotBlank(message = "Binder must have a name.")
    @Column(name = "name")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binder_analyzer"))
    private String name;

    @Column(name = "email")
    @Email(message = "Valid email address required.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binder_analyzer"))
    private String email;

    @Column(name = "phone")
    @NotNull
    @NotBlank(message = "Phone number required.")
    @Size(max = 13, message = "Phone no. can be maximum {max} characters long.")
    @BinderPhoneDigitValidate(message = "Please insert digits only.")
    //@UniqueBinderPhone(message = "Other binder is using this phone number.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binder_analyzer"))
    private String phone;

    @OneToOne(/*fetch = FetchType.LAZY, */cascade = CascadeType.ALL)
    private Address address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "binder", orphanRemoval = true)
    //@Column(name = "binderPlaceOrderId")
    private List<BinderOrder> binderOrders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "binder", orphanRemoval = true)
    //@Column(name = "binderReceiveOrderId")
    private List<BinderReceive> binderReceives;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "binder", orphanRemoval = true)
    //@Column(name = "binderPlaceOrderId")
    private List<BinderPayment> binderPayments;

    public Binder() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<BinderPayment> getBinderPayments() {
        return binderPayments;
    }

    public void setBinderPayments(List<BinderPayment> binderPayments) {
        this.binderPayments = binderPayments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public void setId(int id) {
        this.id = id;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<BinderOrder> getBinderOrders() {
        return binderOrders;
    }

    public void setBinderOrders(List<BinderOrder> binderOrders) {
        this.binderOrders = binderOrders;
    }

    public List<BinderReceive> getBinderReceives() {
        return binderReceives;
    }

    public void setBinderReceives(List<BinderReceive> binderReceives) {
        this.binderReceives = binderReceives;
    }

    @Override
    public int compare(Binder a, Binder b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
