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

describe('Genre e2e test', () => {
  const genrePageUrl = '/genre';
  const genrePageUrlPattern = new RegExp('/genre(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const genreSample = { name: 'clicks-and-mortar Unbranded' };

  let genre: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/genres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/genres').as('postEntityRequest');
    cy.intercept('DELETE', '/api/genres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (genre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/genres/${genre.id}`,
      }).then(() => {
        genre = undefined;
      });
    }
  });

  it('Genres menu should load Genres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('genre');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Genre').should('exist');
    cy.url().should('match', genrePageUrlPattern);
  });

  describe('Genre page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(genrePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Genre page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/genre/new$'));
        cy.getEntityCreateUpdateHeading('Genre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genrePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/genres',
          body: genreSample,
        }).then(({ body }) => {
          genre = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/genres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [genre],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(genrePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Genre page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('genre');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genrePageUrlPattern);
      });

      it('edit button click should load edit Genre page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Genre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genrePageUrlPattern);
      });

      it('last delete button click should delete instance of Genre', () => {
        cy.intercept('GET', '/api/genres/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('genre').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', genrePageUrlPattern);

        genre = undefined;
      });
    });
  });

  describe('new Genre page', () => {
    beforeEach(() => {
      cy.visit(`${genrePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Genre');
    });

    it('should create an instance of Genre', () => {
      cy.get(`[data-cy="name"]`).type('matrix').should('have.value', 'matrix');

      cy.get(`[data-cy="note"]`).type('XSS Clothing Plastic').should('have.value', 'XSS Clothing Plastic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        genre = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', genrePageUrlPattern);
    });
  });
});
