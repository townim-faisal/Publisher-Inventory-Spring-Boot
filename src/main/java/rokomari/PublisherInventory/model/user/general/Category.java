package rokomari.PublisherInventory.model.user.general;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import rokomari.PublisherInventory.model.admin.Publisher;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "category_analyzer",
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
public class Category implements Comparator<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator ="generateId" )
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="category_analyzer"))
    private int id;

    @Column(name = "name")
    @NotNull
    @NotBlank(message = "Category name required.")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="category_analyzer"))
    private String name;

    @Column(name = "created")
    private Timestamp created;

    @Column(name = "updated")
    private Timestamp updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisherId")
    @NotNull
    @IndexedEmbedded
    private Publisher publisher;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "categories")
    private List<Book> books;

    public List<Book> getBookList() {
        return books;
    }
    public Category() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public void setBookList(List<Book> bookList) {
        this.books = bookList;
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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public int compare(Category a, Category b) {
        return  a.name.compareToIgnoreCase(b.name);
    }
}
