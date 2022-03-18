package com.netz00.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$")
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "year")
    private Integer year;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties(value = { "user", "book" }, allowSetters = true)
    private Set<UserBookLending> userBookLendings = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(name = "rel_book__genres", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genres_id"))
    @JsonIgnoreProperties(value = { "authors", "books" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "books", "genres" }, allowSetters = true)
    private Author author;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Book id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Book isbn(String isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return this.year;
    }

    public Book year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getNote() {
        return this.note;
    }

    public Book note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<UserBookLending> getUserBookLendings() {
        return this.userBookLendings;
    }

    public void setUserBookLendings(Set<UserBookLending> userBookLendings) {
        if (this.userBookLendings != null) {
            this.userBookLendings.forEach(i -> i.setBook(null));
        }
        if (userBookLendings != null) {
            userBookLendings.forEach(i -> i.setBook(this));
        }
        this.userBookLendings = userBookLendings;
    }

    public Book userBookLendings(Set<UserBookLending> userBookLendings) {
        this.setUserBookLendings(userBookLendings);
        return this;
    }

    public Book addUserBookLending(UserBookLending userBookLending) {
        this.userBookLendings.add(userBookLending);
        userBookLending.setBook(this);
        return this;
    }

    public Book removeUserBookLending(UserBookLending userBookLending) {
        this.userBookLendings.remove(userBookLending);
        userBookLending.setBook(null);
        return this;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Book genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public Book addGenres(Genre genre) {
        this.genres.add(genre);
        genre.getBooks().add(this);
        return this;
    }

    public Book removeGenres(Genre genre) {
        this.genres.remove(genre);
        genre.getBooks().remove(this);
        return this;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book author(Author author) {
        this.setAuthor(author);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", isbn='" + getIsbn() + "'" +
            ", title='" + getTitle() + "'" +
            ", year=" + getYear() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
