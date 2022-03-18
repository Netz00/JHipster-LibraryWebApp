import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-book-lending.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserBookLendingDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userBookLendingEntity = useAppSelector(state => state.userBookLending.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userBookLendingDetailsHeading">
          <Translate contentKey="libraryApp.userBookLending.detail.title">UserBookLending</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userBookLendingEntity.id}</dd>
          <dt>
            <span id="loantime">
              <Translate contentKey="libraryApp.userBookLending.loantime">Loantime</Translate>
            </span>
          </dt>
          <dd>
            {userBookLendingEntity.loantime ? (
              <TextFormat value={userBookLendingEntity.loantime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="returntime">
              <Translate contentKey="libraryApp.userBookLending.returntime">Returntime</Translate>
            </span>
          </dt>
          <dd>
            {userBookLendingEntity.returntime ? (
              <TextFormat value={userBookLendingEntity.returntime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="libraryApp.userBookLending.status">Status</Translate>
            </span>
          </dt>
          <dd>{userBookLendingEntity.status}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="libraryApp.userBookLending.note">Note</Translate>
            </span>
          </dt>
          <dd>{userBookLendingEntity.note}</dd>
          <dt>
            <Translate contentKey="libraryApp.userBookLending.user">User</Translate>
          </dt>
          <dd>{userBookLendingEntity.user ? userBookLendingEntity.user.fullname : ''}</dd>
          <dt>
            <Translate contentKey="libraryApp.userBookLending.book">Book</Translate>
          </dt>
          <dd>{userBookLendingEntity.book ? userBookLendingEntity.book.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-book-lending" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-book-lending/${userBookLendingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserBookLendingDetail;
