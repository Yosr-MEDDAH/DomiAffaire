import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAccountantProfileComponent } from './update-accountant-profile.component';

describe('UpdateAccountantProfileComponent', () => {
  let component: UpdateAccountantProfileComponent;
  let fixture: ComponentFixture<UpdateAccountantProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateAccountantProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateAccountantProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
