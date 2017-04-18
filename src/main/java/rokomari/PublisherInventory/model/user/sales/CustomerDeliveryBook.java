package rokomari.PublisherInventory.model.user.sales;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrderBook;
import rokomari.PublisherInventory.model.user.purchase.BinderReceive;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "customerDeliveryBook_analyzer",
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
public class CustomerDeliveryBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    //@NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="customerDeliveryBook_analyzer"))
    private int id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name="bookId")
    private Book book;

    //@NotNull
    @Column(name = "unitPrice")
    private double unitPrice;

    //@NotNull
    @Column(name = "orderQuantity")
    private int orderQuantity;

    @Column(name = "unitDiscount")
    private double unitDiscount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customerDeliveryId")
    private CustomerDelivery customerDelivery;

    @Column(name = "deliveryQuantity")
    private int deliveryQuantity;

    @OneToOne
    @JoinColumn(name="customerOrderId")
    private CustomerOrder customerOrder;

    @OneToOne
    @JoinColumn(name="customerOrderBookId")
    private CustomerOrderBook customerOrderBook;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public CustomerDeliveryBook() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public double getUnitDiscount() {
        return unitDiscount;
    }

    public void setUnitDiscount(double unitDiscount) {
        this.unitDiscount = unitDiscount;
    }

    public CustomerDelivery getCustomerDelivery() {
        return customerDelivery;
    }

    public void setCustomerDelivery(CustomerDelivery customerDelivery) {
        this.customerDelivery = customerDelivery;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public CustomerOrderBook getCustomerOrderBook() {
        return customerOrderBook;
    }

    public void setCustomerOrderBook(CustomerOrderBook customerOrderBook) {
        this.customerOrderBook = customerOrderBook;
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