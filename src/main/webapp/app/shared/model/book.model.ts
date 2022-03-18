import { IUserBookLending } from 'app/shared/model/user-book-lending.model';
import { IGenre } from 'app/shared/model/genre.model';
import { IAuthor } from 'app/shared/model/author.model';

export interface IBook {
  id?: number;
  isbn?: string;
  title?: string;
  year?: number | null;
  note?: string | null;
  userBookLendings?: IUserBookLending[] | null;
  genres?: IGenre[];
  author?: IAuthor;
}

export const defaultValue: Readonly<IBook> = {};
