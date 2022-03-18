import { IBook } from 'app/shared/model/book.model';
import { IGenre } from 'app/shared/model/genre.model';

export interface IAuthor {
  id?: number;
  fullname?: string;
  birtrthyear?: number | null;
  deathyear?: number | null;
  note?: string | null;
  books?: IBook[] | null;
  genres?: IGenre[];
}

export const defaultValue: Readonly<IAuthor> = {};
