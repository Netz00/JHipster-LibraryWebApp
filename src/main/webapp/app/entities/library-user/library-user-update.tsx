import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './library-user.reducer';
import { ILibraryUser } from 'app/shared/model/library-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LibraryUserUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const libraryUserEntity = useAppSelector(state => state.libraryUser.entity);
  const loading = useAppSelector(state => state.libraryUser.loading);
  const updating = useAppSelector(state => state.libraryUser.updating);
  const updateSuccess = useAppSelector(state => state.libraryUser.updateSuccess);
  const handleClose = () => {
    props.history.push('/library-user' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.birthdate = convertDateTimeToServer(values.birthdate);
    values.memeberdate = convertDateTimeToServer(values.memeberdate);

    const entity = {
      ...libraryUserEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          birthdate: displayDefaultDateTime(),
          memeberdate: displayDefaultDateTime(),
        }
      : {
          ...libraryUserEntity,
          birthdate: convertDateTimeFromServer(libraryUserEntity.birthdate),
          memeberdate: convertDateTimeFromServer(libraryUserEntity.memeberdate),
          user: libraryUserEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="libraryApp.libraryUser.home.createOrEditLabel" data-cy="LibraryUserCreateUpdateHeading">
            <Translate contentKey="libraryApp.libraryUser.home.createOrEditLabel">Create or edit a LibraryUser</Translate>
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
                  id="library-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('libraryApp.libraryUser.fullname')}
                id="library-user-fullname"
                name="fullname"
                data-cy="fullname"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.birthdate')}
                id="library-user-birthdate"
                name="birthdate"
                data-cy="birthdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.memeberdate')}
                id="library-user-memeberdate"
                name="memeberdate"
                data-cy="memeberdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.email')}
                id="library-user-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.mobile')}
                id="library-user-mobile"
                name="mobile"
                data-cy="mobile"
                type="text"
                validate={{
                  pattern: {
                    value: /^(\+\d{1,3}[- ]?)?\d{10}$/,
                    message: translate('entity.validation.pattern', { pattern: '^(\\+\\d{1,3}[- ]?)?\\d{10}$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.adress')}
                id="library-user-adress"
                name="adress"
                data-cy="adress"
                type="text"
              />
              <ValidatedField
                label={translate('libraryApp.libraryUser.note')}
                id="library-user-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedBlobField
                label={translate('libraryApp.libraryUser.image')}
                id="library-user-image"
                name="image"
                data-cy="image"
                isImage
                accept="image/*"
              />
              <ValidatedField
                id="library-user-user"
                name="user"
                data-cy="user"
                label={translate('libraryApp.libraryUser.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/library-user" replace color="info">
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

export default LibraryUserUpdate;
