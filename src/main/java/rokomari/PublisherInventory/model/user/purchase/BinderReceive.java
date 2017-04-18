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
@AnalyzerDef(name = "binderReceive_analyzer",
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
public class BinderReceive implements Comparator<BinderReceive> {

    public enum ConfirmationStatus {NOT_CONFIRMED,CONFIRMED}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderReceive_analyzer"))
    private int id;

    @Column(name = "receiveId")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderReceive_analyzer"))
    private String receiveId;

    @Column(name = "receiveSerial")
    private int receiveSerial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "binderId")
    @NotNull
    @IndexedEmbedded
    private Binder binder;

    @ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "binderOrderBinderReceive", joinColumns = {
            @JoinColumn(name = "receiveId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "orderId", insertable = false, updatable = false)})
    private List<BinderOrder> binderOrders;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "binderReceive", orphanRemoval = true)
    private List<BinderReceiveBook> binderReceiveBooks;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;
    /*@ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "orderOrderPayment", joinColumns = {
            @JoinColumn(name = "receiveId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "paymentId", insertable = false, updatable = false)})
    private List<BinderPayment> binderPayments;*/

    @NotNull
    @Column(name = "receiveTotalQuantity")
    private int receiveTotalQuantity;

    @NotNull
    @Column(name = "receiveTotalAmount")
    private double receiveTotalAmount;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "orderReceiveDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderReceive_analyzer"))
    private Calendar orderReceiveDate;

    @Column(name = "confirmationStatus")
    public ConfirmationStatus confirmationStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public BinderReceive() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiveSerial() {
        return receiveSerial;
    }

    public void setReceiveSerial(int receiveSerial) {
        this.receiveSerial = receiveSerial;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    /*public List<BinderPayment> getBinderPayments() {
        return binderPayments;
    }

    public void setBinderPayments(List<BinderPayment> binderPayments) {
        this.binderPayments = binderPayments;
    }*/

    /*public List<BinderReceiveItemDetails> getBinderReceiveItemDetails() {
                    return binderReceiveItemDetails;
                }

                public void setBinderReceiveItemDetails(List<BinderReceiveItemDetails> binderReceiveItemDetails) {
                    this.binderReceiveItemDetails = binderReceiveItemDetails;
                }*/
    public List<BinderOrder> getBinderOrders() {
        return binderOrders;
    }

    public List<BinderReceiveBook> getBinderReceiveBooks() {
        return binderReceiveBooks;
    }

    public void setBinderReceiveBooks(List<BinderReceiveBook> binderReceiveBooks) {
        this.binderReceiveBooks = binderReceiveBooks;
    }

    public void setBinderOrders(List<BinderOrder> binderOrders) {
        this.binderOrders = binderOrders;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public int getReceiveTotalQuantity() {
        return receiveTotalQuantity;
    }

    public void setReceiveTotalQuantity(int receiveTotalQuantity) {
        this.receiveTotalQuantity = receiveTotalQuantity;
    }

    public double getReceiveTotalAmount() {
        return receiveTotalAmount;
    }

    public void setReceiveTotalAmount(double receiveTotalAmount) {
        this.receiveTotalAmount = receiveTotalAmount;
    }

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    public Calendar getOrderReceiveDate() {
        return orderReceiveDate;
    }

    public void setOrderReceiveDate(Calendar orderReceiveDate) {
        this.orderReceiveDate = orderReceiveDate;
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
    public int compare(BinderReceive a, BinderReceive b) {
        if (a.orderReceiveDate.before(b.orderReceiveDate)) {
            return -1;
        } else if (a.orderReceiveDate.after(b.orderReceiveDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}