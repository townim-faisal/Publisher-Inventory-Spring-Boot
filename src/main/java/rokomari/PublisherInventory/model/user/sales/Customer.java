package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.common.Address;
import rokomari.PublisherInventory.service.user.annotations.customer.CustomerPhoneDigitValidate;

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
@AnalyzerDef(name = "customer_analyzer",
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

public class Customer implements Comparator<Customer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customer_analyzer"))
    private int id;

    @NotNull
    @NotBlank(message = "Customer must have a name.")
    @Column(name = "name")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customer_analyzer"))
    private String name;

    @Column(name = "email")
    @Email(message = "Valid email address required.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customer_analyzer"))
    private String email;

    @Column(name = "phone")
    @NotNull
    @NotBlank(message = "Phone number required.")
    @Size(max = 13, message = "Phone no. can be maximum {max} characters long.")
    @CustomerPhoneDigitValidate(message = "Please insert digits only.")
    //@UniqueBinderPhone(message = "Other customer is using this phone number.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customer_analyzer"))
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    private List<CustomerOrder> customerOrders;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    private List<CustomerDelivery> customerDeliveries;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer", orphanRemoval = true)
    private List<CustomerPayment> customerPayments;

    public Customer() {
        this.created = this.updated = new Timestamp(new Date().getTime());
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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<CustomerDelivery> getCustomerDeliveries() {
        return customerDeliveries;
    }

    public void setCustomerDeliveries(List<CustomerDelivery> customerDeliveries) {
        this.customerDeliveries = customerDeliveries;
    }

    public List<CustomerPayment> getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(List<CustomerPayment> customerPayments) {
        this.customerPayments = customerPayments;
    }

    @Override
    public int compare(Customer a, Customer b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
