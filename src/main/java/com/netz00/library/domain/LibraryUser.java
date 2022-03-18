package com.netz00.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LibraryUser.
 */
@Entity
@Table(name = "library_user")
public class LibraryUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private Instant birthdate;

    @NotNull
    @Column(name = "memeberdate", nullable = false)
    private Instant memeberdate;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    @Column(name = "mobile", unique = true)
    private String mobile;

    @Column(name = "adress")
    private String adress;

    @Column(name = "note")
    private String note;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = { "user", "book" }, allowSetters = true)
    private Set<UserBookLending> userBookLendings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LibraryUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return this.fullname;
    }

    public LibraryUser fullname(String fullname) {
        this.setFullname(fullname);
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Instant getBirthdate() {
        return this.birthdate;
    }

    public LibraryUser birthdate(Instant birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public Instant getMemeberdate() {
        return this.memeberdate;
    }

    public LibraryUser memeberdate(Instant memeberdate) {
        this.setMemeberdate(memeberdate);
        return this;
    }

    public void setMemeberdate(Instant memeberdate) {
        this.memeberdate = memeberdate;
    }

    public String getEmail() {
        return this.email;
    }

    public LibraryUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public LibraryUser mobile(String mobile) {
        this.setMobile(mobile);
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdress() {
        return this.adress;
    }

    public LibraryUser adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNote() {
        return this.note;
    }

    public LibraryUser note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return this.image;
    }

    public LibraryUser image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public LibraryUser imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LibraryUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<UserBookLending> getUserBookLendings() {
        return this.userBookLendings;
    }

    public void setUserBookLendings(Set<UserBookLending> userBookLendings) {
        if (this.userBookLendings != null) {
            this.userBookLendings.forEach(i -> i.setUser(null));
        }
        if (userBookLendings != null) {
            userBookLendings.forEach(i -> i.setUser(this));
        }
        this.userBookLendings = userBookLendings;
    }

    public LibraryUser userBookLendings(Set<UserBookLending> userBookLendings) {
        this.setUserBookLendings(userBookLendings);
        return this;
    }

    public LibraryUser addUserBookLending(UserBookLending userBookLending) {
        this.userBookLendings.add(userBookLending);
        userBookLending.setUser(this);
        return this;
    }

    public LibraryUser removeUserBookLending(UserBookLending userBookLending) {
        this.userBookLendings.remove(userBookLending);
        userBookLending.setUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryUser)) {
            return false;
        }
        return id != null && id.equals(((LibraryUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibraryUser{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", memeberdate='" + getMemeberdate() + "'" +
            ", email='" + getEmail() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", adress='" + getAdress() + "'" +
            ", note='" + getNote() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
