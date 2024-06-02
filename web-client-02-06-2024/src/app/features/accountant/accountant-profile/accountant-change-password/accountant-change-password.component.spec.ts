import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountantChangePasswordComponent } from './accountant-change-password.component';

describe('AccountantChangePasswordComponent', () => {
  let component: AccountantChangePasswordComponent;
  let fixture: ComponentFixture<AccountantChangePasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountantChangePasswordComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountantChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
