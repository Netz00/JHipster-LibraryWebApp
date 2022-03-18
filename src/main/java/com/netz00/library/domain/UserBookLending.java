package com.netz00.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netz00.library.domain.enumeration.LendingStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A UserBookLending.
 */
@Entity
@Table(name = "user_book_lending")
public class UserBookLending implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "loantime", nullable = false)
    private Instant loantime;

    @Column(name = "returntime")
    private Instant returntime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LendingStatus status;

    @Column(name = "note")
    private String note;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "userBookLendings" }, allowSetters = true)
    private LibraryUser user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "userBookLendings", "genres", "author" }, allowSetters = true)
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserBookLending id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoantime() {
        return this.loantime;
    }

    public UserBookLending loantime(Instant loantime) {
        this.setLoantime(loantime);
        return this;
    }

    public void setLoantime(Instant loantime) {
        this.loantime = loantime;
    }

    public Instant getReturntime() {
        return this.returntime;
    }

    public UserBookLending returntime(Instant returntime) {
        this.setReturntime(returntime);
        return this;
    }

    public void setReturntime(Instant returntime) {
        this.returntime = returntime;
    }

    public LendingStatus getStatus() {
        return this.status;
    }

    public UserBookLending status(LendingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public UserBookLending note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LibraryUser getUser() {
        return this.user;
    }

    public void setUser(LibraryUser libraryUser) {
        this.user = libraryUser;
    }

    public UserBookLending user(LibraryUser libraryUser) {
        this.setUser(libraryUser);
        return this;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public UserBookLending book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBookLending)) {
            return false;
        }
        return id != null && id.equals(((UserBookLending) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBookLending{" +
            "id=" + getId() +
            ", loantime='" + getLoantime() + "'" +
            ", returntime='" + getReturntime() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
