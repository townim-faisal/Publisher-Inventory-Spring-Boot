package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;
import rokomari.PublisherInventory.model.user.purchase.BinderPaymentOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderReceive;

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
@AnalyzerDef(name = "customerOrder_analyzer",
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
public class CustomerOrder implements Comparator<CustomerOrder> {

    public enum PaymentStatus{NOT_PAID, PART_PAID, FULL_PAID}
    public enum ConfirmationStatus {NOT_CONFIRMED,CONFIRMED}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerOrder_analyzer"))
    private int id;

//    @NotNull
    @Column(name = "orderId")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerOrder_analyzer"))
    private String orderId;

//    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "orderPlaceDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerOrder_analyzer"))
    private Calendar orderPlaceDate;

//    @NotNull
    @Column(name = "orderSerial")
    private int orderSerial;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "customerOrders")
    private List<CustomerDelivery> customerDeliveries;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "customerOrder", orphanRemoval = true)
    private List<CustomerOrderBook> customerOrderBooks;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "customerPayment")
    private List<CustomerPaymentOrder> customerPaymentOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerId")
    @NotNull
    @IndexedEmbedded
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    //@NotNull
    @Column(name = "totalQuantity")
    private int totalQuantity;

    //@NotNull
    //Transient Property
    @Column(name = "orderSubTotalAmount")
    private double orderSubTotalAmount;


    @Column(name = "discount")
    private double discount;

    //Transient Property
    //NotNull
    @Column(name = "orderTotalAmount")
    private double orderTotalAmount;

    /*@NotNull*/
    @Column(name = "paymentStatus")
    public PaymentStatus paymentStatus;

    @Column(name = "confirmationStatus")
    public ConfirmationStatus confirmationStatus;

    //@NotNull
    @Column(name = "payable")
    private double payable;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    /*private boolean isFullPaid;*/

    public CustomerOrder() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Calendar getOrderPlaceDate() {
        return orderPlaceDate;
    }

    public void setOrderPlaceDate(Calendar orderPlaceDate) {
        this.orderPlaceDate = orderPlaceDate;
    }

    public int getOrderSerial() {
        return orderSerial;
    }

    public void setOrderSerial(int orderSerial) {
        this.orderSerial = orderSerial;
    }

    public List<CustomerDelivery> getCustomerDeliveries() {
        return customerDeliveries;
    }

    public void setCustomerDeliveries(List<CustomerDelivery> customerDeliveries) {
        this.customerDeliveries = customerDeliveries;
    }

    public List<CustomerOrderBook> getCustomerOrderBooks() {
        return customerOrderBooks;
    }

    public void setCustomerOrderBooks(List<CustomerOrderBook> customerOrderBooks) {
        this.customerOrderBooks = customerOrderBooks;
    }

    public List<CustomerPaymentOrder> getCustomerPaymentOrders() {
        return customerPaymentOrders;
    }

    public void setCustomerPaymentOrders(List<CustomerPaymentOrder> customerPaymentOrders) {
        this.customerPaymentOrders = customerPaymentOrders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getOrderSubTotalAmount() {
        return orderSubTotalAmount;
    }

    public void setOrderSubTotalAmount(double orderSubTotalAmount) {
        this.orderSubTotalAmount = orderSubTotalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
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
    public int compare(CustomerOrder a, CustomerOrder b) {
        if (a.orderPlaceDate.before(b.orderPlaceDate)) {
            return -1;
        } else if (a.orderPlaceDate.after(b.orderPlaceDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}