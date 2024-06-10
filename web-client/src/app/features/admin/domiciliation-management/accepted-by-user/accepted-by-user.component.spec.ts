import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptedByUserComponent } from './accepted-by-user.component';

describe('AcceptedByUserComponent', () => {
  let component: AcceptedByUserComponent;
  let fixture: ComponentFixture<AcceptedByUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptedByUserComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptedByUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
