import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('LibraryUser e2e test', () => {
  const libraryUserPageUrl = '/library-user';
  const libraryUserPageUrlPattern = new RegExp('/library-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const libraryUserSample = {
    fullname: 'Salad',
    birthdate: '2022-03-18T14:34:48.678Z',
    memeberdate: '2022-03-18T06:17:27.997Z',
    email: '\\Qw@.{)mjw.7J',
  };

  let libraryUser: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/library-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/library-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/library-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (libraryUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/library-users/${libraryUser.id}`,
      }).then(() => {
        libraryUser = undefined;
      });
    }
  });

  it('LibraryUsers menu should load LibraryUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('library-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LibraryUser').should('exist');
    cy.url().should('match', libraryUserPageUrlPattern);
  });

  describe('LibraryUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(libraryUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LibraryUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/library-user/new$'));
        cy.getEntityCreateUpdateHeading('LibraryUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/library-users',
          body: libraryUserSample,
        }).then(({ body }) => {
          libraryUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/library-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/library-users?page=0&size=20>; rel="last",<http://localhost/api/library-users?page=0&size=20>; rel="first"',
              },
              body: [libraryUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(libraryUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LibraryUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('libraryUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryUserPageUrlPattern);
      });

      it('edit button click should load edit LibraryUser page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LibraryUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryUserPageUrlPattern);
      });

      it('last delete button click should delete instance of LibraryUser', () => {
        cy.intercept('GET', '/api/library-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('libraryUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', libraryUserPageUrlPattern);

        libraryUser = undefined;
      });
    });
  });

  describe('new LibraryUser page', () => {
    beforeEach(() => {
      cy.visit(`${libraryUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LibraryUser');
    });

    it('should create an instance of LibraryUser', () => {
      cy.get(`[data-cy="fullname"]`).type('Bike Texas Operations').should('have.value', 'Bike Texas Operations');

      cy.get(`[data-cy="birthdate"]`).type('2022-03-18T16:35').should('have.value', '2022-03-18T16:35');

      cy.get(`[data-cy="memeberdate"]`).type('2022-03-18T08:19').should('have.value', '2022-03-18T08:19');

      cy.get(`[data-cy="email"]`).type('bq7w@F2P7k.:;99^').should('have.value', 'bq7w@F2P7k.:;99^');

      cy.get(`[data-cy="mobile"]`).type('6616779784').should('have.value', '6616779784');

      cy.get(`[data-cy="adress"]`).type('Account Jewelery THX').should('have.value', 'Account Jewelery THX');

      cy.get(`[data-cy="note"]`).type('Account').should('have.value', 'Account');

      cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        libraryUser = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', libraryUserPageUrlPattern);
    });
  });
});
