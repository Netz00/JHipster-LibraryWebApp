import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IUserBookLending } from 'app/shared/model/user-book-lending.model';

export interface ILibraryUser {
  id?: number;
  fullname?: string;
  birthdate?: string;
  memeberdate?: string;
  email?: string;
  mobile?: string | null;
  adress?: string | null;
  note?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  user?: IUser | null;
  userBookLendings?: IUserBookLending[] | null;
}

export const defaultValue: Readonly<ILibraryUser> = {};
