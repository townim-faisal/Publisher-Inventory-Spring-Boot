package rokomari.PublisherInventory.model.admin;


import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.common.Address;
import rokomari.PublisherInventory.model.user.general.Author;
import rokomari.PublisherInventory.model.user.general.Book;
import rokomari.PublisherInventory.model.user.general.Category;
import rokomari.PublisherInventory.model.user.inventory.Inventory;
import rokomari.PublisherInventory.model.user.purchase.Binder;
import rokomari.PublisherInventory.model.user.purchase.BinderOrder;
import rokomari.PublisherInventory.service.admin.annotations.publisher.PublisherPhoneDigitValidate;
import rokomari.PublisherInventory.service.admin.annotations.publisher.UniquePublisherPhone;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Indexed

// This is for partial search
@UniquePublisherPhone(message ="Other Publisher is using this phone number")
@AnalyzerDef(name = "publisher_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class ),
        filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @org.hibernate.search.annotations.Parameter(name = "minGramSize", value = "2"),
                                @org.hibernate.search.annotations.Parameter(name = "maxGramSize", value = "2") } )
        }
)
/*@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonAutoDetect*/
public class Publisher implements Comparator<Publisher> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES/*, analyzer=@Analyzer(definition="publisher_analyzer")*/)
    private int id;

    @NotNull
    @NotBlank(message = "Publisher must have a name.")
    @Column(name = "name")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="publisher_analyzer"))
    private String name;

    @Column(name = "phone")
    @NotNull
    @NotBlank(message = "Phone number required.")
    @Size(max = 13, message = "Phone no. can be maximum {max} characters long.")
    @PublisherPhoneDigitValidate(message = "Please insert digits only.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="publisher_analyzer"))
    private String phone;

    @Column(name="email", unique = true)
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="publisher_analyzer"))
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    //@Column(name = "address")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "endUserId")
    private List<EndUser> endUsers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "authorId")
    private List<Author> authors;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "categoryId")
    private List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "binderId")
    private List<Binder> binders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "bookId")
    private List<Book> books;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "binderOrderId")
    private List<BinderOrder> binderOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
    @Column(name = "inventoryId")
    private List<Inventory> inventories;

    public List<Author> getAuthors() {
        return authors;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Publisher() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public List<EndUser> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<EndUser> endUsers) {
        this.endUsers = endUsers;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Binder> getBinders() {
        return binders;
    }

    public void setBinders(List<Binder> binders) {
        this.binders = binders;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<BinderOrder> getBinderOrders() {
        return binderOrders;
    }

    public void setBinderOrders(List<BinderOrder> binderOrders) {
        this.binderOrders = binderOrders;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }

    @Override
    public int compare(Publisher a, Publisher b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
