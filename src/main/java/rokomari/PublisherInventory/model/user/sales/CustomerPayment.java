package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.purchase.BinderPaymentDetails;
import rokomari.PublisherInventory.model.user.purchase.BinderPaymentOrder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "customerPayment_analyzer",
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
public class CustomerPayment implements Comparator<CustomerPayment> {

    public enum PaymentMethod {CASH,CHEQUE}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerPayment_analyzer"))
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    @NotNull
    @IndexedEmbedded
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "customerPayment")
    private List<CustomerPaymentOrder> customerPaymentOrders;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private CustomerPaymentDetails customerPaymentDetails;

    @NotNull
    @Column(name = "paymentAmount")
    private double paymentAmount;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "paymentDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerPayment_analyzer"))
    private Calendar paymentDate;

    @Column(name = "paymentMethod")
    public PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public CustomerPayment() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerPaymentOrder> getCustomerPaymentOrders() {
        return customerPaymentOrders;
    }

    public void setCustomerPaymentOrders(List<CustomerPaymentOrder> customerPaymentOrders) {
        this.customerPaymentOrders = customerPaymentOrders;
    }

    public CustomerPaymentDetails getCustomerPaymentDetails() {
        return customerPaymentDetails;
    }

    public void setCustomerPaymentDetails(CustomerPaymentDetails customerPaymentDetails) {
        this.customerPaymentDetails = customerPaymentDetails;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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

    @Override
    public int compare(CustomerPayment a, CustomerPayment b) {
        if (a.paymentDate.before(b.paymentDate)) {
            return -1;
        } else if (a.paymentDate.after(b.paymentDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}