import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './library-user.reducer';
import { ILibraryUser } from 'app/shared/model/library-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LibraryUser = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const libraryUserList = useAppSelector(state => state.libraryUser.entities);
  const loading = useAppSelector(state => state.libraryUser.loading);
  const totalItems = useAppSelector(state => state.libraryUser.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="library-user-heading" data-cy="LibraryUserHeading">
        <Translate contentKey="libraryApp.libraryUser.home.title">Library Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="libraryApp.libraryUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="libraryApp.libraryUser.home.createLabel">Create new Library User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {libraryUserList && libraryUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="libraryApp.libraryUser.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fullname')}>
                  <Translate contentKey="libraryApp.libraryUser.fullname">Fullname</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('birthdate')}>
                  <Translate contentKey="libraryApp.libraryUser.birthdate">Birthdate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('memeberdate')}>
                  <Translate contentKey="libraryApp.libraryUser.memeberdate">Memeberdate</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="libraryApp.libraryUser.email">Email</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('mobile')}>
                  <Translate contentKey="libraryApp.libraryUser.mobile">Mobile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('adress')}>
                  <Translate contentKey="libraryApp.libraryUser.adress">Adress</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('note')}>
                  <Translate contentKey="libraryApp.libraryUser.note">Note</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('image')}>
                  <Translate contentKey="libraryApp.libraryUser.image">Image</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="libraryApp.libraryUser.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {libraryUserList.map((libraryUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${libraryUser.id}`} color="link" size="sm">
                      {libraryUser.id}
                    </Button>
                  </td>
                  <td>{libraryUser.fullname}</td>
                  <td>
                    {libraryUser.birthdate ? <TextFormat type="date" value={libraryUser.birthdate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {libraryUser.memeberdate ? <TextFormat type="date" value={libraryUser.memeberdate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{libraryUser.email}</td>
                  <td>{libraryUser.mobile}</td>
                  <td>{libraryUser.adress}</td>
                  <td>{libraryUser.note}</td>
                  <td>
                    {libraryUser.image ? (
                      <div>
                        {libraryUser.imageContentType ? (
                          <a onClick={openFile(libraryUser.imageContentType, libraryUser.image)}>
                            <img src={`data:${libraryUser.imageContentType};base64,${libraryUser.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {libraryUser.imageContentType}, {byteSize(libraryUser.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{libraryUser.user ? libraryUser.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${libraryUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${libraryUser.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${libraryUser.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="libraryApp.libraryUser.home.notFound">No Library Users found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={libraryUserList && libraryUserList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default LibraryUser;
