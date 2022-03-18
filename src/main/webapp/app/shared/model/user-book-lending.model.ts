import dayjs from 'dayjs';
import { ILibraryUser } from 'app/shared/model/library-user.model';
import { IBook } from 'app/shared/model/book.model';
import { LendingStatus } from 'app/shared/model/enumerations/lending-status.model';

export interface IUserBookLending {
  id?: number;
  loantime?: string;
  returntime?: string | null;
  status?: LendingStatus;
  note?: string | null;
  user?: ILibraryUser;
  book?: IBook;
}

export const defaultValue: Readonly<IUserBookLending> = {};
