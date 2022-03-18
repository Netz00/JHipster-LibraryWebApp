import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserBookLending from './user-book-lending';
import UserBookLendingDetail from './user-book-lending-detail';
import UserBookLendingUpdate from './user-book-lending-update';
import UserBookLendingDeleteDialog from './user-book-lending-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserBookLendingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserBookLendingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserBookLendingDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserBookLending} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserBookLendingDeleteDialog} />
  </>
);

export default Routes;
