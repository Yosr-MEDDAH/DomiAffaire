describe('Admin Dashboard', () => {
    it('should login as admin and access the admin dashboard', () => {
      cy.visit('/admin/login');
      cy.get('input[name="email"]').type('yosr.meddah92@gmail.com');
      cy.get('input[name="pwd"]').type('yosrMeddah123_');
      cy.get('button[type="submit"]').click();
      cy.url().should('include', '/admin');
    });
  
    it('should redirect unauthorized users to the login page', () => {
      cy.visit('/admin');
      cy.url().should('include', '/login');
    });
  });
  