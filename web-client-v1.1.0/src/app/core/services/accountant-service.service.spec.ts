import { TestBed } from '@angular/core/testing';

import { AccountantServiceService } from './accountant-service.service';

describe('AccountantServiceService', () => {
  let service: AccountantServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountantServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
