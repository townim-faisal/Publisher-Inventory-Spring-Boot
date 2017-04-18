package rokomari.PublisherInventory.model.user.inventory;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.user.general.Book;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "inventoryDetails_analyzer",
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
public class InventoryDetails implements Comparator<InventoryDetails> {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="inventoryDetails_analyzer"))
    private int id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name="bookId")
    private Book book;

    @Column(name = "bookQuantity")
    private int bookQuantity;

    @Column(name = "inventoryValue")
    private Double inventoryValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventoryId")
    //@NotNull
    @IndexedEmbedded
    private Inventory inventory;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public InventoryDetails() {
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

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public Double getInventoryValue() {
        return inventoryValue;
    }

    public void setInventoryValue(Double inventoryValue) {
        this.inventoryValue = inventoryValue;
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

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int compare(InventoryDetails a, InventoryDetails b) {
        return  a.book.getName().compareToIgnoreCase(b.book.getName());
    }
}