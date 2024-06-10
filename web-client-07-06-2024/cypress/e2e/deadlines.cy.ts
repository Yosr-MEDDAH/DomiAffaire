describe('DeadlinesComponent', () => {
    beforeEach(() => {
        cy.visit('/admin/deadlines');
        cy.get('input[name="email"]').type('yosr.meddah92@gmail.com');
        cy.get('input[name="pwd"]').type('yosrMeddah123_');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/admin');
    });
  
    it('should display clients with payment deadlines', () => {
      cy.contains('Clients avec des Échéances de Paiement dans les Deux Prochaines Semaines').should('be.visible');
      cy.get('.legend').should('be.visible');
      cy.get('.legend-item').should('have.length', 2);
      cy.get('.gantt-chart').should('be.visible');
      cy.get('.gantt-chart .gantt_task').should('have.length.gt', 0);
      });
  });
  