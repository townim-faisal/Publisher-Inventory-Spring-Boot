package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderReceiveBook;

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
@AnalyzerDef(name = "customerDelivery_analyzer",
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
public class CustomerDelivery implements Comparator<CustomerDelivery> {

    public enum ConfirmationStatus {NOT_CONFIRMED,CONFIRMED}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerDelivery_analyzer"))
    private int id;

    @Column(name = "deliveryId")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerDelivery_analyzer"))
    private String deliveryId;

    @Column(name = "deliverySerial")
    private int deliverySerial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    @NotNull
    @IndexedEmbedded
    private Customer customer;

    @ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "customerOrderCustomerDelivery", joinColumns = {
            @JoinColumn(name = "deliveryId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "orderId", insertable = false, updatable = false)})
    private List<CustomerOrder> customerOrders;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "customerDelivery", orphanRemoval = true)
    private List<CustomerDeliveryBook> customerDeliveryBooks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    @NotNull
    @Column(name = "deliveryTotalQuantity")
    private int deliveryTotalQuantity;

    @NotNull
    @Column(name = "deliveryTotalAmount")
    private double deliveryTotalAmount;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "orderDeliveryDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerDelivery_analyzer"))
    private Calendar orderDeliveryDate;

    @Column(name = "confirmationStatus")
    public ConfirmationStatus confirmationStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public CustomerDelivery() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getDeliverySerial() {
        return deliverySerial;
    }

    public void setDeliverySerial(int deliverySerial) {
        this.deliverySerial = deliverySerial;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public List<CustomerDeliveryBook> getCustomerDeliveryBooks() {
        return customerDeliveryBooks;
    }

    public void setCustomerDeliveryBooks(List<CustomerDeliveryBook> customerDeliveryBooks) {
        this.customerDeliveryBooks = customerDeliveryBooks;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public int getDeliveryTotalQuantity() {
        return deliveryTotalQuantity;
    }

    public void setDeliveryTotalQuantity(int deliveryTotalQuantity) {
        this.deliveryTotalQuantity = deliveryTotalQuantity;
    }

    public double getDeliveryTotalAmount() {
        return deliveryTotalAmount;
    }

    public void setDeliveryTotalAmount(double deliveryTotalAmount) {
        this.deliveryTotalAmount = deliveryTotalAmount;
    }

    public Calendar getOrderDeliveryDate() {
        return orderDeliveryDate;
    }

    public void setOrderDeliveryDate(Calendar orderDeliveryDate) {
        this.orderDeliveryDate = orderDeliveryDate;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
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
    public int compare(CustomerDelivery a, CustomerDelivery b) {
        if (a.orderDeliveryDate.before(b.orderDeliveryDate)) {
            return -1;
        } else if (a.orderDeliveryDate.after(b.orderDeliveryDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}