describe('ConsultationRequestComponent', () => {
    beforeEach(() => {
      cy.visit('/client/consultation-request');
  
      // Login first
      cy.get('input[name="email"]')
        .should('be.visible')
        .type('wassim.ouertani.25@gmail.com', { force: true });
      cy.get('input[name="pwd"]').type('yosrMeddah123_', { force: true });
      cy.get('input[type="submit"]').click({ force: true });
      cy.url().should('include', '/client'); 
    });
  
    it('should display validation errors for required fields', () => {
        // Submit form without filling required fields
        cy.get('form').first().submit();
    
        // Verify error messages for required fields
        cy.contains("draft status shouldn't be blank").should('be.visible');
        cy.contains("subject shouldn't be blank").should('be.visible');
        cy.contains("details shouldn't be blank").should('be.visible');
      });
   
  
    // it('should reset the form when cancel button is clicked', () => {
    //   // Fill in form fields
    //   cy.get('#subject-input').type('Test Subject', { force: true });
    //   cy.get('#date-input').type('2024-06-09T12:00', { force: true });
    //   cy.get('#crud-form-4').type('500', { force: true });
    //   cy.get('#draft-status-select').select('SARL', { force: true });
    //   cy.get('.angular-editor-textarea').type('Test details', { force: true });
  
    //   // Click cancel button
    //   cy.contains('Cancel').click();
  
    //   // Verify form is reset
    //   cy.get('#subject-input').should('have.value', '');
    //   cy.get('#date-input').should('have.value', '');
    //   cy.get('#crud-form-4').should('have.value', '');
    //   cy.get('#draft-status-select').should('have.value', '');
    //   cy.get('.angular-editor-textarea').should('have.text', '');
    // });
  });
  