package com.netz00.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Genre.
 */
@Entity
@Table(name = "genre")
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "note")
    private String note;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnoreProperties(value = { "books", "genres" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    @ManyToMany(mappedBy = "genres")
    @JsonIgnoreProperties(value = { "userBookLendings", "genres", "author" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Genre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Genre name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return this.note;
    }

    public Genre note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        if (this.authors != null) {
            this.authors.forEach(i -> i.removeGenres(this));
        }
        if (authors != null) {
            authors.forEach(i -> i.addGenres(this));
        }
        this.authors = authors;
    }

    public Genre authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Genre addAuthors(Author author) {
        this.authors.add(author);
        author.getGenres().add(this);
        return this;
    }

    public Genre removeAuthors(Author author) {
        this.authors.remove(author);
        author.getGenres().remove(this);
        return this;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.removeGenres(this));
        }
        if (books != null) {
            books.forEach(i -> i.addGenres(this));
        }
        this.books = books;
    }

    public Genre books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public Genre addBooks(Book book) {
        this.books.add(book);
        book.getGenres().add(this);
        return this;
    }

    public Genre removeBooks(Book book) {
        this.books.remove(book);
        book.getGenres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        return id != null && id.equals(((Genre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Genre{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
