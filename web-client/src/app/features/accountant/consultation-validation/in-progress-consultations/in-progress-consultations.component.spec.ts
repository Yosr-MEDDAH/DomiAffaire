import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InProgressConsultationsComponent } from './in-progress-consultations.component';

describe('InProgressConsultationsComponent', () => {
  let component: InProgressConsultationsComponent;
  let fixture: ComponentFixture<InProgressConsultationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InProgressConsultationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InProgressConsultationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
