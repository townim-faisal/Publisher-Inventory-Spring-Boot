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
@AnalyzerDef(name = "binderPayment_analyzer",
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
public class BinderPayment implements Comparator<BinderPayment> {

    public enum PaymentMethod {CASH,CHEQUE}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderPayment_analyzer"))
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "binderId")
    @NotNull
    @IndexedEmbedded
    private Binder binder;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "binderPayment")
    private List<BinderPaymentOrder> binderPaymentOrders;

    @NotNull
    @Column(name = "paymentAmount")
    private double paymentAmount;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "paymentDate")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderPayment_analyzer"))
    private Calendar paymentDate;

    @Column(name = "paymentMethod")
    public PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BinderPaymentDetails binderPaymentDetails;

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

    public BinderPayment() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public List<BinderPaymentOrder> getBinderPaymentOrders() {
        return binderPaymentOrders;
    }

    public void setBinderPaymentOrders(List<BinderPaymentOrder> binderPaymentOrders) {
        this.binderPaymentOrders = binderPaymentOrders;
    }

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public Calendar getPaymentDate() {
        return paymentDate;
    }

    public BinderPaymentDetails getBinderPaymentDetails() {
        return binderPaymentDetails;
    }

    public void setBinderPaymentDetails(BinderPaymentDetails binderPaymentDetails) {
        this.binderPaymentDetails = binderPaymentDetails;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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

    @Override
    public int compare(BinderPayment a, BinderPayment b) {
        if (a.paymentDate.before(b.paymentDate)) {
            return -1;
        } else if (a.paymentDate.after(b.paymentDate)) {
            return 1;
        } else {
            return 0;
        }
    }
}