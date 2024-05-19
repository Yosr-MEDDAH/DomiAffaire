import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliationRequestDetailsComponent } from './domiciliation-request-details.component';

describe('DomiciliationRequestDetailsComponent', () => {
  let component: DomiciliationRequestDetailsComponent;
  let fixture: ComponentFixture<DomiciliationRequestDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomiciliationRequestDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomiciliationRequestDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
