import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './book.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BookDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const bookEntity = useAppSelector(state => state.book.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookDetailsHeading">
          <Translate contentKey="libraryApp.book.detail.title">Book</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookEntity.id}</dd>
          <dt>
            <span id="isbn">
              <Translate contentKey="libraryApp.book.isbn">Isbn</Translate>
            </span>
          </dt>
          <dd>{bookEntity.isbn}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="libraryApp.book.title">Title</Translate>
            </span>
          </dt>
          <dd>{bookEntity.title}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="libraryApp.book.year">Year</Translate>
            </span>
          </dt>
          <dd>{bookEntity.year}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="libraryApp.book.note">Note</Translate>
            </span>
          </dt>
          <dd>{bookEntity.note}</dd>
          <dt>
            <Translate contentKey="libraryApp.book.genres">Genres</Translate>
          </dt>
          <dd>
            {bookEntity.genres
              ? bookEntity.genres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {bookEntity.genres && i === bookEntity.genres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="libraryApp.book.author">Author</Translate>
          </dt>
          <dd>{bookEntity.author ? bookEntity.author.fullname : ''}</dd>
        </dl>
        <Button tag={Link} to="/book" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/book/${bookEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookDetail;
