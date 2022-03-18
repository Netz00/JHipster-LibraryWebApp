import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './author.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AuthorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const authorEntity = useAppSelector(state => state.author.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="authorDetailsHeading">
          <Translate contentKey="libraryApp.author.detail.title">Author</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{authorEntity.id}</dd>
          <dt>
            <span id="fullname">
              <Translate contentKey="libraryApp.author.fullname">Fullname</Translate>
            </span>
          </dt>
          <dd>{authorEntity.fullname}</dd>
          <dt>
            <span id="birtrthyear">
              <Translate contentKey="libraryApp.author.birtrthyear">Birtrthyear</Translate>
            </span>
          </dt>
          <dd>{authorEntity.birtrthyear}</dd>
          <dt>
            <span id="deathyear">
              <Translate contentKey="libraryApp.author.deathyear">Deathyear</Translate>
            </span>
          </dt>
          <dd>{authorEntity.deathyear}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="libraryApp.author.note">Note</Translate>
            </span>
          </dt>
          <dd>{authorEntity.note}</dd>
          <dt>
            <Translate contentKey="libraryApp.author.genres">Genres</Translate>
          </dt>
          <dd>
            {authorEntity.genres
              ? authorEntity.genres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {authorEntity.genres && i === authorEntity.genres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/author" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/author/${authorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuthorDetail;
