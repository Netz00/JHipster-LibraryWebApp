import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LibraryUser from './library-user';
import LibraryUserDetail from './library-user-detail';
import LibraryUserUpdate from './library-user-update';
import LibraryUserDeleteDialog from './library-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LibraryUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LibraryUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LibraryUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={LibraryUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LibraryUserDeleteDialog} />
  </>
);

export default Routes;
