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

describe('UserBookLending e2e test', () => {
  const userBookLendingPageUrl = '/user-book-lending';
  const userBookLendingPageUrlPattern = new RegExp('/user-book-lending(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userBookLendingSample = { loantime: '2022-03-18T15:03:28.296Z', status: 'RETURNED_DAMAGED' };

  let userBookLending: any;
  //let libraryUser: any;
  //let book: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/library-users',
      body: {"fullname":"Gorgeous South","birthdate":"2022-03-18T10:45:57.493Z","memeberdate":"2022-03-18T10:04:20.411Z","email":"G[dU4@Ul,x~`.$gY{","mobile":"4203202615","adress":"Auto bus","note":"Frozen structure","image":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","imageContentType":"unknown"},
    }).then(({ body }) => {
      libraryUser = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/books',
      body: {"isbn":"3838784788","title":"Handmade","year":22391,"note":"invoice incubate"},
    }).then(({ body }) => {
      book = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-book-lendings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-book-lendings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-book-lendings/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/library-users', {
      statusCode: 200,
      body: [libraryUser],
    });

    cy.intercept('GET', '/api/books', {
      statusCode: 200,
      body: [book],
    });

  });
   */

  afterEach(() => {
    if (userBookLending) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-book-lendings/${userBookLending.id}`,
      }).then(() => {
        userBookLending = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (libraryUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/library-users/${libraryUser.id}`,
      }).then(() => {
        libraryUser = undefined;
      });
    }
    if (book) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/books/${book.id}`,
      }).then(() => {
        book = undefined;
      });
    }
  });
   */

  it('UserBookLendings menu should load UserBookLendings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-book-lending');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserBookLending').should('exist');
    cy.url().should('match', userBookLendingPageUrlPattern);
  });

  describe('UserBookLending page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userBookLendingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserBookLending page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-book-lending/new$'));
        cy.getEntityCreateUpdateHeading('UserBookLending');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userBookLendingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-book-lendings',
          body: {
            ...userBookLendingSample,
            user: libraryUser,
            book: book,
          },
        }).then(({ body }) => {
          userBookLending = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-book-lendings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-book-lendings?page=0&size=20>; rel="last",<http://localhost/api/user-book-lendings?page=0&size=20>; rel="first"',
              },
              body: [userBookLending],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userBookLendingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userBookLendingPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserBookLending page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userBookLending');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userBookLendingPageUrlPattern);
      });

      it('edit button click should load edit UserBookLending page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserBookLending');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userBookLendingPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of UserBookLending', () => {
        cy.intercept('GET', '/api/user-book-lendings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userBookLending').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', userBookLendingPageUrlPattern);

        userBookLending = undefined;
      });
    });
  });

  describe('new UserBookLending page', () => {
    beforeEach(() => {
      cy.visit(`${userBookLendingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserBookLending');
    });

    it.skip('should create an instance of UserBookLending', () => {
      cy.get(`[data-cy="loantime"]`).type('2022-03-18T04:36').should('have.value', '2022-03-18T04:36');

      cy.get(`[data-cy="returntime"]`).type('2022-03-17T17:43').should('have.value', '2022-03-17T17:43');

      cy.get(`[data-cy="status"]`).select('RETURNED');

      cy.get(`[data-cy="note"]`).type('Designer Lead Table').should('have.value', 'Designer Lead Table');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="book"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        userBookLending = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', userBookLendingPageUrlPattern);
    });
  });
});
