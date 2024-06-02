import { TestBed } from '@angular/core/testing';

import { NoGuardGuard } from './no-guard.guard';

describe('NoGuardGuard', () => {
  let guard: NoGuardGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(NoGuardGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
