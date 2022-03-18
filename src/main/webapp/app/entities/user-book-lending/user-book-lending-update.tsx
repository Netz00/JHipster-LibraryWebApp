import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ILibraryUser } from 'app/shared/model/library-user.model';
import { getEntities as getLibraryUsers } from 'app/entities/library-user/library-user.reducer';
import { IBook } from 'app/shared/model/book.model';
import { getEntities as getBooks } from 'app/entities/book/book.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-book-lending.reducer';
import { IUserBookLending } from 'app/shared/model/user-book-lending.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { LendingStatus } from 'app/shared/model/enumerations/lending-status.model';

export const UserBookLendingUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const libraryUsers = useAppSelector(state => state.libraryUser.entities);
  const books = useAppSelector(state => state.book.entities);
  const userBookLendingEntity = useAppSelector(state => state.userBookLending.entity);
  const loading = useAppSelector(state => state.userBookLending.loading);
  const updating = useAppSelector(state => state.userBookLending.updating);
  const updateSuccess = useAppSelector(state => state.userBookLending.updateSuccess);
  const lendingStatusValues = Object.keys(LendingStatus);
  const handleClose = () => {
    props.history.push('/user-book-lending' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getLibraryUsers({}));
    dispatch(getBooks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.loantime = convertDateTimeToServer(values.loantime);
    values.returntime = convertDateTimeToServer(values.returntime);

    const entity = {
      ...userBookLendingEntity,
      ...values,
      user: libraryUsers.find(it => it.id.toString() === values.user.toString()),
      book: books.find(it => it.id.toString() === values.book.toString()),
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
          loantime: displayDefaultDateTime(),
          returntime: displayDefaultDateTime(),
        }
      : {
          status: 'LENDED',
          ...userBookLendingEntity,
          loantime: convertDateTimeFromServer(userBookLendingEntity.loantime),
          returntime: convertDateTimeFromServer(userBookLendingEntity.returntime),
          user: userBookLendingEntity?.user?.id,
          book: userBookLendingEntity?.book?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="libraryApp.userBookLending.home.createOrEditLabel" data-cy="UserBookLendingCreateUpdateHeading">
            <Translate contentKey="libraryApp.userBookLending.home.createOrEditLabel">Create or edit a UserBookLending</Translate>
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
                  id="user-book-lending-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('libraryApp.userBookLending.loantime')}
                id="user-book-lending-loantime"
                name="loantime"
                data-cy="loantime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('libraryApp.userBookLending.returntime')}
                id="user-book-lending-returntime"
                name="returntime"
                data-cy="returntime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('libraryApp.userBookLending.status')}
                id="user-book-lending-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {lendingStatusValues.map(lendingStatus => (
                  <option value={lendingStatus} key={lendingStatus}>
                    {translate('libraryApp.LendingStatus.' + lendingStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('libraryApp.userBookLending.note')}
                id="user-book-lending-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedField
                id="user-book-lending-user"
                name="user"
                data-cy="user"
                label={translate('libraryApp.userBookLending.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {libraryUsers
                  ? libraryUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fullname}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="user-book-lending-book"
                name="book"
                data-cy="book"
                label={translate('libraryApp.userBookLending.book')}
                type="select"
                required
              >
                <option value="" key="0" />
                {books
                  ? books.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-book-lending" replace color="info">
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

export default UserBookLendingUpdate;
