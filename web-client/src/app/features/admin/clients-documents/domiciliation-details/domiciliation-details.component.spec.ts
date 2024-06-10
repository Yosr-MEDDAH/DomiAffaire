import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliationDetailsComponent } from './domiciliation-details.component';

describe('DomiciliationDetailsComponent', () => {
  let component: DomiciliationDetailsComponent;
  let fixture: ComponentFixture<DomiciliationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomiciliationDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomiciliationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
