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

describe('Book e2e test', () => {
  const bookPageUrl = '/book';
  const bookPageUrlPattern = new RegExp('/book(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bookSample = { isbn: '854264597X', title: 'copying deposit' };

  let book: any;
  //let genre: any;
  //let author: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/genres',
      body: {"name":"Generic","note":"Designer"},
    }).then(({ body }) => {
      genre = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/authors',
      body: {"fullname":"Optimization empowering Unbranded","birtrthyear":98659,"deathyear":72096,"note":"Concrete Handcrafted quantifying"},
    }).then(({ body }) => {
      author = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/books+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/books').as('postEntityRequest');
    cy.intercept('DELETE', '/api/books/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/user-book-lendings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/genres', {
      statusCode: 200,
      body: [genre],
    });

    cy.intercept('GET', '/api/authors', {
      statusCode: 200,
      body: [author],
    });

  });
   */

  afterEach(() => {
    if (book) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/books/${book.id}`,
      }).then(() => {
        book = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (genre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/genres/${genre.id}`,
      }).then(() => {
        genre = undefined;
      });
    }
    if (author) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/authors/${author.id}`,
      }).then(() => {
        author = undefined;
      });
    }
  });
   */

  it('Books menu should load Books page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('book');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Book').should('exist');
    cy.url().should('match', bookPageUrlPattern);
  });

  describe('Book page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bookPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Book page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/book/new$'));
        cy.getEntityCreateUpdateHeading('Book');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bookPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/books',
          body: {
            ...bookSample,
            genres: genre,
            author: author,
          },
        }).then(({ body }) => {
          book = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/books+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/books?page=0&size=20>; rel="last",<http://localhost/api/books?page=0&size=20>; rel="first"',
              },
              body: [book],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bookPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(bookPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Book page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('book');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bookPageUrlPattern);
      });

      it('edit button click should load edit Book page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Book');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bookPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Book', () => {
        cy.intercept('GET', '/api/books/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('book').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bookPageUrlPattern);

        book = undefined;
      });
    });
  });

  describe('new Book page', () => {
    beforeEach(() => {
      cy.visit(`${bookPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Book');
    });

    it.skip('should create an instance of Book', () => {
      cy.get(`[data-cy="isbn"]`).type('9797290434187').should('have.value', '9797290434187');

      cy.get(`[data-cy="title"]`).type('indexing Bedfordshire').should('have.value', 'indexing Bedfordshire');

      cy.get(`[data-cy="year"]`).type('85744').should('have.value', '85744');

      cy.get(`[data-cy="note"]`).type('invoice').should('have.value', 'invoice');

      cy.get(`[data-cy="genres"]`).select([0]);
      cy.get(`[data-cy="author"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        book = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', bookPageUrlPattern);
    });
  });
});
