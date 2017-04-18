package rokomari.PublisherInventory.model.user.general;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
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
@AnalyzerDef(name = "book_analyzer",
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
public class Book implements Comparator<Book> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="book_analyzer"))
    private int id;

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Book must have a name.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="book_analyzer"))
    private String name;

    @Column(name = "isbn")
    /*@UniqueIsbn(message = "This ISBN is assigned to other book.")*/
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="book_analyzer"))
    private String isbn;

    @Column(name = "forma")
    private int forma;

    @Column(name = "coverPrice")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="book_analyzer"))
    private double coverPrice;

    @Column(name = "discount")
    private double discount;

    @Column(name = "sellPrice")
    private double sellPrice;

    @Column(name = "authorRoyalty")
    private double authorRoyalty;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publicationDate")
    private Calendar publicationDate;

    @Column(name = "inventoryQuantity")
    private int inventoryQuantity;

    @Column(name = "totalPages")
    private int totalPages;

    @ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "bookAuthor", joinColumns = {
            @JoinColumn(name = "bookId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "authorId", insertable = false, updatable = false)})
    private List<Author> authors;

    @ManyToMany
    @IndexedEmbedded
    @JoinTable(name = "bookCategory", joinColumns = {
            @JoinColumn(name = "bookId", insertable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "categoryId", insertable = false, updatable = false)})
    private List<Category> categories;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    public Book() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getForma() {
        return forma;
    }

    public void setForma(int forma) {
        this.forma = forma;
    }

    public double getCoverPrice() {
        return coverPrice;
    }

    public void setCoverPrice(double coverPrice) {
        this.coverPrice = coverPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getAuthorRoyalty() {
        return authorRoyalty;
    }

    public void setAuthorRoyalty(double authorRoyalty) {
        this.authorRoyalty = authorRoyalty;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Calendar publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
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
    public int compare(Book a, Book b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
