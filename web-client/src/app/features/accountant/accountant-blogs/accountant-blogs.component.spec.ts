import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountantBlogsComponent } from './accountant-blogs.component';

describe('AccountantBlogsComponent', () => {
  let component: AccountantBlogsComponent;
  let fixture: ComponentFixture<AccountantBlogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountantBlogsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountantBlogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
