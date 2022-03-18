import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGenre } from 'app/shared/model/genre.model';
import { getEntities as getGenres } from 'app/entities/genre/genre.reducer';
import { getEntity, updateEntity, createEntity, reset } from './author.reducer';
import { IAuthor } from 'app/shared/model/author.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AuthorUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const genres = useAppSelector(state => state.genre.entities);
  const authorEntity = useAppSelector(state => state.author.entity);
  const loading = useAppSelector(state => state.author.loading);
  const updating = useAppSelector(state => state.author.updating);
  const updateSuccess = useAppSelector(state => state.author.updateSuccess);
  const handleClose = () => {
    props.history.push('/author' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGenres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...authorEntity,
      ...values,
      genres: mapIdList(values.genres),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...authorEntity,
          genres: authorEntity?.genres?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="libraryApp.author.home.createOrEditLabel" data-cy="AuthorCreateUpdateHeading">
            <Translate contentKey="libraryApp.author.home.createOrEditLabel">Create or edit a Author</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="author-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('libraryApp.author.fullname')}
                id="author-fullname"
                name="fullname"
                data-cy="fullname"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.author.birtrthyear')}
                id="author-birtrthyear"
                name="birtrthyear"
                data-cy="birtrthyear"
                type="text"
              />
              <ValidatedField
                label={translate('libraryApp.author.deathyear')}
                id="author-deathyear"
                name="deathyear"
                data-cy="deathyear"
                type="text"
              />
              <ValidatedField label={translate('libraryApp.author.note')} id="author-note" name="note" data-cy="note" type="text" />
              <ValidatedField
                label={translate('libraryApp.author.genres')}
                id="author-genres"
                data-cy="genres"
                type="select"
                multiple
                name="genres"
              >
                <option value="" key="0" />
                {genres
                  ? genres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/author" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AuthorUpdate;
