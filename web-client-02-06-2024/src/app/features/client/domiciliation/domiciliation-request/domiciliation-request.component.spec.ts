import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomiciliationRequestComponent } from './domiciliation-request.component';

describe('DomiciliationRequestComponent', () => {
  let component: DomiciliationRequestComponent;
  let fixture: ComponentFixture<DomiciliationRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomiciliationRequestComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomiciliationRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
