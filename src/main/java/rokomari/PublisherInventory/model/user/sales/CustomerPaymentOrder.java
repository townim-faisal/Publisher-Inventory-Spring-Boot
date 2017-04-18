package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderPayment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "customerPaymentOrder_analyzer",
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
public class CustomerPaymentOrder {

    public enum PaymentMethod {CASH,CHEQUE}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerPaymentOrder_analyzer"))
    private int id;

    @NotNull
    @Column(name = "payingAmount")
    private double payingAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerPaymentId")
    @IndexedEmbedded
    private CustomerPayment customerPayment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerOrderId")
    //@IndexedEmbedded
    private CustomerOrder customerOrder;

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

    public CustomerPaymentOrder() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
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

    public CustomerPayment getCustomerPayment() {
        return customerPayment;
    }

    public void setCustomerPayment(CustomerPayment customerPayment) {
        this.customerPayment = customerPayment;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
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
}