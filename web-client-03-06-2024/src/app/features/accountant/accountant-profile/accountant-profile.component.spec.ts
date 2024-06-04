import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountantProfileComponent } from './accountant-profile.component';

describe('AccountantProfileComponent', () => {
  let component: AccountantProfileComponent;
  let fixture: ComponentFixture<AccountantProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountantProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountantProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
