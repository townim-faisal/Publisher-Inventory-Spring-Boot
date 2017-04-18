package rokomari.PublisherInventory.model.user.purchase;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;

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
@AnalyzerDef(name = "binderOrder_analyzer",
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
public class BinderOrder implements Comparator<BinderOrder> {

    public enum PaymentStatus{NOT_PAID, PART_PAID, FULL_PAID}
    public enum ConfirmationStatus {NOT_CONFIRMED,CONFIRMED}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderOrder_analyzer"))
    private int id;

//    @NotNull
    @Column(name = "orderId")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderOrder_analyzer"))
    private String orderId;

//    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "orderPlaceDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderOrder_analyzer"))
    private Calendar orderPlaceDate;

//    @NotNull
    @Column(name = "orderSerial")
    private int orderSerial;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "binderOrders")
    private List<BinderReceive> binderReceives;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "binderOrder", orphanRemoval = true)
    private List<BinderOrderBook> binderOrderBooks;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "binderPayment")
    private List<BinderPaymentOrder> binderPaymentOrders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "binderId")
    @NotNull
    @IndexedEmbedded
    private Binder binder;

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

    public BinderOrder() {
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

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    public List<BinderReceive> getBinderReceives() {
        return binderReceives;
    }

    public void setBinderReceives(List<BinderReceive> binderReceives) {
        this.binderReceives = binderReceives;
    }

    public List<BinderOrderBook> getBinderOrderBooks() {
        return binderOrderBooks;
    }

    public void setBinderOrderBooks(List<BinderOrderBook> binderOrderBooks) {
        this.binderOrderBooks = binderOrderBooks;
    }

    public List<BinderPaymentOrder> getBinderPaymentOrders() {
        return binderPaymentOrders;
    }

    public void setBinderPaymentOrders(List<BinderPaymentOrder> binderPaymentOrders) {
        this.binderPaymentOrders = binderPaymentOrders;
    }

    public int getOrderSerial() {
        return orderSerial;
    }

    public void setOrderSerial(int orderSerial) {
        this.orderSerial = orderSerial;
    }

    public double getOrderSubTotalAmount() {
        return orderSubTotalAmount;
    }

    public void setOrderSubTotalAmount(double orderSubTotalAmount) {
        this.orderSubTotalAmount = orderSubTotalAmount;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public Calendar getOrderPlaceDate() {
        return orderPlaceDate;
    }

    public void setOrderPlaceDate(Calendar orderPlaceDate) {
        this.orderPlaceDate = orderPlaceDate;
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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getPayable() {
        return payable;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    @Override
    public int compare(BinderOrder a, BinderOrder b) {
        if (a.orderPlaceDate.before(b.orderPlaceDate)) {
            return -1;
        } else if (a.orderPlaceDate.after(b.orderPlaceDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}