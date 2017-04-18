package rokomari.PublisherInventory.model.user.purchase;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "binderPaymentOrder_analyzer",
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
public class BinderPaymentOrder {

    public enum PaymentMethod {CASH,CHEQUE}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderPaymentOrder_analyzer"))
    private int id;

    @NotNull
    @Column(name = "payingAmount")
    private double payingAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "binderPaymentId")
    @IndexedEmbedded
    private BinderPayment binderPayment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "binderOrderId")
    //@IndexedEmbedded
    private BinderOrder binderOrder;

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

    public BinderPaymentOrder() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
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

    public double getPayingAmount() {
        return payingAmount;
    }

    public void setPayingAmount(double payingAmount) {
        this.payingAmount = payingAmount;
    }

    public BinderPayment getBinderPayment() {
        return binderPayment;
    }

    public void setBinderPayment(BinderPayment binderPayment) {
        this.binderPayment = binderPayment;
    }

    public BinderOrder getBinderOrder() {
        return binderOrder;
    }

    public void setBinderOrder(BinderOrder binderOrder) {
        this.binderOrder = binderOrder;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}