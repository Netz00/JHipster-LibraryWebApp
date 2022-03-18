package com.netz00.library.service.dto;

import com.netz00.library.domain.enumeration.LendingStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netz00.library.domain.UserBookLending} entity.
 */
public class UserBookLendingDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant loantime;

    private Instant returntime;

    @NotNull
    private LendingStatus status;

    private String note;

    private LibraryUserDTO user;

    private BookDTO book;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getLoantime() {
        return loantime;
    }

    public void setLoantime(Instant loantime) {
        this.loantime = loantime;
    }

    public Instant getReturntime() {
        return returntime;
    }

    public void setReturntime(Instant returntime) {
        this.returntime = returntime;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LibraryUserDTO getUser() {
        return user;
    }

    public void setUser(LibraryUserDTO user) {
        this.user = user;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBookLendingDTO)) {
            return false;
        }

        UserBookLendingDTO userBookLendingDTO = (UserBookLendingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userBookLendingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBookLendingDTO{" +
            "id=" + getId() +
            ", loantime='" + getLoantime() + "'" +
            ", returntime='" + getReturntime() + "'" +
            ", status='" + getStatus() + "'" +
            ", note='" + getNote() + "'" +
            ", user=" + getUser() +
            ", book=" + getBook() +
            "}";
    }
}
