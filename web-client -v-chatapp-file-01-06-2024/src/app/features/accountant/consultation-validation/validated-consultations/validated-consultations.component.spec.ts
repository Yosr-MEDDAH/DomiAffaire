import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidatedConsultationsComponent } from './validated-consultations.component';

describe('ValidatedConsultationsComponent', () => {
  let component: ValidatedConsultationsComponent;
  let fixture: ComponentFixture<ValidatedConsultationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidatedConsultationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValidatedConsultationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
