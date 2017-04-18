package rokomari.PublisherInventory.model.user.purchase;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.user.general.Book;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "binderReceiveBooks_analyzer",
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
public class BinderReceiveBook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    //@NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="binderReceiveBooks_analyzer"))
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
    @JoinColumn(name = "binderReceiveId")
    private BinderReceive binderReceive;

    @Column(name = "receiveQuantity")
    private int receiveQuantity;

    @OneToOne
    @JoinColumn(name="binderOrderId")
    private BinderOrder binderOrder;

    @OneToOne
    @JoinColumn(name="binderOrderBookId")
    private BinderOrderBook binderOrderBook;

    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    //@NotNull
    @IndexedEmbedded
    private Publisher publisher;*/

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public BinderReceiveBook() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BinderOrder getBinderOrder() {
        return binderOrder;
    }

    public void setBinderOrder(BinderOrder binderOrder) {
        this.binderOrder = binderOrder;
    }

    public BinderReceive getBinderReceive() {
        return binderReceive;
    }

    public void setBinderReceive(BinderReceive binderReceive) {
        this.binderReceive = binderReceive;
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

    /*public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }*/

    public BinderOrderBook getBinderOrderBook() {
        return binderOrderBook;
    }

    public void setBinderOrderBook(BinderOrderBook binderOrderBook) {
        this.binderOrderBook = binderOrderBook;
    }

    public int getReceiveQuantity() {
        return receiveQuantity;
    }

    public void setReceiveQuantity(int receiveQuantity) {
        this.receiveQuantity = receiveQuantity;
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