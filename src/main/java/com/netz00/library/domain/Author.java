package com.netz00.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "birtrthyear")
    private Integer birtrthyear;

    @Column(name = "deathyear")
    private Integer deathyear;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties(value = { "userBookLendings", "genres", "author" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_author__genres",
        joinColumns = @JoinColumn(name = "author_id"),
        inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    @JsonIgnoreProperties(value = { "authors", "books" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Author id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return this.fullname;
    }

    public Author fullname(String fullname) {
        this.setFullname(fullname);
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getBirtrthyear() {
        return this.birtrthyear;
    }

    public Author birtrthyear(Integer birtrthyear) {
        this.setBirtrthyear(birtrthyear);
        return this;
    }

    public void setBirtrthyear(Integer birtrthyear) {
        this.birtrthyear = birtrthyear;
    }

    public Integer getDeathyear() {
        return this.deathyear;
    }

    public Author deathyear(Integer deathyear) {
        this.setDeathyear(deathyear);
        return this;
    }

    public void setDeathyear(Integer deathyear) {
        this.deathyear = deathyear;
    }

    public String getNote() {
        return this.note;
    }

    public Author note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.setAuthor(null));
        }
        if (books != null) {
            books.forEach(i -> i.setAuthor(this));
        }
        this.books = books;
    }

    public Author books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public Author addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
        return this;
    }

    public Author removeBook(Book book) {
        this.books.remove(book);
        book.setAuthor(null);
        return this;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Author genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public Author addGenres(Genre genre) {
        this.genres.add(genre);
        genre.getAuthors().add(this);
        return this;
    }

    public Author removeGenres(Genre genre) {
        this.genres.remove(genre);
        genre.getAuthors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return id != null && id.equals(((Author) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", birtrthyear=" + getBirtrthyear() +
            ", deathyear=" + getDeathyear() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
