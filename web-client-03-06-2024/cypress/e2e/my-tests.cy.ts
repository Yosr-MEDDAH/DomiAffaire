describe('My App Test', () => {
  it('should load the homepage', () => {
    cy.visit('/');
    cy.contains('DomiAffaire'); // Replace 'Welcome' with a string that appears on your homepage
  });
});
