package rokomari.PublisherInventory.model.user.inventory;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;
import rokomari.PublisherInventory.model.common.Address;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//TODO: Inventory creation view page needs to be created


@Entity
@Indexed
// This is for partial search
@AnalyzerDef(name = "inventory_analyzer",
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
public class Inventory implements Comparator<Inventory> {

    public enum ActiveStatus{ACTIVE, NOT_ACTIVE}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="inventory_analyzer"))
    private int id;

    @NotNull
    @NotBlank(message = "Inventory must have a name.")
    @Column(name = "name")
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer=@Analyzer(definition="inventory_analyzer"))
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory")
    @Column(name = "inventoryDetailsId")
    private List<InventoryDetails> inventoryDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    @Column(name = "activeStatus")
    public ActiveStatus activeStatus;

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

    public Inventory() {
        this.created = this.updated = new Timestamp(new Date().getTime());
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

    public List<InventoryDetails> getInventoryDetails() {
        return inventoryDetails;
    }

    public void setInventoryDetails(List<InventoryDetails> inventoryDetails) {
        this.inventoryDetails = inventoryDetails;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
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

    @Override
    public int compare(Inventory a, Inventory b) {
        if (a.updated.before(b.updated)) {
            return -1;
        } else if (a.updated.after(b.updated)) {
            return 1;
        } else {
            return 0;
        }
    }
}