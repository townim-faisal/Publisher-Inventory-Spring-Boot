package rokomari.PublisherInventory.model.user.purchase;


import org.springframework.format.annotation.DateTimeFormat;
import rokomari.PublisherInventory.model.admin.Publisher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@SequenceGenerator(name = "generateId",initialValue = 100)
public class BinderPaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="generateId")
    @NotNull
    @Column(name = "id")
    private int id;

    @Column(name = "chequeBank")
    private String chequeBank;

    @Column(name = "chequeBankBranch")
    private String chequeBankBranch;

    @Column(name = "chequeBankAddress")
    private String chequeBankAddress;

    @Column(name = "chequeNo")
    private String chequeNo;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "chequeDate")
    private Calendar chequeDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created")
    private Timestamp created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated")
    private Timestamp updated;

    public BinderPaymentDetails() {
        this.created = this.updated = new Timestamp(new Date().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChequeBank() {
        return chequeBank;
    }

    public void setChequeBank(String chequeBank) {
        this.chequeBank = chequeBank;
    }

    public String getChequeBankBranch() {
        return chequeBankBranch;
    }

    public void setChequeBankBranch(String chequeBankBranch) {
        this.chequeBankBranch = chequeBankBranch;
    }

    public String getChequeBankAddress() {
        return chequeBankAddress;
    }

    public void setChequeBankAddress(String chequeBankAddress) {
        this.chequeBankAddress = chequeBankAddress;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public Calendar getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Calendar chequeDate) {
        this.chequeDate = chequeDate;
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
