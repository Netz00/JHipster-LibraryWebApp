package com.netz00.library.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netz00.library.domain.Author} entity.
 */
public class AuthorDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullname;

    private Integer birtrthyear;

    private Integer deathyear;

    private String note;

    private Set<GenreDTO> genres = new HashSet<>();

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

    public Integer getBirtrthyear() {
        return birtrthyear;
    }

    public void setBirtrthyear(Integer birtrthyear) {
        this.birtrthyear = birtrthyear;
    }

    public Integer getDeathyear() {
        return deathyear;
    }

    public void setDeathyear(Integer deathyear) {
        this.deathyear = deathyear;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorDTO)) {
            return false;
        }

        AuthorDTO authorDTO = (AuthorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorDTO{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", birtrthyear=" + getBirtrthyear() +
            ", deathyear=" + getDeathyear() +
            ", note='" + getNote() + "'" +
            ", genres=" + getGenres() +
            "}";
    }
}
