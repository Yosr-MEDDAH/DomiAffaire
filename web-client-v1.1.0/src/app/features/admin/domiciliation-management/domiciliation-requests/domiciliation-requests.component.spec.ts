import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliationRequestsComponent } from './domiciliation-requests.component';

describe('DomiciliationRequestsComponent', () => {
  let component: DomiciliationRequestsComponent;
  let fixture: ComponentFixture<DomiciliationRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomiciliationRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomiciliationRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
