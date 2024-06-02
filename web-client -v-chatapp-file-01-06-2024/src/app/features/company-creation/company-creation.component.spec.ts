import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyCreationComponent } from './company-creation.component';

describe('CompanyCreationComponent', () => {
  let component: CompanyCreationComponent;
  let fixture: ComponentFixture<CompanyCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyCreationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompanyCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
