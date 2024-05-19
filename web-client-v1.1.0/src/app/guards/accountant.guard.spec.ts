import { TestBed } from '@angular/core/testing';

import { AccountantGuard } from './accountant.guard';

describe('AccountantGuard', () => {
  let guard: AccountantGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(AccountantGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
