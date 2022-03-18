import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LibraryUser from './library-user';
import Author from './author';
import Genre from './genre';
import Book from './book';
import UserBookLending from './user-book-lending';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}library-user`} component={LibraryUser} />
      <ErrorBoundaryRoute path={`${match.url}author`} component={Author} />
      <ErrorBoundaryRoute path={`${match.url}genre`} component={Genre} />
      <ErrorBoundaryRoute path={`${match.url}book`} component={Book} />
      <ErrorBoundaryRoute path={`${match.url}user-book-lending`} component={UserBookLending} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
