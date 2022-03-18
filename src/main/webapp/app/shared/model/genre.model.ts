import { IAuthor } from 'app/shared/model/author.model';
import { IBook } from 'app/shared/model/book.model';

export interface IGenre {
  id?: number;
  name?: string;
  note?: string | null;
  authors?: IAuthor[] | null;
  books?: IBook[] | null;
}

export const defaultValue: Readonly<IGenre> = {};
