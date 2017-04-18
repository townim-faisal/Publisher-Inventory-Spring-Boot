package rokomari.PublisherInventory.model.user.general;


import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.common.Address;
import rokomari.PublisherInventory.service.user.annotations.author.AuthorPhoneDigitValidate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.*;


//@UniqueAuthorPhone(message = "Validation Test Message from Author Controller")
@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "author_analyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class ),
        filters = {
                @TokenFilterDef(factory = StandardFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = StopFilterFactory.class),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @Parameter(name = "minGramSize", value = "3"),
                                @Parameter(name = "maxGramSize", value = "3") } )
        }
)
@SequenceGenerator(name = "generateId",initialValue = 100)
public class Author implements Comparator<Author> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="generateId" )
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="author_analyzer"))
    private int id;

    @NotNull
    @NotBlank(message = "Author must have a name.")
    @Column(name = "name")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="author_analyzer"))
    private String name;

    @Temporal(TemporalType.DATE) // not using this annotation makes the column in DB as "TimeStamp"
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dateOfBirth")
    private Calendar dateOfBirth; // Using TimeStamp gives error

    @Column(name = "email")
    @Email(message = "Valid email address required.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES,analyzer=@Analyzer(definition="author_analyzer"))
    private String email;

    @Column(name = "phone")
    @NotNull
    @NotBlank(message = "Phone number required.")
    @Size(max = 13, message = "Phone no. can be maximum {max} characters long.")
    @AuthorPhoneDigitValidate(message = "Please insert digits only.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="author_analyzer"))
    private String phone;

    @Column(name = "facebook")
    private String facebook;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "authors")
    private List<Book> books;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Author() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    // Not using this property insists to use setter methods
    // on update
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
    public int compare(Author a, Author b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
