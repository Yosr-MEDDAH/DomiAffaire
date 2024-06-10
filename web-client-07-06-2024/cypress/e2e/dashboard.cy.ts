describe('Dashboard Charts', () => {
    beforeEach(() => {
        // Visit the login page and log in as an admin
        cy.visit('/admin/login');
        cy.get('input[name="email"]').type('yosr.meddah92@gmail.com');
        cy.get('input[name="pwd"]').type('yosrMeddah123_');
        cy.get('button[type="submit"]').click();
        cy.url().should('include', '/admin');
    });

    it('should display all charts on the dashboard', () => {
        // Check if all chart containers are present
        cy.get('.content.container').should('exist');
        cy.get('.chart-container').should('exist');

        // Check if each individual chart component is present
        cy.get('app-packs-chart').should('exist');
        cy.get('app-capitaux-chart').should('exist');
        cy.get('app-deadline-chart').should('exist');
        cy.get('app-reservation-chart').should('exist');
    });

    // it('should have data in each chart', () => {
    //     // Check if each chart contains data
    //     cy.get('app-packs-chart').should('contain', 'PRESTIGE'); // Change 'someData' to the expected data
    //     cy.get('app-capitaux-chart').should('contain', "Nombre d'entreprises");
    //     cy.get('app-deadline-chart').should('contain', 'jours');
    //     cy.get('app-reservation-chart').should('contain', 'Projecteur');
    // });
});
