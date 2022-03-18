package com.netz00.library.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netz00.library.domain.LibraryUser} entity.
 */
public class LibraryUserDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullname;

    @NotNull
    private Instant birthdate;

    @NotNull
    private Instant memeberdate;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$")
    private String mobile;

    private String adress;

    private String note;

    @Lob
    private byte[] image;

    private String imageContentType;
    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Instant getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public Instant getMemeberdate() {
        return memeberdate;
    }

    public void setMemeberdate(Instant memeberdate) {
        this.memeberdate = memeberdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LibraryUserDTO)) {
            return false;
        }

        LibraryUserDTO libraryUserDTO = (LibraryUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, libraryUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LibraryUserDTO{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", memeberdate='" + getMemeberdate() + "'" +
            ", email='" + getEmail() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", adress='" + getAdress() + "'" +
            ", note='" + getNote() + "'" +
            ", image='" + getImage() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
