import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptedByAdminComponent } from './accepted-by-admin.component';

describe('AcceptedByAdminComponent', () => {
  let component: AcceptedByAdminComponent;
  let fixture: ComponentFixture<AcceptedByAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptedByAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptedByAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
